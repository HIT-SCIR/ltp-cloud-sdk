#include "LTPService.h"

namespace HIT_IR_LTP{
	LTPService::~LTPService(void)
	{
	}

	void LTPService::Analyze(const std::string& option, const std::string& analyzeString, LTML& ltml_out)
	{
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
			ls.Analyze(analyzeString, resultStr);
			//		printf("result: \n%s\n", resultStr.c_str());
			ltml_out.ClearDOM();
			ltml_out.SetEncoding(ls.GetEncoding());
			ltml_out.LoadLtml(resultStr);
			ltml_out.SetOver();
		}
	}

	void LTPService::Analyze(const std::string& option, const LTML& ltml_in, LTML& ltml_out)
	{
		ls.SetAnalysisOptions(option);
		ls.SetXmlOption(true);
		std::string resultStr;
		ls.Analyze(ltml_in.GetXMLStr(), resultStr);
		ltml_out.ClearDOM();
		ltml_out.SetEncoding(ls.GetEncoding());
		ltml_out.LoadLtml(resultStr);
		ltml_out.SetOver();
	}
}
