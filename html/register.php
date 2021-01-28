<?php

$con=mysqli_connect("localhost","root","Tkdgur0116!","TEST");

mysqli_set_charset($con,"utf8");


if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$email = $_POST['email'];
$password = $_POST['password'];
$name = $_POST['name'];


$result = mysqli_query($con,"insert into member (email,password,name) values ('$email','$password','$name')");


  if($result){
    echo '회원가입 되었습니다.';
  }

  else{
    echo '중복된 아이디입니다.';
  }

mysqli_close($con);

?>