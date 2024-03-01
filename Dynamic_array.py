#!/bin/python3
# problem link -- https://www.hackerrank.com/challenges/dynamic-array/problem?isFullScreen=true
import math
import os
import random
import re
import sys

#
# Complete the 'dynamicArray' function below.
#
# The function is expected to return an INTEGER_ARRAY.
# The function accepts following parameters:
#  1. INTEGER n
#  2. 2D_INTEGER_ARRAY queries
#

def dynamicArray(n, queries):
    # Write your code here
    arr = [list([])for i in range (n)]
    ans = []
    
    last_answer = 0
    for i in range(len(queries)):
        if queries[i][0]==1:
            idx=((queries[i][1]^last_answer)%n)
            arr[idx].append(queries[i][2])
        else:
            idx=((queries[i][1]^last_answer)%n)
            last_answer=arr[idx][queries[i][2]%len(arr[idx])]
            ans.append(last_answer)
    return ans  
if __name__ == '__main__':
    fptr = open(os.environ['OUTPUT_PATH'], 'w')

    first_multiple_input = input().rstrip().split()

    n = int(first_multiple_input[0])

    q = int(first_multiple_input[1])

    queries = []

    for _ in range(q):
        queries.append(list(map(int, input().rstrip().split())))

    result = dynamicArray(n, queries)

    fptr.write('\n'.join(map(str, result)))
    fptr.write('\n')

    fptr.close()
