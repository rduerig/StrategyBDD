require 'java'
require './dist/hbdd-0.8.jar'

module GtpWrapperModule
	import "com.strategy.api.board"
	import "com.strategy.api.logic.prediction"
	import "com.strategy.havannah.board"
	import "com.strategy.util"
	import "com.strategy.api.logic"
	import "com.google.common.base"
	import "com.google.common.collect"
	import "com.strategy.havannah.logic.prediction"
	import "com.strategy.util.preferences"
end

def predict_move(size, moves)
	#GtpWrapperModule::Preferences.createInstance(ARGV)
	wrapper = GtpWrapperModule::GtpWrapper.new
	wrapper.setBoardSize(size)
	wrapper.setMoves(moves.join(' '))
	response = wrapper.getResponse
	return response
end

def predit_moves_list(size, moves)
	#GtpWrapperModule::Preferences.createInstance(ARGV)
	wrapper = GtpWrapperModule::GtpWrapper.new
	wrapper.setBoardSize(size)
	wrapper.setMoves(moves.join(' '))
	response = wrapper.getPredictedMoves
	return response
end

def parse_moves(game)
	if(game)
		moves = game.scan(/;[B|W]\[(.+?)\]/).flatten#.compact.join(',').downcase
		return moves
	else p 'no game given'
		return nil
	end
end

def parse_size(game)
	if(game)
		size = game.scan(/SZ\[(.+?)\]/).flatten[0].to_i
		return size
	else p 'no game given'
		return nil
	end
end

if(!ARGV[0])
	puts "Usage:" 
	puts "predition [PATH] where PATH denotes a relative path to a hgf file" 
	exit
end

path = ARGV[0]
if(!File.exists?("#{path}"))
	puts "could not find file for given path #{path}"
	exit
end

contents = IO.readlines("#{path}")
moves = parse_moves(contents[0])
size = parse_size(contents[0])
if(size && moves)
				predicted = predit_moves_list(size, moves)
	puts "Moves predicted: #{predicted}"
else
	puts "could not parse size and moves from given file #{path}" 
end

