rs.initiate( {
   _id : "rs_showcase",
   members: [
      { _id: 0, host: "mongodb1:27017" },
      { _id: 1, host: "mongodb2:27017" }
   ]
});
