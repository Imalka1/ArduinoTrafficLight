#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WebServer.h>

const char* ssid = "iDialog 4G - 2";
const char* password = "149dialoghomewifi";
const int trigPin1 = 2;  //D4
const int echoPin1 = 0;  //D3
const int trigPin2 = 4;  //D2
const int echoPin2 = 5;  //D1
int duration = 0;
int tempDistance = 0;
boolean isOn1 = false;
boolean isOn2 = false;
int distance = 0;
ESP8266WebServer server(80);

void setup() {
  Serial.begin(9600); //Serial connection
  WiFi.begin(ssid, password);   //WiFi connection
  while (WiFi.status() != WL_CONNECTED) {  //Wait for the WiFI connection completion
    delay(500);
  }
  pinMode(trigPin1, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin1, INPUT); // Sets the echoPin as an Input
  pinMode(trigPin2, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin2, INPUT); // Sets the echoPin as an Input
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);
  server.on("/sensorBody", setDistance);
  server.begin();

  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
    HTTPClient http;    //Declare object of class HTTPClient
    http.begin("http://192.168.9.4:8080/sonicDistance?distance=getDistance"); //Specify request destination
    int httpCode = http.GET(); //Send the request
    if (httpCode > 0) { //Check the returning code
      distance = http.getString().toInt();   //Get the request response payload
    }
    http.end();  //Close connection
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void loop() {
  server.handleClient();

  tempDistance = sonic1();
  if (tempDistance < distance && distance > 0) {
    if (!isOn1) {
      sendRequest(tempDistance);
      digitalWrite(LED_BUILTIN, LOW);
      isOn1 = true;
    }
  } else {
    if (isOn1) {
      sendRequest(tempDistance);
      digitalWrite(LED_BUILTIN, HIGH);
      isOn1 = false;
    }
  }

  delay(10);  //Send a request every 30 seconds

  tempDistance = sonic2();
  if (tempDistance < distance && distance > 0) {
    if (!isOn2) {
      digitalWrite(LED_BUILTIN, LOW);
      sendRequest(tempDistance);
      isOn2 = true;
    }
  } else {
    if (isOn2) {
      digitalWrite(LED_BUILTIN, HIGH);
      sendRequest(tempDistance);
      isOn2 = false;
    }
  }

  delay(10);  //Send a request every 30 seconds
}

int sonic1() {
  digitalWrite(trigPin1, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin1, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin1, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin1, HIGH);
  tempDistance = duration * 0.034 / 2;
  return tempDistance;
}

int sonic2() {
  digitalWrite(trigPin2, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin2, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin2, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin2, HIGH);
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
    }
    http.end();  //Close connection
  } else {
    Serial.println("Error in WiFi connection");
  }
}

void setDistance() {
  distance = server.arg("distance").toInt();
}
