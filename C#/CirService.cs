using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Web;
using System.Net;
using System.IO;

namespace ltp_service
{
    class CirService
    {
        #region Variables

        // private string m_strUrl = @"http://ir.hit.edu.cn/demo/ltp_ws/ltp";
        private string m_strUrl = @"http://api.ltp-cloud.com:8080/ltp_srv/ltp";
        private string m_strUserName = String.Empty;
        private string m_strPassword = String.Empty;
        private string m_strEncoding = "utf-8";

        private bool m_bIsXml = false;
        private char[] m_splitChar = { ':' };
        private string m_strAnalysisOptions = "&t=all";

        #endregion

        #region Constructions

        public CirService(string strAuth)
		{
            Authorize(strAuth);
        }

        #endregion

        #region Public Methods

        public void Authorize(string strAuth)
        {
            string[] listStrings = strAuth.Split(m_splitChar, StringSplitOptions.RemoveEmptyEntries);
            if(listStrings.Length == 2)
            {
                m_strUserName = listStrings[0];
                m_strPassword = listStrings[1];
            }
            else
            {
                throw new Exception(LtpServiceError.AuthorizeFormatError);
            }

        }


        public void setEncoding(string strEncoding)
        {
            if (strEncoding.Equals("utf-8") || strEncoding.Equals("gbk") || strEncoding.Equals("gb2312"))
            {
                m_strEncoding = strEncoding;
            }
            else
            {
                throw new Exception(LtpServiceError.EncodingFormatError);
            }
        }

        public String getEncoding()
        {
            return m_strEncoding;
        }

        public String Analyze(String strParameters)
        {
            if (strParameters == null)
            {
                throw new Exception(String.Format("{0}: CirService.Analyze(strParameters)", LtpServiceError.ParameterNullError));
            }
            String strParam = String.Empty;
            if (!strParameters.Equals(String.Empty))
            {
                strParam = "s=" + strParameters;
            }

            if (m_bIsXml)
            {
                strParam += "&x=y";
            }

            if (m_strEncoding.Equals("utf-8"))
            {
                strParam += "&c=utf8";
            }
            else if (m_strEncoding.Equals("gbk"))
            {
                strParam += "&c=c=";
            }
            else
            {
                strParam += "&c=" + m_strEncoding;
            }

            if (!m_strAnalysisOptions.Equals("&t=all"))
            {
                strParam += m_strAnalysisOptions;
            }
            HttpWebRequest httpWebRequest = (HttpWebRequest)WebRequest.Create(m_strUrl);
            httpWebRequest.Credentials = new NetworkCredential(m_strUserName, m_strPassword);
            httpWebRequest.Method = "POST";
            httpWebRequest.ContentType = "application/x-www-form-urlencoded";
            byte[] lbPostBuffer = Encoding.GetEncoding(m_strEncoding).GetBytes(strParam);
            httpWebRequest.ContentLength = lbPostBuffer.Length;
            Stream loPostData = httpWebRequest.GetRequestStream();
            loPostData.Write(lbPostBuffer, 0, lbPostBuffer.Length);
            loPostData.Close();
            HttpWebResponse loWebResponse = (HttpWebResponse)(httpWebRequest.GetResponse());
            Encoding enc = Encoding.GetEncoding(m_strEncoding);
            StreamReader loResponseStream = new StreamReader(loWebResponse.GetResponseStream(), enc);
            string lcHtml = loResponseStream.ReadToEnd();
            loWebResponse.Close();
            loResponseStream.Close();

            return lcHtml;
        }

        public void SetXmlOption(bool bIsXml)
        {
            m_bIsXml = bIsXml;
	    }

        public void SetAnalysisOptions(string strOption )
	    {
            if (strOption.Equals(LTPOption.WS))
		    {
                m_strAnalysisOptions = "&t=ws";
		    }
            else if (strOption.Equals(LTPOption.POS))
            {
                m_strAnalysisOptions = "&t=pos";
            }
            else if (strOption.Equals(LTPOption.NE))
		    {
                m_strAnalysisOptions = "&t=ner";
		    }
            else if (strOption.Equals(LTPOption.PARSER))
		    {
                m_strAnalysisOptions = "&t=dp";
		    }
            else if (strOption.Equals(LTPOption.WSD))
		    {
                m_strAnalysisOptions = "&t=wsd";
		    }
            else if (strOption.Equals(LTPOption.SRL))
		    {
                m_strAnalysisOptions = "&t=srl";
		    }
            else if (strOption.Equals(LTPOption.ALL))
            {
                m_strAnalysisOptions = "&t=all";
            }
            else
            {
                throw new Exception(LtpServiceError.AnalysisOptionError);
            }
        }
        
        #endregion
    }
}
