CREATE TABLE location (
  id INTEGER  NOT NULL  ,
  name VARCHAR(45)  NOT NULL  ,
  gps_coordinate GEOMETRY  NOT NULL  ,
PRIMARY KEY(id));
