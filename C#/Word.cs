using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Xml;

namespace ltp_service
{
    class SRL
    {
        #region Variables

        private string m_strType;
        private int m_iBegin;
        private int m_iEnd;
        #endregion

        #region Properties

        public int iBegin
        {
            get { return m_iBegin; }
            set { m_iBegin = value; }
        }

        public string strType
        {
            get { return m_strType; }
            set { m_strType = value; }
        }

        public int iEnd
        {
            get { return m_iEnd; }
            set { m_iEnd = value; }
        }

        #endregion

        #region Constructions

        public SRL(string strType, int iBegin, int iEnd)
        {
            m_strType = strType;
            m_iBegin = iBegin;
            m_iEnd = iEnd;
        }

        #endregion

        #region Pubic Methods

        override public String ToString()
        {
            return String.Format("SRL[{0},{1},{2}]", m_strType, m_iBegin, m_iEnd);
        }

        #endregion
    }

    class Word
    {
        #region Variables

        private XmlElement m_xmlElem;

        #endregion

        #region Properties

        public XmlElement XmlElem
        {
            get { return m_xmlElem; }
        }

        #endregion

        #region Constructions

        public Word(XmlElement ele) 
        {
		    m_xmlElem = ele;
        }

        public Word()
        {
            XmlDocument xmlDocument = new XmlDocument();
            XmlNode xmlNode = xmlDocument.CreateNode(XmlNodeType.Element, "word", "");
            xmlDocument.AppendChild(xmlNode);

            m_xmlElem = (XmlElement)xmlNode;

        }

        #endregion

        #region Public Methods

        public int GetID()
        {
		    return int.Parse(m_xmlElem.GetAttribute(LTPTag.ID));
	    }
    	
	    public String GetWS()
        {
		    return m_xmlElem.GetAttribute(LTPTag.CONT);
	    }
    	
	    public String GetPOS()
        {
		    return m_xmlElem.GetAttribute(LTPTag.POS);
	    }

	    public String GetNE(){
		    return m_xmlElem.GetAttribute(LTPTag.NE);
	    }
    	
	    public String GetWSD()
        {
		    return m_xmlElem.GetAttribute(LTPTag.WSD);
	    }
    	
    	
	    public String GetWSDExplanation()
        {
		    return m_xmlElem.GetAttribute(LTPTag.WSD_EXP);
	    }
    	
	    public int GetParserParent()
        {
            if (m_xmlElem.GetAttribute(LTPTag.PSR_PARENT) == String.Empty)
			    return -3;
		    return int.Parse(m_xmlElem.GetAttribute(LTPTag.PSR_PARENT));
	    }
    	
	    public String GetParserRelation()
        {
		    return m_xmlElem.GetAttribute(LTPTag.PSR_RELATE);
	    }
    		
	    public bool IsPredicate()
        {
		    return m_xmlElem.ChildNodes.Count==0 ? false : true;
	    }
    	
	    public List<SRL> GetSRLs()
        {
            
		    List<SRL> srls = new List<SRL>();
            foreach (XmlNode xn in m_xmlElem.ChildNodes)
            {
                XmlElement xe = (XmlElement)xn;
		        srls.Add(
                    
				        new SRL(
                            xe.GetAttribute(LTPTag.SRL_TYPE),
                            int.Parse(xe.GetAttribute(LTPTag.SRL_BEGIN)) ,
                            int.Parse(xe.GetAttribute(LTPTag.SRL_END))
						        )
				        );
		    }
		    return srls;
	    }
    	
	    public void SetID(int id)
        {
            
		    m_xmlElem.SetAttribute(LTPTag.ID, id.ToString());
	    }
    	
	    public void SetWS(String content)
        {
		    m_xmlElem.SetAttribute(LTPTag.CONT, content);
	    }
    	
	    public void SetPOS(String pos)
        {
		    m_xmlElem.SetAttribute(LTPTag.POS, pos);
	    }
    	
	    public void SetNE(String ne)
        {
		    m_xmlElem.SetAttribute(LTPTag.NE, ne);
	    }
    	
	    public void SetWSD(String wsd, String explanation)
        {
		    m_xmlElem.SetAttribute(LTPTag.WSD, wsd);
		    m_xmlElem.SetAttribute(LTPTag.WSD_EXP, explanation);
	    }
    	
	    public void SetParser(int parent, String relation)
        {
		    m_xmlElem.SetAttribute(LTPTag.PSR_PARENT, "" + parent);
		    m_xmlElem.SetAttribute(LTPTag.PSR_RELATE, relation);
	    }
    	
	    public void SetSRL(List<SRL> srls)
        {
		    if(srls.Count == 0 )
            {
			    return;
		    }
		    for(int i = 0; i<srls.Count; ++i)
            {
                m_xmlElem.OwnerDocument.CreateTextNode(LTPTag.SRL_ARG);  
                XmlNode srl = m_xmlElem.OwnerDocument.CreateNode(m_xmlElem.NodeType, LTPTag.SRL_ARG, m_xmlElem.NamespaceURI);
                XmlElement srl_em = (XmlElement)srl;
			    srl_em.SetAttribute(LTPTag.SRL_TYPE, srls[i].strType);
			    srl_em.SetAttribute(LTPTag.SRL_BEGIN, "" + srls[i].iBegin);
			    srl_em.SetAttribute(LTPTag.SRL_END, "" + srls[i].iEnd);
                m_xmlElem.AppendChild(srl);
		    }
		    return;
	    }
    	
	    public bool HasID()
        {
            return FindAttributes(LTPTag.ID);
	    }
    	
	    public bool HasWS()
        {
            return FindAttributes(LTPTag.CONT);
	    }
    	
	    public bool HasPOS()
        {
            return FindAttributes(LTPTag.POS);         
	    }
    	
	    public bool HasNE()
        {
            return FindAttributes(LTPTag.NE);
	    }
    	
	    public bool HasWSD()
        {
            return FindAttributes(LTPTag.WSD) && FindAttributes(LTPTag.WSD_EXP);
	    }
    	
	    public bool HasParser()
        {
            return FindAttributes(LTPTag.PSR_PARENT) && FindAttributes(LTPTag.PSR_RELATE);
        }

        #endregion

        #region Private Methods

        private bool FindAttributes(String strAttribute)
        {
            foreach (XmlAttribute xmlAttribute in m_xmlElem.Attributes)
            {
                if (xmlAttribute.Name.Equals(strAttribute))
                {
                    return true;
                }
            }

            return false;
        }

        #endregion
    }

}

