const express = require("express"); //melakukan import express
const router = express.Router();
const controller = require("../controller/file.controller.js"); //melakukan pemanggilan file controller pada folder controller

let routes = (app) => {
  router.post("/upload", controller.upload);
  router.get("/files", controller.getListFiles);

  app.use(router);
};

module.exports = routes;
