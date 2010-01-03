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
		void Analyze(LTML& ltml_out, const std::string& option, const std::string& analyzeString);
		void Analyze(LTML& ltml_out, const std::string& option, const LTML& ltml_in);
	public:
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
