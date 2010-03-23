using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Net;
using System.Web;
using System.IO;


namespace ltp_service
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 1)
            {
                Usage();
                return;
            }

            if (args[0].Equals("-sentence") && args.Length == 4)
            {
                TestSentence(args[1], args[2], args[3]);               
            }
            else if (args[0].Equals("-xml") && args.Length == 4)
            {
                TestXmlFile(args[1], args[2], args[3]);   
            }
            else if (args[0].Equals("-xmlmerge") && args.Length == 5)
            {
                TestMergeXmlFile(args[1], args[1], args[2], args[4]); 
            }
            else
            {
                Usage();
                return;
            }
            /*
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (ltpService.IsAuthorized())
            {
                LTML temp = ltpService.Analyze(LTPOption.WS, "午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想");
                temp.ListSentences[0].WordList[2].SetWSD("aa", "bb");
                temp.SaveDom(@"D:\hit\wanxiang\ltp-service\temp1.xml");
                

                LTML temp_next = ltpService.Analyze(LTPOption.POS, temp);
                temp_next.SaveDom(@"D:\hit\wanxiang\ltp-service\temp2.xml");

                temp_next.AddSentence(temp_next.ListSentences[0].WordList, 0);
                temp_next.SaveDom(@"D:\hit\wanxiang\ltp-service\temp3.xml");
                temp_next.AddSentence(temp.ListSentences[0].WordList, 0);
                temp_next.SaveDom(@"D:\hit\wanxiang\ltp-service\temp4.xml");
                temp_next.AddSentence(temp_next.ListSentences[0].WordList, 10);
                temp_next.SaveDom(@"D:\hit\wanxiang\ltp-service\temp5.xml");
            }
            
            Example1();
            Example2();
            Example3();
             */

            Console.WriteLine("Succeeded!");
        }

        static void Example1()
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (!ltpService.IsAuthorized())
            {
                return;
            }

            LTML ltml = new LTML();

            List<Word> wordList = new List<Word>();
            Word w1 = new Word();
            w1.SetWS("我");
            w1.SetPOS("r");
            wordList.Add(w1);

            Word w2 = new Word();
            w2.SetWS("爱");
            w2.SetPOS("v");
            wordList.Add(w2);

            Word w3 = new Word();
            w3.SetWS("北京");
            w3.SetPOS("ns");
            wordList.Add(w3);

            w3 = new Word();
            w3.SetWS("天安门");
            w3.SetPOS("ns");
            wordList.Add(w3);

            try
            {
                ltml.AddSentence(wordList, 0);
                ltml.SaveDom(@"D:\hit\wanxiang\ltp-service\example1-ltml.xml");
                LTML ltmlOut = ltpService.Analyze(LTPOption.ALL, ltml);
                ltmlOut.SaveDom(@"D:\hit\wanxiang\ltp-service\example1-ltmOutl.xml");
                Console.WriteLine(ltmlOut.GetXMLStr());
                int sentNum = ltmlOut.CountSentence();
                for (int i = 0; i < sentNum; ++i)
                {
                    string sentCont;
                    sentCont = ltmlOut.GetSentenceContent(i);
                    Console.WriteLine(sentCont);
                    List<Word> wordLista = ltmlOut.GetWords(i);
                    foreach (Word curWord in wordLista)
                    {
                        if (ltmlOut.HasWS())
                        {
                            Console.Write("{0}\t{1}", curWord.GetWS(), curWord.GetID());
                        }
                        if (ltmlOut.HasPOS())
                        {
                            Console.Write("\t" + curWord.GetPOS());
                        }
                        if (ltmlOut.HasNE())
                        {
                            Console.Write("\t" + curWord.GetNE());
                        }
                        if (ltmlOut.HasParser())
                        {
                            Console.Write("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());
                        }
                        if (ltmlOut.HasWSD())
                        {
                            Console.Write("\t" + curWord.GetWSD() + "\t" + curWord.GetWSDExplanation());
                        }
                        Console.WriteLine();

                        if (curWord.IsPredicate())
                        {
                            List<SRL> srls = curWord.GetSRLs();
                            Console.WriteLine(srls.Count);
                            foreach (SRL srl in srls)
                            {
                                Console.WriteLine(srl.ToString());
                            }

                        }

                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        static void TestSentence(string strOption, string strSentFile, string strXMLFile)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (!ltpService.IsAuthorized())
            {
                return;
            }
            LTML ltmlOut = new LTML();
            foreach(String strSentence in File.ReadAllLines(strSentFile,Encoding.UTF8))
            {
                String strSentence_new = strSentence.Trim();
                if (strSentence_new.Equals(String.Empty))
                {
                    continue;
                }
                LTML ltml = ltpService.Analyze(strOption, strSentence_new);
                for (int i = 0; i < ltml.ListParas.Count; i++)
                {
                    foreach (SentIndex sentIndex in ltml.ListParas[i].SentenceList)
                    {
                        ltmlOut.AddSentence(sentIndex.WordList, i);
                    }
                }
                int sentNum = ltml.CountSentence();
                for (int i = 0; i < sentNum; ++i)
                {
                    string sentCont = ltml.GetSentenceContent(i);
                    Console.WriteLine(sentCont);
                    List<Word> wordList = ltml.GetWords(i);
                    //按句子打印输出
                    foreach (Word curWord in wordList)
                    {
                        Console.Write(curWord.GetWS() + "\t" + curWord.GetID());
                        Console.Write("\t" + curWord.GetPOS());
                        Console.Write("\t" + curWord.GetNE());
                        Console.Write("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());
                        Console.Write("\t" + curWord.GetWSD() + "\t" + curWord.GetWSDExplanation());
                        Console.WriteLine();

                        if (curWord.IsPredicate())
                        {
                            List<SRL> srls = curWord.GetSRLs();
                            Console.WriteLine(srls.Count);
                            foreach (SRL srl in srls)
                            {
                                Console.WriteLine(srl.ToString());
                            }

                        }
                    }
                }
            }
            ltmlOut.SaveDom(strXMLFile);
        }

        static void TestXmlFile(string strOption, string strXMLFileIn, string strXMLFileOut)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (!ltpService.IsAuthorized())
            {
                return;
            }
            LTML ltmlIn = new LTML();
            ltmlIn.LoadFromXmlFile(strXMLFileIn);
            LTML ltml = ltpService.Analyze(strOption, ltmlIn);
            ltml.SaveDom(strXMLFileOut);
            Console.WriteLine(ltml.GetXMLStr());
            int sentNum = ltml.CountSentence();
            for (int i = 0; i < sentNum; ++i)
            {
                string sentCont = ltml.GetSentenceContent(i);
                Console.WriteLine(sentCont);
                List<Word> wordList = ltml.GetWords(i);
                //按句子打印输出
                foreach (Word curWord in wordList)
                {
                    Console.Write(curWord.GetWS() + "\t" + curWord.GetID());
                    Console.Write("\t" + curWord.GetPOS());
                    Console.Write("\t" + curWord.GetNE());
                    Console.Write("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());
                    Console.Write("\t" + curWord.GetWSD() + "\t" + curWord.GetWSDExplanation());
                    Console.WriteLine();

                    if (curWord.IsPredicate())
                    {
                        List<SRL> srls = curWord.GetSRLs();
                        Console.WriteLine(srls.Count);
                        foreach (SRL srl in srls)
                        {
                            Console.WriteLine(srl.ToString());
                        }

                    }
                }
            }           
        }

        static void TestMergeXmlFile(string strOption, string strXMLlFile_Main, string strXMLlFile_Merge, string strXMLlFile_Out)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (!ltpService.IsAuthorized())
            {
                return;
            }
            LTML ltmlMain = new LTML();
            ltmlMain.LoadFromXmlFile(strXMLlFile_Main);

            LTML ltmlMerge = new LTML();
            ltmlMerge.LoadFromXmlFile(strXMLlFile_Merge);

            for (int i = 0; i < ltmlMerge.ListParas.Count; i++)
            {
                foreach (SentIndex sentIndex in ltmlMerge.ListParas[i].SentenceList)
                {
                    ltmlMain.AddSentence(sentIndex.WordList, i);
                }
            }

            ltmlMain.SaveDom(strXMLlFile_Out + @".middle");
            LTML ltml = ltpService.Analyze(strOption, ltmlMain);
            ltml.SaveDom(strXMLlFile_Out);
            Console.WriteLine(ltml.GetXMLStr());
            int sentNum = ltml.CountSentence();
            for (int i = 0; i < sentNum; ++i)
            {
                string sentCont = ltml.GetSentenceContent(i);
                Console.WriteLine(sentCont);
                List<Word> wordList = ltml.GetWords(i);
                //按句子打印输出
                foreach (Word curWord in wordList)
                {
                    Console.Write(curWord.GetWS() + "\t" + curWord.GetID());
                    Console.Write("\t" + curWord.GetPOS());
                    Console.Write("\t" + curWord.GetNE());
                    Console.Write("\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());
                    Console.Write("\t" + curWord.GetWSD() + "\t" + curWord.GetWSDExplanation());
                    Console.WriteLine();

                    if (curWord.IsPredicate())
                    {
                        List<SRL> srls = curWord.GetSRLs();
                        Console.WriteLine(srls.Count);
                        foreach (SRL srl in srls)
                        {
                            Console.WriteLine(srl.ToString());
                        }

                    }
                }
            }
        }
        static void Example3()
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize("cir:lovehitcir");
            if (!ltpService.IsAuthorized())
            {
                return;
            }
            LTML ltmlBeg = new LTML();
	        try
            {
		        ltmlBeg = ltpService.Analyze(LTPOption.WS, "午夜巴塞罗那是对爱情的一次诙谐、充满智慧、独具匠心的冥想。");		
		        List<Word> wordList = ltmlBeg.GetWords(0);
		        //输出分词结果
		        foreach(Word curWord in wordList)
		        {
			        Console.WriteLine(curWord.GetID() + "\t" + curWord.GetWS());
		        }
		        Console.WriteLine();

		        //将“午夜”与“巴赛罗那”合并，其它的词不变
		        List<Word> mergeList = new List<Word>();
		        Word mergeWord = new Word();
		        mergeWord.SetWS(wordList[0].GetWS() + wordList[1].GetWS());
		        mergeList.Add(mergeWord);

		        for (int i = 2; i < wordList.Count; i++)
		        {
			        Word others = new Word();
			        others.SetWS(wordList[i].GetWS());
			        mergeList.Add(others);
		        }

		        LTML ltmlSec = new LTML();
		        ltmlSec.AddSentence(mergeList, 0);
		        ltmlSec.SetOver();
                LTML ltmlOut = ltpService.Analyze(LTPOption.PARSER, ltmlSec);

		        //输出合并分词后PARSER结果
		        Console.WriteLine("merge and get parser results.");
		        List<Word> outList = ltmlOut.GetWords(0);		        
		        foreach(Word curWord in outList)
		        {
			        Console.WriteLine(curWord.GetID() + "\t" + curWord.GetWS() + "\t" + curWord.GetPOS() + "\t" + curWord.GetParserParent() + "\t" + curWord.GetParserRelation());				
		        }
		        Console.WriteLine();
                ltmlBeg.SaveDom(@"D:\hit\wanxiang\ltp-service\example3-ltmlBeg.xml");
                ltmlSec.SaveDom(@"D:\hit\wanxiang\ltp-service\example3-ltmlSec.xml");
                ltmlOut.SaveDom(@"D:\hit\wanxiang\ltp-service\example3-ltmlOut.xml");
	        }
            catch(Exception e)
            {
		        Console.WriteLine(e.Message);
	        }
        }

        static void Usage()
        {
            Console.WriteLine("Command format error!");
            Console.WriteLine("The Command like:");
            Console.WriteLine("ltp-service -sentence @option @sentFileName @outXmlFile");
            Console.WriteLine("ltp-service -xml @option @inXmlFile @outXmlFile");
            Console.WriteLine("ltp-service -xmlmerge @option @inXmlFile1 @inXmlFile2 @outXmlFile");
            Console.WriteLine("option can be one of these: ws pos ne wsd parser srl all");
        }
    }
}
