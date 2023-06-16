require("dotenv").config();
const PORT = process.env.PORT || 5000;
const express = require("express");

const usersRoutes = require("./routes/users.js");
const loginsRoutes = require("./routes/logins.js");
const outputsRoutes = require("./routes/outputs.js");
const historysRoutes = require("./routes/historys.js");

const middlewareLogRequest = require("./middleware/logs.js");
const upload = require("./middleware/multer");

const app = express();

app.use(middlewareLogRequest);
app.use(express.json());

//Link
app.use("/users", usersRoutes);
app.use("/logins", loginsRoutes);
app.use("/outputs", outputsRoutes);
app.use("/historys", historysRoutes);
//Upload Image Local
app.post("/upload", upload.single("image"), (req, res) => {
  res.json({
    message: "upload berhasil",
  });
});

app.use((err, req, res, next) => {
  res.json({
    message: err.message,
  });
});

//Beranda
app.get("/beranda", (req, res) => {
  res.send("Selamat Datang di Aplikasi Animal_Recognition");
});

//PORT
app.listen(PORT, () => {
  console.log(`Server berhasil di running di port ${PORT}`);
});
