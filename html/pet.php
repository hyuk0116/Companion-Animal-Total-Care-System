<?php  

$link=mysqli_connect("localhost","root","Tkdgur0116!", "TEST" );  
if (!$link)  
{  
   echo "MySQL ���� ���� : ";
   echo mysqli_connect_error();
   exit();  
}  

mysqli_set_charset($link,"utf8"); 


$sql="select * from Pet";

$result=mysqli_query($link,$sql);
$data = array();   
if($result){  
   
   while($row=mysqli_fetch_array($result)){
       array_push($data, 
           array('idPet'=>$row[0],
			'pName'=>$row[1],
			'pSpecies'=>$row[2],
			'pBreed'=>$row[3],
		   'pAge'=>$row[4],

		));
   }

header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("Pet"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

}  
else{  
   echo "SQL�� ó���� ���� �߻� : "; 
   echo mysqli_error($link);
} 


mysqli_close($link);  
  
?>