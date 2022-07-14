#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "http://smoke-detector-8175f.firebaseio.com/"
#define FIREBASE_AUTH "sSLJ9JK6T4hnTwA3o7yRKN8ICMzk2dKKJEVPRfls"
#define WIFI_SSID "OnePlus8T"
#define WIFI_PASSWORD "123456789"

int led_white = D0; // led white light is connected with the digital pin D0
int led_blue = D1; // led blue light is connected with the digital pin D1
int buzzer = D2; // buzzer is connected to the digital pin D2
int mq2 = A0; // smoke sensor is connected with the analog pin A0 
int data = 0; // Data input taken from the sensor

FirebaseData firebaseData; 

void setup()
{
  Serial.begin(115200);
  pinMode(led_white, OUTPUT); // Initiating pin for white led 
  pinMode(led_blue, OUTPUT); // Initiating pin for blue led
  pinMode(buzzer, OUTPUT); // Initiating pin for buzzer
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD); // Beginning the wifi
  Serial.print("Connecting to wifi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print("Connection Failed");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH); // Beginning firebase
  Firebase.reconnectWiFi(true); // Connecting firebase with WiFi
}

void loop()
{
   data=analogRead(mq2); // Data read from the pin
   Serial.println(data);
   digitalWrite(led_white,HIGH); // led white on
   Firebase.getString(firebaseData,"Sensor");
   int datavalue=firebaseData.to<int>();
   while(data>datavalue){
    data=analogRead(mq2); // Data read from the pin
    digitalWrite(led_white, LOW); // led white off
    digitalWrite(led_blue, HIGH); // led blue on
    digitalWrite(buzzer,HIGH); //buzzer on
    Firebase.setInt(firebaseData, "Value", data); // Setting the value of data in the firebase database
    Firebase.getString(firebaseData,"Sensor");
    datavalue=firebaseData.to<int>();
    delay(5000);
   }
    digitalWrite(led_white, HIGH); // led white on
    digitalWrite(buzzer,LOW); // led buzzer off
    digitalWrite(led_blue,LOW); // led blue off
   if(Firebase.setInt(firebaseData, "/Value", data)) // Setting the value of data in the firebase database
  {
     Serial.println("Upload successful");
  }
  else{
    Serial.println("Upload failed");
  }
}
