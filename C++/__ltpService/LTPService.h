#ifndef _LTPSERVICE_H_
#define _LTPSERVICE_H_
#pragma once
#include "..\__ltpService\CirService.h"
#include "Ltml.h"
#include <string>

namespace HIT_IR_LTP{
	class LTPService 
	{
	private:
		CirService ls;
	public:
		LTPService(const std::string& authorization)
			:ls(authorization){};
		~LTPService(void);
	public:
		void Analyze(const std::string& option, const std::string& analyzeString, LTML& ltml_out);
		void Analyze(const std::string& option, const LTML& ltml_in, LTML& ltml_out);
	public:
		bool isAuthorized()
		{
			return ls.isAuthorized();
		}
		bool SetEncoding(const std::string& encodingType)
		{
			return ls.SetEncoding(encodingType);
		}
		void SetAnalysisOptions(const std::string& op)
		{
			return ls.SetAnalysisOptions( op);
		}

	};
}

#endif
