# encoding: UTF-8
require 'ltpservice'

client = LTPService.new('username', 'password')

ltml = LTML.new
ltml.build_from_words(['我', '爱', '北京', '天安门'], 'UTF-8')
puts client.analyze(ltml)
