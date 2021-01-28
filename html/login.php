<?php
	$con = mysqli_connect("localhost", "root", "Tkdgur0116!", "TEST");

	$email = $_POST["email"];
	$password = $_POST["password"];

	$statement = mysqli_prepare($con, "SELECT * FROM member WHERE email = ? AND password = ?");
	mysqli_stmt_bind_param($statement, "ss", $email, $password);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	//mysqli_stmt_bind_result($statement, $email);

	$response = array();
	//$response["success"] = false;

	if(mysqli_stmt_fetch($statement)){
  $reponse["success"] = true;
  $reponse["email"] = $email;
  $reponse["password"] = $password;
 }

else
{
  $reponse["success"] = false;
 }

  echo json_encode($reponse);
?>