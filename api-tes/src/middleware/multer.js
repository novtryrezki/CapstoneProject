const multer = require("multer");
const express = require("express");
const app = express();

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, "public/images");
  },
  filename: (req, file, cb) => {
    const timestamp = new Date().getTime();
    const originalname = file.originalname;
    // const extension = path.extname(file.originalname);

    cb(null, `${timestamp}-${originalname}`);
  },
});

const upload = multer({
  storage: storage,
  limits: {
    fileSize: 3 * 1000 * 1000, // 3 MB
  },
});

app.use("/profile", express.static("upload/images"));
app.post("/upload", upload.single("profile"), (req, res) => {
  res.json({
    success: 1,
    profile_url: `http://localhost:4000/profile/${req.file.filename}`,
  });
});

function errHandler(err, req, res, next) {
  if (err instanceof multer.MulterError) {
    res.json({
      success: 0,
      message: err.message,
    });
  }
}
app.use(errHandler);

module.exports = upload;
