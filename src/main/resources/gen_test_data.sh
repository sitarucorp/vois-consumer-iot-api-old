#! /bin/bash

#
#		\      //            ||`          .|';
#		 \    //             ||           ||
#		  \  //   .|''|, .|''||   '''|.  '||'  .|''|, `||''|,  .|''|,
#		   \//    ||  || ||  ||  .|''||   ||   ||  ||  ||  ||  ||..||
#		    /     `|..|' `|..||. `|..||. .||.  `|..|' .||  ||. `|...
#

# VoIS Demo Consumer Event IoT Input Data Generation Script

HOME_DIR=/g/vois/workspace/vois-consumer-iot/vois-consumer-iot-api
FILE="$HOME_DIR/testdata.csv"
cd $HOME_DIR
touch $FILE

[ $(echo $?) -ne 0 ] && echo "script will unable to continue, check home director path is valid" && exit -1

NB_RECORDS=100

#"DateTime" , "EventId" , "ProductId" , "Latitude" , "Longitude" , "Battery" , "Light" , "AirplaneMode"

CONTENT=$(
    cat <<EOF
DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode
1582605077000,10001,WG11155638,51.5185,-0.1736,0.99,OFF,OFF
1582605137000,10002,WG11155638,51.5185,-0.1736,0.99,OFF,OFF
1582605197000,10003,WG11155638,51.5185,-0.1736,0.98,OFF,OFF
1582605257000,10004,WG11155638,51.5185,-0.1736,0.98,OFF,OFF
1582605257000,10005,6900001001,40.73061,-73.935242,0.11,NA,OFF
1582605258000,10006,6900001001,40.73071,-73.935242,0.1,NA,OFF
1582605259000,10007,6900001001,40.73081,-73.935242,0.1,NA,OFF
1582605317000,10008,WG11155800,45.5185,-12.52029,0.11,ON,OFF
1582605377000,10009,WG11155800,45.5186,-12.52027,0.1,ON,OFF
1582605437000,10010,WG11155800,45.5187,-12.52025,0.09,ON,OFF
EOF
)

NUMBERS=$(perl -e 'printf "%03d\n", $_  for (0..999);')
ONOFFS=("ON", "OFF")

function getOnOrOff() {
    rand=$((RANDOM % 2))
    echo ${ONOFFS[$rand]}
}

function getRandomNumberO9() {
 rand=$((RANDOM % 104))
     echo ${NUMBERS[$rand]}
}


get_battery_life() {
 local random_int=$((RANDOM % 100))
   printf "0.%02d\n" "$random_int"
}

generate_random_float() {
  echo "24.284"
}


function genRecord() {
    ts=echo "$(date -d "-3 minutes" +%s)"000
    line="${ts},$(printf "VOIS%03d" {0..999}), $(echo "WGXX$(printf "%03d" {0..999})"),51.5185,-0.1736,$(get_battery_life),$(getOnOrOff),$(getOnOrOff)"

}

echo $CONTENT

for i in {1..4}; do generate_random_float; done
