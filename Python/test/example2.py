# -*- coding:utf8 -*-
import ltpservice
from account import username, password

client = ltpservice.LTPService("%s:%s" % (username, password))
ltml = ltpservice.LTML()
ltml.build_from_words(["我", "爱", "北京", "天安门"])

result = client.analysis(ltml, ltpservice.LTPOption.PARSER)
print result.tostring()
