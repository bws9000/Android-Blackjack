/*
 * 
    Copyright (C) 2014  Burt Wiley Snyder
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or 
     any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	armorsoft@gmail.com
	
*/


package com.wileynet.blackjack;

public final class Player {
	
	final Blackjack game;
	
	private GameLogic gameLogic;
	public int playerType;
	public String playerName;
	
	public Player(String name,
					int type,
					final Blackjack gam){
		
		this.game = gam;
		
		//type::
		//0 player
		//1 player split
		playerName = name;
		playerType = type;
		gameLogic = new GameLogic(game);
		
	}
	
	public GameLogic gamelogic(){
		return gameLogic;
	}
	
	public void resetGameLogic(){
		gameLogic = new GameLogic(game);
	}

}
