INSERT INTO location VALUES (:id,:name,ST_GeomFromText(:coordinate, 4326));
