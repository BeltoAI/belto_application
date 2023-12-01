const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs-extra'); 
const cron = require('cron');
const https = require("https");
const request = require("request");
const app = express();
const port = 3000;

// Serve static files from the 'public' directory
app.use(express.static('public'));

// Middleware to parse URL-encoded bodies (as sent by HTML forms)
app.use(express.urlencoded({ extended: true }));

const uploadsDir = path.join(__dirname, 'uploads');

// Set up storage for uploaded files
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, uploadsDir);
  },
  filename: (req, file, cb) => {
    cb(null, file.originalname);
  },
});

const upload = multer({ storage: storage });

// Serve HTML form
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public/index.html'));
});

// Handle file upload
app.post('/upload', upload.single('file'), (req, res) => {
  const uploadedFile = req.file;

  // Source PDF file
  const SourceFile = uploadedFile.path;
  const API_KEY = "info@limex.world_6141d715dea956e00fed19b0f5554f9f8768ad968e7b17b1d7dbe7df5dd05bb0f58b7f21";
  const Pages = "";
  const Password = "";
  const DestinationFile = "./output/output.html";
  const PlainHtml = false;
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
              .catch(err => {
                console.log('Error in uploadFile:', err);
              });
      })
      .catch(err => {
        console.log('Error in getPresignedUrl:', err);
      });

  function getPresignedUrl(apiKey, localFile) {
    return new Promise((resolve, reject) => {
      let queryPath = `/v1/file/upload/get-presigned-url?contenttype=application/octet-stream&name=${path.basename(localFile)}`;
      let reqOptions = {
        host: "api.pdf.co",
        path: encodeURI(queryPath),
        headers: { 
            "x-api-key": apiKey 
        }
      };
      https.get(reqOptions, (response) => {
        let responseData = '';
        response.on("data", (d) => {
          responseData += d;
        });
        response.on("end", () => {
          let data = JSON.parse(responseData);
          if (data.error == false) {
            resolve([data.presignedUrl, data.url]);
          } else {
            reject(new Error("getPresignedUrl: " + data.message));
          }
        });
      }).on("error", (e) => {
        reject(new Error("getPresignedUrl: " + e.message));
      });
    });
  }

  function uploadFile(apiKey, localFile, uploadUrl) {
    return new Promise((resolve, reject) => {
      fs.readFile(localFile, (err, data) => {
        if (err) {
          reject(err);
          return;
        }
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
          } else {
            reject(new Error("uploadFile: " + err.message));
          }
        });
      });
    });
  }

  function convertPdfToHtml(apiKey, uploadedFileUrl, password, pages, plainHtml, columnLayout, destinationFile) {
    var queryPath = `/v1/pdf/convert/to/html`;

    var jsonPayload = JSON.stringify({
      name: path.basename(destinationFile), password: password, pages: pages, simple: plainHtml, columns: columnLayout, url: uploadedFileUrl
    });

    var reqOptions = {
      host: "api.pdf.co",
      method: "POST",
      path: queryPath,
      headers: {
        "x-api-key": apiKey,
        "Content-Type": "application/json",
        "Content-Length": Buffer.byteLength(jsonPayload, 'utf8')
      }
    };

    var postRequest = https.request(reqOptions, (response) => {
      response.on("data", (d) => {
        response.setEncoding("utf8");
        let data = JSON.parse(d);
        if (data.error == false) {
          var file = fs.createWriteStream(destinationFile);
          https.get(data.url, (response2) => {
            response2.pipe(file)
              .on("close", () => {
                console.log(`Generated HTML file saved as "${destinationFile}" file.`);
              });
          });
        }
        else {
          console.log("convertPdfToHtml(): " + data.message);
        }
      });
    }).on("error", (e) => {
      console.log("convertPdfToHtml(): " + e);
    });

    postRequest.write(jsonPayload);
    postRequest.end();
  }    

  res.send('File and data uploaded successfully!');
});

// Schedule a cron job to clean up files older than 24 hours
const cleanupJob = new cron.CronJob('0 0 */1 * * *', () => {
  cleanupUploadsFolder();
});

cleanupJob.start();

function cleanupUploadsFolder() {
  fs.readdir(uploadsDir, (err, files) => {
    if (err) throw err;

    files.forEach((file) => {
      const filePath = path.join(uploadsDir, file);

      fs.stat(filePath, (err, stats) => {
        if (err) throw err;

        if (Date.now() - stats.mtimeMs > 24 * 3600 * 1000) {
          fs.unlink(filePath, (err) => {
            if (err) throw err;
            console.log(`Deleted file: ${filePath}`);
          });
        }
      });
    });
  });
}

app.listen(port, () => {
  // Check if REPL_SLUG and REPL_OWNER environment variables are available
  if (process.env.REPL_SLUG && process.env.REPL_OWNER) {
    // Construct the URL for Replit environment
    const url = `https://${process.env.REPL_SLUG}.${process.env.REPL_OWNER}.repl.co`;
    console.log(`Server is running on ${url}`);
  } else {
    // Fallback to localhost if running locally or if environment variables are not set
    console.log(`Server is running on http://localhost:${port}`);
  }
});
