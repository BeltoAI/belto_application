# belto_repo

MAKE SURE YOU HAVE NODE.JS AND NPM DOWNLOADED ON YOUR COMPUTER AND GO TO ROOT FOLDER DIRECTORY AND OPEN THE TERMINAL (I USE VSCODE IDE)

---------------------------------------------------------------
RUN THESE COMMANDS TO CREATE NODE APP AND INSTALL DEPENDENCYS


npm init -y

npm install express request cron natural fs-extra multer

npm i tesseract.js --save



----------------------------------------------------------------
YOU CAN START THE FRONT-END APPLICATION WITH COMMAND



node app.js


--> after running this command 
--> go to browser {url}:3000
--> you will see a file input html front end webpage (index.html)
--> the input file is temporarily stored in uploads folder

----------------------------------------------------------------

TRY OUT THE IMG TO TEXT API AND MACHINE LEARNING ALGORITHM IN THE
fromPdf-to-text.js FILE

MAKE SURE YOU HAVE AN PNG OR JPEG IMAGE IN ROOT FOLDER

THIS USES THE tesseract.js and natural.js API's

RUN THE COMMAND 

node fromPdf-to-text.js


------------------------------------------


Changed the host from Localhost 3000 to the replit cloud URL:

https://${process.env.REPL_SLUG}.${process.env.REPL_OWNER}.repl.co

Index.html and styles are in the public folder, including images for the design.

-----------------------------------------

DONT FUCK UP THE API KEY FORMAT

PDF to HTML Service is now added - resulting HTML file goes to output folder






