using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ltp_service
{
    class LtpServiceError
    {
        public const String AuthorizeFormatError = "Authorize string format Error!";
        public const String EncodingFormatError = "The specified encoding is notsupported!";
        public const String AddSentenceNoteMissMatchError = "AddSentence error: Ensure the sentence you add is match with the note option!";
        public const String ParameterNullError = "One of the input parameter is null!";
        public const String AnalysisOptionError = "The select analysis option is invalid!";
    }
}
