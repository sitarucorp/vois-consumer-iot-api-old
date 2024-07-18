#! /bin/bash

#
#		\      //            ||`          .|';
#		 \    //             ||           ||
#		  \  //   .|''|, .|''||   '''|.  '||'  .|''|, `||''|,  .|''|,
#		   \//    ||  || ||  ||  .|''||   ||   ||  ||  ||  ||  ||..||
#		    /     `|..|' `|..||. `|..||. .||.  `|..|' .||  ||. `|...
#

# VoIS Demo Consumer Event IoT Input Data Generation Script
# Script generates sample test records with N $(num_records) csv file to do the development local testing easy


HOME_DIR=/g/vois/workspace/vois-consumer-iot/vois-consumer-iot-api/src/main/resources/
FILE="$HOME_DIR/sample_data.csv"

cd $HOME_DIR
[[ -e $FILE ]] && rm -rf $FILE || touch $FILE

[ $(echo $?) -ne 0 ] && echo "script will unable to continue, check home director path is valid" && exit -1

num_records=100000   # Number of records to generate
output_file=$FILE

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

ONOFFS=("ON", "OFF")
products=(WG 69)

function onoff() {
    rand=$((RANDOM % 2))
    echo ${ONOFFS[$rand]}
}

function products() {
    rand=$((RANDOM % 2))
    echo ${products[$rand]}
}


[ -e $output_file ] && rm -rf $output_file
# Header
echo "DateTime,EventId,ProductId,Latitude,Longitude,Battery,Light,AirplaneMode" > "$output_file"

# Function to generate a random DateTime
generate_datetime() {
  echo "$(date -d "2000-01-01 + $((RANDOM % 20000)) days" +%s)000"
}

# Function to generate a random EventId
generate_event_id() {
  local suffix=$(printf %09d $(echo $((RANDOM % 10000000))))
  echo EI$suffix
}

# Function to generate a random ProductId
generate_product_id() {
  local suffix=$(printf %09d $(echo $((RANDOM % 10000000))))
  echo $(products)$suffix
}

# Function to generate a random Latitude
generate_latitude() {
  echo "$(awk -v min=-90 -v max=90 'BEGIN{srand(); print min + (max-min)*rand()}')"
}

# Function to generate a random Longitude
generate_longitude() {
  echo "$(awk -v min=-180 -v max=180 'BEGIN{srand(); print min + (max-min)*rand()}')"
}

# Function to generate a random Battery level
generate_battery() {
 local random_int=$((RANDOM % 100))
   printf "0.%02d\n" "$random_int"
}

# Function to generate a random Light level
generate_light() {
  onoff
}

# Function to generate a random AirplaneMode
generate_airplane_mode() {
  onoff
}

# Generate records
for ((i=1; i<=$num_records; i++))
do
  datetime=$(generate_datetime)
  event_id=$(generate_event_id)
  product_id=$(generate_product_id)
  latitude=$(generate_latitude)
  longitude=$(generate_longitude)
  battery=$(generate_battery)
  light=$(generate_light)
  airplane_mode=$(generate_airplane_mode)

  echo "$datetime,$event_id,$product_id,$latitude,$longitude,$battery,$light,$airplane_mode" >> "$output_file"
done

echo "Data generation complete. $num_records records saved to $output_file. size = "$(ls -lh $output_file)
