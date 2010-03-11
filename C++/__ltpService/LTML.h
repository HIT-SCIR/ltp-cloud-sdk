#ifndef _LTML_H_
#define _LTML_H_

#include "..\__xml4nlp\XML4NLP.H"
#include "LTPOption.h"
#include <fstream>

namespace HIT_IR_LTP{
	class LTML{
	private:
		XML4NLP xml;

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
		bool HasWSD() const;
		bool HasSRL() const;
	private:
		bool IsAnalyzed();	//¸Ã·½·¨Î´Ð´

	public:
		int LoadLtml(const std::string & str){
			return xml.LoadXMLFromString(str);
		}
		// clear and save the DOM tree
		void ClearDOM()
		{
			return xml.ClearDOM();
		}
		int SaveDOM(const char* fileName)
		{
			return xml.SaveDOM(fileName);
		}
	private:
		void SaveXMLStr(string &resultStr) const
		{
			return xml.SaveDOM(resultStr);
		}

		// some counting functions
#pragma region counts_zhhan
	public:
		int CountParagraph() const
		{
			return xml.CountParagraphInDocument();
		}
		int CountSentence(int paragraphIdx) const
		{
			return xml.CountSentenceInParagraph(paragraphIdx);
		}
		int CountSentence() const
		{
			return xml.CountSentenceInDocument();
		}
	private:
		int CountWordInSentence(int paragraphIdx, int sentenceIdx) const
		{
			return xml.CountWordInSentence( paragraphIdx, sentenceIdx);
		}
		int CountWordInSentence(int globalSentIdx) const
		{
			return xml.CountWordInSentence( globalSentIdx);
		}
		int CountWordInParagraph(int paragraphIdx) const
		{
			return xml.CountWordInParagraph( paragraphIdx);
		}
		int CountWordInDocument() const
		{
			return xml.CountWordInDocument();
		}
#pragma endregion counts_zhhan

#pragma region word_zhhan
	public:
		bool GetWords(vector<Word> &wordList, int paragraphIdx, int sentenceIdx)
		{
			return xml.GetWords(wordList, paragraphIdx, sentenceIdx);
		}
		bool GetWords(vector<Word> &wordList, int globalSentIdx)
		{
			return xml.GetWordsFromSent(wordList, globalSentIdx);
		}
		bool GetSentenceContent(string &content, int paragraphIdx, int sentenceIdx)
		{
			return xml.GetSentenceContent(content, paragraphIdx, sentenceIdx);
		}
		bool GetSentenceContent(string &content, int globalSentIdx)
		{
			return xml.GetSentenceContent(content, globalSentIdx);
		}
	private:
		Word GetWord( int paragraphIdx, int sentenceIdx, int wordIdx){
			return xml.GetWord(paragraphIdx, sentenceIdx, wordIdx);
		}
		Word GetWord(int globalWordIdx)
		{
			return xml.GetWord(globalWordIdx);
		}

	public:
		//	bool addSentences(const vector<Word> &wordList);
		void AddSentence(const vector<Word> &wordList, int paragraphId)
		{
			try
			{
				xml.AddSentence(wordList, paragraphId);
			}
			catch (const std::exception&)//const std::exception& e)
			{
//				std::cerr<<e.what()<<std::endl;		
				throw;
			}
			return ;
		}
		void AddSentence(const std::string sentenceContent, int paragraphId){
			try {
				xml.AddSentence(sentenceContent, paragraphId);
			}
			catch (const std::exception&)//const std::exception& e)
			{
//				std::cerr<<e.what()<<std::endl;
				throw;
			}
			return;
		}
		bool SetParagraphNumber(int paragraphNumber)
		{
			//		bool result;
			try {
				return xml.SetParagraphNumber(paragraphNumber);
			}
			catch (const std::exception&)//const std::exception& e)
			{
//				std::cerr<<e.what()<<std::endl;
				throw;
			}
			return true;
			//		return result;
		}
#pragma endregion word_zhhan
	};
}

#endif

