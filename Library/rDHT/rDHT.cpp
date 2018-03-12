#include "B4RDefines.h"
namespace B4R {
	Int B4RDht::Read11(Byte Pin) {
		return Dht.read11(Pin);
	}
	Int B4RDht::Read22(Byte Pin) {
		return Dht.read(Pin);
	}
	Double B4RDht::GetHumidity(){
		return Dht.humidity;
	}
	Double B4RDht::GetTemperature(){
		return Dht.temperature;
	}
}