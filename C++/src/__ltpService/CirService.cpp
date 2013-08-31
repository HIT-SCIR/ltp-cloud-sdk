#include "CirService.h"

namespace ltp {
namespace service {

int CirService::Analyze( const std::string& parameters, std::string& message ) {
    // construct the http request
    if(!parameters.empty()){
        param = "s=" + parameters;
    }

    if(isXml){
        param += "&x=y";
    }

    if(isEncoding){
        param += "&c=" + encoding;
    }

    if(!isAllAnalysis){
        param += analysisOptions;
    }

    sockaddr_in sin;
    int sock = socket (AF_INET, SOCK_STREAM, 0); 
    if (sock == -1) {
        return 404;     //not found;
    }
    sin.sin_family = AF_INET;
    sin.sin_port = htons( (unsigned short)ports);

    struct hostent * host_addr = gethostbyname(serverAddress.c_str());

    if(host_addr==NULL) {
        return  400;      //bad request;
    }
    sin.sin_addr.s_addr = *((int*)*host_addr->h_addr_list) ;

    if( connect (sock,(const struct sockaddr *)&sin, sizeof(sockaddr_in) ) == -1 ) {
        return 404;
    }

    SendMessage(sock, uris, param, serverAddress);

    int states;
    int curState = 0;
    bool header = true;
    bool bodies = false;

    while(header){
        char c;
        states = recv(sock, &c, 1, 0);
        if(states<0) {
            header = false;
        }

        switch(curState) {
            case 0:
                if (c == '\r') {
                    ++curState;
                }
                break;
            case 1:
                if(c == '\n') {
                    ++curState;
                } else {
                    curState = 0;
                }
                break;
            case 2:
                if (c == '\r') {
                    ++curState;
                } else {
                    curState = 0;
                }
                break;
            case 3:
                if(c == '\n') {
                    ++curState;
                    header = false;
                    if(message.find("200") != std::string::npos){
                        bodies = true;
                    }
                } else {
                    curState = 0;
                }
                break;
        }
        message += c;
    }

    if (message.find("401") != std::string::npos) {
        return 401;
    }

    message ="";

    if(bodies) {
        char p[1024];
        while((states = recv(sock,p,1023,0)) > 0)  {
            p[states] = '\0';
            message += p;
        }
    } else {
        return 444;
    }
    return 0;
}

bool CirService::SetEncoding(const std::string& en){
    if(en == "utf-8"){
        encoding = "utf8";
        isEncoding = true;
    } else if (en == "gb2312"){
        encoding = "gb2312";
        isEncoding = true;
    } else if (en == "gbk"){
        encoding = "gbk";
        isEncoding = false;
    } else {
        return false;
    }

    return true;
}

void CirService::SetAuthorization(const std::string& au){
    authorization = au;
}

void CirService::SetAnalysisOptions( const std::string& option ) {
    isAllAnalysis = false;

    if (option == LTPOption.WS) {
        analysisOptions = "&t=ws";
        isAllAnalysis = false;
    } else if (option == LTPOption.POS) {
        analysisOptions =  "&t=pos";
        isAllAnalysis = false;
    } else if (option == LTPOption.NE) {
        analysisOptions =  "&t=ner";
        isAllAnalysis = false;
    } else if (option == LTPOption.PARSER) {
        analysisOptions = "&t=dp";
        isAllAnalysis = false;
    } else if (option == LTPOption.SRL) {
        analysisOptions = "&t=srl";
        isAllAnalysis = false;
    } else {
        isAllAnalysis = true;
    }

    return;
}

void CirService::SendMessage(int sock, 
        const std::string& uris,
        const std::string& param,
        const std::string& serverAddress) {
    std::string send_str = "POST " + 
        uris + 
        " HTTP/1.0\r\nAccept: */*\r\nUser-Agent: Mozilla/4.0\r\n";

    send(sock,send_str.c_str(),send_str.length(),0);

    char param_length[100];
    sprintf(param_length,"Content-Length: %d\r\n", param.length());
    send(sock,param_length,strlen(param_length),0);

    send_str = std::string("") 
        + "Accept-Language: zh-cn\r\n"
        + "Accept-Encoding: gzip, deflate\r\n" 
        + "Host: " + serverAddress + "\r\n" 
        + "Content-Type: application/x-www-form-urlencoded\r\n" 
        + authorization 
        + param
        + "\r\n";

    send(sock,send_str.c_str(),send_str.length(),0);
}

void CirService::SetAnalysisAll(){
    isAllAnalysis = true;
}

void CirService::SetXmlOption( bool isXml ) {
    this->isXml = isXml;
}

std::string CirService::GetEncoding() const {
    if (encoding == "utf8") {
        return "utf-8";
    }
    return encoding;
}

bool CirService::IsAuthorized() {
    sockaddr_in sin;
    int sock = socket (AF_INET, SOCK_STREAM, 0);
    if (sock == -1) {
        return false;     //not found;
    }
    sin.sin_family = AF_INET;
    sin.sin_port = htons( (unsigned short)ports);

    struct hostent * host_addr = gethostbyname(serverAddress.c_str());
    if(host_addr==NULL) {
        return  false;      //bad request;
    }
    sin.sin_addr.s_addr = *((int*)*host_addr->h_addr_list) ;

    if( connect (sock,(const struct sockaddr *)&sin, sizeof(sockaddr_in) ) == -1 ) {
        //       std::cout<<"connect failed"<<std::endl;
        return false;
    }

    SendMessage(sock, uris, "s=ltpcwyg", serverAddress);

    int states;
    int curState = 0;
    bool header = true;
    std::string message = "";

    while(header){
        char c;

        states = recv(sock, &c, 1, 0);

        if(states<0){
            header = false;
        }

        switch(curState) {
            case 0:
                if (c == '\r') { ++curState; }
                break;
            case 1:
                if (c == '\n') {
                    ++curState; 
                } else {
                    curState = 0;
                }
                break;
            case 2:
                if (c == '\r') {
                    ++curState;
                } else {
                    curState = 0;
                }
                break;
            case 3:
                if(c == '\n') {
                    ++curState;
                    header = false;

                    if(message.find("200") != std::string::npos){
                        return true;
                    } else if (message.find("401") != std::string::npos) {
                        header = false;
                        return false;
                    }

                } else {
                    curState = 0;
                }
                break;
        }
        message += c;
    }

    return message.find("401") == std::string::npos;
}

}   //  end for namespace service
}   //  end for namespace ltp
