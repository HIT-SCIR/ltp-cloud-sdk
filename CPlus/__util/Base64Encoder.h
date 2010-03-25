#ifndef __BASE64ENCODER_H__
#define __BASE64ENCODER_H__

#include <string>

namespace HIT_IR_LTP{
	class Base64Encoder
	{
		static char baseMap[64];
	public:

		static std::string encodeStr(const std::string& str);
		static void encode(const char* str, int length, char* bkStr);
	};
}
#endif

