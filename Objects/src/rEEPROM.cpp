#include "B4RDefines.h"
#ifndef ESP_H
#include <avr/eeprom.h>
#else
#include <EEPROM.h>
#endif
namespace B4R {
	#ifdef ESP_H
	static void EEPROM_begin() {
		static bool initialized = false;
		if (initialized)
			return;
		initialized = true;
		EEPROM.begin(1024);
	}
	#endif
	UInt B4REEPROM::getSize() {
		#ifdef ESP_H
			return 1024;
		#else
			return E2END + 1;
		#endif
	}
	void B4REEPROM::WriteBytes(ArrayByte* Src, UInt Position) {
		#ifdef ESP_H
			EEPROM_begin();
			for (UInt i = 0;i < Src->length;i++)
				EEPROM.write(Position + i, ByteFromArray(Src, i));
			EEPROM.commit();
		#else
			eeprom_update_block(Src->data, (void*)Position, Src->length);
		#endif
		
	}
	ArrayByte* B4REEPROM::ReadBytes(UInt Position, UInt Count) {
		ArrayByte* arr = CreateStackMemoryObject(ArrayByte);
		arr->data = StackMemory::buffer + StackMemory::cp;
		StackMemory::cp += Count;
		arr->length = Count;
		ReadBytes2(Position, Count, arr);
		return arr;
	}
	void B4REEPROM::ReadBytes2(UInt Position, UInt Count, ArrayByte* Dest) {
		#ifdef ESP_H
			EEPROM_begin();
			for (UInt i = 0;i < Count;i++)
				((Byte*)Dest->data)[i] = EEPROM.read(Position + i);
		#else
			eeprom_read_block(Dest->data, (void*)Position, Count);
		#endif
		
	}
	
}