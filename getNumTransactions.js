'use strict';

const fs = require('fs');
const https = require('https');

process.stdin.resume();
process.stdin.setEncoding('utf-8');

let inputString = '';
let currentLine = 0;

process.stdin.on('data', function(inputStdin) {
      inputString += inputStdin;
});

process.stdin.on('end', function() {
    inputString = inputString.split('\n');
    main();
});

function readLine() {
      return inputString[currentLine++];
}

const axios = require('axios');

async function getNumTransactions(username) {
    // write your code here
    // API endpoint: https://jsonmock.hackerrank.com/api/article_users?username=<username>
    // API endpoint: https://jsonmock.hackerrank.com/api/transactions?&userId=<userId>
    try {
        const { data } = await axios.get(`https://jsonmock.hackerrank.com/api/article_users?username=${username}`);
        if (data.data && data.data.length !== 0) {
            const userId = data.data[0].id;
            const response = await axios.get(`https://jsonmock.hackerrank.com/api/transactions?&userId=${userId}`);
            return response.data.total;
        } else {
            return "Username Not Found";
        }
    } catch (err) {
        console.log(err);
    }
    
}

async function main() {
    const ws = fs.createWriteStream(process.env.OUTPUT_PATH);
    const username = readLine().trim();
    const result = await getNumTransactions(username);
    ws.write(result.toString());
}





/* Explain

Importing axios: The code begins by importing the axios library, which is a popular HTTP client for Node.js. Axios is used for making HTTP requests to external APIs.

Async Function Definition: The getNumTransactions function is defined as an asynchronous function. This allows the function to use the await keyword to wait for asynchronous operations to complete, such as making HTTP requests.

Try-Catch Block: The function is wrapped in a try-catch block to handle any errors that may occur during execution. If an error occurs during the execution of the try block, it will be caught and logged to the console.

Making HTTP Requests: Inside the try block, two HTTP GET requests are made using axios. The first request fetches data from the API endpoint https://jsonmock.hackerrank.com/api/article_users with the provided username. The response data is destructured to extract the data property.

Checking for Username Existence: The code checks if data.data is not empty, meaning that at least one user with the provided username exists. If the user exists, the code proceeds to extract the user's userId from the response data.

Fetching Transaction Data: With the userId obtained, a second HTTP GET request is made to fetch transaction data from the API endpoint https://jsonmock.hackerrank.com/api/transactions using the obtained userId. The total number of transactions is extracted from the response data.

Returning Results: If the username exists and transaction data is successfully retrieved, the total number of transactions is returned. If the username is not found (i.e., data.data is empty), the function returns the string "Username Not Found".

Error Handling: If any error occurs during the execution of the function (e.g., network errors, API errors), it will be caught in the catch block, and the error will be logged to the console.

Overall, this function asynchronously fetches data from two different API endpoints, handles errors gracefully, and returns the total number of transactions associated with a given username or a message indicating that the username was not found.*/