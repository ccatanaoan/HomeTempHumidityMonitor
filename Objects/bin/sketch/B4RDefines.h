#ifndef B4RDefines_h
#define B4RDefines_h

#define B4R_SERIAL
#define B4R_STANDARD_PROMOTIONS

#include <stdarg.h>
#include <Arduino.h>
#include <string.h>
#include <Print.h>
#include <Client.h>
#define AUTO_FLUSH_LOGS true
#define STACK_BUFFER_SIZE 3000
#if defined(ESP_H) || PLATFORM_ID == 0xAE
	#define B4R_MEMORY_ALIGNMENT 3
#else
	#define B4R_MEMORY_ALIGNMENT 0
#endif
#define CHECK_ARRAY_BOUNDS




#include "rCore.h"
#include "rESP8266.h"
#include "rESP8266WiFi.h"
#include "rMQTT.h"
#include "rRandomAccessFile.h"
#include "rESP8266TimeTools.h"


#include "b4r_main.h"

#endif