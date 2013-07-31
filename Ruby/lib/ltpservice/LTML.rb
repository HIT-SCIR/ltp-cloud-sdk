require 'rexml/document'
require 'iconv'

class String
    def gbk_to_utf8
        Iconv.conv("GBK", "UTF-8", self)
    end
end

class LTML
    def initialize(xmlstr)
        @doc = REXML::Document.new(xmlstr)
    end

    def build(sentence)
    end
end
