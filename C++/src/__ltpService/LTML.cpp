#include "LTML.h"

namespace ltp {
namespace service {

LTML::LTML() : XML4NLP(),
    encoding("utf-8"),
    sentenceNumber(0),
    paragraphNumber(1) {
}

bool LTML::SetEncoding(const string& encodeType){
    encoding = encodeType;
    return true;
}

std::string LTML::GetXMLStr() const {
    std::string xmlStr;
    XML4NLP::SaveDOM(xmlStr);
    return xmlStr;
}

bool LTML::HasSent() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_SENT);
}

bool LTML::HasWS() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_WORD);
}

bool LTML::HasPOS() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_POS);
}

bool LTML::HasNE() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_NE);
}

bool LTML::HasParser() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_PARSER);
}

bool LTML::HasSRL() const {
    return XML4NLP::QueryNote(LTPOption.NOTE_SRL);
}

void LTML::SetOver() {
    sentenceNumber = -1;
}

LTML::~LTML() {
    XML4NLP::ClearDOM();
}

void LTML::InitNull() {
    std::string ltmlNull;
    ltmlNull.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\
            <xml4nlp><note sent=\"n\" word=\"n\" pos=\"n\" ne=\"n\" parser=\"n\" wsd=\"n\" srl=\"n\" />\
            <doc></doc></xml4nlp>");
    XML4NLP::LoadXMLFromString(ltmlNull);
}

int LTML::LoadLtml(const std::string & str) {
    return XML4NLP::LoadXMLFromString(str);
}

void LTML::ClearDOM() {
    return XML4NLP::ClearDOM();
}

int LTML::SaveDOM(const char * filename) {
    return XML4NLP::SaveDOM(filename);
}

int LTML::CountParagraph() const {
    return XML4NLP::CountParagraphInDocument();
}

int LTML::CountSentence(int paragraphIdx) const {
    return XML4NLP::CountSentenceInParagraph(paragraphIdx);
}

int LTML::CountSentence() const {
    return XML4NLP::CountSentenceInDocument();
}

int LTML::GetWords(std::vector<Word> & wordList, 
        int paragraphIdx, 
        int sentenceIdx) {
    if (0 != CheckRange(paragraphIdx, sentenceIdx)) {
        return false;
    }

    vector<Word_t> &eleList = m_document_t.paragraphs[paragraphIdx].sentences[sentenceIdx].words;

    for (int i = 0; i< eleList.size(); ++i) {
        Word w(*(eleList[i].wordPtr));
        wordList.push_back(w);
    }

    return true;
}

int LTML::GetWords(vector<Word> &wordList,
        int globalSentIdx) {
    pair<int, int> paraIdx_sentIdx;
    if (0 != MapGlobalSentIdx2paraIdx_sentIdx(globalSentIdx, 
                paraIdx_sentIdx)) {
        return 0;
    }
    return GetWords(wordList,
            paraIdx_sentIdx.first,
            paraIdx_sentIdx.second);
}

int LTML::GetSentenceContent(string & content, 
        int paragraphIdx, 
        int sentenceIdx) {
    if (0 != CheckRange(paragraphIdx, sentenceIdx)) {
        return false;
    }

    content = m_document_t.paragraphs[paragraphIdx].sentences[sentenceIdx].sentencePtr->Attribute("cont");
    return true;
}

int LTML::GetSentenceContent(string & content, 
        int globalSentIdx) {
    pair<int, int> paraIdx_sentIdx;
    if (0 != MapGlobalSentIdx2paraIdx_sentIdx(globalSentIdx, paraIdx_sentIdx)) return 0;
    content = m_document_t.paragraphs[paraIdx_sentIdx.first].sentences[paraIdx_sentIdx.second].sentencePtr->Attribute("cont");
    return 1;
}

Word LTML::GetWord(int paragraphIdx, int sentenceIdx, int wordIdx) {
    if (0 != CheckRange(paragraphIdx, sentenceIdx, wordIdx) ) {
        Word w;
        cerr<< "GetWord(int, int ,int) out of range"<<endl;
        return w;
    }

    Word w(*(m_document_t.paragraphs[paragraphIdx].sentences[sentenceIdx].words[wordIdx].wordPtr));
    return w;
}

Word LTML::GetWord(int globalWordIdx) {
    int pid, sid, wid;

    return GetWord(pid, sid, wid);
}

