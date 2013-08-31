/************************************************************************/
/* PROGRAM: example10
 * 对SERVICE及LTML重新封装后的测试程序
 * HISTORY: 2009/12/24  韩中华  FIRST RELEASE
 * zhhan@ir.hit.edu.cn
 * 哈工大信息检索研究中心
 */
/************************************************************************/

#include "LTPOption.h"
#include "LTPService.h"
#include "LTML.h"
#include "Word.h"  

using namespace ltp::service;

int main(){
    LTPService ls("email:token");
    LTML ltml;

    if (!ls.Analyze(LTPOption.ALL,"我爱北京天安门。", ltml)) {
        cerr<<"Authorization is denied!"<<endl;
        exit(EXIT_FAILURE);
    }

    int sentNum = ltml.CountSentence();

    for (int i = 0; i < sentNum; ++i) {
        string sentCont;
        ltml.GetSentenceContent(sentCont, i);
        cout << sentCont << endl;
        vector<Word> wordList;

        ltml.GetWords(wordList, i);
        for(vector<Word>::iterator iter = wordList.begin(); iter!= wordList.end(); ++iter){
            cout << iter->GetWS() 
                << "\t" << iter->GetID() 
                << "\t" << iter->GetPOS() 
                << "\t" << iter->GetNE()
                << "\t" << iter->GetParserParent()
                << "\t" << iter->GetParserRelation() 
                << endl;

            if( iter->IsPredicate() ){
                vector<SRL> srls;
                iter->GetSRLs(srls);
                for(vector<SRL>::iterator iter = srls.begin(); iter != srls.end(); ++iter){
                    cout << "\t" << iter->type
                        << "\t" << iter->beg
                        << "\t" << iter->end
                        << endl;
                }
            }
        }
    }

    return 0;
}
