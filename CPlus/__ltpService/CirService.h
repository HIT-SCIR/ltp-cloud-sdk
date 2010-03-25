#ifndef _CIRSERVICE_H_
#define _CIRSERVICE_H_
// #define LINUX_OS
// #define WIN_OS

//For commn
#include <iostream>
#include <string>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "LTPOption.h"
#include "Base64Encoder.h"

#ifdef LINUX_OS
 #include <netdb.h>
#endif

#ifdef WIN_OS
 #include <Winsock2.h>
 #pragma comment(lib,"libws2_32.a")
#endif

namespace HIT_IR_LTP{
	#define SEND_RQ(MSG) send(sock,MSG,strlen(MSG),0);std::cout<<MSG;
	/*
	static const int WS=0;
	static const int POS=1;
	static const int NER=2;
	static const int WSD=3;
	static const int DP=4;
	static const int SRL=5;
	// */

	class CirService{
	public:
		//注意初初始化
		CirService(const std::string& au)
			:serverAddress("ir.hit.edu.cn"), uris("/demo/ltp_ws/ltp"),ports(80),param()
	//    :serverAddress("202.118.250.16"),uris("/ltp"),ports(54321),param()
	//    :serverAddress("192.168.3.134"),uris("/ltp"),ports(12345),param()
	//    :serverAddress("www.hit.edu.cn"),uris("/index.htm"),ports(80),param()
		,encoding("c="),analysisOptions("t="),isEncoding(false),isAllAnalysis(true)
		, authorization("Authorization: Basic " + Base64Encoder::encodeStr(au)), isXml(false)
		{			
			#ifdef WIN_OS
				WSAStartup (0x0101, &WsaData);
			#endif
			authorization += "\r\n\r\n";
		};
		~CirService(){
			#ifdef WIN_OS
				WSACleanup();
			#endif
		};

		bool IsAuthorized();

		int Analyze(const std::string& parameters, std::string& message);
	//    void setS();                //设置向服务器端发送的语句
		// void setSplitSentence(); 	// 设置是否进行分段
		/*
		void setSegmentWord(); 	    // 设置是否进行Word segment
		void setPosTag();		 	// 设置是否进行POS Tagging
		void setNER(); 			    // 设置是否进行Named entity recognition
		void setWSD(); 			    // 设置是否进行Word sense disambiguation
		void setGParser(); 			// 设置是否进行Dependency parser
		void setSRL(); 			    // 设置是否进行Semantic role labeling
		//*/
		bool SetEncoding(const std::string& encodeType);   //设置编码：utf8, gb2312
		std::string GetEncoding() const;
		void SetAuthorization(const std::string& authorization);  //设置授权信息
		//ws, pos, ner, wsd, dp, srl
		void SetAnalysisOptions(const std::string& option);
		void SetAnalysisAll();
		void SetXmlOption(bool isXml);
	private:
		void SendMessage(int sock
						,const std::string& uris
						, const std::string& param
						, const std::string& serverAddress
						 );

	private:
		std::string serverAddress;
		std::string uris;
		int ports;
		std::string param;
		std::string encoding;
		std::string analysisOptions;
		bool isEncoding;            //字符编码
		bool isAllAnalysis;         //全部解析
		std::string authorization;
		bool isXml;
		#ifdef WIN_OS
			WSADATA	WsaData;
		#endif
	};
	#endif

}
