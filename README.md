# Blind Man's Stick

A **Smart Cane for Visually Impaired People** made for Level-3 Term-1 **Microcontroller, Microprocessor and Embedded Systems Sessional** Project created by [Abu Nowshed Sakib (1705107)](https://github.com/ansakib), [Md Sultanul Arifin (1805097)](https://github.com/arifinnasif) and [Kamruj Jaman Sheam (1805099)](https://github.com/Sheam1685)

Under the kind supervision of -

* Md. Shariful Islam Bhuyan - Assistant Professor, CSE
* Sukarna Barua - Assistant Professor, CSE
* Md. Masum Mushfiq - Lecturer, CSE

### Hardware Tools Used
* Arduino Uno
* HC-SR04 (Ultrasonic Distance Sensor)
* Servo Motor
* HC-05 (Bluetooth Module)

### Software Tools Used
![Arduino](https://img.shields.io/badge/-Arduino-00979D?style=for-the-badge&logo=Arduino&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white)

### Circuit Diagram
![Circuit Diagram](https://github.com/arifinnasif/Blind-Man-s-Stick/tree/master/.images/circuit_diagram.jpg?raw=true)

### How to get started
#### Arduino
1. Wire up according to the circuit diagram
2. Install and open Arduino IDE
3. Open the Arduino project in the folder named `Arduino`
4. Install these libraries from Arduino IDE's Library Manager
   - [Servo 1.1.18](https://www.arduino.cc/reference/en/libraries/servo/)
   - [HCSR04 2.0.0](https://github.com/Martinsos/arduino-lib-hc-sr04)
   
5. Connect the Arduino UNO through an USB cable (type A/B)
6. Select `Arduino UNO` in `Tools > Board`
7. Upload the code to Arduino

#### Android App
1. Initiate developer mode on your Android device
2. Turn on `USB Debugging` from developer options
3. Connect your android with PC using usb port
4. Install and open Android Studio
5. Open the folder `Android App` in android studio
6. Select your device from device list
7. Click `Run`. An app named `TheStick` will be installed on your device



### Usage
Connect the app to the stick after powering on the device. Whenever there is an obstacle ahead of the stick with 1 meter distance the app would tell the user to **STOP** through an audible message and start its 180° scanning procedure. After that it would suggest the user which way to turn - either **TURN LEFT** or **TURN RIGHT** - whichever direction has more free space. The user need to rotate in that direction within 5 seconds. After which an audio cue would inform the user that it is ready to detect obstacles again



[![HitCount](http://hits.dwyl.com/arifinnasif/Blind-Man-s-Stick.svg)](http://hits.dwyl.com/arifinnasif/Blind-Man-s-Stick)
