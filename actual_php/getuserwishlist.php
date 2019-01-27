<?php

include('db_connection.php');
include('index.php');

$securecode =  htmlentities($_POST['securecode'] );
$user_id =  htmlentities($_POST['user_id'] );
// remove back slash from the variable if any...


$securecode =   stripslashes($securecode);  //  "1234567890";//
$user_id =  stripslashes($user_id);

//echo "  outside ";

	$status =0;
	$jsonarray =  array();
	$rowProdJsonArray = array();
	$totalPrice =0;
	$msg ="No Product exist on User wishlist";
	$information = "No Product exist on User wishlist";
	
	$count =0;
	$notExist = true;
	
	

if(isset($securecode)  && !empty($securecode)  && !empty($user_id) ) {

global $conn;
	
	if($conn-> connect_error){
		die(" connecction has failed ". $conn-> connect_error)	;
	}
	// get current date
	
	
	
	
	 $stmt = $conn->prepare("SELECT user_id, prod_id FROM wishlist WHERE user_id=?");
	 $stmt ->bind_param(i, $user_id);
	 $stmt->execute();
	 $stmt->store_result();
	 $stmt->bind_result ( $col1, $col2);
	 
	 while($stmt->fetch() ){
	 
	 		$notExist = false;

	 		$rowUser_id = $col1;
	 		$rowProdJsonArray = $col2;
	 					
	 }
	 
	//$msg = "No Product exist on User 888 cart ". $notExist ; 
	 if( $notExist ){
	 		// user didn't add any product till now
	 		$status =1;
			$msg ="No Product exist on User wishlist";
			$information = "No Product exist on User wishlist";
	 
	 
	 }else {
	 
	 		
	 	$oldarray = json_decode( $rowProdJsonArray, true) ;
	  	
	  	$prodIDexist = false;
	  	
	  	 foreach($oldarray as $arraykey) {
			 //  echo "prod id ".$arraykey['prod_id'];
			 // for each product id get product details from table productdetails  
			 
			 
			 $stmt = $conn->prepare("SELECT prod_id, prod_name, prod_mrp, prod_price,prod_rating,prod_rating_count, prod_img_url FROM productdetails WHERE prod_id=?");
			 $stmt ->bind_param(i, $arraykey['prod_id']);
			 $stmt->execute();
			 $stmt->store_result();
			 $stmt->bind_result ( $col1, $col2, $col3, $col4, $col5,$col6,$col7);
			  
			   while($stmt->fetch() ){
	 
			 			
						$msg =" user wishlist details is here";
			
			       
				$jsonarray[$count] = array(
	 					 'id' => $col1,
	 					 'name' => $col2,	 					 
	 					 'mrp' => $col3,
	 					 'price' => $col4,
	 					 'rating' => $col5,
	 					 'rating_count' => $col6,
	 					 'img_url' => $col7	 );
	 					 
			 	$count = $count+1;				
			 }
			  
			  
			   
		  }
		  
		  $status =1;
		  
		  $information =  $jsonarray;
						
						//$jsonarray;
		//  $msg = "No Product exist on ---cart ". $notExist ;
		  
	 
	 }
	 
  	
	

 	
 	mysqli_close($conn);
 	

 }else{
 
			$status =1;
			$msg ="No Product exist on User wishlist";
			$information = "No Product exist on User wishlist";
 
 
 }
 
 
 	
 	$post_data = array(
 			 'status' => $status,
 			 'msg' => $msg,
 			 'Information' => $information );
 	
 	
 	$post_data= json_encode( $post_data );
 	
 	echo $post_data;
 	
 		

?>