void LTML::AddSentence(const vector<Word> &wordList, int paragraphId) {
    if (wordList.empty()){
        return;
    }

    // the LTML is set over handle.
    if(sentenceNumber == -1) {
        throw "AddSentence error: Had been set over, can not be write again";
    } else if (sentenceNumber == 0) {
        if (0 != BuildDOMFrame()) {
            throw "AddSentence error: DOM frame build error";
            return;
        }

        vector<Word>::const_iterator iter = wordList.begin();
        SetNote(NOTE_SENT);
 
        if (iter->HasWS())      { SetNote(NOTE_WORD); }
        if (iter->HasPOS())     { SetNote(NOTE_POS); }
        if (iter->HasNE())      { SetNote(NOTE_NE);  }
        if (iter->HasParser())  { SetNote(NOTE_PARSER); }
        if (iter->HasWSD())     { SetNote(NOTE_WSD); }

        for(int i = 0; i< paragraphNumber; ++i) {
            Paragraph_t paraEle;
            paraEle.paragraphPtr = new TiXmlElement(TAG_PARA);
            paraEle.paragraphPtr->SetAttribute(TAG_ID, i);

            m_document_t.documentPtr->LinkEndChild(paraEle.paragraphPtr);
            m_document_t.paragraphs.push_back(paraEle);
        }
    } else if (true == QueryNote(NOTE_SENT) &&
            false == QueryNote(NOTE_WORD) &&
            false == QueryNote(NOTE_POS) &&
            false == QueryNote(NOTE_NE) && 
            false == QueryNote(NOTE_WSD) && 
            false == QueryNote(NOTE_PARSER)) {
        throw "AddSentence error: Only sentence content can be set, there is no other attribute can be done";
        return;
    }

    if (0 != CheckRange(paragraphId)) {
        throw "AddSentence error: Paragraph id is out of the range!";
        return;
    }

    TiXmlElement* paraPtr = m_document_t.paragraphs[paragraphId].paragraphPtr;
    vector<Sentence_t>& sentVec = m_document_t.paragraphs[paragraphId].sentences;

    Sentence_t sentEle;
    sentEle.sentencePtr = new TiXmlElement(TAG_SENT);
    string contents = "";

    for (vector<Word>::const_iterator iter = wordList.begin(); 
            iter != wordList.end(); 
            ++iter) {
        if (!CheckWordMatch(*iter)) {
            throw "AddSentence error: Some word's attribute missed!";
        }

        Word_t wordEle;

        wordEle.wordPtr = new TiXmlElement(TAG_WORD);
        wordEle.wordPtr->SetAttribute(TAG_ID, sentEle.words.size());

        if (QueryNote(NOTE_WORD)) {
            wordEle.wordPtr->SetAttribute(TAG_CONT, 
                    iter->GetWS().c_str());
            contents += iter->GetWS();
        }

        if (QueryNote(NOTE_POS)) { 
            wordEle.wordPtr->SetAttribute(TAG_POS, 
                    iter->GetPOS().c_str());
        }

        if (QueryNote(NOTE_NE)) { 
            wordEle.wordPtr->SetAttribute(TAG_NE, 
                    iter->GetNE().c_str());
        }

        /*if (QueryNote(NOTE_WSD)) { 
            wordEle.wordPtr->SetAttribute(TAG_WSD, 
                    iter->GetWSD().c_str());
            wordEle.wordPtr->SetAttribute(TAG_WSD_EXP, 
                    iter->GetWSDExplanation().c_str());
        }*/

        if (QueryNote(NOTE_PARSER)) {
            wordEle.wordPtr->SetAttribute(TAG_PSR_PARENT, 
                    iter->GetParserParent());
            wordEle.wordPtr->SetAttribute(TAG_PSR_RELATE, 
                    iter->GetParserRelation().c_str());
        }

        sentEle.sentencePtr->LinkEndChild(wordEle.wordPtr);
        sentEle.words.push_back(wordEle);
    }

    sentEle.sentencePtr->SetAttribute(TAG_ID, 
            sentVec.size());
    sentEle.sentencePtr->SetAttribute(TAG_CONT, 
            contents.c_str());

    paraPtr->LinkEndChild(sentEle.sentencePtr);
    sentVec.push_back(sentEle);
    ++ sentenceNumber;

    return;
}

void LTML::AddSentence(const std::string & sentenceContent, int paragraphId) {

}

bool LTML::SetParagraphNumber(int pid) {
    paragraphNumber = pid;
	return true;
}

bool LTML::CheckWordMatch(const Word & w) {
    if (QueryNote(NOTE_WORD) == true   && w.HasWS() == false)     { return false; }
    if (QueryNote(NOTE_POS) == true    && w.HasPOS() == false)    { return false; }
    if (QueryNote(NOTE_NE) == true     && w.HasNE() == false)     { return false; }
    if (QueryNote(NOTE_WSD) == true    && w.HasWSD() == false)    { return false; }
    if (QueryNote(NOTE_PARSER) == true && w.HasParser() == false) { return false; }
    return true;
}

}   //  end for service
}   //  end for ltp
