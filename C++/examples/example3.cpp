/************************************************************************/
/*	
	PROGRAM:	example11
				对SERVICE及LTML重新封装后的测试程序
	HISTORY:	2009/1/3	韩中华	FIRST RELEASE
				zhhan@ir.hit.edu.cn
				哈工大信息检索研究中心
*/
/************************************************************************/

#include "../__ltpService/LTPOption.h"
#include "../__ltpService/LTPService.h"
#include "../__ltpService/LTML.h"
#include "../__ltpService/Word.h"
 
using namespace HIT_IR_LTP;
int main11(){
	LTPService ls("zhhan@ir.hit.edu.cn");
	LTML ltmlBeg;

	try{
		ls.Analyze(ltmlBeg, LTPOption.WS, "午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想。");		

		vector<Word> wordList;
		ltmlBeg.GetWords(wordList, 0);
		//输出分词结果
		for( vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter )
		{
			cout<<iter->GetID()<<"\t"<<iter->GetWS()<<endl;
		}
		cout<<endl;

		//将“午夜”与“巴赛罗那”合并，其它的词不变
		vector<Word> mergeList;
		Word mergeWord;
		mergeWord.SetWS(wordList.at(0).GetWS() + wordList.at(1).GetWS());
		mergeList.push_back(mergeWord);

		for (vector<Word>::iterator iter = wordList.begin()+2; iter != wordList.end(); ++iter)
		{
			Word others;
			others.SetWS(iter->GetWS());
			mergeList.push_back(others);
		}
		LTML ltmlSec;
		ltmlSec.AddSentence(mergeList, 0);
		LTML ltmlOut;
		ls.Analyze(ltmlOut, LTPOption.PARSER, ltmlSec);

		//输出合并分词后PARSER结果
		cout<<"merge and get parser results."<<endl;
		vector<Word> outList;
		ltmlOut.GetWords(outList,0);
		for (vector<Word>::iterator iter = outList.begin(); iter != outList.end(); ++iter)
		{
			cout<<iter->GetID()<<"\t"<<iter->GetWS()<<"\t"<<iter->GetPOS()<<"\t"<<iter->GetParserParent()<<"\t"<<iter->GetParserRelation()<<endl;				
		}
		cout<<endl;
	}catch(exception& e){
		std::cerr<<e.what();
	}
	return 0;
}
