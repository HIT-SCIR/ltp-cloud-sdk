#!/usr/bin/env python
import urllib, urllib2, base64
import LTPOption

from LTML import LTML

addr = "http://api.ltp-cloud.com:8080"
uris = "/ltp_srv/ltp"

class LTPService(object):
    """
    ltp service class
    """
    #   @param[in]  au      authorization token
    def __init__(self, au, encoding="utf-8"):
        self.au = au
        self.encoding = encoding;

    #
    # execute the analysis process
    #
    #   @param[in]  input   the input sentence
    #   @param[in]  opt     the options
    #   @return     LTML    the result
    def analysis(self, input, opt = None):

        request = urllib2.Request(addr + uris);
        request.add_header("Authorization", 
                "Basic %s" % (base64.encodestring(self.au).replace("\n", "")))

        if isinstance(input, str):
            data = {
                    's': input,
                    'x': 'n',
                    'c': self.encoding,
                    't': (opt if opt is not None else LTPOption.ALL)}

            params = urllib.urlencode(data)
            result = urllib2.urlopen(request, params)
            content = result.read().strip()
            return LTML(content)

        elif isinstance(input, LTML):
            data = {
                    's' : input.tostring(self.encoding),
                    'x' : 'y',
                    'c' : self.encoding,
                    't' : (opt if opt is not None else LTPOption.ALL),}

            params = urllib.urlencode(data)
            result = urllib2.urlopen(request, params)
            content = result.read().strip()
            return LTML(content)
