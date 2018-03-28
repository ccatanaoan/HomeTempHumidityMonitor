#pragma once
#include "B4RDefines.h"
//~version: 1.00

namespace B4R {
	
	//~shortname: D1Pins
	//Pins constants for WeMos D1 boards.
	class D1Pins {
		public:
		static const Byte D0   = 16;
		static const Byte D1   = 5;
		static const Byte D2   = 4;
		static const Byte D3   = 0;
		static const Byte D4   = 2;
		static const Byte D5   = 14;
		static const Byte D6   = 12;
		static const Byte D7   = 13;
		static const Byte D8   = 15;
		static const Byte RX   = 3;
		static const Byte TX   = 1;
	};
	//~shortname: ESP8266
	class B4RESP8266 {
		public:
			void Restart();
	};
}