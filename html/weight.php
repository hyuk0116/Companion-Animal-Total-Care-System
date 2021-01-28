<?php  

$link=mysqli_connect("localhost","root","Tkdgur0116!", "TEST" );  
if (!$link)  
{  
   echo "MySQL ���� ���� : ";
   echo mysqli_connect_error();
   exit();  
}  

mysqli_set_charset($link,"utf8"); 


$sql="select * from Weights";

$result=mysqli_query($link,$sql);
$data = array();   
if($result){  
   
   while($row=mysqli_fetch_array($result)){
       array_push($data, 
           array('idWeights'=>$row[0],
			'idPet'=>$row[1],
			'date'=>$row[2],
			'weight'=>$row[3],
		));
   }

header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("Weight"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

}  
else{  
   echo "SQL�� ó���� ���� �߻� : "; 
   echo mysqli_error($link);
} 


mysqli_close($link);  
  
?>