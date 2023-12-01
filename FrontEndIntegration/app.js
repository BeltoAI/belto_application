const express = require('express');
const multer = require('multer');
const path = require('path');
const fs = require('fs-extra'); 
const cron = require('cron');

const app = express();
const port = 3000;

// Serve static files from the 'public' directory
app.use(express.static('public'));

// Middleware to parse URL-encoded bodies (as sent by HTML forms)
app.use(express.urlencoded({ extended: true }));

const uploadsDir = path.join(__dirname, 'uploads');
if (!fs.existsSync(uploadsDir)){
    fs.mkdirSync(uploadsDir);
}

// Set up storage for uploaded files
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/');
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
app.post('/upload', upload.fields([{ name: 'file', maxCount: 1 }, { name: 'camera', maxCount: 1 }]), (req, res) => {
  // Extracting form data and storing in variables
  const firstName = req.body.firstName;
  const lastName = req.body.lastName;
  const dob = req.body.dob;
  const houseNumber = req.body['House Number'];
  const street = req.body.Street;
  const state = req.body.State;
  const zipCode = req.body['Zip Code'];
  const city = req.body.City;
  const phone = req.body.phone;


  // Access the uploaded file through req.files
  const uploadedFile = req.files.file ? req.files.file[0] : (req.files.camera ? req.files.camera[0] : null);
  const isHandFilled = req.body.handFilled === 'on';

  // Log the information to the console
  console.log('Uploaded File:', uploadedFile);
  console.log('Form filled by hand:', isHandFilled);
  console.log('First Name:', firstName);
  console.log('Last Name:', lastName);
  console.log('Date of Birth:', dob);
  console.log('House Number:', houseNumber);
  console.log('Street:', street);
  console.log('State:', state);
  console.log('Zip Code:', zipCode);
  console.log('City:', city);
  console.log('Phone:', phone);

  res.send('File and data uploaded successfully!');
});

// Schedule a cron job to clean up files older than 24 hours
const cleanupJob = new cron.CronJob('0 0 */1 * * *', () => {
  cleanupUploadsFolder();
});

cleanupJob.start();

// Function to manually clean up the "uploads" folder
function cleanupUploadsFolder() {
  const uploadDir = path.join(__dirname, 'uploads');

  fs.readdir(uploadDir, (err, files) => {
    if (err) throw err;

    files.forEach((file) => {
      const filePath = path.join(uploadDir, file);

      fs.unlink(filePath, (err) => {
        if (err) throw err;
        console.log(`Deleted file: ${filePath}`);
      });
    });
  });
}

app.listen(port, () => {
  // Construct the URL for Replit environment
  const url = `https://${process.env.REPL_SLUG}.${process.env.REPL_OWNER}.repl.co`;
  console.log(`Server is running on ${url}`);
});
