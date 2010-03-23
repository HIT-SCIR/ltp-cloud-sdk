using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace ltp_service
{
        //m_wordList not need to be initialized
    class SentIndex
    {
        #region Variables

        private XmlElement m_xmlElem;
        private String m_strContent;       
        private List<Word> m_wordList = new List<Word>();

        #endregion

        #region Properties

        public XmlElement XmlElem
        {
            get { return m_xmlElem; }
            set { m_xmlElem = value; }
        }

        public String StrContent
        {
            get { return m_strContent; }
            set { m_strContent = value; }
        }

        public List<Word> WordList
        {
            get { return m_wordList; }
            set { m_wordList = value; }
        }

        #endregion
    }

    //m_sentenceList not need to be initialized
    class ParaIndex
    {
        #region Variables

        private XmlElement m_xmlElem;
        private int m_iGlobalSentenceStartId;     
        private List<SentIndex> m_sentenceList = new List<SentIndex>();

        #endregion

        #region Properties

        public XmlElement XmlElem
        {
            get { return m_xmlElem; }
            set { m_xmlElem = value; }
        }

        public int GlobalSentenceStartId
        {
            get { return m_iGlobalSentenceStartId; }
            set { m_iGlobalSentenceStartId = value; }
        }

        public List<SentIndex> SentenceList
        {
            get { return m_sentenceList; }
            set { m_sentenceList = value; }
        }

        #endregion
    }

    class LTML
    {
        #region Variables

        private XmlDocument m_xmlDocument;
        private XmlElement m_xmlRoot;
        private XmlElement m_xmlNote;
        //there is almost no infor in para element and the paragraph index is built on the sentence actually
        private List<ParaIndex> m_listParas = new List<ParaIndex>();	//paragraph index     
        private List<SentIndex> m_listSentences = new List<SentIndex>();	//sentence index     
        private bool m_bIsOverAdd = false;

        #endregion

        #region Properties

        public List<ParaIndex> ListParas
        {
            get { return m_listParas; }
        }

        public List<SentIndex> ListSentences
        {
            get { return m_listSentences; }
        }

        #endregion

        #region Constructions

        public LTML()
        {
            m_bIsOverAdd = false;
            BuildDOMFrame();
        }

        #endregion

        #region Public Methods

        public void LoadFromXmlStr(String xmlStr)
        {
            m_bIsOverAdd = false;
	        m_xmlDocument = new XmlDocument();
            m_xmlDocument.LoadXml(xmlStr);
            m_xmlRoot = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.DOC)[0]);
            m_xmlNote = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.NOTE)[0]);
	        InitIndex();
        }

        public void LoadFromXmlFile(String strFileName)
        {
            m_bIsOverAdd = false;
            m_xmlDocument = new XmlDocument();
            m_xmlDocument.Load(strFileName);
            m_xmlRoot = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.DOC)[0]);
            m_xmlNote = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.NOTE)[0]);
            InitIndex();
        }

        public void BuildDOMFrame()
        {
            m_bIsOverAdd = false;
            m_xmlDocument = new XmlDocument();
            XmlElement xmlRoot = m_xmlDocument.DocumentElement;
            XmlDeclaration xmldecl = m_xmlDocument.CreateXmlDeclaration("1.0", "gbk", null);
            m_xmlDocument.InsertBefore(xmldecl, xmlRoot);
            
            // body necessary
            XmlNode xml4nlpNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "xml4nlp", "");
            XmlNode noteNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "note", "");
            XmlNode rootNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "doc", "");
            XmlAttribute noteAttributeSent = m_xmlDocument.CreateAttribute("sent");
            noteAttributeSent.Value = "n";
            noteNode.Attributes.Append(noteAttributeSent);
            XmlAttribute noteAttributeWord = m_xmlDocument.CreateAttribute("word");
            noteAttributeWord.Value = "n";
            noteNode.Attributes.Append(noteAttributeWord);
            XmlAttribute noteAttributePos = m_xmlDocument.CreateAttribute("pos");
            noteAttributePos.Value = "n";
            noteNode.Attributes.Append(noteAttributePos);
            XmlAttribute noteAttributeNe = m_xmlDocument.CreateAttribute("ne");
            noteAttributeNe.Value = "n";
            noteNode.Attributes.Append(noteAttributeNe);
            XmlAttribute noteAttributeParser = m_xmlDocument.CreateAttribute("parser");
            noteAttributeParser.Value = "n";
            noteNode.Attributes.Append(noteAttributeParser);
            XmlAttribute noteAttributeWsd = m_xmlDocument.CreateAttribute("wsd");
            noteAttributeWsd.Value = "n";
            noteNode.Attributes.Append(noteAttributeWsd);
            XmlAttribute noteAttributeSrl = m_xmlDocument.CreateAttribute("srl");
            noteAttributeSrl.Value = "n";
            noteNode.Attributes.Append(noteAttributeSrl);

            xml4nlpNode.AppendChild(noteNode);
            xml4nlpNode.AppendChild(rootNode);
            m_xmlDocument.AppendChild(xml4nlpNode);

            // Initialize
            m_xmlRoot = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.DOC)[0]);
            m_xmlNote = (XmlElement)(m_xmlDocument.GetElementsByTagName(LTPTag.NOTE)[0]);
            InitIndex();
        }

        public void SaveDom(string strFile)
        {
            m_xmlDocument.Save(strFile);
        }

        public void Clear()
        {
            m_bIsOverAdd = false;
            BuildDOMFrame();
        }
        //*/ 	
        private void IndexClear()
        {
	        if(m_listParas.Count != 0)
            {
		        m_listParas.Clear();
	        }
	        if(m_listSentences.Count != 0)
            {
		        m_listSentences.Clear();
	        }
        }
    		
        private void InitIndex()
        {
	        IndexClear();

            foreach(XmlNode paraXn in m_xmlRoot.ChildNodes)
            {
                XmlElement paraXe = (XmlElement)paraXn;
                ParaIndex paragraph = new ParaIndex();
                paragraph.GlobalSentenceStartId = m_listSentences.Count;
                foreach(XmlNode senXn in paraXe.ChildNodes)
                {
                    XmlElement senXe = (XmlElement)senXn;
                    SentIndex sentence = new SentIndex();
                    foreach(XmlNode wordXn in senXe.ChildNodes)
                    {
                        sentence.WordList.Add(new Word((XmlElement)wordXn));
                    }
                    sentence.XmlElem = senXe;
                    sentence.StrContent = senXe.GetAttribute("cont");
                    paragraph.SentenceList.Add(sentence);
                    m_listSentences.Add(sentence);
                }

                paragraph.XmlElem = paraXe;
		        m_listParas.Add(paragraph);	
            }
        }

        public String GetXMLStr()
        {
            return m_xmlDocument.InnerXml;
        }
    	
        public bool HasSent()
        {	
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_SENT).Equals("y");
        }

        public bool HasWS()
        {	
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_WORD).Equals("y");
        }

        public bool HasPOS()
        {
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_POS).Equals("y");
        }

        public bool HasNE()
        {
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_NE).Equals("y");
        }

        public bool HasParser()
        {
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_PARSER).Equals("y");
        }

        public bool HasWSD()
        {
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_WSD).Equals("y");
        }

        public bool HasSRL()
        {
	        return m_xmlNote.GetAttribute(LTPTag.NOTE_SRL).Equals("y");
        }

        public int CountParagraph()
        {
	        return m_listParas.Count;
        }

        public int CountSentence(int paragraphIdx)
        {
	        return m_listParas[paragraphIdx].SentenceList.Count;
        }
    	
        public int CountSentence()
        {
	        return m_listSentences.Count;
        }

        public int CountWordInSentence(int paragraphIdx, int sentenceIdx)
        {
	        return m_listParas[paragraphIdx].SentenceList[sentenceIdx].WordList.Count;
        }
    	
        public int CountWordInSentence(int globalSentIdx)
        {
	        return m_listSentences[globalSentIdx].WordList.Count;
        }

        public int CountWordInParagraph(int paragraphIdx)
        {
	        int counts = 0;
            foreach (SentIndex SentIndex in m_listParas[paragraphIdx].SentenceList)
            {
                counts += SentIndex.WordList.Count;
            }
	        return counts;	
        }

        public int CountWordInDocument()
        {
	        int counts = 0;
	        for(int i = 0; i<m_listSentences.Count; ++i)
            {
		        counts += m_listSentences[i].WordList.Count;
	        }
	        return counts;
    		
        }

        public Word GetWord(int paragraphId, int sentenceId, int wordId) 
        {
            return new Word(m_listParas[paragraphId].SentenceList[sentenceId].WordList[wordId].XmlElem);
        }

        public Word GetWord(int globalSentenceId, int wordId)
        {
            return new Word(m_listSentences[globalSentenceId].WordList[wordId].XmlElem);
        }
    	
        public List<Word> GetWords(int paragraphId, int sentenceId)
        {
            List<Word> wordList = new List<Word>();
            for (int i = 0; i < m_listParas[paragraphId].SentenceList[sentenceId].WordList.Count; i++)
            {
                wordList.Add(GetWord(paragraphId, sentenceId, i));
            }
            return wordList;
        }
    	
        public List<Word> GetWords(int globalSentenceId)
        {
            List<Word> wordList = new List<Word>();
            for (int i = 0; i < m_listSentences[globalSentenceId].WordList.Count; i++)
            {
                wordList.Add(GetWord(globalSentenceId, i));
            }
            return wordList;	        		
        }
    	
        public String GetSentenceContent(int paragraphIdx, int sentenceIdx)
        {
            return m_listParas[paragraphIdx].SentenceList[sentenceIdx].StrContent;
        }
    	
        public String GetSentenceContent(int globalSentIdx){

	        return m_listSentences[globalSentIdx].StrContent;
        }

        public String GetEncoding() 
        {
            XmlElement xmlElement = (XmlElement)(m_xmlDocument.FirstChild);
            return xmlElement.GetAttribute("encoding");
        }

        public void SetEncoding(String enCoding) 
        {
            XmlElement xmlElement = (XmlElement)(m_xmlDocument.FirstChild);
            xmlElement.SetAttribute("encoding", enCoding);
        }
    	
        public void AddSentence(List<Word> wordList, int paragraphIdx)
        {

            if (wordList.Count == 0 || m_bIsOverAdd)
            {
                return;
            }

            if (true == HasSent() && false == HasWS() && false == HasPOS()
                && false == HasNE() && false == HasWSD() && false == HasSRL()
               )
            {
                throw new Exception(LtpServiceError.AddSentenceNoteMissMatchError);
            }

            if (m_listParas.Count == 0)
            {
                Word w = wordList[0];
                // if the xmlDocument is empty, paragraphIdx should be zero
                m_xmlNote.SetAttribute(LTPTag.NOTE_SENT, "y");
                if (w.HasWS())
                {
                    m_xmlNote.SetAttribute(LTPTag.NOTE_WORD, "y");
                }
                if (w.HasPOS())
                {
                    m_xmlNote.SetAttribute(LTPTag.NOTE_POS, "y");
                }
                if (w.HasNE())
                {
                    m_xmlNote.SetAttribute(LTPTag.NOTE_NE, "y");
                }
                if (w.HasWSD())
                {
                    m_xmlNote.SetAttribute(LTPTag.NOTE_WSD, "y");
                }
                if (w.HasParser())
                {
                    m_xmlNote.SetAttribute(LTPTag.NOTE_PARSER, "y");
                }
            }
            String strSentence = String.Empty;
            foreach (Word w in wordList)
            {
                if (CheckWordMatch(w) == false)
                {
                    return;
                    //throw new Exception("AddSentence error: Some word's attribute mismatch !");
                }
                strSentence = strSentence + w.XmlElem.GetAttribute("cont");
            }

            SetParagraphNumber(paragraphIdx + 1);

            XmlElement xmlParaElement = m_listParas[paragraphIdx].XmlElem;

            XmlNode xmlSentNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "sent", "");
            XmlAttribute xmlSentIdAttribute = m_xmlDocument.CreateAttribute("id");
            xmlSentIdAttribute.Value = m_listParas[paragraphIdx].SentenceList.Count.ToString();
            xmlSentNode.Attributes.Append(xmlSentIdAttribute);
            XmlAttribute xmlSentContAttribute = m_xmlDocument.CreateAttribute("cont");
            xmlSentContAttribute.Value = strSentence;
            xmlSentNode.Attributes.Append(xmlSentContAttribute);
            xmlParaElement.AppendChild(xmlSentNode);

            SentIndex sentIndexCur = new SentIndex();
            sentIndexCur.StrContent = strSentence;
            sentIndexCur.XmlElem = (XmlElement)xmlSentNode;
            int globalSentenceId = m_listParas[paragraphIdx].GlobalSentenceStartId + m_listParas[paragraphIdx].SentenceList.Count;
            m_listParas[paragraphIdx].SentenceList.Add(sentIndexCur);
            m_listSentences.Insert(globalSentenceId, sentIndexCur);
            for (int i = paragraphIdx + 1; i < m_listParas.Count; i++)
            {
                m_listParas[i].GlobalSentenceStartId++;
            }

            int length = wordList.Count;
            for (int i = 0; i < length; i++)
            {
                XmlNode wordNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "word", "");
                xmlSentNode.AppendChild(wordNode);
                XmlElement wordElem = (XmlElement)wordNode;
                wordElem.SetAttribute("id", i.ToString());
                Word curWord = new Word(wordElem);
                if (HasWS())
                {
                    wordElem.SetAttribute(LTPTag.CONT, wordList[i].GetWS());
                }
                if (HasPOS())
                {
                    wordElem.SetAttribute(LTPTag.POS, wordList[i].GetPOS());
                }
                if (HasNE())
                {
                    wordElem.SetAttribute(LTPTag.NE, wordList[i].GetNE());
                }
                if (HasParser())
                {
                    wordElem.SetAttribute(LTPTag.PSR_PARENT, "" + wordList[i].GetParserParent());
                    wordElem.SetAttribute(LTPTag.PSR_RELATE, wordList[i].GetParserRelation());
                }
                if (HasWSD())
                {
                    wordElem.SetAttribute(LTPTag.WSD, wordList[i].GetWSD());
                    wordElem.SetAttribute(LTPTag.WSD_EXP, wordList[i].GetWSDExplanation());
                }
                curWord.SetSRL(wordList[i].GetSRLs());
                
                m_listSentences[globalSentenceId].WordList.Add(curWord);
            }
        }

        public void AddSentence(String strSentence, int paragraphIdx)
        {
            if (m_bIsOverAdd)
            {
                return;
            }

            if (true == HasSent() && (true == HasWS() || true == HasPOS() || 
                true == HasNE() || true == HasWSD() || true == HasSRL())
                )
            {
                throw new Exception(LtpServiceError.AddSentenceNoteMissMatchError);
            }

            m_xmlNote.SetAttribute(LTPTag.NOTE_SENT, "y");

            SetParagraphNumber(paragraphIdx + 1);

            XmlElement xmlParaElement = m_listParas[paragraphIdx].XmlElem;

            XmlNode xmlSentNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "sent", "");
            XmlAttribute xmlSentIdAttribute = m_xmlDocument.CreateAttribute("id");
            xmlSentIdAttribute.Value = m_listParas[paragraphIdx].SentenceList.Count.ToString();
            xmlSentNode.Attributes.Append(xmlSentIdAttribute);
            XmlAttribute xmlSentContAttribute = m_xmlDocument.CreateAttribute("cont");
            xmlSentContAttribute.Value = strSentence;
            xmlSentNode.Attributes.Append(xmlSentContAttribute);
            xmlParaElement.AppendChild(xmlSentNode);

            SentIndex sentIndexCur = new SentIndex();
            sentIndexCur.StrContent = strSentence;
            sentIndexCur.XmlElem = (XmlElement)xmlSentNode;
            int globalSentenceId = m_listParas[paragraphIdx].GlobalSentenceStartId + m_listParas[paragraphIdx].SentenceList.Count;
            m_listParas[paragraphIdx].SentenceList.Add(sentIndexCur);
            m_listSentences.Insert(globalSentenceId, sentIndexCur);
            for (int i = paragraphIdx + 1; i < m_listParas.Count; i++)
            {
                m_listParas[i].GlobalSentenceStartId++;
            }
        }

        public void SetParagraphNumber(int paragraphNumber)
        {
            for (int i = m_listParas.Count; i < paragraphNumber; i++)
            {
                XmlNode xmlParaNode = m_xmlDocument.CreateNode(XmlNodeType.Element, "para", "");
                XmlAttribute xmlParaAttribute = m_xmlDocument.CreateAttribute("id");
                xmlParaAttribute.Value = i.ToString();
                xmlParaNode.Attributes.Append(xmlParaAttribute);
                m_xmlRoot.AppendChild(xmlParaNode);
                ParaIndex paraIndexCur = new ParaIndex();
                paraIndexCur.XmlElem = (XmlElement)xmlParaNode;
                m_listParas.Add(paraIndexCur);
                paraIndexCur.GlobalSentenceStartId = m_listSentences.Count;
            }
        }

        public void SetOver()
        {
            m_bIsOverAdd = true;
        }

        #endregion

        #region Private Methods

        private bool CheckWordMatch(Word w)
        {
	        if(HasWS() == true && w.HasWS() == false)
            {
		        return false;
	        }
	        if(HasPOS() == true && w.HasPOS() == false)
            {
		        return false;
	        }
	        if(HasNE() == true && w.HasNE() == false)
            {
		        return false;
	        }
	        if(HasParser() == true && w.HasParser() == false)
            {
		        return false;
	        }
	        if(HasWSD() == true && w.HasWSD() == false)
            {
		        return false;
	        }
	        return true;
        }

        #endregion
    }

}
