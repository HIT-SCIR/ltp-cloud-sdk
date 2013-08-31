#ifndef __LTPSERVICE_LTML_H__
#define __LTPSERVICE_LTML_H__

#include "Xml4nlp.h"
#include "Word.h"
#include "LTPOption.h"
#include <fstream>

namespace ltp {
namespace service {

class LTML : public XML4NLP {
private:
    std::string encoding;
    int         sentenceNumber;
    int         paragraphNumber;
public:
    LTML();
    ~LTML();

public:
    bool SetEncoding(const std::string& encodeType);
    void SetOver();
    std::string GetXMLStr() const;
    void InitNull();	//initial a null xml;

public:
    bool HasSent() const;
    bool HasWS() const;
    bool HasPOS() const;
    bool HasNE() const;
    bool HasParser() const;
    bool HasSRL() const;

public:
    /*
     * Load LTML from a string
     *
     *  @param[in]  str     the string
     *  @return
     */
    int LoadLtml(const std::string & str);

    /*
     * Clear and save the DOM tree
     */
    void ClearDOM();

    /*
     * Save the DOM tree to file
     *
     *  @param[in]  filename    the filename
     *  @return
     */
    int SaveDOM(const char* filename);

private:
    void SaveXMLStr(string &resultStr) const {
        XML4NLP::SaveDOM(resultStr);
    }

    bool CheckWordMatch(const Word & w);
public:
    /*
     * Get number of paragraph in document
     *
     *  @return int     the number of paragraph
     */
    int CountParagraph() const;
    int CountSentence(int paragraphIdx) const;
    int CountSentence() const;

private:
    int CountWordInSentence(int paragraphIdx, int sentenceIdx) const;
    int CountWordInParagraph(int paragraphIdx) const;

public:
    int GetWords(std::vector<Word> & wordList,
            int paragraphIdx,
            int sentenceIdx);

    int GetWords(std::vector<Word> & wordList,
            int globalIdx);

    int GetSentenceContent(std::string & content, 
            int paragraphIdx,
            int sentenceIdx);

    int GetSentenceContent(std::string & content,
            int globalSentIdx);

private:
    Word GetWord(int paragraphIdx, int sentenceIdx, int wordIdx);
    Word GetWord(int globalWordIdx);

public:
    void AddSentence(const vector<Word> & wordList,
            int paragraphId);

    void AddSentence(const std::string & sentenceContent,
            int paragraphId);

    bool SetParagraphNumber(int paragraphNumber);
};

}       //  end for namespace service
}       //  end for namespace ltp

#endif  //  end for __LTPSERVICE_LTML_H__
