#ifndef __LTPSERVICE_H__
#define __LTPSERVICE_H__

#include "CirService.h"
#include "LTML.h"
#include <string>

namespace ltp {
namespace service {

class LTPService {
private:
    CirService ls;

public:

    LTPService(const std::string& authorization);

    ~LTPService(void);

public:

    bool Analyze(const std::string & option, 
            const std::string & analyzeString, 
            LTML & ltml_out);

    bool Analyze(const std::string & option, 
            const LTML & ltml_in, 
            LTML & ltml_out);

public:

    bool IsAuthorized() {
        return ls.IsAuthorized();
    }

    bool SetEncoding(const std::string& encodingType) {
        return ls.SetEncoding(encodingType);
    }

    void SetAnalysisOptions(const std::string& op) {
        return ls.SetAnalysisOptions(op);
    }
};  //  end for class LTPService

}   //  end for namespace service
}   //  end for namespace ltp

#endif  //  end for define __LTPSERVICE_H__
