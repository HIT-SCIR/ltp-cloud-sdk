#ifndef __LTPSERVICE_CIRSERVICE_H__
#define __LTPSERVICE_CIRSERVICE_H__

#include <iostream>
#include <string>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "LTPOption.h"
#include "Base64Encoder.h"

#if defined(_WIN32) || defined(__WIN32__) || defined(__WINDOWS__)
    #define _CIRSERVICE_WIN32_
#else
    #define _CIRSERVICE_POSIX_
#endif

#ifdef _CIRSERVICE_POSIX_
    #include <netdb.h>
#elif _CIRSERVICE_WIN32_
    #include <Winsock2.h>
    #pragma comment(lib,"WS2_32.Lib")
#endif

namespace ltp {
namespace service {

#define SEND_RQ(MSG) send(sock,MSG,strlen(MSG),0);std::cout<<MSG;

class CirService {
public:
    CirService(const std::string& au)
        : serverAddress("api.ltp-cloud.com"),
        uris("/ltp_srv/ltp"), 
        ports(8080), 
        param(), 
        encoding("c="), 
        analysisOptions("t="),
        isEncoding(false), 
        isAllAnalysis(true),
        authorization("Authorization: Basic " + Base64Encoder::encodeStr(au)), 
        isXml(false) {

#ifdef _CIRSERVICE_WIN32_
        WSAStartup (0x0101, &WsaData);
#endif  //  end for _CIRSERVICE_WIN32_

        authorization += "\r\n\r\n";
    };

    ~CirService(){

#ifdef _CIRSERVICE_WIN32_
        WSACleanup();
#endif  //  end for WIN_OS
    };

    bool IsAuthorized();

    int Analyze(const std::string& parameters, std::string& message);
    bool SetEncoding(const std::string& encodeType);
    std::string GetEncoding() const;
    void SetAuthorization(const std::string& authorization);
    void SetAnalysisOptions(const std::string& option);
    void SetAnalysisAll();
    void SetXmlOption(bool isXml);

private:
    void SendMessage(int sock, 
            const std::string& uris,
            const std::string& param,
            const std::string& serverAddress);

private:
    std::string serverAddress;
    std::string uris;
    int ports;
    std::string param;
    std::string encoding;
    std::string analysisOptions;
    bool isEncoding;
    bool isAllAnalysis;
    std::string authorization;
    bool isXml;

#ifdef _CIRSERVICE_WIN32_
    WSADATA	WsaData;
#endif  //  end for WIN_OS
};      //  end for class CIRService

}       //  end for namespace service
}       //  end for namespace ltp
#endif  //  end for define __LTPSERVICE_

