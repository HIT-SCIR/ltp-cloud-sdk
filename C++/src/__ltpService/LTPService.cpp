#include "LTPService.h"

namespace ltp {
namespace service {

LTPService::LTPService(const std::string & authorization) : 
    ls(authorization) {
}


LTPService::~LTPService(void) {
}

bool LTPService::Analyze(const std::string & option, 
        const std::string& analyzeString, 
        LTML& ltml_out) {

    int flag = -1;
    ls.SetAnalysisOptions(option);
    ls.SetXmlOption(false);
    std::string resultStr;

    if(analyzeString.size() == 0) {
        std::cerr<<"Input analyzeString is null!"<<std::endl;
    } else {
        flag = ls.Analyze(analyzeString, resultStr);
        ltml_out.ClearDOM();
        ltml_out.SetEncoding(ls.GetEncoding());
        ltml_out.LoadLtml(resultStr);
        ltml_out.SetOver();
    }

    return (flag == 0);
}

bool LTPService::Analyze(const std::string& option, 
        const LTML& ltml_in, 
        LTML& ltml_out) {
    int flag = -1;
    ls.SetAnalysisOptions(option);
    ls.SetXmlOption(true);
    std::string resultStr;

    flag = ls.Analyze(ltml_in.GetXMLStr(), resultStr);

    ltml_out.ClearDOM();
    ltml_out.SetEncoding(ls.GetEncoding());
    ltml_out.LoadLtml(resultStr);
    ltml_out.SetOver();

    return (flag == 0);

}

}       //  end for namespace service
}       //  end for namespace ltp
