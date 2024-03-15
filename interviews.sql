SELECT 
    A.CONTEST_ID,
    A.HACKER_ID,
    A.NAME, 
    SUM(TOTAL_SUBMISSIONS) AS TOTAL_SUBMISSIONS, 
    SUM(TOTAL_ACCEPTED_SUBMISSIONS) AS TOTAL_ACCEPTED_SUBMISSIONS,
    SUM(TOTAL_VIEWS) AS TOTAL_VIEWS,
    SUM(TOTAL_UNIQUE_VIEWS) AS TOTAL_UNIQUE_VIEWS
FROM 
    CONTESTS AS A
LEFT JOIN 
    COLLEGES AS B ON A.CONTEST_ID = B.CONTEST_ID
LEFT JOIN 
    CHALLENGES AS C ON B.COLLEGE_ID = C.COLLEGE_ID 
LEFT JOIN 
    (SELECT 
         CHALLENGE_ID, 
         SUM(TOTAL_VIEWS) AS TOTAL_VIEWS, 
         SUM(TOTAL_UNIQUE_VIEWS) AS TOTAL_UNIQUE_VIEWS
     FROM 
         VIEW_STATS
     GROUP BY 
         CHALLENGE_ID) AS D ON C.CHALLENGE_ID = D.CHALLENGE_ID 
LEFT JOIN 
    (SELECT 
         CHALLENGE_ID, 
         SUM(TOTAL_SUBMISSIONS) AS TOTAL_SUBMISSIONS, 
         SUM(TOTAL_ACCEPTED_SUBMISSIONS) AS TOTAL_ACCEPTED_SUBMISSIONS
     FROM 
         SUBMISSION_STATS
     GROUP BY 
         CHALLENGE_ID) AS E ON C.CHALLENGE_ID = E.CHALLENGE_ID
GROUP BY 
    A.CONTEST_ID, 
    A.HACKER_ID, 
    A.NAME
HAVING 
    (TOTAL_SUBMISSIONS + TOTAL_ACCEPTED_SUBMISSIONS + TOTAL_VIEWS + TOTAL_UNIQUE_VIEWS) > 0 
ORDER BY 
    A.CONTEST_ID;

 

/*Explanation:

SELECT: Specifies the columns to be retrieved, including the contest ID, hacker ID, name, and aggregated statistics.
FROM: Defines the primary table to select data from (CONTESTS) and joins it with other tables (COLLEGES, CHALLENGES) using LEFT JOIN.
Subqueries (D and E): Calculate aggregated statistics from VIEW_STATS and SUBMISSION_STATS tables for each challenge.
GROUP BY: Groups the results by contest ID, hacker ID, and name.
HAVING: Filters out records where the sum of all statistics (submissions, accepted submissions, views, and unique views) is greater than 0.
ORDER BY: Orders the results by contest ID.*/