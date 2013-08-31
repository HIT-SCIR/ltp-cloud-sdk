# -*- coding:utf8 -*-
import ltpservice
from account import username, password

client = ltpservice.LTPService("%s:%s" % (username, password))
result = client.analysis("我爱北京天安门。天安门上太阳伞。", ltpservice.LTPOption.WS)

pid = 0
for sid in xrange(result.count_sentence(pid)):
    print "|".join([word.encode("utf8") for word in result.get_words(pid, sid)])
