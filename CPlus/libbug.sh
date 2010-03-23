#!/bin/bash

grep -v "LDFLAGS" __xml4nlp/Makefile > __xml4nlp/Makefile.backup
mv __xml4nlp/Makefile.backup __xml4nlp/Makefile
