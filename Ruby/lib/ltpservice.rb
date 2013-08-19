# ltpservice.rb

$addr = 'api.ltp-cloud.com:8080'
$port = '80'
$uris = '/ltp_srv/ltp'

require 'net/http'
require 'uri'

class LTPService
    def initialize(username, password)
        @username = username
        @password = password
    end

    def analyze(input, opt = nil, encoding = 'UTF-8')
        url = URI('http://' + $addr + ':' + $port + $uris)
        req = Net::HTTP::Post.new(url.path)
        req.basic_auth @username, @password

        if input.kind_of? String
            req.set_form_data({'s' => input,
                            'c' => encoding, 
                            't' => (opt.nil? ? LTPOption::ALL : opt)})

            res = Net::HTTP.new(url.host, url.port).start do |http|
                http.request(req)
            end
        elsif input.kind_of? LTML
            #puts input.to_s
            req.set_form_data({'s' => input.to_s,
                            'x' => 'y',
                            'c' => encoding, 
                            't' => (opt.nil? ? LTPOption::ALL : opt)})

            res = Net::HTTP.new(url.host, url.port).start do |http|
                http.request(req)
            end
        end

        LTML.new(res.body)
    end
end

require_relative 'ltpservice/LTML'
require_relative 'ltpservice/LTPOption'
