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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class SpriteAtlas {
	
	final Blackjack game;
	
    //TextureAtlas card_atlas,
    TextureAtlas ui_atlas,
    			 copy_atlas,
    			 score_atlas;
    
    //AtlasRegion[] card_array,
    AtlasRegion[] ui_array,
    		 	  copy_array,
    		 	  dealer_score_array,
    		 	  player_score_array,
    		 	  split_score_array;
    
    /*cards*/
    
    /*
    AtlasRegion ac,ad,ah,as;
    AtlasRegion kc,kd,kh,ks;
    AtlasRegion qc,qd,qh,qs;
    AtlasRegion jc,jd,jh,js;
    AtlasRegion c2,d2,h2,s2;
    AtlasRegion c3,d3,h3,s3;
    AtlasRegion c4,d4,h4,s4;
    AtlasRegion c5,d5,h5,s5;
    AtlasRegion c6,d6,h6,s6;
    AtlasRegion c7,d7,h7,s7;
    AtlasRegion c8,d8,h8,s8;
    AtlasRegion c9,d9,h9,s9;
    AtlasRegion c10,d10,h10,s10;
    AtlasRegion face;
    */
    
    /*ui*/
    AtlasRegion table,
    		    black_circle,
    		    black_white_circle,
    		    green_circle,
    		    green_copy_back,
    		    green_white_circle,
    		    dealer_white_circle,
    		    player_white_circle,
    		    dealer_red_white_circle,
    		    player_red_white_circle,
    		    dealer_blue_white_circle,
    		    player_blue_white_circle,
    		    dealer_black_white_circle,
    		    player_black_white_circle,
    		    dealer_green_white_circle,
    		    player_green_white_circle,
    		    bet_tab,
    		    bet_menu,
    		    split_back,
    		    split_white_circle,
    		    split_red_white_circle,
    		    split_blue_white_circle,
    		    split_black_white_circle,
    		    split_green_white_circle,
    		    chips_5,
    		    chips_10,
    		    chips_25,
    		    chips_50,
    		    chips_100,
    		    c_control_panel_back_top,
    		    c_deal,
    		    c_hit,
    		    c_split,
    		    c_stand,
    		    c_control_panel_back_bottom,
    		    bet_black_box,
				bet_down_arrow,
				bet_up_arrow,
			    c100glow,
			    c10glow,
			    c25glow,
			    c50glow,
			    c5glow,
    		    control_panel_back_top,
    		    doubledown,
    		    splash,
    		    buytokens,
    		    buyback1,
    		    bonus_chips,
    		    buy_chip_blink,
    		    full_screen_shadow,
    		    homebutton,
    		    home_screen,
    		    settings_num_two,
    		    settings_num_three,
    		    settings_num_four,
    		    settings_num_five,
    		    settings_num_six,
    		    settings_off,
    		    settings_off_copy,
    		    settings_on,
    		    settings_on_copy,
    		    settings_back_small,
    		    settings_back_big,
    		    settings_sound_back,
    		    settings_deck_back,
    		    settings_dealer_hit_back,
    		    settings_card_dealt_back,
    		    settings_sound_on,
    		    settings_sound_on_copy,
    		    settings_sound_off,
    		    settings_sound_off_copy,
    		    settings_deck_2_on,
    		    settings_deck_2_off,
    		    settings_deck_3_on,
    		    settings_deck_3_off,
    		    settings_deck_4_on,
    		    settings_deck_4_off,
    		    settings_deck_5_on,
    		    settings_deck_5_off,
    		    settings_deck_6_on,
    		    settings_deck_6_off,
    		    settings_dhit_on,
    		    settings_dhit_on_copy,
    		    settings_dhit_off,
    		    settings_dhit_off_copy,
    		    settings_dealt_on,
    		    settings_dealt_on_copy,
    		    settings_dealt_off,
    		    settings_dealt_off_copy,
    		    greenbackarrow_one,
    		    peeler1,
    		    x100_small,
    		    x10_smaller,
    		    x25_smaller,
    		    x50_smaller,
    		    x5_smaller,
    		    bet_menu_bigger2,
    		    bet_menu_bigger2_close,
    		    split_button2,
    		    addingfreechips;
    
    /*copy*/
    AtlasRegion blackjack,bust,lose,win,push,twinkle_small,twinkle_medium,twinkle_large,
    			d_blackjack,d_bust,d_lose,d_win,d_push,
    			s1_blackjack,s1_bust,s1_lose,s1_win,s1_push,
    			s2_blackjack,s2_bust,s2_lose,s2_win,s2_push;
    
    /*dealer score*/
    AtlasRegion dsc0,dsc1,dsc2,dsc3,dsc4,dsc5,dsc6,dsc7,dsc8,dsc9,dsc10,dsc11,dsc12,dsc13,dsc14,dsc15,
    dsc16,dsc17,dsc18,dsc19,dsc20,dsc21,dsc22,dsc23,dsc24,dsc25,dsc26,dsc27,dsc28,dsc29,dsc30,dsc21b;
    
    /*player score*/
    AtlasRegion psc0,psc1,psc2,psc3,psc4,psc5,psc6,psc7,psc8,psc9,psc10,psc11,psc12,psc13,psc14,psc15,
    psc16,psc17,psc18,psc19,psc20,psc21,psc22,psc23,psc24,psc25,psc26,psc27,psc28,psc29,psc30,psc21b;
    
    /*split score*/
    AtlasRegion ssc0,ssc1,ssc2,ssc3,ssc4,ssc5,ssc6,ssc7,ssc8,ssc9,ssc10,ssc11,ssc12,ssc13,ssc14,ssc15,
    ssc16,ssc17,ssc18,ssc19,ssc20,ssc21,ssc22,ssc23,ssc24,ssc25,ssc26,ssc27,ssc28,ssc29,ssc30,ssc21b;
    
    
    
	public SpriteAtlas(final Blackjack gam){
		
		this.game = gam;
		
		//card_atlas = new TextureAtlas(Gdx.files.internal("data/cards2_128_packed/cards2_128.atlas"));
		ui_atlas = new TextureAtlas(Gdx.files.internal("data/ui_packed/ui.atlas"));
		copy_atlas = new TextureAtlas(Gdx.files.internal("data/copy_packed/copy.atlas"));
		score_atlas = new TextureAtlas(Gdx.files.internal("data/score_packed/score.atlas"));
        
        
        /*ui*/
        table = ui_atlas.findRegion("table");
        black_circle = ui_atlas.findRegion("black_circle");
        black_white_circle = ui_atlas.findRegion("black_white_square_smaller");
        green_circle = ui_atlas.findRegion("green_circle");
        green_copy_back = ui_atlas.findRegion("green_copy_back");
        green_white_circle = ui_atlas.findRegion("green_white_square_smaller");
        
        dealer_white_circle = ui_atlas.findRegion("white_square_smaller");
        player_white_circle = ui_atlas.findRegion("white_square_smaller");
        split_white_circle = ui_atlas.findRegion("white_square_smaller");
        
        dealer_red_white_circle = ui_atlas.findRegion("red_white_square_smaller");
        player_red_white_circle = ui_atlas.findRegion("red_white_square_smaller");
        split_red_white_circle = ui_atlas.findRegion("red_white_square_smaller");
        
        dealer_blue_white_circle = ui_atlas.findRegion("blue_white_square_smaller");
        player_blue_white_circle = ui_atlas.findRegion("blue_white_square_smaller");
        split_blue_white_circle = ui_atlas.findRegion("blue_white_square_smaller");
        
        dealer_black_white_circle = ui_atlas.findRegion("black_white_square_smaller");
        player_black_white_circle = ui_atlas.findRegion("black_white_square_smaller");
        split_black_white_circle = ui_atlas.findRegion("black_white_square_smaller");
        
        dealer_green_white_circle = ui_atlas.findRegion("green_white_square_smaller");
        player_green_white_circle = ui_atlas.findRegion("green_white_square_smaller");
        split_green_white_circle = ui_atlas.findRegion("green_white_square_smaller");
        
        bet_tab = ui_atlas.findRegion("bet_tab");
        bet_menu = ui_atlas.findRegion("bet_menu");
        split_back = ui_atlas.findRegion("split_back");
        
        chips_5 = ui_atlas.findRegion("chips_5");
        chips_10 = ui_atlas.findRegion("chips_10");
        chips_25 = ui_atlas.findRegion("chips_25");
        chips_50 = ui_atlas.findRegion("chips_50");
        chips_100 = ui_atlas.findRegion("chips_100");
        
        c_control_panel_back_top = ui_atlas.findRegion("c_control-panel-back");
	    c_deal = ui_atlas.findRegion("c_deal");
	    c_hit = ui_atlas.findRegion("c_hit");
	    c_split = ui_atlas.findRegion("c_split");
	    c_stand = ui_atlas.findRegion("c_stand");
	    //c_control_panel_back_bottom = ui_atlas.findRegion("c_control-panel-back");
	    c_control_panel_back_bottom = ui_atlas.findRegion("control-panel-back");
	    
	    bet_black_box = ui_atlas.findRegion("bet_black_box");
		bet_down_arrow = ui_atlas.findRegion("bet_down_arrow");
		bet_up_arrow = ui_atlas.findRegion("bet_up_arrow");
		
		c100glow = ui_atlas.findRegion("100glow");
		c10glow = ui_atlas.findRegion("10glow");
		c25glow = ui_atlas.findRegion("25glow");
		c50glow = ui_atlas.findRegion("50glow");
		c5glow = ui_atlas.findRegion("5glow");
		control_panel_back_top = ui_atlas.findRegion("control-panel-back-top");
		doubledown = ui_atlas.findRegion("doubledown");
		splash = ui_atlas.findRegion("splash");
		buytokens = ui_atlas.findRegion("buytokens");
		buyback1 = ui_atlas.findRegion("buyback1");
	    bonus_chips = ui_atlas.findRegion("bonus-chips");
	    buy_chip_blink = ui_atlas.findRegion("buy_chip_blink");
	    full_screen_shadow = ui_atlas.findRegion("full_screen_shadow");
	    homebutton = ui_atlas.findRegion("homebutton");
	    home_screen = ui_atlas.findRegion("home_screen");
	    
	    settings_num_two = ui_atlas.findRegion("settings_num_two");
	    settings_num_three = ui_atlas.findRegion("settings_num_three");
	    settings_num_four = ui_atlas.findRegion("settings_num_four");
	    settings_num_five = ui_atlas.findRegion("settings_num_five");
	    settings_num_six = ui_atlas.findRegion("settings_num_six");
	    settings_off = ui_atlas.findRegion("settings_off");
	    settings_off_copy = ui_atlas.findRegion("settings_off_copy");
	    settings_on = ui_atlas.findRegion("settings_on");
	    settings_on_copy = ui_atlas.findRegion("settings_on_copy");
	    settings_back_small = ui_atlas.findRegion("settings_back_small");
	    settings_back_big = ui_atlas.findRegion("settings_back_big");
	    
	    settings_sound_back = ui_atlas.findRegion("settings_back_small");
	    settings_deck_back = ui_atlas.findRegion("settings_back_big");
	    settings_dealer_hit_back = ui_atlas.findRegion("settings_back_small");
	    settings_card_dealt_back = ui_atlas.findRegion("settings_back_small");
	    
	    settings_sound_on = ui_atlas.findRegion("settings_on");
	    settings_sound_on_copy = ui_atlas.findRegion("settings_on_copy");
	    settings_sound_off = ui_atlas.findRegion("settings_off");
	    settings_sound_off_copy = ui_atlas.findRegion("settings_off_copy");
	    settings_deck_2_on = ui_atlas.findRegion("settings_on");
	    settings_deck_2_off = ui_atlas.findRegion("settings_off");
	    settings_deck_3_on = ui_atlas.findRegion("settings_on");
	    settings_deck_3_off = ui_atlas.findRegion("settings_off");
	    settings_deck_4_on = ui_atlas.findRegion("settings_on");
	    settings_deck_4_off = ui_atlas.findRegion("settings_off");
	    settings_deck_5_on = ui_atlas.findRegion("settings_on");
	    settings_deck_5_off = ui_atlas.findRegion("settings_off");
	    settings_deck_6_on = ui_atlas.findRegion("settings_on");
	    settings_deck_6_off = ui_atlas.findRegion("settings_off");
	    settings_dhit_on = ui_atlas.findRegion("settings_on");
	    settings_dhit_on_copy = ui_atlas.findRegion("settings_on_copy");
	    settings_dhit_off = ui_atlas.findRegion("settings_off");
	    settings_dhit_off_copy = ui_atlas.findRegion("settings_off_copy");
	    settings_dealt_on = ui_atlas.findRegion("settings_on");
	    settings_dealt_on_copy = ui_atlas.findRegion("settings_on_copy");
	    settings_dealt_off = ui_atlas.findRegion("settings_off");
	    settings_dealt_off_copy = ui_atlas.findRegion("settings_off_copy");
	    greenbackarrow_one = ui_atlas.findRegion("greenbackarrow_one");
	    peeler1 = ui_atlas.findRegion("peeler1");
	    
	    x100_small = ui_atlas.findRegion("100_small");
	    x10_smaller = ui_atlas.findRegion("10_smaller");
	    x25_smaller = ui_atlas.findRegion("25_smaller");
	    x50_smaller = ui_atlas.findRegion("50_smaller");
	    x5_smaller = ui_atlas.findRegion("5_smaller");
	    bet_menu_bigger2 = ui_atlas.findRegion("bet_menu_bigger2");
	    bet_menu_bigger2_close = ui_atlas.findRegion("bet_menu_bigger2_close");
	    split_button2 = ui_atlas.findRegion("split_button2");
	    addingfreechips = ui_atlas.findRegion("addingfreechips");
        
        ui_array = new AtlasRegion[]{table, //0
        						      black_circle, //1
        						      black_white_circle, //2
        						      green_circle,//3
        							  green_copy_back,//4
        							  green_white_circle,//5
        							  dealer_white_circle, //6
        							  player_white_circle, //7
        							  dealer_red_white_circle, //8
        							  player_red_white_circle, //9
        							  dealer_blue_white_circle, //10
        							  player_blue_white_circle, //11
        							  dealer_black_white_circle, //12
        							  player_black_white_circle, //13
        							  dealer_green_white_circle, //14
        							  player_green_white_circle, //15
        							  bet_tab, //16
        							  bet_menu, //17
        							  split_back, //18
							          split_white_circle, //19
								      split_red_white_circle, //20
								      split_blue_white_circle, //21
								      split_black_white_circle, //22
								      split_green_white_circle, //23
								      chips_5,//24
								      chips_10,//25
								      chips_25,//26
								      chips_50,//27
								      chips_100,//28
								      c_control_panel_back_top,//29
								      c_deal,//30
									  c_hit,//31
									  c_split,//32
									  c_stand,//33
									  c_control_panel_back_bottom,//34
									  bet_black_box,//35
									  bet_down_arrow,//36
									  bet_up_arrow,//37
									  c100glow,//38
									  c10glow,//39
									  c25glow,//40
									  c50glow,//41
									  c5glow,//42
									  control_panel_back_top,//43
									  doubledown,//44
									  splash,//45
									  buytokens,//46
									  buyback1,//47
						    		  bonus_chips,//48
						    		  buy_chip_blink,//49
						    		  full_screen_shadow,//50
						    		  homebutton,//51
						    		  home_screen,//52								
        							  settings_num_two,//53
        							  settings_num_three,//54
        							  settings_num_four,//55
        							  settings_num_five,//56
        							  settings_num_six,//57
        							  settings_off,//58
        							  settings_off_copy,//59
        							  settings_on,//60
        							  settings_on_copy,//61
        							  settings_back_small,//62
        							  settings_back_big,//63
        							  settings_sound_back,//64
        							  settings_deck_back,//65
        							  settings_dealer_hit_back,//66
        							  settings_card_dealt_back,//67
        							  settings_sound_on,//68
        							  settings_sound_on_copy,//69
        							  settings_sound_off,//70
        							  settings_sound_off_copy,//71
        							  settings_deck_2_on,//72
        							  settings_deck_2_off,//73
        							  settings_deck_3_on,//74
        							  settings_deck_3_off,//75
        							  settings_deck_4_on,//76
        							  settings_deck_4_off,//77
        							  settings_deck_5_on,//78
        							  settings_deck_5_off,//79
        							  settings_deck_6_on,//80
        							  settings_deck_6_off,//81
        							  settings_dhit_on,//82
        							  settings_dhit_on_copy,//83
        							  settings_dhit_off,//84
        							  settings_dhit_off_copy,//85
        							  settings_dealt_on,//86
        							  settings_dealt_on_copy,//87
        							  settings_dealt_off,//88
        							  settings_dealt_off_copy,//89
        							  greenbackarrow_one,//90
        							  peeler1,//91
        							  x100_small,//92
        				    		  x10_smaller,//93
        				    		  x25_smaller,//94
        				    		  x50_smaller,//95
        				    		  x5_smaller,//96
        				    		  bet_menu_bigger2,//97
        				    		  bet_menu_bigger2_close,//98
        				    		  split_button2,//99
        				    		  addingfreechips};//100
        
        
        /*copy*/
        blackjack = copy_atlas.findRegion("blackjack_smaller");
        bust = copy_atlas.findRegion("bust_small");
        lose = copy_atlas.findRegion("lose_small");
        win = copy_atlas.findRegion("win_small");
        push = copy_atlas.findRegion("push_small");
        twinkle_small = copy_atlas.findRegion("twinkle_small");
        twinkle_medium = copy_atlas.findRegion("twinkle_medium");
        twinkle_large = copy_atlas.findRegion("twinkle_large");
        d_blackjack = copy_atlas.findRegion("blackjack_smaller");
        d_bust = copy_atlas.findRegion("bust_small");
        d_lose = copy_atlas.findRegion("lose_small");
        d_win = copy_atlas.findRegion("win_small");
        d_push = copy_atlas.findRegion("push_small");
        s1_blackjack = copy_atlas.findRegion("blackjack_smaller");
        s1_bust = copy_atlas.findRegion("bust_small");
        s1_lose = copy_atlas.findRegion("lose_small");
        s1_win = copy_atlas.findRegion("win_small");
        s1_push = copy_atlas.findRegion("push_small");
        s2_blackjack = copy_atlas.findRegion("blackjack_smaller");
        s2_bust = copy_atlas.findRegion("bust_small");
        s2_lose = copy_atlas.findRegion("lose_small");
        s2_win = copy_atlas.findRegion("win_small");
        s2_push = copy_atlas.findRegion("push_small");
        
        copy_array = new AtlasRegion[]{blackjack,
        							   bust,
        							   lose,
        							   win,
        							   push,
        							   twinkle_small,
        							   twinkle_medium,
        							   twinkle_large,
        							   d_blackjack,
        							   d_bust,
        							   d_lose,
        							   d_win,
        							   d_push,
        							   s1_blackjack,
        							   s1_bust,
        							   s1_lose,
        							   s1_win,
        							   s1_push,
        							   s2_blackjack,
        							   s2_bust,
        							   s2_lose,
        							   s2_win,
        							   s2_push};
        
        /*dealer score*/
        dsc0 = score_atlas.findRegion("0");
        dsc1 = score_atlas.findRegion("1");
        dsc2 = score_atlas.findRegion("2");
        dsc3 = score_atlas.findRegion("3");
        dsc4 = score_atlas.findRegion("4");
        dsc5 = score_atlas.findRegion("5");
        dsc6 = score_atlas.findRegion("6");
        dsc7 = score_atlas.findRegion("7");
        dsc8 = score_atlas.findRegion("8");
        dsc9 = score_atlas.findRegion("9");
        dsc10 = score_atlas.findRegion("10");
        dsc11 = score_atlas.findRegion("11");
        dsc12 = score_atlas.findRegion("12");
        dsc13 = score_atlas.findRegion("13");
        dsc14 = score_atlas.findRegion("14");
        dsc15 = score_atlas.findRegion("15");
        dsc16 = score_atlas.findRegion("16");
        dsc17 = score_atlas.findRegion("17");
        dsc18 = score_atlas.findRegion("18");
        dsc19 = score_atlas.findRegion("19");
        dsc20 = score_atlas.findRegion("20");
        dsc21 = score_atlas.findRegion("21");
        dsc22 = score_atlas.findRegion("22");
        dsc23 = score_atlas.findRegion("23");
        dsc24 = score_atlas.findRegion("24");
        dsc25 = score_atlas.findRegion("25");
        dsc26 = score_atlas.findRegion("26");
        dsc27 = score_atlas.findRegion("27");
        dsc28 = score_atlas.findRegion("28");
        dsc29 = score_atlas.findRegion("29");
        dsc30 = score_atlas.findRegion("30");
        dsc21b = score_atlas.findRegion("21_blackjack");
        
        /*player score*/
        psc0 = score_atlas.findRegion("0");
        psc1 = score_atlas.findRegion("1");
        psc2 = score_atlas.findRegion("2");
        psc3 = score_atlas.findRegion("3");
        psc4 = score_atlas.findRegion("4");
        psc5 = score_atlas.findRegion("5");
        psc6 = score_atlas.findRegion("6");
        psc7 = score_atlas.findRegion("7");
        psc8 = score_atlas.findRegion("8");
        psc9 = score_atlas.findRegion("9");
        psc10 = score_atlas.findRegion("10");
        psc11 = score_atlas.findRegion("11");
        psc12 = score_atlas.findRegion("12");
        psc13 = score_atlas.findRegion("13");
        psc14 = score_atlas.findRegion("14");
        psc15 = score_atlas.findRegion("15");
        psc16 = score_atlas.findRegion("16");
        psc17 = score_atlas.findRegion("17");
        psc18 = score_atlas.findRegion("18");
        psc19 = score_atlas.findRegion("19");
        psc20 = score_atlas.findRegion("20");
        psc21 = score_atlas.findRegion("21");
        psc22 = score_atlas.findRegion("22");
        psc23 = score_atlas.findRegion("23");
        psc24 = score_atlas.findRegion("24");
        psc25 = score_atlas.findRegion("25");
        psc26 = score_atlas.findRegion("26");
        psc27 = score_atlas.findRegion("27");
        psc28 = score_atlas.findRegion("28");
        psc29 = score_atlas.findRegion("29");
        psc30 = score_atlas.findRegion("30");
        psc21b = score_atlas.findRegion("21_blackjack");
        
        /*split two score*/
        ssc0 = score_atlas.findRegion("0");
        ssc1 = score_atlas.findRegion("1");
        ssc2 = score_atlas.findRegion("2");
        ssc3 = score_atlas.findRegion("3");
        ssc4 = score_atlas.findRegion("4");
        ssc5 = score_atlas.findRegion("5");
        ssc6 = score_atlas.findRegion("6");
        ssc7 = score_atlas.findRegion("7");
        ssc8 = score_atlas.findRegion("8");
        ssc9 = score_atlas.findRegion("9");
        ssc10 = score_atlas.findRegion("10");
        ssc11 = score_atlas.findRegion("11");
        ssc12 = score_atlas.findRegion("12");
        ssc13 = score_atlas.findRegion("13");
        ssc14 = score_atlas.findRegion("14");
        ssc15 = score_atlas.findRegion("15");
        ssc16 = score_atlas.findRegion("16");
        ssc17 = score_atlas.findRegion("17");
        ssc18 = score_atlas.findRegion("18");
        ssc19 = score_atlas.findRegion("19");
        ssc20 = score_atlas.findRegion("20");
        ssc21 = score_atlas.findRegion("21");
        ssc22 = score_atlas.findRegion("22");
        ssc23 = score_atlas.findRegion("23");
        ssc24 = score_atlas.findRegion("24");
        ssc25 = score_atlas.findRegion("25");
        ssc26 = score_atlas.findRegion("26");
        ssc27 = score_atlas.findRegion("27");
        ssc28 = score_atlas.findRegion("28");
        ssc29 = score_atlas.findRegion("29");
        ssc30 = score_atlas.findRegion("30");
        ssc21b = score_atlas.findRegion("21_blackjack");
        
        dealer_score_array = new AtlasRegion[]{dsc0,dsc1,dsc2,dsc3,dsc4,
        		dsc5,dsc6,dsc7,dsc8,dsc9,dsc10,dsc11,dsc12,dsc13,dsc14,
        		dsc15,dsc16,dsc17,dsc18,dsc19,dsc20,dsc21,dsc22,
        		dsc23,dsc24,dsc25,dsc26,dsc27,dsc28,dsc29,dsc30,dsc21b};
        
        player_score_array = new AtlasRegion[]{psc0,psc1,psc2,psc3,psc4,
        		psc5,psc6,psc7,psc8,psc9,psc10,psc11,psc12,psc13,psc14,
                psc15,psc16,psc17,psc18,psc19,psc20,psc21,psc22,psc23,
        		psc24,psc25,psc26,psc27,psc28,psc29,psc30,psc21b};
        
        split_score_array = new AtlasRegion[]{ssc0,ssc1,ssc2,ssc3,ssc4,
        		ssc5,ssc6,ssc7,ssc8,ssc9,ssc10,ssc11,ssc12,ssc13,ssc14,
                ssc15,ssc16,ssc17,ssc18,ssc19,ssc20,ssc21,ssc22,ssc23,
        		ssc24,ssc25,ssc26,ssc27,ssc28,ssc29,ssc30,ssc21b};
        

	}
	
	
	/*
	public AtlasRegion[] getCardArray(){
		return card_array;
	}
	*/
	
	public AtlasRegion[] getUIArray(){
		return ui_array;
	}
	public AtlasRegion[] getCopyArray(){
		return copy_array;
	}
	public AtlasRegion[] getDealerScoreArray(){
		return dealer_score_array;
	}
	public AtlasRegion[] getPlayerScoreArray(){
		return player_score_array;
	}
	
	
	public void dispose(){
	    ui_atlas.dispose();
	    copy_atlas.dispose();
	    score_atlas.dispose();
	}

}
