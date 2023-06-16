const express = require("express");

const LoginController = require("../controller/logins.js");

const router = express.Router();

//CREATE POST
router.post("/", LoginController.createNewLogin);

module.exports = router;
