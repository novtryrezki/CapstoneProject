const HistorysModel = require("../models/historys");

const createNewHistory = async (req, res) => {
  const { body } = req;

  if (!body.nama_hewan || !body.desk_hewan) {
    return res.status(400).json({
      message: "Anda mengirimkan data yang salah",
      data: null,
    });
  }

  try {
    await HistorysModel.createNewHistory(body);
    res.status(201).json({
      message: "CREATE new history success",
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
