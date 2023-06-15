require("dotenv").config(); //melakukan import dan konfigurasi variabel environment yang ada pada file .env
const PORT = process.env.PORT || 5000; //port digunakan jika port yang telah ditentukan tidak dapat diakses
const express = require("express"); //melakukan import express

//memanggil file pada folder routes
const usersRoutes = require("./routes/users.js");
const loginsRoutes = require("./routes/logins.js");
const outputsRoutes = require("./routes/outputs.js");
const historysRoutes = require("./routes/historys.js");
const indexRoutes = require("./routes/index.js");

const middlewareLogRequest = require("./middleware/logs.js"); //memanggil file log.js pada folder middleware

const app = express();

app.use(middlewareLogRequest);
app.use(express.json());

//Link
app.use("/users", usersRoutes);
app.use("/logins", loginsRoutes);
app.use("/outputs", outputsRoutes);
app.use("/historys", historysRoutes);
app.use(express.urlencoded({ extended: true }));
indexRoutes(app);

//Beranda
app.get("/beranda", (req, res) => {
  res.send("Selamat Datang di Aplikasi Animal_Recognition");
});

//PORT
app.listen(PORT, () => {
  console.log(`Server berhasil di running di port ${PORT}`);
});
