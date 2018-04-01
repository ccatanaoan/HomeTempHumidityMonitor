#include <Arduino.h>
#line 1 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
#line 1 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
#include <WiFi.h>
#include <MQTT.h>
#include <M5Stack.h>

const char ssid[] = "RiseAboveThisHome";
const char pass[] = "SteelReserve";

int pwmChannel = 1;
int ledPin = 36;

WiFiClient net;
MQTTClient client;

unsigned long lastMillis = 0;

int MQ7sensorValue = 0;   // value read from the sensor

#line 18 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
void connect();
#line 50 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
void messageReceived(String &topic, String &payload);
#line 60 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
void setup();
#line 79 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
void loop();
#line 18 "C:\\Users\\Cloyd\\Desktop\\Home Beta\\Home\\Home.ino"
void connect() {

  Serial.print("Connecting to WiFi...");
  M5.Lcd.print("Connecting to WiFi...");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    M5.Lcd.print(".");
    delay(1000);
  }

  Serial.println("\Connected to WiFi");
  M5.Lcd.println("Connected to WiFi");

  String clientId = "Cloyd-";
  clientId += String(random(0xffff), HEX);

  Serial.print("\nConnecting to MQTT...");
  M5.Lcd.print("Connecting to MQTT...");
  while (!client.connect(clientId.c_str(), "vynckfaq", "KHSV1Q1qSUUY")) {
    Serial.print(".");
    M5.Lcd.print(".");
    delay(1000);
  }

  Serial.println("\Connected to MQTT");
  M5.Lcd.println("Connected to MQTT");

  client.subscribe("TempHumid");
  client.subscribe("MQ7");
  // client.unsubscribe("/hello");
}

void messageReceived(String &topic, String &payload) {
  //  M5.Lcd.fillScreen(TFT_BLACK);
  //  M5.Lcd.setTextColor(TFT_WHITE, TFT_BLACK);
  //  M5.Lcd.setTextSize(2);
  //  M5.Lcd.setCursor(0,0);
  //  Serial.println("Incoming: " + topic + " - " + payload);
  //  M5.Lcd.println("Incoming: " + topic + " - " + payload);

}

void setup() {
  Serial.begin(115200);
  // Setup the TFT display
  M5.begin();
  M5.Lcd.setTextSize(2);
  ledcSetup(pwmChannel, 10000, 8);
  ledcAttachPin(ledPin, pwmChannel);
  pinMode(ledPin, INPUT);

  WiFi.begin(ssid, pass);

  // Note: Local domain names (e.g. "Computer.local" on OSX) are not supported by Arduino.
  // You need to set the IP address directly.
  client.begin("m14.cloudmqtt.com", 11816, net);
  client.onMessage(messageReceived);

  connect();
}

void loop() {
  client.loop();
  delay(10);  // <- fixes some issues with WiFi stability

  if (!client.connected()) {
    connect();
  }

  // publish a message roughly every second.
  if (millis() - lastMillis > 1000) {
    lastMillis = millis();
    //client.publish("/hello", "world");
  }

  // A) preparation
  // turn the heater fully on

  M5.Lcd.println("1. Turn the heater fully on");
  Serial.println("1. Turn the heater fully on");
  ledcWrite(pwmChannel, 4095);

  // heat for 1 min
  delay(60000);

  // now reducing the heating power: turn the heater to approx 1,4V
  M5.Lcd.println("2. Reducing the heating power to approx 1.5V");
  Serial.println("2. Reducing the heating power to approx 1.5V");
  ledcWrite(pwmChannel, 1228.5); // 4095x1500/5000

  // heat for 90 sec
  delay(90000);

  // B) reading
  // CO2 via MQ7: we need to read the sensor at 5V, but must not let it heat up. So hurry!
  M5.Lcd.println("3. Read the sensor at 5V, but must not let it heat up.");
  Serial.println("3. Read the sensor at 5V, but must not let it heat up.");
  ledcWrite(pwmChannel, 4095);

  delay(50); // Getting an analog read apparently takes 100uSec
  MQ7sensorValue = analogRead(ledPin);

  // C) print the results to the serial monitor
  Serial.print("MQ-7 PPM: ");
  Serial.println(MQ7sensorValue);

  M5.Lcd.fillScreen(TFT_BLACK);
  M5.Lcd.setTextColor(TFT_WHITE, TFT_BLACK);
  M5.Lcd.setTextSize(2);
  M5.Lcd.setCursor(0, 0);
  M5.Lcd.print("*MQ-7 PPM: ");
  M5.Lcd.println(MQ7sensorValue);

  // D) interpretation
  // Detecting range: 20ppm-2000ppm carbon monoxide
  // air quality-cases: < 200 perfect, 200 - 800 normal, > 800 - 1800 high, > 1800 abnormal

  if (MQ7sensorValue <= 200)
  {
    Serial.println("Air Quality: CO perfect");
    M5.Lcd.println("*Air Quality: CO perfect");
  }
  else if ((MQ7sensorValue > 200) || (MQ7sensorValue <= 800)) // || = or
  {
    Serial.println("Air Quality: CO normal");
    M5.Lcd.println("*Air Quality: CO normal");
  }
  else if ((MQ7sensorValue > 800) || (MQ7sensorValue <= 1800))
  {
    Serial.println("Air Quality: CO high");
    M5.Lcd.println("*Air Quality: CO high");
  }
  else if (MQ7sensorValue > 1800)
  {
    //digitalWrite(ledPin, HIGH); // optical information in case of emergency
    Serial.println("Air Quality: ALARM CO very high");
    M5.Lcd.println("*Air Quality: ALARM CO very high");
    // delay(3000);
    // digitalWrite(ledPin, LOW);
  }
  else
  {
    Serial.println("MQ-7 - cant read any value - check the sensor!");
    M5.Lcd.println("*MQ-7 - cant read any value - check the sensor!");
  }

  M5.update();
}

