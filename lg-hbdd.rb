#!/usr/bin/ruby -W0

require './lg-interface'

class GTPClient
	def initialize(cmdline)
		@io=IO.popen(cmdline,'w+')
	end
	def cmd(c)
		@io.puts c.strip
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

	def call_castro(size, moves)
		gtp = GTPClient.new("java -jar ./target/uber-StrategyBdd-0.0.1-SNAPSHOT.jar -s #{size} -m #{moves}")
		gtp.cmd("hguicoords")
		gtp.cmd("swap 0")
		gtp.cmd("boardsize #{size}")
		case size
			when 4 then gtp.cmd('player_params -g 1')
			when 6 then gtp.cmd('player_params -R 50')
			when 7 then gtp.cmd('player_params -R 60 -p 1')
			when 8 then gtp.cmd('player_params -R 70 -p 1')
			when 9 then gtp.cmd('player_params -R 100 -p 1')
			when 10 then gtp.cmd('player_params -R 130 -p 1')
		end
		gtp.cmd("time -r 0 -g 0 -m #{size*60} -f 0")
		gtp.cmd("playgame " + moves.select{|m| m != 'swap'}.join(' '))
		response = gtp.cmd('genmove')
		gtp.cmd('quit')
		sleep 1
		gtp.close
		return response[2..-3] #trim off the = and newlines
	end
	def parse_make_moves(gameids)
		gameids.each do |g|
			if (game = get_game(g))
				size = game.scan(/SZ\[(.+?)\]/).flatten[0].to_i
				moves = game.scan(/;[B|W]\[(.+?)\]/).flatten.map{|m| coord_HGF2GA(m, size) }.compact

				self.log("Game #{g}, size #{size}: #{moves.join(' ')}")
				newmove = self.call_castro(size, moves)
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


