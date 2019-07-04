#pragma once
#include "B4RDefines.h"
#include <TimeLib.h>
#include <EasyNTPClient.h>
//~version: 1.00
namespace B4R {
       
	//~shortname: ESPTimetools
  //~Author - A.Dalebout
	class B4RESP8266TimeTools {
  private:
      uint8_t backend[sizeof(EasyNTPClient)];
		public:
    //~hide
			EasyNTPClient* lc;
       // NTP Timeserver
       // ntpserver= pool.ntp.org or other
       // timezone = 3600 sec for UTC(1) = MEZ
      void initialize(B4RString* ntpserver, int timezone);
			// the second for the given timestamp (0-59)
			int getseconds(ulong timestamp);
      // the minute for the given timestamp (0-59)
      int getminute(ulong timestamp);
      // the hour for the given timestamp (0-23)
      int gethour(ulong timestamp);
      // the weekday for the given timestamp(1=Sunday ... 7=Saturday)
      int getweekday(ulong timestamp);
      // the day for the given timestamp (1 - 31)
      int getday(ulong timestamp);
       // the month for the given timestamp (1= jan... 12 = dec.)
      int getmonth(ulong timestamp);
      // the year for the given timestamp (2019 ,2020 etc)
      int getyear(ulong timestamp);
      // the hour for the given timestamp in 12 hour format (0-12)
      int gethour12(ulong timestamp);
      //returns true the given timestamp is AM
      bool isam(ulong timestamp);
      // returns true the given timestamp is PM
      bool ispm(ulong timestamp);
      // return the current timestamp
      ulong timestamp();
      // EU summertime
      // will return true on summertime and false on wintertime 
      //
      // year = current mear (2019)
      // month = current month
      // day = current day
      // hour = current hour
      // tzhour = default timezone (UTC=0, MEZ=1 .....)
      bool issummertime_eu(int year, byte month, byte day, byte hour, byte tzHours);
    };
    }
    
   