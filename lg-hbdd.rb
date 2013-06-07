# !/usr/bin/ruby -W0

require 'java'
require './target/uber-StrategyBdd-0.0.1-SNAPSHOT.jar'
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

class GTPClient
	def initialize(cmdline)
		@io=IO.popen(cmdline,'w+')
		#puts @io.readlines
		#puts $stdout.readlines
	end
	def cmd(c)
		puts "Trying write to interpreter: "
		#@io.puts c.strip
		puts c.strip
		#puts @io.readlines
		return @io.gets("\n\n")
	end
	def close
		@io.close
	end
end

class HBdd < LittleGolemInterface
	include HavannahCoords
	LOGIN='hbdd'
	PSW='nojyraca'
	BOSS_ID='34687'
	def initialize
		@supported_gametypes = /Hav/
		super(LOGIN,PSW,BOSS_ID)
	end
	def call_hbdd(size, moves)
		wrapper = GtpWrapperModule::GtpWrapper.new
		wrapper.setBoardSize(size)
		wrapper.setMoves(moves.join(' '))
		response = wrapper.getResponse
		return response[2..-3] #trim off the = and newlines
	end
	def parse_make_moves(gameids)
		gameids.each do |g|
			if (game = get_game(g))
				size = game.scan(/SZ\[(.+?)\]/).flatten[0].to_i
				moves = game.scan(/;[B|W]\[(.+?)\]/).flatten.map{|m| coord_HGF2GA(m, size) }.compact

				self.log("Game #{g}, size #{size}: #{moves.join(' ')}")
				newmove = self.call_hbdd(size, moves)
				self.log("Game #{g}, size #{size}: response #{newmove}")
				self.post_move(g, coord_GA2LG(newmove, size))
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


