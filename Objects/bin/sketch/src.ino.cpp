#include <Arduino.h>
#line 1 "C:\\Users\\CCATAN~1\\Desktop\\CLOYDH~1\\Objects\\src\\src.ino"
#line 1 "C:\\Users\\CCATAN~1\\Desktop\\CLOYDH~1\\Objects\\src\\src.ino"
#include "B4RDefines.h"

#line 3 "C:\\Users\\CCATAN~1\\Desktop\\CLOYDH~1\\Objects\\src\\src.ino"
void setup();
#line 8 "C:\\Users\\CCATAN~1\\Desktop\\CLOYDH~1\\Objects\\src\\src.ino"
void loop();
#line 3 "C:\\Users\\CCATAN~1\\Desktop\\CLOYDH~1\\Objects\\src\\src.ino"
void setup() {
	b4r_main::initializeProcessGlobals();
	b4r_main::_appstart();
}

void loop() {
	while (true) {
		B4R::scheduler.loop();
	}
}







