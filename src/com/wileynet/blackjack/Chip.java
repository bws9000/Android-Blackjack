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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Chip {
	//animated chips
	
	final Blackjack game;
	
	private String name;
	public boolean active = false;
	private int playertype; //0 dealer, 1 player
	private int type;
	
    private int xpos;
    private int ypos;
    
    private final int START_CHIP_POSX = -35;
    private final int START_CHIP_POSY = -35;
    
    AtlasRegion chip_region;
  	Sprite chip_sprite;
  	
	public Chip(int player,
				int cardtype,
				final Blackjack gam){
		
		this.game = gam;
		
		playertype = player;
		type = cardtype;
        
		
		switch (type) {
			case 100: name = "$100 Chip";
					 chip_region = game.x100_small_new;
					 chip_sprite = new Sprite(chip_region);
			         break;
			case 50: name = "$50 Chip";
				     chip_region = game.x50_smaller_new;
				     chip_sprite = new Sprite(chip_region);
					 break;
			case 25: name = "$25 Chip";
		     	     chip_region = game.x25_smaller_new;
		     	     chip_sprite = new Sprite(chip_region);
					 break;
			case 10: name = "$10 Chip";
		     	     chip_region = game.x10_smaller_new;
		     	     chip_sprite = new Sprite(chip_region);
					 break;
			case 5: name = "$5 Chip";
    	     		 chip_region = game.x5_smaller_new;
    	     		 chip_sprite = new Sprite(chip_region);
			         break;
			
		}
		
		chip_sprite = new Sprite(chip_region);
		
		setSpriteInitialPosition();
		        
	}
	
	//get card sprite
	public Sprite getSprite(){
		return chip_sprite;
	}
	//set initial position
	public void setSpriteInitialPosition(){
		chip_sprite.setPosition(START_CHIP_POSX, START_CHIP_POSY);
	}
	
	//set active ... it's been moved
	public void setActive(){
		active = true;
	}
	public boolean getActive(){
		return active;
	}
	
	//graphics
	public void setX(int x){
		xpos = x;
	}
	public int getX(){
		return xpos;
	}
	
	public void setY(int y){
		ypos = y;
	}
	public int getY(){
		return ypos;
	}
		
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public int getPlayerType(){
		return playertype;
	}
	

}

