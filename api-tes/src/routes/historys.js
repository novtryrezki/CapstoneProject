const express = require("express");

const HistoryController = require("../controller/historys.js");

const router = express.Router();

//CREATE POST
router.post("/", HistoryController.createNewHistory);

module.exports = router;
