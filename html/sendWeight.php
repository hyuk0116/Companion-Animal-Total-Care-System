<?php
$link=mysqli_connect("localhost","root","Tkdgur0116!", "TEST" );  
if (!$link)  
{  
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();  
}  


$weight = $_POST[weight];
$email = $_POST[email];
$date = date("Y-m-d");

mysqli_set_charset($link,"utf8"); 


//petId받아오기
$sql1="select petId from member where email = '$email'";
$result=mysqli_query($link,$sql1);

if($result){  
	$idPet = mysqli_result($result);
	echo $idPet;
}
else {
  echo mysqli_error($link);
}
if($idPet == null) echo "??";


//오늘 날짜가 있는지 체크
$sql3="select * from Weights where idPet = $idPet and date = '$date'";
$result3=mysqli_query($link,$sql3);

if($result3){  
	$check = mysqli_result($result3);
	echo $check;
}
else {
  echo mysqli_error($link);
}



if($check){		//있으면 update
	$sql4="UPDATE Weights SET weight=$weight WHERE idPet = $idPet and date='$date'";
	$result4=mysqli_query($link,$sql4);

	if(!$result4){    
	  echo "<br>SQL문 처리중 에러 발생 : "; 
	  echo mysqli_error($link);
	}	

} 
else{		//없으면 insert

	$sql2="INSERT INTO Weights(idPet,date,weight) VALUES($idPet, '$date', $weight)";
	$result2=mysqli_query($link,$sql2);

	if(!$result2){    
	  echo "<br>SQL문 처리중 에러 발생 : "; 
	  echo mysqli_error($link);
	}

}


function mysqli_result($res,$row=0)
{ 
	$data=mysqli_fetch_row($res);
	return $data[$row];
}

mysqli_close($link);  
  
?>
