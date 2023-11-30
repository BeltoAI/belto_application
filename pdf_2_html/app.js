// API KEY :
//  michaelvj.132@gmail.com_ffae4a2239b935121f9449adec8d4a059d340b21f73cd8703f231ebb60e8eb9c4cef4947


let key = "##########################"; // Your pdf.co API KEY

var https = require("https");
var path = require("path");
var fs = require("fs");


// `request` module is required for file upload.
// Use "npm install request" command to install.
var request = require("request");
const API_KEY = key;  // My API KEY IS HARDCODED IN THE key VARIABLE



// Source PDF file
const SourceFile = "./same_color_input.pdf";
// Comma-separated list of page indices (or ranges) to process. Leave empty for all pages. Example: '0,2-5,7-'.
const Pages = "";
// PDF document password. Leave empty for unprotected documents.
const Password = "";
// Destination HTML file name
const DestinationFile = "./output.html";
// Set to `true` to get simplified HTML without CSS. Default is the rich HTML keeping the document design.
const PlainHtml = false;
// Set to `true` if your document has the column layout like a newspaper.
const ColumnLayout = false;




// 1. RETRIEVE PRESIGNED URL TO UPLOAD FILE.
getPresignedUrl(API_KEY, SourceFile)
    .then(([uploadUrl, uploadedFileUrl]) => {
        // 2. UPLOAD THE FILE TO CLOUD.
        uploadFile(API_KEY, SourceFile, uploadUrl)
            .then(() => {
                // 3. CONVERT UPLOADED PDF FILE TO HTML
                convertPdfToHtml(API_KEY, uploadedFileUrl, Password, Pages, PlainHtml, ColumnLayout, DestinationFile);
            })
            .catch(e => {
                console.log(e);
            });
    })
    .catch(e => {
        console.log(e);
    });


    function getPresignedUrl(apiKey, localFile) {
        return new Promise(resolve => {
            // Prepare request to `Get Presigned URL` API endpoint
            let queryPath = `/v1/file/upload/get-presigned-url?contenttype=application/octet-stream&name=${path.basename(SourceFile)}`;
            let reqOptions = {
                host: "api.pdf.co",
                path: encodeURI(queryPath),
                headers: { "x-api-key": API_KEY }
            };
            // Send request
            https.get(reqOptions, (response) => {
                response.on("data", (d) => {
                    let data = JSON.parse(d);
                    if (data.error == false) {
                        // Return presigned url we received
                        console.log("Line 68");
                        resolve([data.presignedUrl, data.url]);
                    }
                    else {
                        // Service reported error
                        console.log("Line 73");
                        console.log("getPresignedUrl(): " + data.message);
                    }
                });
            })
                .on("error", (e) => {
                    // Request error
                    console.log("getPresignedUrl(): " + e);
                });
        });
    }



    function uploadFile(apiKey, localFile, uploadUrl) {
        return new Promise(resolve => {
            fs.readFile(SourceFile, (err, data) => {
                request({
                    method: "PUT",
                    url: uploadUrl,
                    body: data,
                    headers: {
                        "Content-Type": "application/octet-stream"
                    }
                }, (err, res, body) => {
                    if (!err) {
                        resolve();
                    }
                    else {
                        console.log("uploadFile() request error: " + e);
                    }
                });
            });
        });
    }
    
    
    function convertPdfToHtml(apiKey, uploadedFileUrl, password, pages, plainHtml, columnLayout, destinationFile) {
        // Prepare request to `PDF To HTML` API endpoint
        var queryPath = `/v1/pdf/convert/to/html`;
    
        // JSON payload for api request
        var jsonPayload = JSON.stringify({
            name: path.basename(destinationFile), password: password, pages: pages, simple: plainHtml, columns: columnLayout, url: uploadedFileUrl
        });
        
        
        var reqOptions = {
            host: "api.pdf.co",
            method: "POST",
            path: queryPath,
            headers: {
                "x-api-key": API_KEY,
                "Content-Type": "application/json",
                "Content-Length": Buffer.byteLength(jsonPayload, 'utf8')
            }
        };
        
        
    // Send request
    var postRequest = https.request(reqOptions, (response) => {
        response.on("data", (d) => {
            response.setEncoding("utf8");
            // Parse JSON response
            let data = JSON.parse(d);
            if (data.error == false) {
                // Download HTML file
                var file = fs.createWriteStream(destinationFile);
                https.get(data.url, (response2) => {
                    response2.pipe(file)
                        .on("close", () => {
                            console.log(`Generated HTML file saved as "${destinationFile}" file.`);
                        });
                });
            }
            else {
                // Service reported error
                console.log("convertPdfToHtml(): " + data.message);
            }
        });
    })
        .on("error", (e) => {
            // Request error
            console.log("convertPdfToHtml(): " + e);
        });

    // Write request data
    postRequest.write(jsonPayload);
    postRequest.end();
}    






