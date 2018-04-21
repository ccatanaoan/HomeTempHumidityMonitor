// Change the appropriate camera in 
// 1. C:\Users\Cloyd\AppData\Local\Arduino15\packages\ArduCAM_ESP8266_UNO\hardware\ArduCAM_ESP8266_UNO\2.2.3\libraries\ArduCAM\memorysaver.h
// 2. Select the ArduCAM ESP8266 UNO board in the Tools/Board for OV5640

#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <Wire.h>
#include <ArduCAM.h>
#include <SPI.h>
#include "memorysaver.h"
#if !(defined ESP8266 )
#error Please select the ArduCAM ESP8266 UNO board in the Tools/Board
#endif

//This demo can only work on OV2640_MINI_2MP or ARDUCAM_SHIELD_V2 platform.
#if !(defined (OV2640_MINI_2MP)||defined (OV5640_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP_PLUS) \
    || defined (OV5642_MINI_5MP) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) \
    ||(defined (ARDUCAM_SHIELD_V2) && (defined (OV2640_CAM) || defined (OV5640_CAM) || defined (OV5642_CAM))))
#error Please select the hardware platform and camera module in the ../libraries/ArduCAM/memorysaver.h file
#endif
// set GPIO16 as the slave select :
const int CS = 16;


//you can change the value of wifiType to select Station or AP mode.
//Default is AP mode.
int wifiType = 0; // 0:Station  1:AP

//AP mode configuration
//Default is arducam_esp8266.If you want,you can change the AP_aaid  to your favorite name
const char *AP_ssid = "arducam_esp8266";
//Default is no password.If you want to set password,put your password here
const char *AP_password = "";

//Station mode you should put your ssid and password
const char *ssid = "RiseAboveThisHome"; // Put your SSID here
const char *password = "SteelReserve"; // Put your PASSWORD here

static IPAddress gateway(192, 168, 0, 1);
static IPAddress subnet(255, 255, 255, 0);

static const size_t bufferSize = 4096;
static uint8_t buffer[bufferSize] = {0xFF};
uint8_t temp = 0, temp_last = 0;
int i = 0;
bool is_header = false;

#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
ArduCAM myCAM(OV2640, CS);
  static IPAddress ip(192, 168, 0, 88); 
  ESP8266WebServer server(80);
#elif defined (OV5642_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) ||(defined (OV5642_CAM))
  ArduCAM myCAM(OV5642, CS);
  static IPAddress ip(192, 168, 0, 89); 
  ESP8266WebServer server(81);
#endif


void start_capture() {
  myCAM.clear_fifo_flag();
  myCAM.start_capture();
}

void camCapture(ArduCAM myCAM) {
  WiFiClient client = server.client();
  uint32_t len  = myCAM.read_fifo_length();
  if (len >= MAX_FIFO_SIZE) //8M
  {
    Serial.println(F("Over size."));
  }
  if (len == 0 ) //0 kb
  {
    Serial.println(F("Size is 0."));
  }
  myCAM.CS_LOW();
  myCAM.set_fifo_burst();
  if (!client.connected()) return;
  String response = "HTTP/1.1 200 OK\r\n";
  response += "Content-Type: image/jpeg\r\n";
  response += "Content-len: " + String(len) + "\r\n\r\n";
  server.sendContent(response);
  i = 0;
  while ( len-- )
  {
    temp_last = temp;
    temp =  SPI.transfer(0x00);
    //Read JPEG data from FIFO
    if ( (temp == 0xD9) && (temp_last == 0xFF) ) //If find the end ,break while,
    {
      buffer[i++] = temp;  //save the last  0XD9
      //Write the remain bytes in the buffer
      if (!client.connected()) break;
      client.write(&buffer[0], i);
      is_header = false;
      i = 0;
      myCAM.CS_HIGH();
      break;
    }
    if (is_header == true)
    {
      //Write image data to buffer if not full
      if (i < bufferSize)
        buffer[i++] = temp;
      else
      {
        //Write bufferSize bytes image data to file
        if (!client.connected()) break;
        client.write(&buffer[0], bufferSize);
        i = 0;
        buffer[i++] = temp;
      }
    }
    else if ((temp == 0xD8) & (temp_last == 0xFF))
    {
      is_header = true;
      buffer[i++] = temp_last;
      buffer[i++] = temp;
    }
  }
}

