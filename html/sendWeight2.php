<?php
$link=mysqli_connect("localhost","root","Tkdgur0116!","TEST");  
if (!$link)  
{  
   echo "MySQL ���� ���� : ";
   echo mysqli_connect_error();
   exit();  
}

//$idPet = 1;

$weight = $_POST[weight];
//$email = $_POST[email];
$email = "gaga@naver.com";
$date = date("Y-m-d");
//echo "\$idPet = $idPet";echo "<br/>";
//echo "\$date = $date";echo "<br/>";
//echo "\$weight = $weight";echo "<br/>";

mysqli_set_charset($link,"utf8"); 

$sql1="select petId from member where email = '$email'";
$result=mysqli_query($link,$sql1);
  
if($result){
	$idPet = mysqli_num_rows($result);
	echo $idPet;
}
else {
  echo mysqli_error($link);
}

$sql2="insert into Weights(idPet,date,weight)
values('$idPet','$date','$weight')";


$result2=mysqli_query($link,$sql2);

if(!$result2){    
   echo "<br>2222SQL�� ó���� ���� �߻� : <br>"; 
   echo mysqli_error($link);
}


mysqli_close($link);  
  
?>