SELECT
	location2.id,
	ST_Distance(
		location1.gps_coordinate,
		location2.gps_coordinate,
		1
	) as distance
FROM
	location AS location1,
	location AS location2
WHERE
	location1.id = 1
AND
	location2.id <> 1
AND
	PtDistWithin(
		location1.gps_coordinate,
		location2.gps_coordinate,
		:distance,
		1
	) = 1
ORDER BY location2.id