#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char* ssid = "iDialog 4G - 2";
const char* password = "149dialoghomewifi";
const int ledPin = 10;  //D4
WiFiServer server(80);

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
  server.begin();
  Serial.println("Server started");
  // Print the IP address
  Serial.print("http://");
  Serial.print(WiFi.localIP());
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  // Wait until the client sends some data
  Serial.println("new client");
  while (!client.available()) {
    delay(1);
  }
  // Read the first line of the request
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();
  // Match the request
  if (request.indexOf("/led_on") != -1)  {
    digitalWrite(ledPin, HIGH);
    digitalWrite(LED_BUILTIN, LOW);
  }
  if (request.indexOf("/led_off") != -1)  {
    digitalWrite(ledPin, LOW);
    digitalWrite(LED_BUILTIN, HIGH);
  }
  // Return the response
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println("OK"); //  do not forget this one
  delay(1);
  Serial.println("Client disonnected");
}
