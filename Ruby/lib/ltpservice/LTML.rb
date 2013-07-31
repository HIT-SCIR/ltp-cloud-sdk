require 'rexml/document'
require 'builder'

class LTML
    def initialize(xmlstr = nil)
        unless xmlstr.nil?
            @doc = REXML::Document.new(xmlstr)
            #@doc.context[:attribute_quote] = :quote
        end
    end

    def build(sentence, encoding = 'UTF-8')
        buffer = ""
        xml = Builder::XmlMarkup.new(:target => buffer)
        xml.instruct! :xml, :encoding => encoding
        xml.xml4nlp do |xml4nlp|
            xml4nlp.note(:sent => 'n', :word => 'n', 
                         :pos => 'n', :parser => 'n', 
                         :wsd => 'n', :srl => 'n')
            xml4nlp.doc do |doc|
                doc.para do |para|
                    para.sent(:id => '0', :cont => sentence)
                end
            end
        end
        @doc = REXML::Document.new(buffer)
        #@doc.context[:attribute_quote] = :quote
    end

    def build_from_words(words, encoding = 'UTF-8')
        buffer = ""
        sentence = words.join("")
        xml = Builder::XmlMarkup.new(:target => buffer)
        xml.instruct! :xml, :encoding => encoding
        xml.xml4nlp do |xml4nlp|
            xml4nlp.note(:sent => 'y', :word => 'y',
                         :pos => 'n', :parser => 'n',
                         :wsd => 'n', :srl => 'n')
            xml4nlp.doc do |doc|
                doc.para do |para|
                    para.sent(:id => 0, :cont => sentence)
                    para.sent do |sent|
                        words.each_index do |i|
                            sent.word(:id => i, :cont => words.fetch(i))
                        end
                    end
                end
            end
        end
        @doc = REXML::Document.new(buffer)
        @doc.context[:attribute_quote] = :quote
    end

    def count_paragraph
        count = 0
        @doc.each_element("xml4nlp/doc/para") do |n|
            count = count + 1 
        end
        count
    end

    def count_sentence(pid)
        count = 0
        p = @doc.root.elements["doc"].elements[pid + 1]
        p.each_element("sent") do |n|
            count = count + 1
        end
        count
    end

    def get_words(pid, sid)
        words = []
        s = @doc.root.elements[2].elements[pid + 1].elements[sid + 1]
        s.each_element("word") do |w|
            words << w.attributes['cont']
        end
        words
    end

    def get_sentence(pid, sid)
        s = @doc.root.elements[2].elements[pid + 1].elements[sid + 1]
        s.attributes['cont']
    end

    def to_s
        @doc.to_s
    end
end
