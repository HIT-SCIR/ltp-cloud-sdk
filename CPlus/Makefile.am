AUTOMAKE_OPTIONS = foreign

#AM_CPPFLAGS = -I$(top_srcdir) \
#	-I ~/lib/include
INCLUDES = -I __ltpService \
	-I __util \
	-I __xml4nlp

#libltp_a_LIBADD = -L __ltpService -lservice \
#	-L __util -lutil \
#	-L __xml4nlp -lxml4nlp

SUBDIRS = __util __xml4nlp __ltpService 

include_HEADERS = __ltpService/CirService.h \
		__ltpService/LTPOption.h \
		__ltpService/Word.h \
		__ltpService/LTML.h \
		__ltpService/LTPService.h \
		__util/Base64Encoder.h \
		__util/MyLib.h \
		__xml4nlp/XML4NLP.H \
		__xml4nlp/tinystr.h \
		__xml4nlp/tinyxml.h

