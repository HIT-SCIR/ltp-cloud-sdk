/************************************************************************/
/*	
	PROGRAM:	example10
				对SERVICE及LTML重新封装后的测试程序
	HISTORY:	2009/12/24	韩中华	FIRST RELEASE
				zhhan@ir.hit.edu.cn
				哈工大信息检索研究中心
*/
/************************************************************************/

#include "LTPOption.h"
#include "LTPService.h"
#include "LTML.h"
#include "Word.h"  

using namespace HIT_IR_LTP;
int main(){
	LTPService ls("zhhan@ir.hit.edu.cn");

	LTML ltml;
	ls.Analyze(ltml, LTPOption.ALL,"我们都是赛尔人。");
//	cout<<ltml.GetXMLStr()<<endl;
	int sentNum = ltml.CountSentence();
	for ( int i = 0; i<sentNum; ++i) { 
		string sentCont;
		ltml.GetSentenceContent(sentCont, i);
		cout<< sentCont <<endl;
		vector<Word> wordList;
		ltml.GetWords(wordList, i);
		//按句子打印输出
		for( vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter ){
			cout<<iter->GetWS()<<"\t"<<iter->GetID();
			cout<<"\t"<<iter->GetPOS();
			cout<<"\t"<<iter->GetNE();
			cout<<"\t"<<iter->GetParserParent()<<"\t"<<iter->GetParserRelation();
			cout<<"\t"<<iter->GetWSD()<<"\t"<<iter->GetWSDExplanation();
			cout<<endl;

			if( iter->IsPredicate() ){
				vector<SRL> srls;
				iter->GetSRLs(srls);
//				cout<<srls.size()<<endl;
				for(vector<SRL>::iterator iter = srls.begin(); iter != srls.end(); ++iter){
					cout<<"\t"<<iter->type
						<<"\t"<<iter->beg
						<<"\t"<<iter->end
						<<endl;
				}
			}

		}
	}
	return 0;
}
