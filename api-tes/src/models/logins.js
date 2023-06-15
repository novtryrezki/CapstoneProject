const dbpool = require("../config/db_animal");

const createNewLogin = (body) => {
  const SQLQuery = `  INSERT INTO logins (email, password) 
                        VALUES ('${body.email}', '${body.password}')`;

  return dbpool.execute(SQLQuery);
};

module.exports = {
  createNewLogin,
};
