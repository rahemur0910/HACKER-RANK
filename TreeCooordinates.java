import java.util.*;
import java.io.*;

class CustomNode implements Comparable<CustomNode> {
    int xCoordinate;
    int yCoordinate;
    int value;

    public CustomNode(int x, int y, int val) {
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.value = val;
    }

    @Override
    public int compareTo(CustomNode other) {
        return this.value - other.value;
    }
}

public class DistanceCalculator {

    // Time complexity: O(n log n), Space complexity: O(n log n)
    static int[][] buildSparseTable(int[] arr) {
        int pow = 1;
        while ((1 << pow) < arr.length) pow++;
        int[][] result = new int[arr.length][pow];
        for (int i = 0; i < arr.length; i++) result[i][0] = arr[i];
        for (int j = 1; j <= pow; j++) {
            for (int i = 0; i + (1 << j) <= arr.length; i++) {
                result[i][j] = Math.min(result[i][j-1], result[i + (1 << (j-1))][j-1]);
            }
        }
        return result;
    }

    // Time complexity: O(n + e), Space complexity: O(n)
    static int calculateDistances(int numberOfNodes, int[][] treeEdges, int[][] coordinates) {
        ArrayList<Integer>[] adjacencyList = new ArrayList[numberOfNodes + 1];
        for (int i = 1; i <= numberOfNodes; i++) adjacencyList[i] = new ArrayList<>();

        for (int[] edge : treeEdges) {
            adjacencyList[edge[0]].add(edge[1]);
            adjacencyList[edge[1]].add(edge[0]);
        }

        int root = 0;
        int tail = 0;

        // Finding Diameter (Two BFS)
        {
            class BFSNode {
                int node;
                int distance;

                public BFSNode(int node, int distance) {
                    this.node = node;
                    this.distance = distance;
                }
            }
            LinkedList<BFSNode> queue = new LinkedList<>();
            boolean[] visited = new boolean[numberOfNodes + 1];
            visited[1] = true;
            queue.offer(new BFSNode(1, 0));

            int maxDistance = 0;
            int farNode = 1;

            while (!queue.isEmpty()) {
                BFSNode current = queue.poll();
                if (current.distance > maxDistance) {
                    maxDistance = current.distance;
                    farNode = current.node;
                }
                for (int neighbor : adjacencyList[current.node]) {
                    if (visited[neighbor]) continue;
                    visited[neighbor] = true;
                    queue.offer(new BFSNode(neighbor, current.distance + 1));
                }
            }
            root = farNode;

            queue = new LinkedList<>();
            visited = new boolean[numberOfNodes + 1];
            visited[root] = true;
            queue.offer(new BFSNode(root, 0));
            maxDistance = 0;
            farNode = root;

            while (!queue.isEmpty()) {
                BFSNode current = queue.poll();
                if (current.distance > maxDistance) {
                    maxDistance = current.distance;
                    farNode = current.node;
                }
                for (int neighbor : adjacencyList[current.node]) {
                    if (visited[neighbor]) continue;
                    visited[neighbor] = true;
                    queue.offer(new BFSNode(neighbor, current.distance + 1));
                }
            }
            tail = farNode;
        }

        // Euler Tour
        int[] eulerTour = new int[2 * numberOfNodes - 1];
        final int[] depth = new int[numberOfNodes + 1];
        int[] eulerLevels = new int[2 * numberOfNodes - 1];
        int[] eulerIndex = new int[numberOfNodes + 1];
        boolean[] visited = new boolean[numberOfNodes + 1];

        int[] stack = new int[numberOfNodes];
        int stackPos = 0;
        stack[0] = root;
        int pos = 0;
        int[] neighborCount = new int[numberOfNodes + 1];

        while (stackPos > -1) {
            int current = stack[stackPos--];
            if (!visited[current]) {
                depth[current] = stackPos + 1;
                eulerIndex[current] = pos;
                visited[current] = true;
            }
            eulerLevels[pos] = stackPos + 1;
            eulerTour[pos] = current;
            pos++;

            while (neighborCount[current] < adjacencyList[current].size()) {
                if (visited[adjacencyList[current].get(neighborCount[current])]) {
                    neighborCount[current]++;
                    continue;
                }
                int next = adjacencyList[current].get(neighborCount[current]);
                stack[++stackPos] = current;
                stack[++stackPos] = next;
                neighborCount[current]++;
                break;
            }
        }

        // Build Sparse Table for Euler Levels
        int[][] sparseTable = buildSparseTable(eulerLevels);

        // Lists for storing different combinations of points
        List<CustomNode> list1 = new ArrayList<>(coordinates.length);
        List<CustomNode> list2 = new ArrayList<>(coordinates.length);
        List<CustomNode> list3 = new ArrayList<>(coordinates.length);
        List<CustomNode> list4 = new ArrayList<>(coordinates.length);

        // Time complexity: O(m log m), Space complexity: O(m)
        for (int i = 0; i < coordinates.length; i++) {
            int x = coordinates[i][0];
            int y = coordinates[i][1];
            int xLcaLevel;
            {
                int start = Math.min(eulerIndex[x], eulerIndex[tail]);
                int end = Math.max(eulerIndex[x], eulerIndex[tail]);
                int pow = 0;
                while (1 << (pow + 1) <= (end - start)) pow++;
                xLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
            }
            int yLcaLevel;
            {
                int start = Math.min(eulerIndex[y], eulerIndex[tail]);
                int end = Math.max(eulerIndex[y], eulerIndex[tail]);
                int pow = 0;
                while (1 << (pow + 1) <= (end - start)) pow++;
                yLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
            }

            // Calculate different values based on points
            int val1 = depth[x] + depth[y];
            list1.add(new CustomNode(x, y, val1));

            int val2 = -depth[x] - depth[y] + 2 * xLcaLevel + 2 * yLcaLevel;
            list2.add(new CustomNode(x, y, val2));

            int val3 = depth[x] + depth[y] - 2 * xLcaLevel;
            list3.add(new CustomNode(x, y, val3));

            int val4 = -depth[x] - depth[y] + 2 * yLcaLevel;
            list4.add(new CustomNode(x, y, val4));
        }

        // Sort the lists
        Collections.sort(list1, Collections.reverseOrder());
        Collections.sort(list2);
        Collections.sort(list3, Collections.reverseOrder());
        Collections.sort(list4);

        // Time complexity: O(m^2), Space complexity: O(1)
        int maxDist = 0;

        for (int i = 0; i < coordinates.length; i++) {
            boolean shouldContinue = false;

            // Iterate through sorted lists to calculate distances
            for (int j = 0; j <= i; j++) {
                CustomNode e1 = list1.get(i - j);
                CustomNode e2 = list2.get(j);
                int potential12 = e1.value - e2.value;
                if (potential12 > maxDist) {
                    shouldContinue = true;
                    int x1 = e1.xCoordinate;
                    int y1 = e1.yCoordinate;
                    int x2 = e2.xCoordinate;
                    int y2 = e2.yCoordinate;
                    int xLcaLevel;
                    {
                        int start = Math.min(eulerIndex[x1], eulerIndex[x2]);
                        int end = Math.max(eulerIndex[x1], eulerIndex[x2]);
                        int pow = 0;
                        while (1 << (pow + 1) <= (end - start)) pow++;
                        xLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
                    }
                    int yLcaLevel;
                    {
                        int start = Math.min(eulerIndex[y1], eulerIndex[y2]);
                        int end = Math.max(eulerIndex[y1], eulerIndex[y2]);
                        int pow = 0;
                        while (1 << (pow + 1) <= (end - start)) pow++;
                        yLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
                    }
                    int actual12 = depth[x1] + depth[x2] - 2 * xLcaLevel + depth[y1] + depth[y2] - 2 * yLcaLevel;
                    maxDist = Math.max(maxDist, actual12);
                }

                CustomNode e3 = list3.get(i - j);
                CustomNode e4 = list4.get(j);
                int potential34 = e3.value - e4.value;
                if (potential34 > maxDist) {
                    shouldContinue = true;
                    int x3 = e3.xCoordinate;
                    int y3 = e3.yCoordinate;
                    int x4 = e4.xCoordinate;
                    int y4 = e4.yCoordinate;
                    int xLcaLevel;
                    {
                        int start = Math.min(eulerIndex[x3], eulerIndex[x4]);
                        int end = Math.max(eulerIndex[x3], eulerIndex[x4]);
                        int pow = 0;
                        while (1 << (pow + 1) <= (end - start)) pow++;
                        xLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
                    }
                    int yLcaLevel;
                    {
                        int start = Math.min(eulerIndex[y3], eulerIndex[y4]);
                        int end = Math.max(eulerIndex[y3], eulerIndex[y4]);
                        int pow = 0;
                        while (1 << (pow + 1) <= (end - start)) pow++;
                        yLcaLevel = Math.min(sparseTable[start][pow], sparseTable[end + 1 - (1 << pow)][pow]);
                    }
                    int actual34 = depth[x3] + depth[x4] - 2 * xLcaLevel + depth[y3] + depth[y4] - 2 * yLcaLevel;
                    maxDist = Math.max(maxDist, actual34);
                }
            }
            if (!shouldContinue) break;
        }

        return maxDist;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nm = scanner.nextLine().split(" ");
        int n = Integer.parseInt(nm[0].trim());
        int m = Integer.parseInt(nm[1].trim());

        int[][] edges = new int[n - 1][2];
        for (int edgesRowItr = 0; edgesRowItr < n - 1; edgesRowItr++) {
            String[] edgesRowItems = scanner.nextLine().split(" ");
            for (int edgesColumnItr = 0; edgesColumnItr < 2; edgesColumnItr++) {
                int edgesItem = Integer.parseInt(edgesRowItems[edgesColumnItr].trim());
                edges[edgesRowItr][edgesColumnItr] = edgesItem;
            }
        }

        int[][] points = new int[m][2];
        for (int pointsRowItr = 0; pointsRowItr < m; pointsRowItr++) {
            String[] pointsRowItems = scanner.nextLine().split(" ");
            for (int pointsColumnItr = 0; pointsColumnItr < 2; pointsColumnItr++) {
                int pointsItem = Integer.parseInt(pointsRowItems[pointsColumnItr].trim());
                points[pointsRowItr][pointsColumnItr] = pointsItem;
            }
        }

        int result = calculateDistances(n, edges, points);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();
    }
}
