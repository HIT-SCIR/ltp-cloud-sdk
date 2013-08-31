# encoding: UTF-8
require 'ltpservice'

client = LTPService.new('email', 'token')
puts client.analyze('我爱北京天安门', LTPOption::WS, 'UTF-8')

