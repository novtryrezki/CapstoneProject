const dbpool = require("../config/db_animal");

// const getAllUsers = () => {
//   const SQLQuery = "SELECT * FROM users";

//   return dbpool.execute(SQLQuery);
// };

const createNewUser = (body) => {
  const SQLQuery = `  INSERT INTO users (userName, email, password) 
                        VALUES ('${body.userName}', '${body.email}', '${body.password}')`;

  return dbpool.execute(SQLQuery);
};

// const updateUser = (body, idUser) => {
//   const SQLQuery = `  UPDATE users
//                         SET userName='${body.userName}', email='${body.email}', password='${body.password}'
//                         WHERE idUser=${idUser}`;

//   return dbpool.execute(SQLQuery);
// };

//   return dbpool.execute(SQLQuery);
// };
// const deleteUser = (idUser) => {
//   const SQLQuery = `DELETE FROM users WHERE idUser=${idUser}`;
module.exports = {
  //getAllUsers,
  createNewUser,
  //updateUser,
  //deleteUser,
};
