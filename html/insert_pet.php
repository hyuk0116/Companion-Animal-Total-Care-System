<?php

$con=mysqli_connect("localhost","root","Tkdgur0116!","TEST");

mysqli_set_charset($con,"utf8");


if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$idPet = $_POST['idPet'];
$pName = $_POST['pName'];
$pSpecies = $_POST['pSpecies'];
$pBreed = $_POST['pBreed'];
$pAge = $_POST['pAge'];


$result = mysqli_query($con,"insert into Pet (idPet,pName,pSpecies,pBreed,pAge) values ('$idPet','$pName','$pSpecies','$pBreed','$pAge')");


  if($result){
    echo '정상등록.';
  }

  else{
    echo '등록실패.';
  }

mysqli_close($con);

?>