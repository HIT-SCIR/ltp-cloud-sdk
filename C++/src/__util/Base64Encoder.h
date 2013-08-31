#ifndef __BASE64ENCODER_H__
#define __BASE64ENCODER_H__

#include <string>

class Base64Encoder {
private:
    static char baseMap[64];
public:
    static std::string encodeStr(const std::string& str);
    static void encode(const char* str, int length, char* bkStr);
};

#endif
