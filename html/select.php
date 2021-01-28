<?php
	$host = "localhost";
	$user = "root";
	$pw = "Tkdgur0116!";
	$db = "php_test";

$conn = mysqli_connect($host,$user,$pw,$db);

if(mysqli_connect_errno()){
	echo "connect failed".mysqli_connect_error();
}
$result = mysqli_query($conn,"select * from test");
while($row = mysqli_fetch_array($result)){
	echo "이름: ".$row['name'];
	echo "<br>";
	echo "나이: ".$row['age'];
	echo "<br>";
	echo "직업: ".$row['job'];
	echo "<br>";
	echo "이메일: ".$row['email'];
	echo "<br>";
}
mysqli_close($conn);
?>