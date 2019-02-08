#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char* ssid = "iDialog 4G - 2";
const char* password = "149dialoghomewifi";
const int ledPin = 10;  //D4
ESP8266WebServer server(80);

void setup() {
  Serial.begin(9600);
  delay(10);
  pinMode(ledPin, OUTPUT);
  digitalWrite(ledPin, LOW);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);
  // Connect to WiFi network
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  Serial.println("WiFi connected");
  
  // Start the server
  server.on("/ledBody", controlLed);
  server.begin();
  Serial.println("Server started");
  // Print the IP address
  Serial.println(WiFi.localIP());
}

void loop() {
  server.handleClient();
}

void controlLed() {
  if (server.arg("led") == "led_on") {
    digitalWrite(ledPin, HIGH);
    digitalWrite(LED_BUILTIN, LOW);
  } else if (server.arg("led") == "led_off") {
    digitalWrite(ledPin, LOW);
    digitalWrite(LED_BUILTIN, HIGH);
  }
  server.send(200, "text / plain", "OK");
}
