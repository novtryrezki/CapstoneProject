const HistorysModel = require("../models/historys"); //memanggil file historys pada folder models

const createNewHistory = async (req, res) => {
  const { body } = req;

  if (!body.nama_hewan || !body.desk_hewan) {
    return res.status(400).json({
      message: "Data yang anda kirimkan salah",
      data: null,
    });
  }

  try {
    await HistorysModel.createNewHistory(body);
    res.status(201).json({
      message: "Membuat history baru",
      data: body,
    });
  } catch (error) {
    res.status(500).json({
      message: "Server Error",
      serverMessage: error,
    });
  }
};

module.exports = {
  createNewHistory,
};
