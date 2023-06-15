const LoginsModel = require("../models/logins"); //melakukan pemanggilan file logins pada folder models

const createNewLogin = async (req, res) => {
  const { body } = req;

  if (!body.email || !body.password) {
    return res.status(400).json({
      message: "Data yang anda kirimkan salah",
      data: null,
    });
  }

  try {
    await LoginsModel.createNewLogin(body);
    res.status(201).json({
      message: "Upaya masuk anda telah berhasil",
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
  createNewLogin,
};
