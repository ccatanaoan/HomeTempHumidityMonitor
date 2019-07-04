#include "B4RDefines.h"
#include <ESP8266WiFi.h>
#include <TimeLib.h>
#include <EasyNTPClient.h>
namespace B4R {
WiFiUDP udp;
  void B4RESP8266TimeTools::initialize(B4RString* ntpserver, int timezone) {
   	lc =  new (backend) EasyNTPClient(udp, ntpserver->data, timezone);
    return;
  }
	int B4RESP8266TimeTools::getseconds(ulong timestamp) {
    int sek;
    sek = second(timestamp);
  	return sek;
  }
	int B4RESP8266TimeTools::getminute(ulong timestamp) {
 	return minute(timestamp);
  }
	int B4RESP8266TimeTools::gethour(ulong timestamp) {
 	return hour(timestamp);
  }
	int B4RESP8266TimeTools::getweekday(ulong timestamp) {
 	return weekday(timestamp);
  }
	int B4RESP8266TimeTools::getday(ulong timestamp) {
  return day(timestamp);
  }
	int B4RESP8266TimeTools::getmonth(ulong timestamp) {
 	return month(timestamp);
  }
	int B4RESP8266TimeTools::getyear(ulong timestamp) {
 	return year(timestamp);
  }
	int B4RESP8266TimeTools::gethour12(ulong timestamp) {
 	return hourFormat12(timestamp);
  }
	bool B4RESP8266TimeTools::isam(ulong timestamp) {
 	return isAM(timestamp);
  }
	bool B4RESP8266TimeTools::ispm(ulong timestamp) {
 	return isPM(timestamp);
}
 ulong B4RESP8266TimeTools::timestamp() {
 return  lc->getUnixTime(); 
  }
  bool B4RESP8266TimeTools::issummertime_eu(int year, byte month, byte day, byte hour, byte tzHours){
 if (month<3 || month>10) return false; 
 if (month>3 && month<10) return true; 
 if (month==3 && (hour + 24 * day)>=(1 + tzHours + 24*(31 - (5 * year /4 + 4) % 7)) || month==10 && (hour + 24 * day)<(1 + tzHours + 24*(31 - (5 * year /4 + 1) % 7)))
   return true;   // it's summertime
 else
   return false; // it's wintertime 
  }  
}

            