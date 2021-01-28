<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // 안드로이드 코드의 postParameters 변수에 적어준 이름을 가지고 값을 전달 받습니다.

        $idPet=$_POST['iePet'];
        $pName=$_POST['pName'];
		$pSpecies=$_POST['pSpecies'];
		$pBreed=$_POST['pBreed'];
		$pAge=$_POST['pAge'];

       

        
            try{A
                // SQL문을 실행하여 데이터를 MySQL 서버의 person 테이블에 저장합니다. 
                $stmt = $con->prepare('UPDATE Pet SET pName=(:pName),pSpecies=(:pSpecies),pBreed=(:pBreed),pAge=(:pAge) WHERE idPet=(:idPet)');
                $stmt->bindParam(':idPet', $idPet);
                $stmt->bindParam(':pName', $pName);
				$stmt->bindParam(':pSpecies', $pSpecies);
				$stmt->bindParam(':pBreed', $pBreed);
				$stmt->bindParam(':pAge', $pAge);

                if($stmt->execute())
                {
                    $successMSG = "update pet success";
                }
                else
                {
                    $errMSG = "update pet error";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    

?>


<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                idPet: <input type = "text" idPet = "idPet" />
                pName: <input type = "text" pName = "pName" />
				pSpecies: <input type = "text" pSpecies = "pSpecies" />
				pBreed: <input type = "text" pBreed = "pBreed" />
				pAge: <input type = "text" pAge = "pAge" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>