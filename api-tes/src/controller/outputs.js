const OutputsModel = require("../models/outputs");

const getOutputs = async (req, res) => {
  const { id } = req.params;

  try {
    const [data] = await OutputsModel.getOutputs(id);

    res.json({
      message: "GET all outputs success",
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
