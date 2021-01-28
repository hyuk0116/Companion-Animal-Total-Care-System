<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {

        // �ȵ���̵� �ڵ��� postParameters ������ ������ �̸��� ������ ���� ���� �޽��ϴ�.

        $email=$_POST['email'];
        $password=$_POST['password'];
		$name=$_POST['name'];
		$petId=$_POST['petId'];

       

        
            try{
                // SQL���� �����Ͽ� �����͸� MySQL ������ person ���̺� �����մϴ�. 
                $stmt = $con->prepare('UPDATE member SET password=(:password), petId=(:petId) WHERE email=(:email)');
                $stmt->bindParam(':petId', $petId);
                $stmt->bindParam(':email', $email);
				$stmt->bindParam(':password', $password);

                if($stmt->execute())
                {
                    $successMSG = "���ο� ����ڸ� �߰��߽��ϴ�.";
                }
                else
                {
                    $errMSG = "����� �߰� ����";
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
                email: <input type = "text" email = "email" />
                password: <input type = "text" password = "password" />
				name: <input type = "text" name = "name" />
				petId: <input type = "text" petId = "petId" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>