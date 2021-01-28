<?php  

$link=mysqli_connect("localhost","root","Tkdgur0116!", "TEST" );  
if (!$link)  
{  
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();  
}  

mysqli_set_charset($link,"utf8"); 

$sql="select * from Disease";

$result=mysqli_query($link,$sql);
$data = array();   
if($result){  
   
   while($row=mysqli_fetch_array($result)){
       array_push($data, 
           array('Did'=>$row[0],
			'Dname'=>$row[1],
			'Symptom'=>$row[2],
			'Prevention'=>$row[3],
			'Species'=>$row[4],
			'Treat'=>$row[5]
       ));
   }

header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("Disease"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

}  
else{  
   echo "SQL문 처리중 에러 발생 : "; 
   echo mysqli_error($link);
} 


mysqli_close($link);  
  
?>