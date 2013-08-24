using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ltp_service
{
    public class LTPService
    {
        #region Variables

        private CirService m_cirServ = null;

        #endregion

        #region Constructions

        public LTPService(String strAuthor)
        {
            m_cirServ = new CirService(strAuthor);
            Authorize(strAuthor);
        }

        #endregion

        #region Public Methods


        public bool SetEncoding(String encodeType)
        {
            try
            {
                m_cirServ.setEncoding(encodeType);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return false;
            }

            return true;          
        }

        public LTML Analyze(String strOption, String strAnalyzeString)
        {
            if (strOption == null || strAnalyzeString == null)
            {
                throw new Exception(String.Format("{0}: LTPService.Analyze(strOption, strAnalyzeString)", LtpServiceError.ParameterNullError));
            }
            m_cirServ.SetAnalysisOptions(strOption);
		    m_cirServ.SetXmlOption(false);
            LTML ltml_out = new LTML();
            String resultStr = m_cirServ.Analyze(strAnalyzeString);
            ltml_out.LoadFromXmlStr(resultStr);
            return ltml_out;
        }

        public LTML Analyze(String strOption, LTML ltmlIn)
        {
            if (strOption == null || ltmlIn == null)
            {
                throw new Exception(String.Format("{0}: LTPService.Analyze(strOption, ltmlIn)", LtpServiceError.ParameterNullError));
            }
            m_cirServ.SetAnalysisOptions(strOption);
            m_cirServ.SetXmlOption(true);
            String resultStr = m_cirServ.Analyze(ltmlIn.GetXMLStr());
            LTML ltml_out = new LTML();
            ltml_out.LoadFromXmlStr(resultStr);
            return ltml_out;
        }

        #endregion

        #region Private Methods

        private int Authorize(String strAuthor)
        {
            try
            {
                m_cirServ.Authorize(strAuthor);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return 0;
            }

            return 1;
        }

        #endregion
    }
}
