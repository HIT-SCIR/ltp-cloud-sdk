#include "Base64Encoder.h"

namespace HIT_IR_LTP{
	char Base64Encoder::baseMap[64] = {
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
			,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
			,'0','1','2','3','4','5','6','7','8','9'
			,'+', '/'
		};


	std::string Base64Encoder::encodeStr( const std::string& str )
	{
		char charStr[512] = "";
		encode(str.c_str(), str.length(), charStr);
		std::string strStr(charStr);
		return strStr;
	}

	void Base64Encoder::encode( const char* str, int length, char* bkStr )
	{
		int i = 0, j = 0;
		//*
		for (;i < length - length % 3; i += 3)
		{
			bkStr[j++] = baseMap[(str[i] >> 2) & 0x3F];
			bkStr[j++] = baseMap[((str[i] << 4) & 0x30) + ((str[i +1]>>4 )&0xF)];
			bkStr[j++] = baseMap[((str[i+1] << 2 ) & 0x3C) + ((str[i + 2] >> 6)&0x3)];
			bkStr[j++] = baseMap[str[i+2] & 0x3F];
		}

		if(length % 3 == 1) 
		{
			bkStr[j++] = baseMap[(str[i] >>2) & 0x3F];
			bkStr[j++] = baseMap[(str[i] << 4) & 0x30];
			bkStr[j++] = '=';
			bkStr[j++] = '=';
		}
		else if (length % 3 == 2)
		{
			bkStr[j++] = baseMap[(str[i] >>2) & 0x3F];
			bkStr[j++] = baseMap[((str[i] << 4) & 0x30 ) + ((str[i+1] >> 4) & 0xF)];
			bkStr[j++] = baseMap[(str[i+1] <<2) & 0x3C];
			bkStr[j++] = '=';
		}
		// */
	}
}