void serverCapture() {
  myCAM.flush_fifo();
  myCAM.clear_fifo_flag();
  start_capture();
  Serial.println(F("CAM Capturing"));
  int total_time = 0;
  total_time = millis();
  while (!myCAM.get_bit(ARDUCHIP_TRIG, CAP_DONE_MASK));
  total_time = millis() - total_time;
  Serial.print(F("capture total_time used (in miliseconds):"));
  Serial.println(total_time, DEC);
  total_time = 0;
  Serial.println(F("CAM Capture Done."));
  total_time = millis();
  camCapture(myCAM);
  total_time = millis() - total_time;
  Serial.print(F("send total_time used (in miliseconds):"));
  Serial.println(total_time, DEC);
  Serial.println(F("CAM send Done."));
}

void serverStream() {
// Cloyd 4/10/2018
// http://192.168.0.89/?&ql=1 <-- 1 is the resolution in integer. 
//
//#define OV2640_160x120    0 //160x120
//#define OV2640_176x144    1 //176x144
//#define OV2640_320x240    2 //320x240
//#define OV2640_352x288    3 //352x288
//#define OV2640_640x480    4 //640x480
//#define OV2640_800x600    5 //800x600
//#define OV2640_1024x768   6 //1024x768
//#define OV2640_1280x1024  7 //1280x1024
//#define OV2640_1600x1200  8 //1600x1200

//#define OV5642_320x240    0 //320x240
//#define OV5642_640x480    1 //640x480
//#define OV5642_1024x768   2 //1024x768
//#define OV5642_1280x960   3 //1280x960
//#define OV5642_1600x1200  4 //1600x1200
//#define OV5642_2048x1536  5 //2048x1536
//#define OV5642_2592x1944  6 //2592x1944

#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
    myCAM.OV2640_set_JPEG_size(4);
#elif defined (OV5642_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) ||(defined (OV5642_CAM))
    myCAM.OV5642_set_JPEG_size(1);
#endif
 
 WiFiClient client = server.client();

  String response = "HTTP/1.1 200 OK\r\n";
  response += "Content-Type: multipart/x-mixed-replace; boundary=frame\r\n\r\n";
  server.sendContent(response);

  while (client.connected()) {

    start_capture();

    while (!myCAM.get_bit(ARDUCHIP_TRIG, CAP_DONE_MASK));

    size_t len = myCAM.read_fifo_length();
    if (len >= 0x07ffff) {
      Serial.println("Over size.");
      continue;
    } else if (len == 0 ) {
      Serial.println("Size is 0.");
      continue;
    }

    myCAM.CS_LOW();
    myCAM.set_fifo_burst();
#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
    SPI.transfer(0xFF);
#endif
    if (!client.connected()) break;
    response = "--frame\r\n";
    response += "Content-Type: image/jpeg\r\n\r\n";
    server.sendContent(response);

    static uint8_t buffer[bufferSize] = {0xFF};

    while (len) {
      size_t will_copy = (len < bufferSize) ? len : bufferSize;
      SPI.transferBytes(&buffer[0], &buffer[0], will_copy);
      if (!client.connected()) break;
      client.write(&buffer[0], will_copy);
      len -= will_copy;
    }
    myCAM.CS_HIGH();

    if (!client.connected()) break;
  }
}

void handleNotFound() {
  String message = "Server is running!\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  server.send(200, "text/plain", message);

// Cloyd 4/15/2018
// http://192.168.0.89:81/?&ql=1 <-- 1 is the resolution in integer. 
//
//#define OV2640_160x120     0 //160x120
//#define OV2640_176x144    1 //176x144
//#define OV2640_320x240    2 //320x240
//#define OV2640_352x288    3 //352x288
//#define OV2640_640x480    4 //640x480
//#define OV2640_800x600    5 //800x600
//#define OV2640_1024x768   6 //1024x768
//#define OV2640_1280x1024  7 //1280x1024
//#define OV2640_1600x1200  8 //1600x1200

//#define OV5642_320x240    0 //320x240
//#define OV5642_640x480    1 //640x480
//#define OV5642_1024x768   2 //1024x768
//#define OV5642_1280x960   3 //1280x960
//#define OV5642_1600x1200  4 //1600x1200
//#define OV5642_2048x1536  5 //2048x1536
//#define OV5642_2592x1944  6 //2592x1944

if (server.hasArg("ql")) {
    int ql = server.arg("ql").toInt();
#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
    myCAM.OV2640_set_JPEG_size(ql);
#elif defined (OV5640_MINI_5MP_PLUS) || defined (OV5640_CAM)
    myCAM.OV5640_set_JPEG_size(ql);
#elif defined (OV5642_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) ||(defined (OV5642_CAM))
    myCAM.OV5642_set_JPEG_size(ql);
#endif
    delay(1000);
    Serial.println("QL change to: " + server.arg("ql"));
  }
}

