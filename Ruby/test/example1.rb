# encoding: UTF-8
require 'ltpservice'

client = LTPService.new('username', 'password')
puts client.analyze('我爱北京天安萌', 'UTF-8')

