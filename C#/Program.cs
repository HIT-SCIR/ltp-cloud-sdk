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
            try
            {
                if (args.Length == 5 && args[0].Equals("-sentence"))
                {
                    TestSentence(args[1], args[2], args[3], args[4]);
                }
                else if (args.Length == 5 && args[0].Equals("-xml"))
                {
                    TestXmlFile(args[1], args[2], args[3], args[4]);
                }
                else if (args.Length == 6 && args[0].Equals("-xmlmerge"))
                {
                    TestMergeXmlFile(args[1], args[2], args[3], args[4], args[5]);
                }
                else
                {
                    Usage();
                    return;
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

            Console.WriteLine("Succeeded!");
        }


        static void TestSentence(string strAuthorize, string strOption, string strSentFile, string strXMLFile)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize(strAuthorize);
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

        static void TestXmlFile(string strAuthorize, string strOption, string strXMLFileIn, string strXMLFileOut)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize(strAuthorize);
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

        static void TestMergeXmlFile(string strAuthorize, string strOption, string strXMLlFile_Main, string strXMLlFile_Merge, string strXMLlFile_Out)
        {
            LTPService ltpService = new LTPService();
            ltpService.Authorize(strAuthorize);
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

        static void Usage()
        {
            Console.WriteLine("Command format error!");
            Console.WriteLine("The Command like:");
            Console.WriteLine("ltp-service -sentence strAuthorize option sentFileName outXmlFile");
            Console.WriteLine("sentFileName is of utf8 file format");
            Console.WriteLine("ltp-service -xml strAuthorize option inXmlFile outXmlFile");
            Console.WriteLine("ltp-service -xmlmerge strAuthorize option inXmlFile1 inXmlFile2 outXmlFile");
            Console.WriteLine("option can be one of these: ws pos ne wsd parser srl all");
        }
    }
}
