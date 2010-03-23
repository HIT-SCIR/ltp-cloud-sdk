/************************************************************************/
/*	
	PROGRAM:	example9
				对SERVICE及LTML重新封装后的测试程序
	HISTORY:	2009/12/23	韩中华	FIRST RELEASE
				zhhan@ir.hit.edu.cn
				哈工大信息检索研究中心
*/
/************************************************************************/

#include "LTPOption.h"
#include "LTPService.h"
#include "LTML.h"
#include "Word.h"
 
using namespace HIT_IR_LTP;
int main_1(){
	LTPService ls("username:password");
	if (!ls.IsAuthorized())
	{
		cerr<<"Authorization is denied!"<<endl;
		exit(EXIT_FAILURE);
	}

	LTML ltml;

	vector<Word> wordList;
	Word w1;
	w1.SetWS("我");
	w1.SetPOS("r");
	wordList.push_back(w1);

	Word w;
	w.SetWS("爱");
	w.SetPOS("v");
	wordList.push_back(w);

	w.SetWS("北京");
	w.SetPOS("ns");
	wordList.push_back(w);

	w.SetWS("天安门");
	w.SetPOS("ns");
	wordList.push_back(w);

	try{

		ltml.AddSentence(wordList, 0);

		ls.Analyze(LTPOption.WSD, ltml, ltml);
		cout<<ltml.GetXMLStr()<<endl;
		int sentNum = ltml.CountSentence();
		for ( int i = 0; i<sentNum; ++i) { 
			string sentCont;
			ltml.GetSentenceContent(sentCont, i);
			cout<< sentCont <<endl;
			vector<Word> wordList;
			ltml.GetWords(wordList, i);
			for( vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter ){
				if (ltml.HasWS())
				{
					cout<<iter->GetWS()<<"\t"<<iter->GetID();
				}
				if (ltml.HasPOS())
				{
					cout<<"\t"<<iter->GetPOS();
				}
				if(ltml.HasNE())
				{
					cout<<"\t"<<iter->GetNE();
				}
				if(ltml.HasParser())
				{
					cout<<"\t"<<iter->GetParserParent()<<"\t"<<iter->GetParserRelation();
				}
				if(ltml.HasWSD())
				{
					cout<<"\t"<<iter->GetWSD()<<"\t"<<iter->GetWSDExplanation();
				}

				cout<<endl;

				if( iter->IsPredicate() ){
					vector<SRL> srls;
					iter->GetSRLs(srls);
					cout<<srls.size()<<endl;
					for(vector<SRL>::iterator iter = srls.begin(); iter != srls.end(); ++iter){
						cout<<"\t"<<iter->type
							<<"\t"<<iter->beg
							<<"\t"<<iter->end
							<<endl;
					}
				}

			}
		}
	}catch(exception& e){
		std::cerr<<e.what();
	}
	return 0;
}
