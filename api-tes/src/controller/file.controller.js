const processFile = require("../middleware/upload"); //memanggil file upload dalam folder middleware
const { format } = require("util");
const { Storage } = require("@google-cloud/storage"); //mengimport @google-cloud/storage

const storage = new Storage({ keyFilename: "capstone-project-389607-92e1bfcb2c87.json" }); //memasukkan nama file key dalam bentuk json
const bucket = storage.bucket("upload-scan"); //menyimpan pada variabel penyimpanan bucket  pada google storage

const upload = async (req, res) => {
  //fungsi untuk mengunggah file pada cloud storage
  try {
    await processFile(req, res);

    if (!req.file) {
      return res.status(400).send({ message: "Tolong Unggah File Foto!" }); //mengeluarkan pesan eror jika tidak file diunggah
    }

    const blob = bucket.file(req.file.originalname);
    const blobStream = blob.createWriteStream({
      resumable: false,
    });

    blobStream.on("error", (err) => {
      res.status(500).send({ message: err.message });
    });

    blobStream.on("finish", async (data) => {
      const publicUrl = format(`https://storage.googleapis.com/${bucket.name}/${blob.name}`);

      try {
        await bucket.file(req.file.originalname).makePublic();
      } catch {
        return res.status(500).send({
          message: `Upload foto berhasil: ${req.file.originalname}, tapi akses publik ditolak!`, //menampilkan pesan bahwa foto telah berhasil di upload, tetapi akses publik ditolak
          url: publicUrl,
        });
      }

      res.status(200).send({
        message: "Selamat upload foto anda berhasil: " + req.file.originalname, //menampilkan pesan bahwa foto telah berhasil di upload
        url: publicUrl,
      });
    });

    blobStream.end(req.file.buffer);
  } catch (err) {
    console.log(err);

    if (err.code == "LIMIT_FILE_SIZE") {
      return res.status(500).send({
        message: "Ukuran file anda melebihi kapasitas 2MB!", //menampilkan pesan eror jika file yang diunggah melebihi kapasitas
      });
    }

    res.status(500).send({
      message: `Tidak bisa mengunggah file: ${req.file.originalname}. ${err}`, //menampilkan pesan eror file tidak bisa diunggah
    });
  }
};

const getListFiles = async (req, res) => {
  try {
    const [files] = await bucket.getFiles();
    let fileInfos = [];

    files.forEach((file) => {
      fileInfos.push({
        name: file.name,
        url: file.metadata.mediaLink,
      });
    });

    res.status(200).send(fileInfos);
  } catch (err) {
    console.log(err);

    res.status(500).send({
      message: "Tidak bisa membaca list file!", //menampilkan pesan eror tidak bisa menampilkan list file
    });
  }
};

module.exports = {
  upload,
  getListFiles,
};
