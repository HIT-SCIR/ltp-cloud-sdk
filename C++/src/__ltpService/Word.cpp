#include "Word.h"

namespace ltp {
namespace service {

Word::Word( const TiXmlElement& e ) : ele(e) {
}

Word::Word( const Word& w ) : ele(w.ele) {
}

Word::Word() :ele("word") {
}

// copy assign
void Word::operator=( const Word& w ) {
    this->ele = w.ele;
}

Word::~Word() {
    ele.Clear();
}

int Word::GetID() const {
    if(ele.Attribute("id")!=NULL) {
        return atoi(ele.Attribute("id"));
    }
    return -1;
}

std::string Word::GetWS() const {
    if (!HasWS()) {
        return "";
    }
    return ele.Attribute("cont");
}

std::string Word::GetPOS() const {
    if (!HasPOS()) {
        return "";
    }

    return ele.Attribute("pos");
}

std::string Word::GetNE() const {
    if (!HasNE()) {
        return "";
    }
    return ele.Attribute("ne");
}

std::string Word::GetWSD() const {
    if (!HasWSD()) {
        return "";
    }
    return ele.Attribute("wsd");
}

//************************************
// Method:    getParseParent
// FullName:  Word::getParseParent
// Access:    public 
// Returns:   int
// Qualifier: when the parent is null, return -3;
//************************************
int Word::GetParserParent() const {
    if(ele.Attribute("parent")!=NULL) {
        return atoi(ele.Attribute("parent"));
    }
    return -3;
}

std::string Word::GetParserRelation() const {
    if (!HasParser()) {
        return "";
    }
    return ele.Attribute("relate");
}

bool Word::IsPredicate() const {
    if(ele.FirstChildElement("arg") != NULL) {
        return true;
    }
    return false;
}

bool Word::GetSRLs( std::vector<SRL> &srls ) {
    TiXmlElement *argPtr = NULL;
    if (( argPtr = ele.FirstChildElement("arg"))!=NULL) {
        SRL srlSingle = {
            argPtr->Attribute("type")
                , atoi(argPtr->Attribute("beg"))
                , atoi(argPtr->Attribute("end"))
        };

        srls.push_back(srlSingle);


        while((argPtr = argPtr->NextSiblingElement("arg")) != NULL) {
            SRL srlSingleTmp = {
                argPtr->Attribute("type")
                    , atoi(argPtr->Attribute("beg"))
                    , atoi(argPtr->Attribute("end"))

            };

            srls.push_back(srlSingleTmp);
        }
        return true;
    }

    return false;
}

void Word::SetID( int id ) {
    ele.SetAttribute("id", id);
}

void Word::SetWS( const std::string& content ) {
    ele.SetAttribute("cont", content.c_str());
}

void Word::SetPOS( const std::string& pos ) {
    ele.SetAttribute("pos", pos.c_str());
}

void Word::SetNE( const std::string& ne ) {
    ele.SetAttribute("ne", ne.c_str());
}

bool Word::HasWS() const {
    if(ele.Attribute("cont") != NULL) {
        return true;
    }

    return false;
}

bool Word::HasPOS() const {
    if(ele.Attribute("pos") != NULL) {
        return true;
    }

    return false;
}

bool Word::HasNE() const{
    if(ele.Attribute("ne") != NULL) {
        return true;
    }

    return false;
}

bool Word::HasParser() const {
    if(ele.Attribute("parent") != NULL 
            && ele.Attribute("relate") != NULL){
        return true;
    }
    return false;
}

bool Word::HasWSD() const {
    if (ele.Attribute("wsd") != NULL && ele.Attribute("wsdexp") != NULL) {
        return true;
    }
    return false;
}

}   //  end for namespace service
}   //  end for namespace ltp
