<?php

	$con = mysqli_connect("localhost", "root", "Tkdgur0116!", "TEST");
	mysqli_set_charset($con,"utf8"); 
	$result = mysqli_query($con, "SELECT * FROM noticeboard ORDER BY date DESC;");
	$response = array();

	while($row = mysqli_fetch_array($result)){
		array_push($response, array("title"=>$row[0], "content"=>$row[1], "date"=>$row[2]));
	}

	header('Content-Type: application/json; charset=utf8');
	echo json_encode(array("response"=>$response),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);

	mysqli_close($con);
?>
