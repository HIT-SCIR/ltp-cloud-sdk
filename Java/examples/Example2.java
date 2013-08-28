// package edu.hit.ir.examples;

import java.util.ArrayList;

import edu.hit.ir.ltpService.LTML;
import edu.hit.ir.ltpService.LTPOption;
import edu.hit.ir.ltpService.LTPService;
import edu.hit.ir.ltpService.SRL;
import edu.hit.ir.ltpService.Word;

public class Example2 {
    /**
     * <p>Title: main</p>
     * <p>Description:</p>
     * 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        LTPService ls = new LTPService("email:token");
        LTML ltml = new LTML();

        ArrayList<Word> wordList = new ArrayList<Word>();

        Word w1 = new Word(); w1.setWS("我");     w1.setPOS("r");  wordList.add(w1);
        Word w2 = new Word(); w2.setWS("爱");     w2.setPOS("v");  wordList.add(w2);
        Word w3 = new Word(); w3.setWS("北京");   w3.setPOS("ns"); wordList.add(w3);
        Word w4 = new Word(); w4.setWS("天安门"); w4.setPOS("ns"); wordList.add(w4);

        try {
            ltml.setParagraphNumber(2);
            ltml.addSentence(wordList, 0);

            ltml.printXml();

            ltml = ls.analyze(LTPOption.WSD, ltml);

            int sentNum = ltml.countSentence();
            for (int i = 0; i < sentNum; ++i) {
                ArrayList<Word> sentWords = ltml.getWords(i);
                for (int j = 0; j < sentWords.size(); ++j) {
                    if (ltml.hasWS()) {
                        System.out.print("\t" + sentWords.get(j).getWS());
                    }
                    if (ltml.hasPOS()) {
                        System.out.print("\t" + sentWords.get(j).getPOS());
                    }
                    if (ltml.hasNE()) {
                        System.out.print("\t" + sentWords.get(j).getNE());
                    }
                    if (ltml.hasParser()) {
                        System.out.print("\t"
                                + sentWords.get(j).getParserParent() + "\t"
                                + sentWords.get(j).getParserRelation());
                    }

                    if (ltml.hasSRL() && sentWords.get(j).isPredicate()) {
                        ArrayList<SRL> srls = sentWords.get(j).getSRLs();
                        System.out.println();
                        for (int k = 0; k < srls.size(); ++k) {
                            System.out.println("\t\t" + srls.get(k).type + "\t"
                                    + srls.get(k).beg + "\t" + srls.get(k).end);
                        }
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            ls.close();
        }
    }
}
