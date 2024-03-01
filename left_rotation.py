#!/bin/python3
#Problem Link -- https://www.hackerrank.com/challenges/array-left-rotation/problem?isFullScreen=true&h_r=next-challenge&h_v=zen
import math
import os
import random
import re
import sys

#
# Complete the 'rotateLeft' function below.
#
# The function is expected to return an INTEGER_ARRAY.
# The function accepts following parameters:
#  1. INTEGER d
#  2. INTEGER_ARRAY arr
#

def rotateLeft(d, arr):
    # Write your code here
    def rotation(arr,left,right):
        while left<right:
            arr[left],arr[right]=arr[right],arr[left]
            left+=1
            right-=1
    rotation(arr,0,d-1)
    rotation(arr,d,len(arr)-1)
    rotation(arr,0,len(arr)-1)
    return arr

if __name__ == '__main__':
    fptr = open(os.environ['OUTPUT_PATH'], 'w')

    first_multiple_input = input().rstrip().split()

    n = int(first_multiple_input[0])

    d = int(first_multiple_input[1])

    arr = list(map(int, input().rstrip().split()))

    result = rotateLeft(d, arr)

    fptr.write(' '.join(map(str, result)))
    fptr.write('\n')

    fptr.close()
