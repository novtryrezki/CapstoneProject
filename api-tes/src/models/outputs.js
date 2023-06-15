const dbpool = require("../config/db_animal");

const getOutputs = (id) => {
  const SQLQuery = `SELECT* FROM outputs WHERE id=${id}`;

  return dbpool.execute(SQLQuery);
};

module.exports = {
  getOutputs,
};