///////////////////////////////////////////////////////
//   restart board -- Cloyd                          //
///////////////////////////////////////////////////////
void restartBoard()
{
  ESP.restart();
}

void setup() {
  uint8_t vid, pid;
  uint8_t temp;
#if defined(__SAM3X8E__)
  Wire1.begin();
#else
  Wire.begin();
#endif
  Serial.begin(115200);
  Serial.println(F("ArduCAM Start!"));
  // set the CS as an output:
  pinMode(CS, OUTPUT);
  // initialize SPI:
  SPI.begin();
  SPI.setFrequency(4000000); //4MHz
  //Check if the ArduCAM SPI bus is OK
  myCAM.write_reg(ARDUCHIP_TEST1, 0x55);
  temp = myCAM.read_reg(ARDUCHIP_TEST1);
  if (temp != 0x55) {
    Serial.println(F("SPI1 interface Error!"));
    while (1);
  }
#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
  //Check if the camera module type is OV2640
  myCAM.wrSensorReg8_8(0xff, 0x01);
  myCAM.rdSensorReg8_8(OV2640_CHIPID_HIGH, &vid);
  myCAM.rdSensorReg8_8(OV2640_CHIPID_LOW, &pid);
  if ((vid != 0x26 ) && (( pid != 0x41 ) || ( pid != 0x42 )))
    Serial.println(F("Can't find OV2640 module!"));
  else
    Serial.println(F("OV2640 detected."));
#elif defined (OV5642_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) ||(defined (OV5642_CAM))
  //Check if the camera module type is OV5642
  myCAM.wrSensorReg16_8(0xff, 0x01);
  myCAM.rdSensorReg16_8(OV5642_CHIPID_HIGH, &vid);
  myCAM.rdSensorReg16_8(OV5642_CHIPID_LOW, &pid);
  if ((vid != 0x56) || (pid != 0x42)) {
    Serial.println(F("Can't find OV5642 module!"));
  }
  else
    Serial.println(F("OV5642 detected."));
#endif

  //Change to JPEG capture mode and initialize the OV2640 module
  myCAM.set_format(JPEG);
  myCAM.InitCAM();
#if defined (OV2640_MINI_2MP) || defined (OV2640_CAM)
  myCAM.OV2640_set_JPEG_size(OV2640_320x240);
#elif defined (OV5642_MINI_5MP_PLUS) || defined (OV5642_MINI_5MP) || defined (OV5642_MINI_5MP_BIT_ROTATION_FIXED) ||(defined (OV5642_CAM))
  myCAM.write_reg(ARDUCHIP_TIM, VSYNC_LEVEL_MASK);   //VSYNC is active HIGH
  myCAM.OV5640_set_JPEG_size(OV5642_320x240);
#endif

  myCAM.clear_fifo_flag();
  if (wifiType == 0) {
    if (!strcmp(ssid, "SSID")) {
      Serial.println(F("Please set your SSID"));
      while (1);
    }
    if (!strcmp(password, "PASSWORD")) {
      Serial.println(F("Please set your PASSWORD"));
      while (1);
    }
    // Connect to WiFi network
    Serial.println();
    Serial.println();
    Serial.print(F("Connecting to "));
    Serial.println(ssid);

    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(F("."));
    }

    WiFi.config(ip, gateway, subnet); // remove this line to use DHCP
        
    Serial.println(F("WiFi connected"));
    Serial.println("");
    Serial.println(WiFi.localIP());
  } else if (wifiType == 1) {
    Serial.println();
    Serial.println();
    Serial.print(F("Share AP: "));
    Serial.println(AP_ssid);
    Serial.print(F("The password is: "));
    Serial.println(AP_password);

    WiFi.mode(WIFI_AP);
    WiFi.softAP(AP_ssid, AP_password);
    Serial.println("");
    Serial.println(WiFi.softAPIP());
  }

  // Start the server
  server.on("/capture", HTTP_GET, serverCapture);
  server.on("/stream", HTTP_GET, serverStream);
  server.on("/restart", restartBoard); // Cloyd
  server.onNotFound(handleNotFound);
  server.begin();
  Serial.println(F("Server started"));
}

void loop() {
  server.handleClient();
}

