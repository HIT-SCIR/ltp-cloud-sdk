#!/usr/bin/env python
import sys
import LTPOption
import xml.etree.ElementTree as ET

from xml.etree.ElementTree import ElementTree
from xml.etree.ElementTree import Element
from xml.etree.ElementTree import SubElement
from xml.etree.ElementTree import tostring

class LTML(object):
    def __init__(self, xmlstr = None, encoding="utf-8"):
        if xmlstr is not None:
            self.dom = ET.fromstring(xmlstr)

    def set_sent_on_note(self):
        self.note.set("sent", "y")

    def set_word_on_note(self):
        self.set_sent_on_note()
        self.note.set("word", "y")

    def set_pos_on_note(self):
        self.set_word_on_note()
        self.note.set("pos", "y")

    def set_ne_on_note(self):
        self.set_pos_on_note()
        self.note.set("ne", "y")

    def set_parser_on_note(self):
        self.set_pos_on_note()
        self.note.set("parser", "y")

    def set_srl_on_note(self):
        self.set_parser_on_note()
        self.note.set("srl", "y")

    def set_all_on_note(self):
        self.set_srl_on_note()
        self.note.set("ne", "y")

    def _clean_note(self):
        self.note.set("sent",   "n")
        self.note.set("word",   "n")
        self.note.set("pos",    "n")
        self.note.set("parser", "n")
        self.note.set("ne",     "n")
        self.note.set("srl",    "n")

    # build ltml from string
    #
    #   @param[in]  buffer      the string buffer
    #   @param[in]  encoding    the encoding
    #   @return     the xml
    def build(self, buffer, encoding="utf-8"):
        self.xml4nlp = Element('xml4nlp')
        self.note    = SubElement(self.xml4nlp, 'note')
        self.doc     = SubElement(self.xml4nlp, 'doc')

        para    = SubElement(self.doc, 'para')
        para.set("id", "0")
        para.text = buffer.decode(encoding)

        self._clean_note()
        self.dom = self.xml4nlp

    # 
    # build ltml from words, automatically detect input type
    #
    #   @param[in]  words       the words, list(str), list(list), 
    #                           list(tuple) is supported
    def build_from_words(self, words, encoding="utf-8"):
        if isinstance(words, str):
            self.build(words)
        elif isinstance(words, list):
            flag = "seg"
            assert len(words) > 0

            word = words[0]
            if isinstance(word, str):
                flag = "seg"
            elif (isinstance(word, list) 
                    and len(word) == 2 
                    and isinstance(word[0], str)
                    and isinstance(word[1], str)):
                flag = "pos"
            elif (isinstance(word, tuple) 
                    and len(word) == 2 
                    and isinstance(word[0], str)
                    and isinstance(word[1], str)):
                flag = "pos"
            else:
                flag = "unknown"

            self.xml4nlp = Element('xml4nlp')
            self.note    = SubElement(self.xml4nlp, 'note')
            self.doc     = SubElement(self.xml4nlp, 'doc')

            para    = SubElement(self.doc, 'para')
            sent    = SubElement(para, 'sent')

            para.set("id", "0")
            sent.set("id", "0")

            self._clean_note()

            if flag == "seg":
                for id, word in enumerate(words):
                    sent.append(Element('word', {
                        'id': ('%d' % id),
                        'cont': word.decode(encoding),}))

                sent.set('cont', ("".join(words)).decode(encoding))
                self.set_word_on_note()
            elif flag == "pos":
                for id, wordpos in enumerate(words):
                    word, pos  = wordpos
                    sent.append(Element('word', {
                        'id': ('%d' % id),
                        'cont' : word.decode(encoding),
                        'pos' : pos,}))
                sent.set('cont', ("".join([word[0] for word in words])).decode(encoding))
                self.set_pos_on_note()

            self.dom = self.xml4nlp

    #
    # count number of paragraph given the paragraph index
    #
    #   @param[in]  pid         the index of paragraph
    #   @return     int         number of sentence
    def count_paragraph(self):
        return len(self.dom[1].findall('para'))

    #
    # count number of sentence given the paragraph index
    #
    #   @param[in]  pid         the index of paragraph
    #   @return     int         number of sentence
    def count_sentence(self, pid = None):
        if isinstance(pid, int):
            return len(self.dom[1][pid].findall('sent'))
        else:
            return len([sent for sent in self.dom[1].iter('sent')])

    # 
    # Maintain a list of word
    #
    #   @param[in]  pid         the index of paragraph
    #   @param[in]  sid         the index of sentence
    #   @param[in]  encoding    the encoding
    #   @return     list(str)   list of words
    def get_words(self, pid, sid, encoding=None):
        ret = [e.get('cont') for e in self.dom[1][pid][sid].findall('word')]

        if encoding is None:
            return ret
        else:
            return [r.encode(encoding) for r in ret]

    # 
    # Maintain a list of pos
    #
    #   @param[in]  pid         the index of paragraph
    #   @param[in]  sid         the index of sentence
    #   @param[in]  encoding    the encoding
    #   @return     list(str)   list of pos
    def get_pos(self, pid, sid, encoding=None):
        ret = [e.get('pos') for e in self.dom[1][pid][sid].findall('word')]

        if encoding is None:
            return ret
        else:
            return [r.encode(encoding) for r in ret]

    # 
    # Maintain a list of parents
    #
    #   @param[in]  pid         the index of paragraph
    #   @param[in]  sid         the index of sentence
    #   @param[in]  encoding    the encoding
    #   @return     list(str)   list of parents for each word
    def get_parents(self, pid, sid, encoding=None):
        ret = [e.get('parent') for e in self.dom[1][pid][sid].findall('word')]

        if encoding is None:
            return ret
        else:
            return [r.encode(encoding) for r in ret]

    # 
    # Maintain a list of relations
    #
    #   @param[in]  pid         the index of paragraph
    #   @param[in]  sid         the index of sentence
    #   @param[in]  encoding    the encoding
    #   @return     list(str)   list of relation between a word and its parent
    def get_relations(self, pid, sid, encoding=None):
        ret = [e.get('relate') for e in self.dom[1][pid][sid].findall('word')]

        if encoding is None:
            return ret
        else:
            return [r.encode(encoding) for r in ret]


    #
    # maintain the sentence string
    #
    #   @param[in]  pid     the index of paragraph
    #   @param[in]  sid     the index of sentence
    #   @return     str     the sentence
    def get_sentence(self, pid, sid, encoding=None):
        ret = self.dom[1][pid][sid].get('cont')

        if encoding is None:
            return ret
        else:
            return ret.encode(encoding)

    def tostring(self, encoding="utf-8"):
        return tostring(self.dom, encoding)

