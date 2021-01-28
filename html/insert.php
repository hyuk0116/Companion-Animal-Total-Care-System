<?php
	$host = "localhost";
	$user = "root";
	$pw = "Tkdgur0116!";
	$db = "php_test";

$conn = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno()){
	echo "connect failed".mysqli_connect_error();
}

$myname = $_POST[myname];
$myage = $_POST[myage];
$myjob = $_POST[myjob];
$myemail = $_POST[myemail];

$sql = mysqli_query($conn,"insert into test(name,age,job,email)
values('$myname', '$myage', '$myjob', '$myemail')");

if(!mysql_query($sql,$conn)){
	die('Error: '.mysql.error());
}
echo "1 record added";

mysqli_close($conn);
?>