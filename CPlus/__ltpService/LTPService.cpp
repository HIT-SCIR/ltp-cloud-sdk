#include "LTPService.h"

namespace HIT_IR_LTP{
	LTPService::~LTPService(void)
	{
	}

	bool LTPService::Analyze(const std::string& option, const std::string& analyzeString, LTML& ltml_out)
	{
		int flag = -1;
		ls.SetAnalysisOptions(option);
		ls.SetXmlOption(false);
		std::string resultStr;
		if(analyzeString.size() == 0)
		{
			std::cerr<<"Input analyzeString is null!"<<std::endl;
		}
		else
		{
			//		printf("analyzeString: \n%s\n", analyzeString.c_str());
			flag = ls.Analyze(analyzeString, resultStr);
			//		printf("result: \n%s\n", resultStr.c_str());
			ltml_out.ClearDOM();
			ltml_out.SetEncoding(ls.GetEncoding());
			ltml_out.LoadLtml(resultStr);
			ltml_out.SetOver();
		}
		if (flag==0)
		{
			return true;
		}
		return false;
	}

	bool LTPService::Analyze(const std::string& option, const LTML& ltml_in, LTML& ltml_out)
	{
		int flag = -1;
		ls.SetAnalysisOptions(option);
		ls.SetXmlOption(true);
		std::string resultStr;
		flag = ls.Analyze(ltml_in.GetXMLStr(), resultStr);
		ltml_out.ClearDOM();
		ltml_out.SetEncoding(ls.GetEncoding());
		ltml_out.LoadLtml(resultStr);
		ltml_out.SetOver();
		if (flag==0)
		{
			return true;
		}
		return false;
	}
}
