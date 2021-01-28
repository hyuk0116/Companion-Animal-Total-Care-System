<?php  

$link=mysqli_connect("localhost","root","Tkdgur0116!", "TEST" );  
if (!$link)  
{  
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();  
}  

mysqli_set_charset($link,"utf8"); 


$sql="select * from FEED";

$result=mysqli_query($link,$sql);
$data = array();   
if($result){  
	while($row=mysqli_fetch_array($result)){
		   array_push($data, 
			   array('idFEED'=>$row[0],
				'FName'=>$row[1],
				'FCompany'=>$row[2],
				'FCountry'=>$row[3],
			    'FPrice'=>$row[4],
				'FAge'=>$row[5],
				'FIngredient'=>$row[6],
				'FiRate'=>$row[7],
				'FImg'=>$row[8],
		));
	 }

header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("FEED"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

}  
else{  
   echo "SQL문 처리중 에러 발생 : "; 
   echo mysqli_error($link);
} 


mysqli_close($link);  
  
?>