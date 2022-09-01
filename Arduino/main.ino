#include <HCSR04.h>
#include <Servo.h>
#include <SoftwareSerial.h>

Servo myservo;
int position;
int angleArray[180];
double distanceArray[180];
int targetDirection;

#define SERVO_PWM_PIN 9
#define ULTRASONIC_TRIGGER_PIN 6
#define ULTRASONIC_ECHO_PIN 7
#define ARDUNIO_RX_BT_TX 2
#define ARDUNIO_TX_BT_RX 3 // through a 10k/10k pot div

#define MIN_OBS_DISTANCE 100

#define TEMPERATURE 32
#define DEFAULT_DIRECTION 0



UltraSonicDistanceSensor distanceSensor(ULTRASONIC_TRIGGER_PIN, ULTRASONIC_ECHO_PIN);
SoftwareSerial myBT(ARDUNIO_RX_BT_TX, ARDUNIO_TX_BT_RX);

enum State {NORMAL, OBSTACLE_AHEAD_DETECTED, WAITING_FOR_USER_CHANGE_DIRECTION} state;

int calculateDirection(int arraySize){
    double minDistance = 10000;
    int minDistIdx = -1;

    for(int i=0; i<arraySize; i++){
        if((angleArray[i] <= 80 || angleArray[i] >= 100) && distanceArray[i]!=-1 && distanceArray[i]<minDistance){
            minDistance = distanceArray[i];
            minDistIdx = i;
        }
    }
    
    return minDistIdx;
}

void setup () {
    Serial.begin(9600);  // We initialize serial connection so that we could print values from sensor.
    myBT.begin(9600);
    myservo.attach(SERVO_PWM_PIN);

    pinMode(LED_BUILTIN, OUTPUT);
}

void loop () {
    if(state == NORMAL) { // no obstacle
      delay(100);
      Serial.println("NORMAL");
      if(myservo.read() != 90) {
        myservo.write(90);
        delay(15);
      }

      double dist = distanceSensor.measureDistanceCm(TEMPERATURE);
      Serial.println(dist);

      if(dist != -1 && dist <= MIN_OBS_DISTANCE) {
        // notify user through BT that there is an obstacle ahead
        myBT.write("S");
        Serial.println("obstacle detected");
        
        

        state = OBSTACLE_AHEAD_DETECTED;
      }
      
    } else if(state == OBSTACLE_AHEAD_DETECTED) { // self explanatory

      Serial.println("repositoning the scanner to 0 degree...");
      
        int i=0;
        for(position=90; position>=0; position--){
            myservo.write(position); 
            delay(15);
        }
        Serial.println("repositoning complete");

        Serial.println("starting scan...");
        for(position=0; position<=180; position++){
          myservo.write(position); 
            delay(15);
          if(position%5 == 0){
            angleArray[i] = position;
            Serial.print(myservo.read());
            Serial.print(" , ");
            distanceArray[i] = distanceSensor.measureDistanceCm(TEMPERATURE);
            Serial.println(distanceArray[i]);
            i++;
          }
            
        }
        Serial.println("scan complete");

        int directionIdx = calculateDirection(i);
//        double directionAngle = angleArray[directionIdx];
        //myBT.println(direction);
        Serial.println("----------------------");
        if(directionIdx != -1) Serial.println(angleArray[directionIdx]);
        Serial.println("----------------------");
        if(directionIdx == -1 || angleArray[directionIdx] < 90) { 
          Serial.println("----------------------LEFT-------------------");
          myBT.write("L");
        } else {
          Serial.println("----------------------RIGHT-------------------");
          myBT.write("R");
        }
        
        
        
        
        Serial.println("repositoning the scanner to 90 degree...");
        for(position=180; position>=90; position--){
            myservo.write(position); 
            delay(15);
        }
        Serial.println("repositoning complete");
        state=WAITING_FOR_USER_CHANGE_DIRECTION;
    } else if(state == WAITING_FOR_USER_CHANGE_DIRECTION) {
      digitalWrite(LED_BUILTIN, LOW);
      Serial.println("waiting for user to rotate");
      delay(5000);
      state = NORMAL;
      myBT.write("N");
      digitalWrite(LED_BUILTIN, HIGH);
    }
}
