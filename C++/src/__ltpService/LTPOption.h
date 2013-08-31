#ifndef __LTPSERVICE_LTPOPTION_H__
#define __LTPSERVICE_LTPOPTION_H__

#include <string>

struct LTP_Option {
public:
    static const std::string WS;
    static const std::string POS;
    static const std::string NE;
    static const std::string WSD;
    static const std::string PARSER;
    static const std::string SRL;
    static const std::string ALL;

    static const std::string GB2312;
    static const std::string UTF8;
    static const std::string GBK;
public:
    static const char * const TAG_DOC;
    static const char * const TAG_NOTE;
    static const char * const TAG_PARA;
    static const char * const TAG_SENT;
    static const char * const TAG_WORD;
    static const char * const TAG_CONT;
    static const char * const TAG_POS;
    static const char * const TAG_NE;
    static const char * const TAG_PSR_PARENT;
    static const char * const TAG_PSR_RELATE;
    static const char * const TAG_WSD;
    static const char * const TAG_WSD_EXP;
    static const char * const TAG_SRL_ARG;
    static const char * const TAG_SRL_TYPE;
    static const char * const TAG_BEGIN;
    static const char * const TAG_END;
    static const char * const TAG_ID;

    static const char * const NOTE_SENT;
    static const char * const NOTE_WORD;
    static const char * const NOTE_POS;
    static const char * const NOTE_NE;
    static const char * const NOTE_PARSER;
    static const char * const NOTE_WSD;
    static const char * const NOTE_SRL;
    /*
       static const char * const NOTE_CLASS;
       static const char * const NOTE_SUM;
       static const char * const NOTE_CR;
    //*/
};

static LTP_Option LTPOption;

#endif
