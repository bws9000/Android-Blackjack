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

//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Card {

	final Blackjack game;

	private String name;
	private int playertype; //0 dealer, 1 player
	private int type;
	private int group;
    private int value;
    boolean is_ace = false;

    private int xpos;
    private int ypos;

    private final int START_CARD_POSX = 220;
    private final int START_CARD_POSY = Blackjack.screen_top + 110;

  	AtlasRegion card_region;
  	Sprite card_sprite;

	public Card(int cardtype,
				 int player,
				 final Blackjack gam){

		this.game = gam;

		playertype = player;
		type = cardtype; //0=dealer, 1=player, 2=other


		switch (type) {
			case 0:  name = "Ace of Clubs";
					 value = 0; // ACE VALUE DETERMINED IN GameLogic class
					 is_ace = true;
					 card_region = game.ac;
					 card_sprite = new Sprite(card_region);
					 group = 0;
			         break;
			case 1:  name = "Ace of Diamonds";
					 value = 0; // ACE VALUE DETERMINED IN GameLogic class
					 is_ace = true;
					 card_region = game.ad;
					 card_sprite = new Sprite(card_region);
					 group = 0;
					 break;
			case 2:  name = "Ace of Hearts";
					 value = 0; // ACE VALUE DETERMINED IN GameLogic class
					 is_ace = true;
					 card_region = game.ah;
					 card_sprite = new Sprite(card_region);
					 group = 0;
					 break;
			case 3:  name = "Ace of Spades";
					 value = 0; // ACE VALUE DETERMINED IN GameLogic class
					 is_ace = true;
					 card_region = game.as;
					 card_sprite = new Sprite(card_region);
					 group = 0;
					 break;
			case 4:  name = "King of Clubs";
					 value = 10;
					 card_region = game.kc;
					 card_sprite = new Sprite(card_region);
					 group = 1;
			         break;
			case 5:  name = "King of Diamonds";
					 value = 10;
					 card_region = game.kd;
					 card_sprite = new Sprite(card_region);
					 group = 1;
					 break;
			case 6:  name = "King of Hearts";
					 value = 10;
					 card_region = game.kh;
					 card_sprite = new Sprite(card_region);
					 group = 1;
					 break;
			case 7:  name = "King of Spades";
					 value = 10;
					 card_region = game.ks;
					 card_sprite = new Sprite(card_region);
					 group = 1;
					 break;
			case 8:  name = "Queen of Clubs";
					 value = 10;
					 card_region = game.qc;
					 card_sprite = new Sprite(card_region);
					 group = 2;
			         break;
			case 9:  name = "Queen of Diamonds";
					 value = 10;
					 card_region = game.qd;
					 card_sprite = new Sprite(card_region);
					 group = 2;
					 break;
			case 10: name = "Queen of Hearts";
					 value = 10;
					 card_region = game.qh;
					 card_sprite = new Sprite(card_region);
					 group = 2;
					 break;
			case 11: name = "Queen of Spades";
					 value = 10;
					 card_region = game.qs;
					 card_sprite = new Sprite(card_region);
					 group = 2;
					 break;
			case 12: name = "Jack of Clubs";
					 value = 10;
					 card_region = game.jc;
					 card_sprite = new Sprite(card_region);
					 group = 3;
			         break;
			case 13: name = "Jack of Diamonds";
					 value = 10;
					 card_region = game.jd;
					 card_sprite = new Sprite(card_region);
					 group = 3;
					 break;
			case 14: name = "Jack of Hearts";
					 value = 10;
					 card_region = game.jh;
					 card_sprite = new Sprite(card_region);
					 group = 3;
					 break;
			case 15: name = "Jack of Spades";
					 value = 10;
					 card_region = game.jh;
					 card_sprite = new Sprite(card_region);
					 group = 3;
					 break;
			case 16: name = "Two of Clubs";
					 value = 2;
					 card_region = game.c2;
					 card_sprite = new Sprite(card_region);
					 group = 4;
			         break;
			case 17: name = "Two of Diamonds";
					 value = 2;
					 card_region = game.d2;
					 card_sprite = new Sprite(card_region);
					 group = 4;
					 break;
			case 18: name = "Two of Hearts";
					 value = 2;
					 card_region = game.h2;
					 card_sprite = new Sprite(card_region);
					 group = 4;
					 break;
			case 19: name = "Two of Spades";
					 value = 2;
					 card_region = game.s2;
					 card_sprite = new Sprite(card_region);
					 group = 4;
					 break;
			case 20: name = "Three of Clubs";
					 value = 3;
					 card_region = game.c3;
					 card_sprite = new Sprite(card_region);
					 group = 5;
			         break;
			case 21: name = "Three of Diamonds";
					 value = 3;
					 card_region = game.d3;
					 card_sprite = new Sprite(card_region);
					 group = 5;
					 break;
			case 22: name = "Three of Hearts";
					 value = 3;
					 card_region = game.h3;
					 card_sprite = new Sprite(card_region);
					 group = 5;
					 break;
			case 23: name = "Three of Spades";
					 value = 3;
					 card_region = game.s3;
					 card_sprite = new Sprite(card_region);
					 group = 5;
					 break;
			case 24: name = "Four of Clubs";
					 value = 4;
					 card_region = game.c4;
					 card_sprite = new Sprite(card_region);
					 group = 6;
			         break;
			case 25: name = "Four of Diamonds";
					 value = 4;
					 card_region = game.d4;
					 card_sprite = new Sprite(card_region);
					 group = 6;
					 break;
			case 26: name = "Four of Hearts";
					 value = 4;
					 card_region = game.h4;
					 card_sprite = new Sprite(card_region);
					 group = 6;
					 break;
			case 27: name = "Four of Spades";
					 value = 4;
					 card_region = game.s4;
					 card_sprite = new Sprite(card_region);
					 group = 6;
					 break;
			case 28: name = "Five of Clubs";
					 value = 5;
					 card_region = game.c5;
					 card_sprite = new Sprite(card_region);
					 group = 7;
			         break;
			case 29: name = "Five of Diamonds";
					 value = 5;
					 card_region = game.d5;
					 card_sprite = new Sprite(card_region);
					 group = 7;
					 break;
			case 30: name = "Five of Hearts";
					 value = 5;
					 card_region = game.h5;
					 card_sprite = new Sprite(card_region);
					 group = 7;
					 break;
			case 31: name = "Five of Spades";
					 value = 5;
					 card_region = game.s5;
					 card_sprite = new Sprite(card_region);
					 group = 7;
					 break;
			case 32: name = "Six of Clubs";
					 value = 6;
					 card_region = game.c6;
					 card_sprite = new Sprite(card_region);
					 group = 8;
			         break;
			case 33: name = "Six of Diamonds";
					 value = 6;
					 card_region = game.d6;
					 card_sprite = new Sprite(card_region);
					 group = 8;
					 break;
			case 34: name = "Six of Hearts";
					 value = 6;
					 card_region = game.h6;
					 card_sprite = new Sprite(card_region);
					 group = 8;
					 break;
			case 35: name = "Six of Spades";
					 value = 6;
					 card_region = game.s6;
					 card_sprite = new Sprite(card_region);
					 group = 8;
					 break;
			case 36: name = "Seven of Clubs";
					 value = 7;
					 card_region = game.c7;
					 card_sprite = new Sprite(card_region);
					 group = 9;
			         break;
			case 37: name = "Seven of Diamonds";
					 value = 7;
					 card_region = game.d7;
					 card_sprite = new Sprite(card_region);
					 group = 9;
					 break;
			case 38: name = "Seven of Hearts";
					 value = 7;
					 card_region = game.h7;
					 card_sprite = new Sprite(card_region);
					 group = 9;
					 break;
			case 39: name = "Seven of Spades";
					 value = 7;
					 card_region = game.s7;
					 card_sprite = new Sprite(card_region);
					 group = 9;
					 break;
			case 40: name = "Eight of Clubs";
					 value = 8;
					 card_region = game.c8;
					 card_sprite = new Sprite(card_region);
					 group = 10;
			         break;
			case 41: name = "Eight of Diamonds";
					 value = 8;
					 card_region = game.d8;
					 card_sprite = new Sprite(card_region);
					 group = 10;
					 break;
			case 42: name = "Eight of Hearts";
					 value = 8;
					 card_region = game.h8;
					 card_sprite = new Sprite(card_region);
					 group = 10;
					 break;
			case 43: name = "Eight of Spades";
					 value = 8;
					 card_region = game.s8;
					 card_sprite = new Sprite(card_region);
					 group = 10;
					 break;
			case 44: name = "Nine of Clubs";
					 value = 9;
					 card_region = game.c9;
					 card_sprite = new Sprite(card_region);
					 group = 11;
			         break;
			case 45: name = "Nine of Diamonds";
					 value = 9;
					 card_region = game.d9;
					 card_sprite = new Sprite(card_region);
					 group = 11;
					 break;
			case 46: name = "Nine of Hearts";
					 value = 9;
					 card_region = game.h9;
					 card_sprite = new Sprite(card_region);
					 group = 11;
					 break;
			case 47: name = "Nine of Spades";
					 value = 9;
					 card_region = game.s9;
					 card_sprite = new Sprite(card_region);
					 group = 11;
					 break;
			case 48: name = "Ten of Clubs";
					 value = 10;
					 card_region = game.c10;
					 card_sprite = new Sprite(card_region);
					 group = 12;
			         break;
			case 49: name = "Ten of Diamonds";
					 value = 10;
					 card_region = game.d10;
					 card_sprite = new Sprite(card_region);
					 group = 12;
					 break;
			case 50: name = "Ten of Hearts";
					 value = 10;
					 card_region = game.h10;
					 card_sprite = new Sprite(card_region);
					 group = 12;
					 break;
			case 51: name = "Ten of Spades";
					 value = 10;
					 card_region = game.s10;
					 card_sprite = new Sprite(card_region);
					 group = 12;
					 break;
			case 52: name = "Face Card";
					 value = 0;
					 card_region = game.face;
					 card_sprite = new Sprite(card_region);
					 break;

		}
		setSpriteInitialPosition();

	}

	//get card sprite
	public Sprite getSprite(){
		return card_sprite;
	}

	//set initial position
	public void setSpriteInitialPosition(){
		card_sprite.setPosition(START_CARD_POSX, START_CARD_POSY);
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

	// everything else
	public int getGroup(){
		return group;
	}
	public String getName() {
		return name;
	}
	public int getType() {
		return type;
	}
	public int getValue() {
		return value;
	}
	public int getPlayerType(){
		return playertype;
	}


}
