# Back-End API Development using Node.js and Express.js

To run to support back-end programs, a Restful API was created

<br>

## Summary

Created for the AnimalScope application capstone project from the C23-PS386 group, some of the features included in this API are the POST feature for login, register, and photo upload. The GET feature is also used to retrieve data that has been provided in the database.

<br>

## Main Depedencies

For making this back-end API we use Node.js which is used as a JavaScript runtime environment. The reason we chose node.js is because it is non-blocking, has good execution performance and is fast, and can run on one process at the same time.
As the framework we use express.js because it has direct support from the Google v8 engine, uses the JavaScript programming language, has a caching feature so there is no need to execute code repeatedly, and is able to scale applications quickly with support from Node.js.

<br>

#### Clone this repository

https://github.com/novtryrezki/CapstoneProject.git

#### Initialize the JavaScript project (package.json)

npm init

#### Installed express js as a framework

npm install express

#### Install nodemon to run the application automatically

npm install nodemon

#### Run the API server

npm run start

<br>

### Deploy on Google Cloud Platform

#### 1. Create a new project, then activate billing so that the project can run

#### 2. Create a Cloud SQL instance to connect MySQL database

#### 3. Open the cloud shell and clone the github repository

https://github.com/ayuisti29/app-deploy.git

#### 4. Then enter the cloned folder

cd app-deploy

#### 5. Install node js in the cloned folder

#### 6. Create an .env file and enter the following code to connect to the Mysql database

PORT = 4000
DB_HOST = 34.128.80.191
DB_USERNAME = root
DB_PASSWORD = 12345678
DB_NAME = animal_recognition

#### 7. Then create an app.yaml file and enter the following script

runtime: nodejs18

#### 8. Deploy the API with the following command

gcloud app deploy
