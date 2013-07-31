# ltpservice.rb

$addr = 'ir.hit.edu.cn'
$port = '80'
$uris = '/demo/ltp_ws/ltp'

require 'net/http'
require 'uri'

class LTPService
    def initialize(username, password)
        @username = username
        @password = password
    end

    def analyze(sent, encoding)
        url = URI('http://' + $addr + ':' + $port + $uris)
        req = Net::HTTP::Post.new(url.path)
        req.basic_auth @username, @password

        req.set_form_data({'s' => sent,
                          'c' => encoding,})

        res = Net::HTTP.new(url.host, url.port).start { |http|
            http.request(req)
        }

        res.body
    end
end
