#pragma once
#include "B4RDefines.h"

//~version: 1.20
namespace B4R {
	//~shortname: EEPROM
	class B4REEPROM {
		public:
			//Writes the array of bytes to the EEPROM.
			//This method updates the data. Existing stored bytes will not be modified if they are the same.
			//Src - Data to write.
			//Position - Address of the first byte.
			void WriteBytes(ArrayByte* Src, UInt Position);
			//Reads data from the EEPROM and returns it as a new array.
			//Position - Address of the first byte.
			//Count - Number of bytes to read.
			ArrayByte* ReadBytes(UInt Position, UInt Count);
			//Similar to ReadBytes. The data is copied to the passed array.
			void ReadBytes2(UInt Position, UInt Count, ArrayByte* Dest);
			//Returns the size of the EEPROM.
			UInt getSize();
	};
}