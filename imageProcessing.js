'use strict';

const fs = require('fs');

process.stdin.resume();
process.stdin.setEncoding("ascii");
let inputString = "";
let currentLine = 0;

process.stdin.on("data", function (chunk) {
    inputString += chunk;
});
process.stdin.on("end", function () {
    inputString = inputString.split('\n');
    main();
});

function readLine() {
  return inputString[currentLine++];
}

class Size {
    constructor(width, height) {
        this.width = width;
        this.height = height;
    }
}

class Image {
    // Add methods here
    constructor(url, size) {
        this.url = url;
        this.size = size;
    }
    getUrl() {
        return this.url;
    }
    setUrl(url) {
        this.url = url;
    }
    setSize(width, height) {
        this.size = new Size(width, height);
    }
    getSize() {
        return this.size;
    }
    cloneImage() {
        return new Image(this.url, this.size);
    }
}



function main() {
    const ws = fs.createWriteStream(process.env.OUTPUT_PATH);
    
    let images = [];
    
    let numberOfImages = parseInt(readLine().trim());
    
    while (numberOfImages-- > 0) {
        let inputs = readLine().trim().split(' ');
        images.push(new Image(inputs[0], new Size(parseInt(inputs[1]), parseInt(inputs[2]))));
    }

    let numberOfOperations = parseInt(readLine().trim());
    while (numberOfOperations-- > 0) {
        let inputs = readLine().trim().split(' ');
        const image = images[parseInt(inputs[1]) - 1];
        const operation = inputs[0];
        
        switch(operation) {
            case 'Clone':
                images.push(image.cloneImage());
                break;
            case 'UpdateUrl':
                image.setUrl(inputs[2]);
                break;
            case 'UpdateSize':
                image.setSize(parseInt(inputs[2]), parseInt(inputs[3]));
                break;
            default:
                break;
        }
    }
    
    images.forEach((img) => {
        const size = img.getSize();
        ws.write(`${img.getUrl()} ${size.width} ${size.height}\n`);
    })
}

/* Explain 

constructor(url, size): This is the constructor method for the Image class. It initializes a new Image object with the provided url and size. The url parameter represents the URL of the image, and the size parameter represents the dimensions of the image. These parameters are stored as properties of the Image object.

getUrl(): This method returns the URL of the image stored in the url property.

setUrl(url): This method sets a new URL for the image. It takes a url parameter and assigns it to the url property of the Image object.

setSize(width, height): This method sets the size of the image. It takes width and height parameters representing the dimensions of the image and creates a new Size object with these dimensions. This Size object is then assigned to the size property of the Image object.

getSize(): This method returns the size of the image stored in the size property. It returns the Size object representing the dimensions of the image.

cloneImage(): This method creates a clone of the current Image object. It creates a new Image object with the same URL and size as the original object and returns this new Image object.

Overall, this Image class provides basic functionality for working with image objects, including getting and setting the URL and size of an image, as well as cloning an image object.

*/