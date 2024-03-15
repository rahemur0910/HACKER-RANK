
SELECT SUBMISSION_DATE,
(SELECT COUNT(DISTINCT HACKER_ID)  
 FROM SUBMISSIONS S2  
 WHERE S2.SUBMISSION_DATE = S1.SUBMISSION_DATE AND    
(SELECT COUNT(DISTINCT S3.SUBMISSION_DATE) 
 FROM SUBMISSIONS S3 WHERE S3.HACKER_ID = S2.HACKER_ID AND S3.SUBMISSION_DATE < S1.SUBMISSION_DATE) = DATEDIFF(S1.SUBMISSION_DATE , '2016-03-01')),
(SELECT HACKER_ID FROM SUBMISSIONS S2 WHERE S2.SUBMISSION_DATE = S1.SUBMISSION_DATE 
GROUP BY HACKER_ID ORDER BY COUNT(SUBMISSION_ID) DESC, HACKER_ID LIMIT 1) AS TMP,
(SELECT NAME FROM HACKERS WHERE HACKER_ID = TMP)
FROM
(SELECT DISTINCT SUBMISSION_DATE FROM SUBMISSIONS) S1
GROUP BY SUBMISSION_DATE;

/*EXPLAIN:

*The outermost SELECT statement selects the submission date and performs subqueries to get the count of unique hackers for each day, the hacker_id with the maximum submissions for each day, and the name of the hacker corresponding to that hacker_id.
The first subquery calculates the count of distinct hackers who made submissions on each day of the contest.
The second subquery aims to find the hacker_id with the maximum submissions for each day. It uses a subquery to group submissions by hacker_id and orders them by the count of submissions in descending order. Then, it selects the hacker_id with the highest count.
The third subquery retrieves the name of the hacker corresponding to the hacker_id found in the previous subquery.
The innermost subquery selects distinct submission dates from the submissions table.
While this query attempts to achieve the desired result, it's quite complex and could be prone to errors. Additionally, it might not be the most efficient way to accomplish the task. You may want to test it thoroughly with your dataset to ensure it produces the correct output. If you encounter any issues, you might consider simplifying the query or using a different approach.*/