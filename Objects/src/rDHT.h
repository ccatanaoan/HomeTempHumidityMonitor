#pragma once
#include "B4RDefines.h"
//~dependson: <dht.h>
#include "dht.h"
namespace B4R {
	//~Version: 2.0
	//~shortname: dht
	//DHT11 and DHT22 lib
	class B4RDht {
		private:
			dht Dht;
		public:
			/**
			*Read value from DHT11 specified pin.
			*Returns 0 if there was a failure.
			*/
			Int Read11(Byte Pin);
			/**
			*Read value from DHT22 specified pin.
			*Returns 0 if there was a failure.
			*/
			Int Read22(Byte Pin);
			/**
			*Read Humidity as Percentage from DHT.
			*Returns 0 if there was a failure.
			*/
			Double GetHumidity();
			/**
			*Read temperature from DHT (Celsius).
			*Returns 0 if there was a failure.
			*/
			Double GetTemperature();	
	};
}