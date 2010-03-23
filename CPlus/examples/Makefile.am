INCLUDES = -I.

bin_PROGRAMS = ltp1

ltp1_SOURCES = example1.cpp

ltp1_LDADD = \
	-L../__util -lutil \
	-L../__ltpservice -lltpservice \
	-L../__xml4nlp -lxml4nlp \
	-L../__util -lbase64

ltp1_LDFLAGS = -ldl

