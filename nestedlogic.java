import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);
        
        int a_day,a_month,a_year;
        int e_day,e_month,e_year;
        a_day = sc.nextInt();
        a_month = sc.nextInt();
        a_year = sc.nextInt();
        
        e_day = sc.nextInt();
        e_month = sc.nextInt();
        e_year = sc.nextInt();
        
        int fine = 0;
        if(a_year<e_year)
        {
            fine = 0;
        }
        else
        {
            if(a_year>e_year)
            {
                fine = 10000;
            }
            else if(a_month>e_month)
            {
                fine = 500 * (a_month - e_month);
            }
            else if(a_day>e_day)
            {
                fine = 15 *(a_day-e_day);
            }
        }
        System.out.println(fine);
    }
}