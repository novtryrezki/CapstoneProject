const UsersModel = require("../models/users"); //memanggil file users pada folder models

const createNewUser = async (req, res) => {
  const { body } = req;

  if (!body.userName || !body.email || !body.password) {
    return res.status(400).json({
      message: "Data yang anda kirimkan salah",
      data: null,
    });
  }

  try {
    await UsersModel.createNewUser(body);
    res.status(201).json({
      message: "Registrasi berhasil",
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
  //getAllUsers,
  createNewUser,
  //updateUser,
  //deleteUser,
};
