package edu.hit.ir.examples;

import java.util.ArrayList;
import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.SRL;
import edu.hit.ir.ltpService.Word;

public class Ltp_example1 {
	public static void main(String[] args) {

		LTPService ls = new LTPService("username:password"); 
		try {
			ls.setEncoding(LTPOption.GBK);
//			for(int u = 0; u<100; ++u){
			LTML ltml = ls.analyze(LTPOption.ALL,"我们都是赛尔人。");
//			System.out.println(ls.getAnalyze(LTPOption.ALL,"我们都是赛尔人。"));
//			ltml.printXml();
			
			//*
			int sentNum = ltml.countSentence();
			for(int i = 0; i< sentNum; ++i){
//				逐句访问
				ArrayList<Word> wordList = ltml.getWords(i);
				System.out.println(ltml.getSentenceContent(i));
				for(int j = 0; j < wordList.size(); ++j){
					System.out.print("\t" + wordList.get(j).getWS());
					System.out.print("\t" + wordList.get(j).getPOS());
					System.out.print("\t" + wordList.get(j).getNE());
					System.out.print("\t" + wordList.get(j).getWSD() + "\t" + wordList.get(j).getWSDExplanation());
					System.out.print("\t" + wordList.get(j).getParserParent() + "\t" + wordList.get(j).getParserRelation());
//					如果是谓词则输出
					if(ltml.hasSRL() && wordList.get(j).isPredicate()){
						ArrayList<SRL> srls = wordList.get(j).getSRLs();
						System.out.println();
						for(int k = 0; k <srls.size(); ++k){
							System.out.println("\t\t" + srls.get(k).type + "\t" + srls.get(k).beg + "\t" + srls.get(k).end);
						}
					}
					System.out.println();
				}
			}
			//*/
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
