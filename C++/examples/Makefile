INCLUDE=../include/
LIBRARY=../lib/
LINKS=-lltpservice
CXX=g++
C=gcc

all: example1 example2 example3

example1: example1.cpp
	$(CXX) -o example1 -I$(INCLUDE) -L$(LIBRARY) example1.cpp $(LINKS)

example2: example2.cpp
	$(CXX) -o example2 -I$(INCLUDE) -L$(LIBRARY) example2.cpp $(LINKS)

example3: example3.cpp
	$(CXX) -o example3 -I$(INCLUDE) -L$(LIBRARY) example3.cpp $(LINKS)

.PHONY: clean

clean:
	rm example1
	rm example2
	rm example3
