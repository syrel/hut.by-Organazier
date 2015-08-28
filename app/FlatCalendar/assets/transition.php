<?php
set_time_limit(0);
/***********CONSTANTS***********/
$settlement = array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2);
$eviction =   array(0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2);
$transition_array[0] =array(0,1,2,3,4,5,6,7,8);  //FREE
$transition_array[1] =array(1,2,2,5,6,7,8,7,8);	 //RENT
$transition_array[2] =array(3,5,7,4,4,6,6,8,8);	 //BOOK
$settlement_array[0][0] = array(0,1,2);			 //FREE - FREE
$settlement_array[0][1] = array(1,2,2);			 //FREE - RENT
$settlement_array[0][2] = array(1,2,2);			 //FREE - BOOK
$settlement_array[1][0] = array(0,1,2);			 //RENT - FREE
$settlement_array[1][1] = array(0,1,2);			 //RENT - RENT
$settlement_array[2][0] = array(0,1,2);			 //BOOK - FREE
$settlement_array[2][2] = array(0,1,2);			 //BOOK - BOOK
$eviction_array[0][0] = array(0,1,2);			 //FREE - FREE
$eviction_array[0][1] = array(0,1,2);			 //FREE - RENT
$eviction_array[0][2] = array(0,1,2);			 //FREE - BOOK
$eviction_array[1][0] = array(1,2,2);			 //RENT - FREE
$eviction_array[1][1] = array(0,1,2);			 //RENT - RENT
$eviction_array[2][0] = array(1,2,2);			 //BOOK - FREE
$eviction_array[2][2] = array(0,1,2);			 //BOOK - BOOK
$array[0] =   array(0,1,2,3,4,5,6,7,8,1,2,2,3,4,4,5,7,5,6,6,6,7,7,8,8,8,2,2,2,4,4,4,5,6,7,8,6,7,8,7,8,6,7,8,6,8,6,6,7,7,8,8,8,8,0,1,2,0,3,4,1,2,3,4,5,5,6,6,7,7,8,1,2,3,4,1,3,2,2,4,4,5,7,5,6,5,6,6,6,7,7,7,8,8,8,2,2,4,4,2,4,2,2,4,4,5,6,7,8,6,7,8,5,6,7,8,6,7,8,6,6,6,6,7,7,7,7,8,8,8,8,0,1,2,0,3,4,0,0,0,0,1,1,1,2,2,3,3,3,4,4,5,5,5,6,6,6,7,7,8,8,1,2,3,4,1,3,1,1,1,1,2,2,2,3,3,3,3,4,4,4,5,7,5,6,5,5,5,6,6,6,7,7,7,8,8,8,2,4,2,4,2,2,2,2,4,4,4,4,6,5,7,8,5,6,7,8,5,5,5,5,6,6,6,6,7,7,7,7,8,8,8,8);
$array[1] =   array(0,1,2,3,4,5,6,7,8,0,1,2,0,3,4,1,2,3,4,5,6,5,7,6,7,8,0,1,2,0,3,4,0,0,0,0,1,1,1,2,2,3,3,3,4,4,5,6,5,7,5,6,7,8,1,2,2,3,4,4,5,7,5,6,6,7,6,8,7,8,8,1,2,3,4,3,1,5,7,5,6,2,2,4,4,5,6,7,8,6,7,8,6,7,8,1,2,3,4,3,1,5,7,5,6,1,1,1,1,2,2,2,3,3,3,3,4,4,4,5,6,7,8,5,6,7,8,5,6,7,8,2,2,2,4,4,4,5,6,7,8,6,7,8,7,8,6,7,8,6,8,6,7,8,7,6,8,7,8,7,8,2,2,4,4,4,2,5,6,7,8,6,7,8,5,6,7,8,6,7,8,2,2,4,4,6,7,8,6,7,8,6,7,8,6,7,8,2,4,4,2,5,6,7,8,5,6,7,8,2,2,2,2,4,4,4,4,5,6,7,8,5,6,7,8,5,6,7,8,5,6,7,8);
/********************************/
$state[0] = 2;
$state[1] = 0;
for ($i = 0; $i < count($array[0]); $i++){
	$AM_NEW = $transition_array[$state[0]][$array[0][$i]];
	$PM_NEW = $transition_array[$state[1]][$array[1][$i]];
	$settlement_NEW = $settlement_array[$state[0]][$state[1]][$settlement[$i]];
	$eviction_NEW = $eviction_array[$state[0]][$state[1]][$eviction[$i]];
	
	$found = 0;
	for ($j = 0; $j < count($array[0]); $j++){
		if ($AM_NEW == $array[0][$j] && $PM_NEW == $array[1][$j]){
			if ($settlement_NEW == $settlement[$j] && $eviction_NEW == $eviction[$j]){
				$array_new[$i] = $j;
				$found = 1;
				break;
			}
		}
	}
	if ($found == 0){
		echo "ERROR! NOT FOUND! CHECK YOUR TABLE! [$i AM_OLD:".$array[0][$i]." PM_OLD:".$array[1][$i]." AM_NEW:".$AM_NEW." PM_NEW:".$PM_NEW." SETT_OLD:".$settlement[$i]." EVIC_OLD:".$eviction[$i]." SETT_NEW:".$settlement_NEW." EVIC_NEW:".$eviction_NEW."]<BR>\n";
	}
}
for ($i =0; $i < count($array_new); $i++){
	$key[$i] = 0;
}
for ($i =0; $i < count($array_new); $i++){
	$array_untransition[$key[$array_new[$i]]][$array_new[$i]] = $i;
	$key[$array_new[$i]]++;
}
echo "-------------------------------------------------------<BR>\n";
for ($i = 0; $i < count($array_new);$i++){
	echo $array_new[$i]."<BR>\n";
}
echo "-------------------------------------------------------<BR>\n";
for ($i = 0; $i < count($array_new);$i++){
	if ($i != 0){
		echo ",".$array_new[$i];
	}
	else {
		echo $array_new[$i];
	}
}
echo "<BR>\n---------------------U N T R A N S I T I O N----------------------------------<BR>\n";
for ($i = 0; $i < count($array_untransition);$i++){
	echo "{";
	for ($j = 0; $j < count($array_new);$j++){
		if ($j == 0){
			if (strlen($array_untransition[$i][$j]) ==0 ){
				echo "-1";
			}
			else {
				echo $array_untransition[$i][$j];
			}
		}
		else {
			if (strlen($array_untransition[$i][$j]) ==0 ){
				echo ",-1";
			}
			else {
				echo ",".$array_untransition[$i][$j];
			}
		}
	}
	echo "},\n";
}
?>