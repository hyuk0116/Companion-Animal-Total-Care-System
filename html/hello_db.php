<?php
$db = new mysqli("localhost", "root", "Tkdgur0116!", "php_test");
if ($db->connect_errno) die("Connect failed: ".$db->connect_error);
$result = $db->query("SHOW DATABASES;");
echo "<xmp>";
while ($row = $result->fetch_object()) print_r($row);
echo "</xmp>";
$result->close();
$db->close();
?>
