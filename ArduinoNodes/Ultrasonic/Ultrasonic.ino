#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

const char* ssid = "iDialog 4G - 2";
const char* password = "149dialoghomewifi";
const int trigPin = 2;  //D4
const int echoPin = 0;  //D3
long duration = 0;
int tempDistance = 0;
int distance = 0;
boolean isOn = false;

void setup() {
  Serial.begin(9600); //Serial connection
  WiFi.begin(ssid, password);   //WiFi connection
  while (WiFi.status() != WL_CONNECTED) {  //Wait for the WiFI connection completion
    delay(500);
  }
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input

  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://192.168.9.4:8080/sonicDistance?distance=getDistance"); //Specify request destination
    int httpCode = http.GET(); //Send the request
    if (httpCode > 0) { //Check the returning code
      distance = http.getString().toInt();   //Get the request response payload
      Serial.println(distance); //Print the response payload
    }
    http.end();  //Close connection
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void loop() {
  tempDistance = sonic();
  if (tempDistance < distance && distance > 0) {
    if (!isOn) {
      sendRequest(tempDistance);
      isOn = true;
    }
  } else {
    if (isOn) {
      sendRequest(tempDistance);
      isOn = false;
    }
  }
  delay(100);  //Send a request every 30 seconds
}

int sonic() {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  tempDistance = duration * 0.034 / 2;
  return tempDistance;
}

void sendRequest(int tempDistance) {
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://192.168.9.4:8080/sonicDistance?distance=" + String(tempDistance) + "&mac=" + WiFi.macAddress()); //Specify request destination
    int httpCode = http.GET(); //Send the request
    if (httpCode > 0) { //Check the returning code
      String payload = http.getString();   //Get the request response payload
      Serial.println(payload); //Print the response payload
    }
    http.end();  //Close connection
  } else {
    Serial.println("Error in WiFi connection");
  }
  Serial.print("Distance: ");
  Serial.println(tempDistance);
}
