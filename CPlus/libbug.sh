#!/bin/bash

grep -v "LDFLAGS" __xml4nlp/Makefile > __xml4nlp/Makefile.backup
mv __xml4nlp/Makefile.backup __xml4nlp/Makefile
grep -v "LDFLAGS" __util/Makefile > __util/Makefile.backup
mv __util/Makefile.backup __util/Makefile
