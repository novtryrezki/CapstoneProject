const OutputsModel = require("../models/outputs"); //memanggil file outputs pada folder models

const getOutputs = async (req, res) => {
  const { id } = req.params;

  try {
    const [data] = await OutputsModel.getOutputs(id);

    res.json({
      message: "Memanggil data hewan berhasil",
      data: data,
    });
  } catch (error) {
    res.status(500).json({
      message: "Server Error",
      serverMessage: error,
    });
  }
};

module.exports = {
  getOutputs,
};
