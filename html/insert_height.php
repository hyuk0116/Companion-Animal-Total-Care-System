<?php

$con=mysqli_connect("localhost","root","Tkdgur0116!","TEST");

mysqli_set_charset($con,"utf8");


if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$idHeights = $_POST['idHeights'];
$pHeights = $_POST['pHeights'];
$petID = $_POST['petID'];
$date = $_POST['date'];


$result = mysqli_query($con,"insert into Heights (idHeights,pHeights,petID,date) values ('$idHeights','$pHeights','$petID','$date')");


  if($result){
    echo '정상등록.';
  }

  else{
    echo '등록실패.';
  }

mysqli_close($con);

?>