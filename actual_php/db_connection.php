<?php

    define('HOST',  'localhost');
    define('DB1',  'id6615395_shopingapp' );
    define('USER', 'id6615395_abirdas' );
    define('PASS', '8471828696');
    
    $conn = new mysqli(HOST,USER,PASS,DB1);
    
    if($conn->connection_error){
        trigger_error('Database connection has failed '.$conn->connect_error,E_USER_ERROR);
    }

?>