require 'java'
require './dist/hbdd.jar'
require './lg-interface'

module GtpWrapperModule
	import "com.strategy.api.board"
	import "com.strategy.api.logic.prediction"
	import "com.strategy.havannah.board"
	import "com.strategy.util"
	import "com.strategy.api.logic"
	import "com.google.common.base"
	import "com.google.common.collect"
	import "com.strategy.havannah.logic.prediction"
end

class HBdd < LittleGolemInterface
	include HavannahCoords
	LOGIN='hbdd'
	PSW='nojyraca'
	BOSS_ID='34282'
	def initialize
		@supported_gametypes = /Hav/
		super(LOGIN,PSW,BOSS_ID)
	end
	def call_hbdd(size, moves)
		wrapper = GtpWrapperModule::GtpWrapper.new
		wrapper.setBoardSize(size)
		wrapper.setMoves(moves.join(' '))
		response = wrapper.getResponse
		#return response[2..-3] #trim off the = and newlines
		return response
	end
	def parse_make_moves(gameids)
		gameids.each do |g|
			if (game = get_game(g))
				size = game.scan(/SZ\[(.+?)\]/).flatten[0].to_i
				moves = game.scan(/;[B|W]\[(.+?)\]/).flatten.compact
				self.log("Game #{g}, size #{size}: #{moves.join(' ')}")
				newmove = self.call_hbdd(size, moves)
				self.log("Game #{g}, size #{size}: response #{newmove}")
				move = coord_HGF2GA(newmove, size)
				self.post_move(g, coord_GA2LG(move, size))
			else
				self.log('error getting game')
				sleep(600)
			end
		end
	end
end

#enable to cause the http library to show warnings/errors
#$VERBOSE = 1

w=HBdd.new
#w.test_coords
loop {
	begin
		while w.parse
		end
		sleep(30)
	rescue Timeout::Error => e
		p 'timeout error (rbuff_fill exception), try again in 30 seconds'
		sleep(30)
	rescue => e
		p e.message
		p e.backtrace
		p 'An error... wait 5 minutes'
		sleep(300)
	end
}


