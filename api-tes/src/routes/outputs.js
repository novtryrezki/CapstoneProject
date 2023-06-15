const express = require("express");

const OutputController = require("../controller/outputs.js");

const router = express.Router();

//CREATE GET
router.get("/:id", OutputController.getOutputs);

module.exports = router;
