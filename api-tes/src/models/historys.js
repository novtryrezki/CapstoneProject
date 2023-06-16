const dbpool = require("../config/db_animal");

const createNewHistory = (body) => {
  const SQLQuery = `  INSERT INTO historys (nama_hewan, desk_hewan) 
                        VALUES ('${body.nama_hewan}', '${body.desk_hewan}')`;

  return dbpool.execute(SQLQuery);
};

module.exports = {
  createNewHistory,
};
