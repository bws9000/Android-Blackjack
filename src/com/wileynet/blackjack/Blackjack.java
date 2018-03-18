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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.wileynet.blackjack.accessors.SpriteAccessor;
import com.wileynet.blackjack.db.Database;
import com.wileynet.blackjack.db.Database.Result;
import com.wileynet.blackjack.internal.InternalStorage;
import com.wileynet.blackjack.ScreenShotFactory;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Blackjack extends Game implements Result {
	
	Skin skin;
	public static boolean enable_admob = true;
	
	//android interface
	public ActionResolver actionResolver;
	public Object actionResolver2;
	//public PlayAds pa;
	public CountDownTimer timer;
	
	long bonus_time_pause;
	
	/*cards*/
	TextureAtlas card_atlas;
	
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
    
	/*small chips*/
	TextureAtlas small_chip_atlas;
    
	AtlasRegion x100_small_new;
	AtlasRegion x10_smaller_new;
	AtlasRegion x25_smaller_new;
	AtlasRegion x50_smaller_new;
	AtlasRegion x5_smaller_new;
    
	protected Player playerOne;
	protected Player playerOneSplit;
	
	
	////////////////// TEST MODE ////////////////////////////////
	/* FIRST DEAL MODE
	1 = Player BlackJack / Dealer 12
	2 = Dealer BlackJack / Player 12
	3 = Player BlackJack / Dealer BlackJack
    4 = Dealer 12 / Player 2,3 (HIT) 8
    5 = Dealer 12 / Player 2,10 (HIT) J
    6 = Dealer 12 / Player A,4 (HIT) K
    7 = Dealer 12 / Player A,4 (HIT) 6 "21"
    8 = Dealer 12 / Player K,Q (HIT) J
    9 = Dealer 12 / Player 8,10 (HIT) 10
    10 = Dealer 12 / Player A,A (HIT) 9
    11 = Dealer A,A / Player 2,3
    12 = Dealer A,3 / Player 2,3
    13 = Dealer 6,9 / Player 2,3
    14 = Dealer 2,3 / Player 2,3 PUSH
    15 = Dealer 10,7 / Player 10,7 PUSH
    16 = Dealer 10,6 / Player 10,6 PUSH -DEALER HITS
    17 = Dealer 5,9 HIT 6 / Player 3,J HIT 7
    18 = Dealer A,6 / Player 3,J
    19 = Player A,A / Dealer 2,3
    20 = Dealer 10,7 / Player A,4 (HIT) 6 "21"
    21 = Dealer 6,6 / Player 3s,8s
	*/
	protected static boolean test_mode = false;
	protected static boolean split_test_mode = false;
	protected static boolean test_deck = false;
	private int test_first_deal_mode = 3;
	///////////////// TEST MODE /////////////////////////////////
	
	protected SpriteBatch batch;
	
    protected BitmapFont font32blue;
    protected BitmapFont arialblue1;
    protected BitmapFont arialblack2;
    protected BitmapFont bonusTimer;
    protected BitmapFont settingsFont;
    protected BitmapFont betFont;
    
    private SpriteAtlas sa;
    
    //private AtlasRegion[] cards; 
    protected Card face_card; //actually the front, not "face";change
    protected Card player_card;
    protected Card player_card0;
    protected Card player_card2;
    protected Card dealer_card1;
    protected Card dealer_card3;
    
    private AtlasRegion[] ui;
    private AtlasRegion[] copy;
    private AtlasRegion[] dealer_scores;
    private AtlasRegion[] player_scores;
    
    
    //graphics
    private Sprite hit_button;
    private Sprite stand_button;
    private Sprite deal_button;
    //private Sprite bet_button;
    private Sprite split_button;
    
    protected int deal_button_x = 320;
    protected int stand_button_x = 320;
    protected int hit_button_x = stand_button_x - 150;
    protected int split_button_x = hit_button_x - 150;
    protected int doubledown_x = 268;
    protected int doubledown_y = 250;
    protected boolean doubledown = false;
	protected boolean doubledown_active_one = false;
	protected boolean doubledown_active_two = false;
	protected boolean dd_clicked_one = false;
	protected boolean dd_clicked_two = false;
    
    private Sprite table_green;
    protected Sprite dealer_white_circle;
    protected Sprite dealer_blackjack_circle;
    protected Sprite player_white_circle;
    protected Sprite player_blackjack_circle;
    protected Sprite player_red_white_circle;
    protected Sprite dealer_red_white_circle;
    protected Sprite player_blue_white_circle;
    protected Sprite dealer_blue_white_circle;
    protected Sprite player_green_white_circle;
    protected Sprite dealer_green_white_circle;
	protected Sprite[] dealer_score_sprites;
	protected Sprite[] player_score_sprites;
	protected Sprite[] split_score_sprites;
	protected Sprite split_back;
	protected Sprite split_white_circle;
	protected Sprite split_blackjack_circle;
	protected Sprite split_red_white_circle;
	protected Sprite split_blue_white_circle;
	protected Sprite split_green_white_circle;
	
	//settings
	protected Sprite settings_num_two;
	protected Sprite settings_num_three;
	protected Sprite settings_num_four;
	protected Sprite settings_num_five;
	protected Sprite settings_num_six;
	protected Sprite settings_off;
	protected Sprite settings_off_copy;
	protected Sprite settings_on;
	protected Sprite settings_on_copy;
	protected Sprite settings_back_small;
	protected Sprite settings_back_big;
	
	protected Sprite settings_sound_back;
	protected Sprite settings_deck_back;
	protected Sprite settings_dealer_hit_back;
	protected Sprite settings_card_dealt_back;
	
	protected Sprite settings_sound_on;
	protected Sprite settings_sound_on_copy;
	protected Sprite settings_sound_off;
	protected Sprite settings_sound_off_copy;
	protected Sprite settings_deck_2_on;
	protected Sprite settings_deck_2_off;
	protected Sprite settings_deck_3_on;
	protected Sprite settings_deck_3_off;
	protected Sprite settings_deck_4_on;
	protected Sprite settings_deck_4_off;
	protected Sprite settings_deck_5_on;
	protected Sprite settings_deck_5_off;
	protected Sprite settings_deck_6_on;
	protected Sprite settings_deck_6_off;
	protected Sprite settings_dhit_on;
	protected Sprite settings_dhit_on_copy;
	protected Sprite settings_dhit_off;
	protected Sprite settings_dhit_off_copy;
	protected Sprite settings_dealt_on;
	protected Sprite settings_dealt_on_copy;
	protected Sprite settings_dealt_off;
	protected Sprite settings_dealt_off_copy;
	protected Sprite greenbackarrow_one;
	protected Sprite peeler1;
	
	protected Sprite x100_small; //NOT USED
	protected Sprite x10_smaller; //NOT USED
	protected Sprite x25_smaller; //NOT USED
	protected Sprite x50_smaller; //NOT USED
	protected Sprite x5_smaller; //NOT USED
	
	protected Sprite bet_menu_bigger2;
	protected Sprite bet_menu_bigger2_close;
	protected Sprite split_button2;
	
	//chips bet in hand
	//protected ArrayList<Chip>chipsInPlay = new ArrayList<Chip>();
	protected ChipArray chipArray;
	protected static boolean chipsAnimationFinished = false;
	//protected boolean start_chips_animation = false;
	
	//control graphics
	protected Sprite c_control_panel_back_top;
	protected Sprite c_control_panel_back_bottom;
	protected Sprite homebutton;
	protected Sprite home_screen;
	
    //graphics positions
    protected int player_card_dealt_y = 150;
    protected int dealer_card_dealt_y = 350;
    protected int dscore_y = dealer_card_dealt_y + 112;
    protected int pscore_y = player_card_dealt_y + 112;
    
    protected int y_offset = 94;//score,scorebackground etc
	protected int x_offset = 46;//score,scorebackground etc
    
    //graphics active
    protected boolean dealer_score_active = false;
    protected boolean player_score_active = false;
    
    //sprites
    //protected Sprite[] card_sprites;
    protected Sprite[] score_sprites;
    protected Sprite[] copy_sprites;
    protected Sprite[] ui_sprites;
    
    //sprite registers
    protected int[] dealer_score_sprites_register;
    protected int[] player_score_sprites_register;
    protected int[] split_score_sprites_register;
    protected int[] copy_sprites_register;
    protected int[] ui_sprites_register;
    
    protected Sound dealCardSound;
    protected Sound blackjackSound;
    protected Sound cardFlipSound;
    protected Sound shuffleSound;
    protected Sound winSound;
    protected Sound bustSound;
    protected Sound score1Sound;
    protected Sound winding1;
    protected Sound winding2;
    protected Sound cling_1;
    protected Sound tick_sound;
    protected Sound poker_chips111;
    protected Sound pokersoundroom;
    protected Sound dingding;
    
    protected static boolean play_win_sound = false;
    protected static boolean play_bust_sound = false;
    protected static boolean play_sounds = true;
    
    private Random r;
    
    
    //game play
    protected static boolean dealer_flipped = false;
    protected static boolean split_dealer_flip = false;
    protected static boolean dealer_soft_hand = false;
    protected static boolean player_soft_hand = false;
    protected static boolean dealer_done = false;
    protected static boolean is_push = false;
    protected static boolean first_deal_before_hit = false;
    
    protected static ArrayList<Integer> current_deck = new ArrayList<Integer>();
    protected static ArrayList<Card> cards_in_play_before_dealer = new ArrayList<Card>();
    protected static ArrayList<Card> player_cards = new ArrayList<Card>();
    protected static ArrayList<Card> dealer_cards = new ArrayList<Card>();
    
    protected static ArrayList<Card> split_one = new ArrayList<Card>();
    protected static ArrayList<Card> split_two = new ArrayList<Card>();
    protected static int what_split_hand = 1;
    protected static int split_dealer_score = 0;
    protected static int split_hand_one_score = 0;
    protected static int split_hand_two_score = 0;
    protected static boolean hand_one_push = false;
    protected static boolean hand_two_push = false;
    
    protected static int current_deck_array_index = 0;
    protected static int current_player_playing = 1; //0=dealer, 1=player
    //protected static int last_player_delt = 0;
    protected static int dealer_score = 0;
    protected static int player_score = 0;
    protected boolean play_mode = false; //0=normal, 1=split
    
    //BLACKJACK
    protected static boolean dealer_blackjack = false;
    protected static boolean player_blackjack = false;
    protected static boolean split_dealer_21 = false;
    protected static boolean split_hand_one_21 = false;
    protected static boolean split_hand_two_21 = false;
    
    //BUST
    protected static boolean dealer_bust = false;
    protected static boolean player_bust = false;
    protected static boolean split_dealer_bust = false;
    protected static boolean split_hand_one_bust = false;
    protected static boolean split_hand_two_bust = false;
    
    //LOSE
    protected static boolean dealer_lose = false;
    protected static boolean player_lose = false;
    protected static boolean split_dealer_lose = false;
    protected static boolean split_hand_one_lose = false;
    protected static boolean split_hand_two_lose = false;
    
    //WIN
    protected static boolean dealer_win = false;
    protected static boolean player_win = false;
    protected static boolean split_dealer_win = false;
    protected static boolean split_hand_one_win = false;
    protected static boolean split_hand_two_win = false;
    
    //PLAYER SPLIT
    protected static boolean split_detected = false;
    
    /* screen coordinates */
    protected static int screen_top = 793;
    
    /* card particulars */
    protected static int card_width = 128;
    protected static int card_height = 152;
    protected static int card_left_offset = 2;
    
    //bust/win/lose graphic
    protected int dealer_score_graphic_x = 47;
    protected int dealer_score_graphic_y = dealer_card_dealt_y + 64;
    protected int player_score_graphic_x = 47;
	protected int player_score_graphic_y = player_card_dealt_y + 64;
	
	//blackjack graphic
	protected int dealer_blackjack_graphic_x = 131;
	protected int dealer_blackjack_graphic_y = dealer_card_dealt_y + 61;
	protected int player_blackjack_graphic_x = 131;
	protected int player_blackjack_graphic_y = player_card_dealt_y + 61;
	
	//menu
	protected boolean bet_menu_state = false;//closed
	protected boolean bet_closed_clicked = true;//not clicked
	
	protected static int bet_normal_or_split;
    
    /* settings */
    protected static boolean inline_card_deal = true;
    protected static boolean dealer_hits_soft_17 = true;
    protected static int how_many_decks = 6;//6 decks default
    protected static boolean hmd_2 = false;
    protected static boolean hmd_3 = false;
    protected static boolean hmd_4 = false;
    protected static boolean hmd_5 = false;
    protected static boolean hmd_6 = true;//6 decks default
    
    //other
    //protected static int shuffle_at = how_many_decks * 52;
    //protected static int check_shuffle = (int)shuffle_at / 3; //%75;
    
    //bet 
    protected static int player_one_bet = 0;
    protected static int player_one_total = 600;
    protected int total_numbers_x = 42;
    protected int total_numbers_y = 794;
    protected static int player_one_last_bet_made;
    protected static int player_one_total_before_change;
    
    //buychips touch position
    protected static int buychips_x1 =0;
    protected static int buychips_x2 =195;
    protected static int buychips_y1 = screen_top - 50;
    protected static int buychips_y2 = screen_top;
    
    //score fake SplitScreen / SplitScoreScreen
    protected int score_fake;
    protected int split_animate_fake_score = 0;
    protected boolean split_animate_score_finished = false;
    protected boolean update_splitscreen_score_once = false;
    protected static boolean home_screen_viewed = false;
    protected int dd_split_one_last_bet = 0;
    protected int dd_split_two_last_bet = 0;
    
    //countdown after lost chips
    protected static float countdowndelay = 0f; // seconds
    
    //score sound
    protected boolean winding_played = false;
    
    //splash screen
    protected static boolean main_screen_viewed = false;
    
    //desktop or android
    protected static int application_type = 0; //0=Desktop,1=Android
    
    //broke flag
    protected static boolean score_is_zero = false;
    
    //buybutton
    protected static boolean close_buybutton = false;
    
    //whatad
    protected static int whatad=0;
    
    Preferences prefs;
    
    //STATIC screen state for android lifecycle
    /*
     * MainMenuScreen = 0
     * GameScreen = 1
     * DealScreen = 2
     * Hit = 3
     * DealerFlip = 4
     * ScoreScreen = 5
     * SplitScreen = 6
     * SplitScoreScreen = 7
     * BetMenu = 8
     * BillingScreen = 9
     */
    protected static int screen_state;
    
    
    //InternalStorage
    protected InternalStorage internalStorage;
    
    //DB
    protected Database database;
    
    //admob(interstitial)
    protected static int show_ads_per_hands = 20;
    protected static int hand_count = 0;
    
    //check for wifi
    protected boolean isWifi;
    
    
    /*
	public Blackjack(Database db, InternalStorage is, int app_type,
			ActionResolver ar, Admob am, AppnextAds apn){
    */
    
	public Blackjack(Database db, 
					 InternalStorage is, 
					 int app_type,
					 ActionResolver ar, 
					 boolean iw, Object ar2){

    	database = db;
    	internalStorage = is;
    	application_type = app_type;
    	actionResolver = ar;
    	actionResolver2 = ar2;
    	isWifi = iw;
    	//admob = am;
    	//appnextads = apn;
    	
    	
	    if(this.getDatabaseScore() != "0"){
	    	if(app_type == 0){
	    		Blackjack.player_one_total = Integer.parseInt(this.getDatabaseScore());
	    	}else{
	    		Blackjack.player_one_total = Integer.parseInt(this.readScoreInternal());
	    	}
	    }else{
	    	this.updateDatabaseScore();
	    	if(app_type == 1){
	    		this.writeScoreInternal(player_one_total);
	    	}
	    }
	    
	    
	    
    }

	//away we go
	public void create() {
		
			//Gdx.input.setCatchBackKey(true);
		
    		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    		
    		playerOne = new Player("Player One",0,this);
    		playerOneSplit = new Player("Player One Split One",1,this);
    		chipArray = new ChipArray(this);
    		
    		//sounds
    		dealCardSound = Gdx.audio.newSound(Gdx.files.internal("data/cardflip4.wav"));
    		//dealCardSound = Gdx.audio.newSound(Gdx.files.internal("data/deal-single3.wav"));
    		//dealCardSound = Gdx.audio.newSound(Gdx.files.internal("data/cardplaced.wav"));
    		//dealCardSound = Gdx.audio.newSound(Gdx.files.internal("data/slide.wav"));
    		blackjackSound = Gdx.audio.newSound(Gdx.files.internal("data/chimeup.wav"));
    		cardFlipSound = Gdx.audio.newSound(Gdx.files.internal("data/cardflippo.wav"));//MAKE BUST
    		bustSound = Gdx.audio.newSound(Gdx.files.internal("data/cardflippo.wav"));
    		//cardFlipSound = Gdx.audio.newSound(Gdx.files.internal("data/cardflip3.wav"));
    		shuffleSound = Gdx.audio.newSound(Gdx.files.internal("data/shuffle1.wav"));
    		//winSound = Gdx.audio.newSound(Gdx.files.internal("data/success.wav"));
    		winSound = Gdx.audio.newSound(Gdx.files.internal("data/success_low.wav"));
    		//cardFlipSound = Gdx.audio.newSound(Gdx.files.internal("data/cardflip2.wav"));
    		score1Sound = Gdx.audio.newSound(Gdx.files.internal("data/score2.wav"));
    		winding1 = Gdx.audio.newSound(Gdx.files.internal("data/winding1.wav"));
    		winding2 = Gdx.audio.newSound(Gdx.files.internal("data/winding2.wav"));
    		cling_1 = Gdx.audio.newSound(Gdx.files.internal("data/cling_1.wav"));
    		tick_sound = Gdx.audio.newSound(Gdx.files.internal("data/tick_sound.wav"));
    		poker_chips111 = Gdx.audio.newSound(Gdx.files.internal("data/poker_chips111.wav"));
    		pokersoundroom = Gdx.audio.newSound(Gdx.files.internal("data/pokerroomsound.wav"));
    		dingding = Gdx.audio.newSound(Gdx.files.internal("data/ding.wav"));
    		
    		
    		
    		//skin = new Skin(Gdx.files.internal("data/uiskin.json"));
    		
    		//initialize graphics
        	uiInit();
        	
            batch = new SpriteBatch();
            
            //fonts
            //home/wiley/hiero.jar
            font32blue = new BitmapFont(Gdx.files.internal("fonts/goboldgreen32.fnt"),
            		Gdx.files.internal("fonts/goboldgreen32.png"),false);
            arialblack2 = new BitmapFont(Gdx.files.internal("fonts/goboldgreen38.fnt"),
            		Gdx.files.internal("fonts/goboldgreen38.png"),false);
            bonusTimer = new BitmapFont(Gdx.files.internal("fonts/bonustime18white.fnt"),
            		Gdx.files.internal("fonts/bonustime18white.png"),false);
            settingsFont = new BitmapFont(Gdx.files.internal("fonts/dejavusansmono28green.fnt"),
            		Gdx.files.internal("fonts/dejavusansmono28green.png"),false);
            betFont = new BitmapFont(Gdx.files.internal("fonts/bet/bet.fnt"),
            		Gdx.files.internal("fonts/bet/bet.png"),false);
            
            
            
            prefs = Gdx.app.getPreferences("blackjack21time");
            
            //this.setScreen(new MainMenuScreen(this));
            
            if(enable_admob){
            	//ADMOB BAN
            	//((ActionResolver) this.actionResolver2).showOrLoadInterstital();
            }
            this.setScreen(new DealScreen(this));
            
    }
    
	
	public void pause(){
		
		updateDatabaseScore();
		if(application_type == 1){
			this.writeScoreInternal(player_one_total);
		}
		
		//
	}
	
	public void resize (int width, int height) {}
	
	public void resume () {
		//
	}
	
	public void hide() {}
	
	
	public void resetPositionOnNoFocus(){
		
		
		//reset card positions
		for(int i=0;i<Blackjack.cards_in_play_before_dealer.size();i++){
			Card card = Blackjack.cards_in_play_before_dealer.get(i);
			   Sprite card_sprite = card.getSprite();
			   if(card_sprite.getX() < screen_top){
			      card_sprite.setPosition(card_sprite.getX(), screen_top + 110);
			   }
		}
		//reset dealer card positions
		for(int i=0;i<Blackjack.dealer_cards.size();i++){
			Card card2 = Blackjack.dealer_cards.get(i);
			Sprite dcard_sprite = card2.getSprite();
			//dcard_sprite.setScale(card_width, card_height); //TWEENMANAGER CONFLICT
			if(dcard_sprite.getX() < screen_top){
					   dcard_sprite.setPosition(dcard_sprite.getX(), screen_top + 110);
			}
	    }
		
		
	}
    
    //shuffle
    public void shuffle(){
    	
    	if(!test_deck){
	    	if(current_deck.isEmpty()){
	    		for(int j=0;j<how_many_decks;j++){
		    		for(int i=0;i<52;i++){
				    	current_deck.add(i);
				    }
	    		}
	    		
	    	}
    	}else{
    		this.test_deck();
    	}
    	
    	int deck_size = current_deck.size();
    	System.out.println("current_deck size: " + deck_size);
    	//if not test mode shuffle
    	if(!test_mode && !test_deck){
    		long seed = System.nanoTime();
    		Collections.shuffle(current_deck,new Random(seed));
    		System.out.println("%%% SHUFFLE " + how_many_decks + " Decks %%%");
    	}
    	
    }
    
    //deal cards
    public void deal(){
    	
    	if(!test_mode  && !play_mode){
	    	// Card(<card 0-51>,who player=1,dealer=0)
	    	if(cards_in_play_before_dealer.isEmpty()){
	    		
	    		if(current_deck_array_index != 0){
		    		//i.e. if first hand last card is 6s you don't
		    		//want first card in next hand 6s
	    			current_deck_array_index++;
	    		}
	    		
	    		//Card player_card0 = new Card(current_deck.get(0),1);
	    		player_card0 = new Card(current_deck.get(current_deck_array_index),1,this);
	    		cards_in_play_before_dealer.add( player_card0 );
	    		player_cards.add( player_card0 );
	    		current_deck_array_index++;
	    		
	    		//Card dealer_card1 = new Card(current_deck.get(1),0);
	    		dealer_card1 = new Card(current_deck.get(current_deck_array_index),0,this);
	    		cards_in_play_before_dealer.add( dealer_card1 );
	    		dealer_cards.add( dealer_card1 );
	    		current_deck_array_index++;
	    		
	    		//Card player_card2 = new Card(current_deck.get(2),1);
	    		player_card2 = new Card(current_deck.get(current_deck_array_index),1,this);
	    		cards_in_play_before_dealer.add( player_card2 );
	    		player_cards.add( player_card2 );
	    		current_deck_array_index++;
	    		
	    		//Card dealer_card3 = new Card(current_deck.get(3),0);//face card up
	    		dealer_card3 = new Card(current_deck.get(current_deck_array_index),0,this);//face card up
	    		cards_in_play_before_dealer.add( dealer_card3 );
	    		dealer_cards.add( dealer_card3 );
	    		
	    		//current_deck_array_index = 3;

	    	}else{
	    		
	    		//hit
	    		
	    		//int cs = cards_in_play_before_dealer.size();
	    		switch(current_player_playing){
				
	    		case 0:
	    			/////// NOT USED
	    			//Card dealer_card = new Card(current_deck.get(cs),0);
	    			//cards_in_play_before_dealer.add( dealer_card );
	    			//dealer_cards.add( dealer_card);
	    			//last_player_delt = 0;
	    			break;
	    		case 1:
	    			
	    			current_deck_array_index++;
	    			//Card player_card = new Card(current_deck.get(cs),1);
	    			player_card = new Card(current_deck.get(current_deck_array_index),1,this);
	    			cards_in_play_before_dealer.add( player_card );
	    			player_cards.add( player_card);
	    			break;
	    			
	    		}
	    		
	    		
	    	}
    	}else{
    		
    		if(play_mode){
    			//SPLIT SCREEN
    			//hit
    			
    			if(what_split_hand == 1){
	    			//split one
	    			if(split_one.isEmpty()){
	    				//if empty add current
	    				split_one.add(Blackjack.cards_in_play_before_dealer.get(0));
	    				//add hit
	    				current_deck_array_index++;
		    			player_card = new Card(current_deck.get(current_deck_array_index),1,this);
		    			split_one.add( player_card );
	    			}else{
	    				//add hit
	    				current_deck_array_index++;
		    			player_card = new Card(current_deck.get(current_deck_array_index),1,this);
		    			split_one.add( player_card );
	    			}
    			}
    			
    			
    			
    			else if(what_split_hand == 2){
	    				//add hit
	    				current_deck_array_index++;
		    			player_card = new Card(current_deck.get(current_deck_array_index),1,this);
		    			split_two.add( player_card );
	    			
    			}
    			
    			
    			
    			
    		}else{
    			testDeal();
    		}
    	}
    	
    }
    
    
    /**
     * called once before deal
     */
    
    public void addFirstSplitHit(){
    	
    	play_mode = true;

    	//second card delt is first card of first split hand
		split_one.add(Blackjack.cards_in_play_before_dealer.get(2));
			
		//hit new card to first hand to start
		current_deck_array_index++;
		player_card = new Card(current_deck.get(current_deck_array_index),1,this);
		split_one.add( player_card );
			
		//first card delt is bottom card of split screen new hand
		split_two.add(Blackjack.cards_in_play_before_dealer.get(0));
    	
    }
    
    
    
    //TEST deal cards
    public void testDeal(){
    	
    	
    	//IF HOW_MANY_DECKS == 1
    	////////////////////////
    	
    	
	    	// Card(<card 0-51>,who player=1,dealer=0)
	    	if(cards_in_play_before_dealer.isEmpty()){
	    		
	    		int fdc1=0;
	    		int fdc2=0;
	    		int fdc3=0;
	    		int fdc4=0;
	    		
	    		switch(test_first_deal_mode){
	    		case 1:
	    			fdc1 = current_deck.get(51);//player 10s
		    		fdc2 = current_deck.get(32);//dealer 6c
		    		fdc3 = current_deck.get(1);//player Ad
		    		fdc4 = current_deck.get(33);//dealer 6d
		    		break;
		    		
	    		case 2:
	    			fdc1 = current_deck.get(32);//player 6c
	    			fdc2 = current_deck.get(51);//dealer 10s
	    			fdc3 = current_deck.get(33);//player 6d
		    		fdc4 = current_deck.get(1);//dealer Ad
		    		break;
		    		
	    		case 3:
	    			fdc1 = current_deck.get(0);//player Ac
	    			fdc2 = current_deck.get(51);//dealer 10s
	    			fdc3 = current_deck.get(50);//player 10h
		    		fdc4 = current_deck.get(1);//dealer Ad
		    		break;
		    		
	    		case 4:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(20);//player 3c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
		    		
	    		case 5:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(48);//player 10c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 6:
	    			fdc1 = current_deck.get(1);//player A
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(24);//player 4c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 7:
	    			fdc1 = current_deck.get(1);//player A
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(24);//player 4c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 8:
	    			fdc1 = current_deck.get(8);//player 2c
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(4);//player 4c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 9:
	    			fdc1 = current_deck.get(40);//player 8c
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(48);//player 10c
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 10:
	    			fdc1 = current_deck.get(1);//player Ad
	    			fdc2 = current_deck.get(33);//dealer 6d
	    			fdc3 = current_deck.get(2);//player Ac
	    			fdc4 = current_deck.get(32);//dealer 6c
		    		break;
	    		case 11:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(1);//dealer A
	    			fdc3 = current_deck.get(20);//player 3c
	    			fdc4 = current_deck.get(2);//dealer A
		    		break;
	    		case 12:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(1);//dealer Ad
	    			fdc3 = current_deck.get(20);//player 3c
	    			fdc4 = current_deck.get(21);//dealer 3d
		    		break;
	    		case 13:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(32);//dealer 6
	    			fdc3 = current_deck.get(20);//player 3c
	    			fdc4 = current_deck.get(44);//dealer 9
		    		break;
	    		case 14:
	    			fdc1 = current_deck.get(16);//player 2c
	    			fdc2 = current_deck.get(17);//dealer 2d
	    			fdc3 = current_deck.get(20);//player 3c
	    			fdc4 = current_deck.get(21);//dealer 3d
		    		break;
	    		case 15:
	    			fdc1 = current_deck.get(48);//player 10
	    			fdc2 = current_deck.get(49);//dealer 10
	    			fdc3 = current_deck.get(37);//player 7
	    			fdc4 = current_deck.get(38);//dealer 7
		    		break;
	    		case 16:
	    			fdc1 = current_deck.get(48);//player 10
	    			fdc2 = current_deck.get(49);//dealer 10
	    			fdc3 = current_deck.get(33);//player 6
	    			fdc4 = current_deck.get(34);//dealer 6
		    		break;
	    		case 17:
	    			fdc1 = current_deck.get(20);//player 3
	    			fdc2 = current_deck.get(29);//dealer 10
	    			fdc3 = current_deck.get(12);//player J
	    			fdc4 = current_deck.get(44);//dealer 6
		    		break;
	    		case 18:
	    			fdc1 = current_deck.get(12);//player J
	    			fdc2 = current_deck.get(1);//dealer A
	    			fdc3 = current_deck.get(49);//player 10
	    			fdc4 = current_deck.get(32);//dealer 6
		    		break;
	    		case 19:
	    			fdc1 = current_deck.get(1);//player J
	    			fdc2 = current_deck.get(16);//dealer A
	    			fdc3 = current_deck.get(2);//player A
	    			fdc4 = current_deck.get(20);//dealer 6
		    		break;
	    		case 20:
	    			fdc1 = current_deck.get(1);//player A
	    			fdc2 = current_deck.get(49);//dealer 6d
	    			fdc3 = current_deck.get(24);//player 4c
	    			fdc4 = current_deck.get(38);//dealer 6c
		    		break;
	    		case 21:
	    			fdc1 = current_deck.get(23);//player 3s
	    			fdc2 = current_deck.get(49);//dealer 6d
	    			fdc3 = current_deck.get(43);//player 8s
	    			fdc4 = current_deck.get(38);//dealer 6c
		    		break;
	    		case 22:
	    			fdc1 = current_deck.get(31);//player 5s
	    			fdc2 = current_deck.get(49);//dealer 6d
	    			fdc3 = current_deck.get(43);//player 8s
	    			fdc4 = current_deck.get(38);//dealer 6c
		    		break;
		    		
		    	default:
		    		//
		    		break;
	    		}
	    		
	    		
	    		Card player_card0 = new Card(fdc1,1,this);
	    		cards_in_play_before_dealer.add( player_card0 );
	    		player_cards.add( player_card0 );
	    		
	    		Card dealer_card1 = new Card(fdc2,0,this);
	    		cards_in_play_before_dealer.add( dealer_card1 );
	    		dealer_cards.add( dealer_card1 );
	    		
	    		Card player_card2 = new Card(fdc3,1,this);
	    		cards_in_play_before_dealer.add( player_card2 );
	    		player_cards.add( player_card2 );
	    		
	    		Card dealer_card3 = new Card(fdc4,0,this);//face card up
	    		cards_in_play_before_dealer.add( dealer_card3 );
	    		dealer_cards.add( dealer_card3 );
	    		
	    		current_deck_array_index = 3;

	    	}else{
	    		
	    		//hit
	    		switch(current_player_playing){
				
	    		case 0:
	    			// NOT USED
	    			//Card dealer_card = new Card(current_deck.get(cs),0);
	    			//cards_in_play_before_dealer.add( dealer_card );
	    			//dealer_cards.add( dealer_card);
	    			//last_player_delt = 0;
	    			//break;
	    		case 1:
	    			
	    			switch(test_first_deal_mode){
	    			
	    			case 4:
	    				Card player_card = new Card(current_deck.get(40),1,this);//8c
	    				cards_in_play_before_dealer.add( player_card );
	    				player_cards.add( player_card);
	    				break;
	    			case 5:
	    				Card player_card2 = new Card(current_deck.get(12),1,this);//Jc
	    				cards_in_play_before_dealer.add( player_card2 );
	    				player_cards.add( player_card2);
	    				break;
	    			case 6:
	    				Card player_card3 = new Card(current_deck.get(4),1,this);//Kc
	    				cards_in_play_before_dealer.add( player_card3 );
	    				player_cards.add( player_card3);
	    				break;
	    			case 7:
	    				Card player_card4 = new Card(current_deck.get(32),1,this);//6c
	    				cards_in_play_before_dealer.add( player_card4 );
	    				player_cards.add( player_card4);
	    				break;
	    			case 8:
	    				Card player_card5 = new Card(current_deck.get(12),1,this);//Jc
	    				cards_in_play_before_dealer.add( player_card5 );
	    				player_cards.add( player_card5);
	    				break;
	    			case 9:
	    				Card player_card6 = new Card(current_deck.get(49),1,this);//10d
	    				cards_in_play_before_dealer.add( player_card6 );
	    				player_cards.add( player_card6);
	    				break;
	    			case 10:
	    				Card player_card7 = new Card(current_deck.get(44),1,this);//9c
	    				cards_in_play_before_dealer.add( player_card7 );
	    				player_cards.add( player_card7);
	    				break;
	    			case 17:
	    				Card player_card8 = new Card(current_deck.get(37),1,this);//7
	    				cards_in_play_before_dealer.add( player_card8 );
	    				player_cards.add( player_card8);
	    				break;
	    			case 20:
	    				Card player_card9 = new Card(current_deck.get(32),1,this);//6
	    				cards_in_play_before_dealer.add( player_card9 );
	    				player_cards.add( player_card9);
	    				break;
	    			}
	    			
	    		}
	    		current_deck_array_index += 1;
	    		
	    	}
    	
    }
    
    public void updateGameState(){
    	
    	System.out.println("");
    	System.out.println("**** UPDATE GAME STATE ****");
    	System.out.println("");
    	
    	if(Blackjack.dealer_done){
    		//gameLogic.updateAfterDealer();
    		playerOne.gamelogic().updateAfterDealer();
    	}else{
    		//gameLogic.update();
    		playerOne.gamelogic().update();
    	}
    	
    	//test
    	if(!Blackjack.current_deck.isEmpty()){
    		Card card = new Card(Blackjack.current_deck.get(Blackjack.current_deck_array_index),2,this);
    		System.out.println("Current Index: " + Blackjack.current_deck_array_index +
    				" " + card.getName());
    	}
    	
    	//check everything except push
    	
    	//dealer
    	switch(playerOne.gamelogic().getDealerState()){
    	case 1: //BLACKJACK
    		dealer_blackjack = true;
    		break;
    	case 2: //BUST
    		dealer_bust = true;
    		break;
    	case 3: //WIN
    		dealer_win = true;
    		break;
    	case 4: //LOSE
    		dealer_lose = true;
    		break;
    	}
    	
    	//player
    	switch(playerOne.gamelogic().getPlayerState()){
    	case 1: //BLACKJACK
    		player_blackjack = true;
    		break;
    	case 2: //BUST
    		player_bust = true;
    		break;
    	case 3: //WIN
    		player_win = true;
    		break;
    	case 4: //LOSE
    		player_lose = true;
    		break;
    	}
    	
    }
    
    
    
    //split gamestate
    public void updateGameStateSplit(){
    	
    	System.out.println("");
    	System.out.println("#### UPDATE SPLIT GAME STATE ####");
    	System.out.println("");
    	
    	playerOneSplit.gamelogic().updateSplit();
    	
    	
    	
    	//dealer
    	switch(playerOneSplit.gamelogic().getSplitDealerState()){
    	case 1: //21
    		split_dealer_21 = true;
    		break;
    	case 2: //BUST
    		split_dealer_bust = true;
    		break;
    	case 3: //WIN
    		split_dealer_win = true;
    		break;
    	case 4: //LOSE
    		split_dealer_lose = true;
    		break;
    	}
    	
    	

    	if(Blackjack.what_split_hand == 1){
    		//player
        	switch(playerOneSplit.gamelogic().getSplitPlayerState()){
        	case 1: //21
        		split_hand_one_21 = true;
        		break;
        	case 2: //BUST
        		split_hand_one_bust = true;
        		break;
        	case 3: //WIN
        		split_hand_one_win = true;
        		break;
        	case 4: //LOSE
        		split_hand_one_lose = true;
        		break;
        	}
    	}else if(Blackjack.what_split_hand == 2){
    		//player
        	switch(playerOneSplit.gamelogic().getSplitPlayerState()){
        	case 1: //21
        		split_hand_two_21 = true;
        		break;
        	case 2: //BUST
        		split_hand_two_bust = true;
        		break;
        	case 3: //WIN
        		split_hand_two_win = true;
        		break;
        	case 4: //LOSE
        		split_hand_two_lose = true;
        		break;
        	}
    	}
    	
    	
    	if(dealer_done){
	    	System.out.println("");
	    	System.out.println("#### ##################### ####");
	    	System.out.println("");
	    	
	    	System.out.println("Split Dealer Score: " + split_dealer_score);
	    	System.out.println("Split Player One Score: " + split_hand_one_score);
	    	System.out.println("Split Player Two Score: " + split_hand_two_score);
	    	if(split_hand_one_bust){
	    		System.out.println("** Split Hand One Bust **");
	    	}
	    	if(split_hand_one_lose){
	    		System.out.println("** Split Hand One Lose **");
	    	}
	    	if(split_hand_two_bust){
	    		System.out.println("** Split Hand One Bust **");
	    	}
	    	if(split_hand_two_lose){
	    		System.out.println("** Split Hand One Lose **");
	    	}
	    	
	    	System.out.println("");
	    	System.out.println("#### ##################### ####");
	    	System.out.println("");
    	}
    	
    	
    }
    
    

    public void render() {
    		
            super.render(); //important!
            //ScreenShotFactory.saveScreenshot(true);
    }
    
    public void dispose() {
    			
    	
    			//called on Desktop when closed
    			updateDatabaseScore();
    			if(application_type == 1){
    				this.writeScoreInternal(player_one_total);
    			}
    			main_screen_viewed = true;
    			//resetPositionOnNoFocus(); // BAD IDEA FOR THIS APP
    			
    			if(screen_state == 9){//BILLING
    				if(application_type == 0){
    					bonus_time_pause = System.currentTimeMillis();
    				}else{
    					bonus_time_pause = actionResolver.getAndroidTime();
    				}
    			}
    			
    			
    			
    	//sa.dispose();
    	//card_atlas.dispose();
		//blackjackSound.dispose();
		//cardFlipSound.dispose();
		//shuffleSound.dispose();
    	//dealCardSound.dispose();
    	//winSound.dispose();
        //font32blue.dispose();
        //database.close();
        //batch.dispose();
    }
    
    public void reinitialize(){
		
    	//shuffle
    	
    	//other
        int shuffle_at = how_many_decks * 52;
        int check_shuffle = (int)shuffle_at / 3; //%75;
        
    	int check = shuffle_at - check_shuffle;
    	
    	System.out.println("CURRENT_ARRAY_INDEX " + Blackjack.current_deck_array_index );
    	System.out.println("CHECK " + check);
    	
		if(Blackjack.current_deck_array_index > check){
			current_deck.clear();
			current_deck_array_index = 0;
		}
		
    	playerOne.resetGameLogic();
		playerOneSplit.resetGameLogic();
		split_one.clear();
		split_two.clear();
        
        cards_in_play_before_dealer.clear();
        
        player_cards.clear();
        dealer_cards.clear();
        current_player_playing = 1; //0=dealer, 1=player
        
        dealer_score = 0;
        player_score = 0;
        //play_mode = //RESET AT BUTTON
        dealer_score_active = false;
        player_score_active = false;
        dealer_blackjack = false;
        player_blackjack = false;
        dealer_bust = false;
        player_bust = false;
        dealer_lose = false;
        player_lose = false;
        dealer_win = false;
        player_win = false;
        dealer_flipped = false;
        dealer_soft_hand = false;
        player_soft_hand = false;
        dealer_done = false;
        is_push = false;
        first_deal_before_hit = false;
        
    }
    
    public void reinitializeSplitTwo(){
		
		playerOneSplit.resetGameLogic();
		//this.play_mode = false;
	    
		
	    split_dealer_21 = false;
	    split_hand_one_21 = false;
	    split_hand_two_21 = false;
	    
	    split_dealer_bust = false;
	    split_hand_one_bust = false;
	    split_hand_two_bust = false;
	    
	    split_dealer_lose = false;
	    split_hand_one_lose = false;
	    split_hand_two_lose = false;
	    
	    split_dealer_win = false;
	    split_hand_one_win = false;
	    split_hand_two_win = false;
	    
	    split_detected = false;
	    
        
    }
    
    public void resetSpriteRegisters(){
    	
    	int uis = ui_sprites_register.length;
    	int csr = copy_sprites_register.length;
    	int dsr = dealer_score_sprites_register.length;
    	int psr = player_score_sprites_register.length;
    	int sss = split_score_sprites_register.length;
    	
    	/*
    	int ui = ui_sprites.length;
    	int cs = copy_sprites.length;
    	int ds = dealer_score_sprites.length;
    	int ps = player_score_sprites.length;
    	int ss = split_score_sprites.length;
    	*/
    	
    	//ui
    	for(int i=0;i<uis;i++){
    		ui_sprites_register[i] = 0;
    	}
    	
    	/*
    	for(int i=0;i<ui;i++){
    		ui_sprites[i] = null;
    	}*/
    	
    	//copy
    	for(int i=0;i<csr;i++){
    		copy_sprites_register[i] = 0;
    	}
    	/*
    	for(int i=0;i<cs;i++){
    		copy_sprites[i] = null;
    	}*/
    	
    	//dealer
    	for(int i=0;i<dsr;i++){
    		dealer_score_sprites_register[i] = 0;
    	}
    	/*
    	for(int i=0;i<ds;i++){
    		dealer_score_sprites[i] = null;
    	}*/
    	
    	//player
    	for(int i=0;i<psr;i++){
    		player_score_sprites_register[i] = 0;
    	}
    	/*
    	for(int i=0;i<ps;i++){
    		player_score_sprites[i] = null;
    	}*/
    	
    	//split(player2)
    	for(int i=0;i<sss;i++){
    		split_score_sprites_register[i] = 0;
    	}
    	/*
    	for(int i=0;i<ss;i++){
    		split_score_sprites[i] = null;
    	}
    	*/
    	
    	//Arrays.fill(ui_sprites_register,0);
    	//Arrays.fill(copy_sprites_register, 0);
    	//Arrays.fill(dealer_score_sprites_register, 0);
    	//Arrays.fill(player_score_sprites_register,0);
    }
    
    
    //sudo dealer ai
  	public void dealerHit(){
  		//
  	}
  	
  	
  	//utils
  	public float randFloat(float min, float max){
  		r = new Random();
  		float x = min + (max - min) * r.nextFloat();
    	return x;
  	}
  	public int randInt(int min, int max){
  		r = new Random();
  		return r.nextInt(max - min + 1) + min;
  	}
  	
  	
  	//UI INITIALIZE ////////////////////////////////
  	private void uiInit(){
  		
  		sa = new SpriteAtlas(this);
  		
  		/*cards*/
    	card_atlas = new TextureAtlas(Gdx.files.internal("data/cards2_128_packed/cards2_128.atlas"));
    	/*cards are running out of memory on android so, put here */
    	/*trying to call only once for all cards in card class*/
		ac = card_atlas.findRegion("ac");
    	ad = card_atlas.findRegion("ad");
    	ah = card_atlas.findRegion("ah");
        as = card_atlas.findRegion("as");
        
        kc = card_atlas.findRegion("kc");
        kd = card_atlas.findRegion("kd");
        kh = card_atlas.findRegion("kh");
        ks = card_atlas.findRegion("ks");
        
        qc = card_atlas.findRegion("qc");
        qd = card_atlas.findRegion("qd");
        qh = card_atlas.findRegion("qh");
        qs = card_atlas.findRegion("qs");
        
        jc = card_atlas.findRegion("js");
        jd = card_atlas.findRegion("jd");
        jh = card_atlas.findRegion("jh");
        js = card_atlas.findRegion("js");
        
        c2 = card_atlas.findRegion("2c");
        d2 = card_atlas.findRegion("2d");
        h2 = card_atlas.findRegion("2h");
        s2 = card_atlas.findRegion("2s");
        
        c3 = card_atlas.findRegion("3c");
        d3 = card_atlas.findRegion("3d");
        h3 = card_atlas.findRegion("3h");
        s3 = card_atlas.findRegion("3s");
        
        c4 = card_atlas.findRegion("4c");
        d4 = card_atlas.findRegion("4d");
        h4 = card_atlas.findRegion("4h");
        s4 = card_atlas.findRegion("4s");
        
        c5 = card_atlas.findRegion("5c");
        d5 = card_atlas.findRegion("5d");
        h5 = card_atlas.findRegion("5h");
        s5 = card_atlas.findRegion("5s");
        
        c6 = card_atlas.findRegion("6c");
        d6 = card_atlas.findRegion("6d");
        h6 = card_atlas.findRegion("6h");
        s6 = card_atlas.findRegion("6s");
        
        c7 = card_atlas.findRegion("7c");
        d7 = card_atlas.findRegion("7d");
        h7 = card_atlas.findRegion("7h");
        s7 = card_atlas.findRegion("7s");
        
        c8 = card_atlas.findRegion("8c");
        d8 = card_atlas.findRegion("8d");
        h8 = card_atlas.findRegion("8h");
        s8 = card_atlas.findRegion("8s");
        
        c9 = card_atlas.findRegion("9c");
        d9 = card_atlas.findRegion("9d");
        h9 = card_atlas.findRegion("9h");
        s9 = card_atlas.findRegion("9s");
        
        c10 = card_atlas.findRegion("10c");
        d10 = card_atlas.findRegion("10d");
        h10 = card_atlas.findRegion("10h");
        s10 = card_atlas.findRegion("10s");
        
        face = card_atlas.findRegion("face");
        //////////////////////////////////////////////////////
  		
        
    	
  		face_card = new Card(52,0,this);
  		
    	ui = sa.getUIArray();
    	dealer_scores = sa.getDealerScoreArray();
    	player_scores = sa.getPlayerScoreArray();
    	copy = sa.getCopyArray();
    	
    	
    	
    	//small chips
    	small_chip_atlas = new TextureAtlas(Gdx.files.internal("data/small_chips_packed/small_chips.atlas"));
		
    	x100_small_new = small_chip_atlas.findRegion("100_small");
    	x10_smaller_new = small_chip_atlas.findRegion("10_smaller");
    	x25_smaller_new = small_chip_atlas.findRegion("25_smaller");
    	x50_smaller_new = small_chip_atlas.findRegion("50_smaller");
    	x5_smaller_new = small_chip_atlas.findRegion("5_smaller");
    	
    	//ui sprites
    	ui_sprites = new Sprite[]{new Sprite(ui[0]), //table
    							   new Sprite(ui[1]), //black_circle
    							   new Sprite(ui[2]), //black_white_circle
    							   new Sprite(ui[3]), //green_circle
    							   new Sprite(ui[4]), //green_copy_back
    							   new Sprite(ui[5]), //green_white_circle
    							   new Sprite(ui[6]), //dealer_white_circle
    							   new Sprite(ui[7]), //player_white_circle
    							   new Sprite(ui[8]), //dealer_red_white_circle
    							   new Sprite(ui[9]), //player_red_white_circle
    							   new Sprite(ui[10]), //dealer_blue_white_circle
    							   new Sprite(ui[11]), //player_blue_white_circle
    							   new Sprite(ui[12]), //dealer_black_white_circle
    							   new Sprite(ui[13]),//player_black_white_circle
    						       new Sprite(ui[14]),//dealer_green_white_circle
    						       new Sprite(ui[15]), //player_green_white_circle
    						       new Sprite(ui[16]), //bet_tab
    						       new Sprite(ui[17]), //bet_menu
    						       new Sprite(ui[18]), //split_back, //18
							       new Sprite(ui[19]), //split_white_circle, //19
								   new Sprite(ui[20]), //split_red_white_circle, //20
								   new Sprite(ui[21]), //split_blue_white_circle, //21
								   new Sprite(ui[22]), //split_black_white_circle, //22
								   new Sprite(ui[23]),//split_green_white_circle}; //23
								   new Sprite(ui[24]),//chips_5
								   new Sprite(ui[25]),//chips_10
								   new Sprite(ui[26]),//chips_25
								   new Sprite(ui[27]),//chips_50
								   new Sprite(ui[28]),//chips_100
								   new Sprite(ui[29]),//c_control_panel_back_top
								   new Sprite(ui[30]),//c_deal
								   new Sprite(ui[31]),//c_hit
								   new Sprite(ui[32]),//c_split
								   new Sprite(ui[33]),//c_stand
								   new Sprite(ui[34]),//c_control_panel_back_bottom
    							   new Sprite(ui[35]),//bet_black_box
    							   new Sprite(ui[36]),//bet_down_arrow
    							   new Sprite(ui[37]),//bet_up_arrow
						    	   new Sprite(ui[38]),//c100glow
						    	   new Sprite(ui[39]),//c10glow
						    	   new Sprite(ui[40]),//c25glow
						    	   new Sprite(ui[41]),//c50glow
						    	   new Sprite(ui[42]),//c5glow
						    	   new Sprite(ui[43]),//control_panel_back_top
						    	   new Sprite(ui[44]),//doubledown
						    	   new Sprite(ui[45]),//splash
						    	   new Sprite(ui[46]),//buytokens
						    	   new Sprite(ui[47]),//buyback1
    	  						   new Sprite(ui[48]),//bonus_chips
    	  						   new Sprite(ui[49]),//buy_chip_blink
    							   new Sprite(ui[50]),//full_screen_shadow
    							   new Sprite(ui[51]),//homebutton
    							   new Sprite(ui[52]),//home_screen
    							   new Sprite(ui[53]),//settings_num_two
    							   new Sprite(ui[54]),//settings_num_three
    							   new Sprite(ui[55]),//settings_num_four
    							   new Sprite(ui[56]),//settings_num_five
    							   new Sprite(ui[57]),//settings_num_six
    							   new Sprite(ui[58]),//settings_off
    							   new Sprite(ui[59]),//settings_off_copy
    							   new Sprite(ui[60]),//settings_on
    							   new Sprite(ui[61]),//settings_on_copy
    							   new Sprite(ui[62]),//settings_back_small
    							   new Sprite(ui[63]),//settings_back_big
		
    							   new Sprite(ui[64]),//settings_sound_back
    							   new Sprite(ui[65]),//settings_deck_back
    							   new Sprite(ui[66]),//settings_dealer_hit_back
    							   new Sprite(ui[67]),//settings_card_dealt_back
    	
    							   new Sprite(ui[68]),//settings_sound_on
    							   new Sprite(ui[69]),//settings_sound_on_copy
    							   new Sprite(ui[70]),//settings_sound_off
    							   new Sprite(ui[71]),//settings_sound_off_copy
    							   new Sprite(ui[72]),//settings_deck_2_on
    							   new Sprite(ui[73]),//settings_deck_2_off
    							   new Sprite(ui[74]),//settings_deck_3_on
    							   new Sprite(ui[75]),//settings_deck_3_off
    							   new Sprite(ui[76]),//settings_deck_4_on
    							   new Sprite(ui[77]),//settings_deck_4_off
    							   new Sprite(ui[78]),//settings_deck_5_on
    							   new Sprite(ui[79]),//settings_deck_5_off
    							   new Sprite(ui[80]),//settings_deck_6_on
    							   new Sprite(ui[81]),//settings_deck_6_off
    							   new Sprite(ui[82]),//settings_dhit_on
    							   new Sprite(ui[83]),//settings_dhit_on_copy
    							   new Sprite(ui[84]),//settings_dhit_off
    							   new Sprite(ui[85]),//settings_dhit_off_copy
    							   new Sprite(ui[86]),//settings_dealt_on
    							   new Sprite(ui[87]),//settings_dealt_on_copy
    							   new Sprite(ui[88]),//settings_dealt_off
    							   new Sprite(ui[89]),//settings_dealt_off_copy
    							   new Sprite(ui[90]),//greenbackarrow_one
    						       new Sprite(ui[91]),//peeler1
    						       new Sprite(ui[92]),//x100_small, // !!!MOVED
    						       new Sprite(ui[93]),//x10_smaller // !!!MOVED
    						       new Sprite(ui[94]),//x25_smaller, // !!!MOVED
    						       new Sprite(ui[95]),//x50_smaller, // !!!MOVED
    						       new Sprite(ui[96]),//x5_smaller,  // !!!MOVED
    						       new Sprite(ui[97]),//bet_menu_bigger2,
    						       new Sprite(ui[98]),//bet_menu_bigger2_close,
    						       new Sprite(ui[99]),//split_button2;
    							   new Sprite(ui[100])};//addfreechips;
    	
    	
    	
    	
    	copy_sprites = new Sprite[]{new Sprite(copy[0]), //blackjack
    								new Sprite(copy[1]), //bust
    								new Sprite(copy[2]), //lose
    								new Sprite(copy[3]), //win
    								new Sprite(copy[4]), //push
    								new Sprite(copy[5]), //small twinkle
    								new Sprite(copy[6]), //medium twinkle
    								new Sprite(copy[7]), //large twinkle
    								new Sprite(copy[8]), //d_blackjack,
     							    new Sprite(copy[9]), //d_bust,
     							    new Sprite(copy[10]), //d_lose,
     							    new Sprite(copy[11]), //d_win,
     							    new Sprite(copy[12]), //d_push,
     							    new Sprite(copy[13]), //s1_blackjack,
     							    new Sprite(copy[14]), //s1_bust,
     							    new Sprite(copy[15]), //s1_lose,
     							    new Sprite(copy[16]), //s1_win,
     							    new Sprite(copy[17]), //s1_push,
     							    new Sprite(copy[18]), //s2_blackjack,
     							    new Sprite(copy[19]), //s2_bust,
     							    new Sprite(copy[20]), //s2_lose,
     							    new Sprite(copy[21]), //s2_win,
     							    new Sprite(copy[22])}; //s2_push};

    	//dealer scores(side of card)
    	dealer_score_sprites = new Sprite[]{new Sprite(dealer_scores[0]),
	    								     new Sprite(dealer_scores[1]),
	    								     new Sprite(dealer_scores[2]),
	    								     new Sprite(dealer_scores[3]),
	    								     new Sprite(dealer_scores[4]),
	    								     new Sprite(dealer_scores[5]),
	    								     new Sprite(dealer_scores[6]),
	    								     new Sprite(dealer_scores[7]),
	    								     new Sprite(dealer_scores[8]),
	    								     new Sprite(dealer_scores[9]),
	    								     new Sprite(dealer_scores[10]),
	    								     new Sprite(dealer_scores[11]),
	    								     new Sprite(dealer_scores[12]),
	    								     new Sprite(dealer_scores[13]),
	    								     new Sprite(dealer_scores[14]),
	    								     new Sprite(dealer_scores[15]),
	    								     new Sprite(dealer_scores[16]),
	    								     new Sprite(dealer_scores[17]),
	    								     new Sprite(dealer_scores[18]),
	    								     new Sprite(dealer_scores[19]),
	    								     new Sprite(dealer_scores[20]),
	    								     new Sprite(dealer_scores[21]),
	    								     new Sprite(dealer_scores[22]),
	    								     new Sprite(dealer_scores[23]),
	    								     new Sprite(dealer_scores[24]),
	    								     new Sprite(dealer_scores[25]),
	    								     new Sprite(dealer_scores[26]),
	    								     new Sprite(dealer_scores[27]),
	    								     new Sprite(dealer_scores[28]),
	    								     new Sprite(dealer_scores[29]),
	    								     new Sprite(dealer_scores[30]),
	    								     new Sprite(dealer_scores[31])};//21_blackjack
    	
    	
    	//player scores(side of card)
    	player_score_sprites = new Sprite[]{new Sprite(player_scores[0]),
										     new Sprite(player_scores[1]),
										     new Sprite(player_scores[2]),
										     new Sprite(player_scores[3]),
										     new Sprite(player_scores[4]),
										     new Sprite(player_scores[5]),
										     new Sprite(player_scores[6]),
										     new Sprite(player_scores[7]),
										     new Sprite(player_scores[8]),
										     new Sprite(player_scores[9]),
										     new Sprite(player_scores[10]),
										     new Sprite(player_scores[11]),
										     new Sprite(player_scores[12]),
										     new Sprite(player_scores[13]),
										     new Sprite(player_scores[14]),
										     new Sprite(player_scores[15]),
										     new Sprite(player_scores[16]),
										     new Sprite(player_scores[17]),
										     new Sprite(player_scores[18]),
										     new Sprite(player_scores[19]),
										     new Sprite(player_scores[20]),
										     new Sprite(player_scores[21]),
										     new Sprite(player_scores[22]),
										     new Sprite(player_scores[23]),
										     new Sprite(player_scores[24]),
										     new Sprite(player_scores[25]),
										     new Sprite(player_scores[26]),
										     new Sprite(player_scores[27]),
										     new Sprite(player_scores[28]),
										     new Sprite(player_scores[29]),
										     new Sprite(player_scores[30]),
										     new Sprite(player_scores[31])};//21_blackjack
    	
    	//split player two sprites
    	split_score_sprites = new Sprite[]{new Sprite(player_scores[0]),
										     new Sprite(player_scores[1]),
										     new Sprite(player_scores[2]),
										     new Sprite(player_scores[3]),
										     new Sprite(player_scores[4]),
										     new Sprite(player_scores[5]),
										     new Sprite(player_scores[6]),
										     new Sprite(player_scores[7]),
										     new Sprite(player_scores[8]),
										     new Sprite(player_scores[9]),
										     new Sprite(player_scores[10]),
										     new Sprite(player_scores[11]),
										     new Sprite(player_scores[12]),
										     new Sprite(player_scores[13]),
										     new Sprite(player_scores[14]),
										     new Sprite(player_scores[15]),
										     new Sprite(player_scores[16]),
										     new Sprite(player_scores[17]),
										     new Sprite(player_scores[18]),
										     new Sprite(player_scores[19]),
										     new Sprite(player_scores[20]),
										     new Sprite(player_scores[21]),
										     new Sprite(player_scores[22]),
										     new Sprite(player_scores[23]),
										     new Sprite(player_scores[24]),
										     new Sprite(player_scores[25]),
										     new Sprite(player_scores[26]),
										     new Sprite(player_scores[27]),
										     new Sprite(player_scores[28]),
										     new Sprite(player_scores[29]),
										     new Sprite(player_scores[30]),
										     new Sprite(player_scores[31])};//21_blackjack
    	
    	//register common active sprites(used for menu(bet))
    	ui_sprites_register = new int[ui_sprites.length];
    	copy_sprites_register = new int[copy_sprites.length];
    	dealer_score_sprites_register = new int[dealer_score_sprites.length];
    	player_score_sprites_register = new int[player_score_sprites.length];
    	split_score_sprites_register = new int[split_score_sprites.length];
    	Arrays.fill(ui_sprites_register,0);
    	Arrays.fill(copy_sprites_register, 0);
    	Arrays.fill(dealer_score_sprites_register, 0);
    	Arrays.fill(player_score_sprites_register,0);
    	Arrays.fill(split_score_sprites_register,0);
    	
    	greenbackarrow_one = ui_sprites[90];
    	
    	//split ,hit,stand,deal,bet controls, homebutton
    	c_control_panel_back_top = ui_sprites[43];
    	c_control_panel_back_top.setPosition(0, 704);
    	homebutton = ui_sprites[51];
    	homebutton.setPosition(421, 745);
    	home_screen = ui_sprites[52];
    	home_screen.setPosition(0, 210);
    	peeler1 = ui_sprites[91];
    	
    	x100_small = ui_sprites[92];// !!MOVED
    	x10_smaller = ui_sprites[93];// !!MOVED
    	x25_smaller = ui_sprites[94];// !!MOVED
    	x50_smaller = ui_sprites[95];// !!MOVED
    	x5_smaller = ui_sprites[96];// !!MOVED
    	bet_menu_bigger2 = ui_sprites[97];
    	bet_menu_bigger2_close = ui_sprites[98];
    	split_button2 = ui_sprites[99];
 
    	c_control_panel_back_bottom = ui_sprites[34];
    	c_control_panel_back_bottom.setPosition(0, 0);
    			
    	//Texture split_button_texture = new Texture(Gdx.files.internal("data/split_placeholder.png"));
    	//split_button = new Sprite(split_button_texture);
    	//split_button.setSize(128, 64);
    	split_button = ui_sprites[32];
    	split_button.setPosition(split_button_x, 10);
    	
    	//Texture hit_button_texture = new Texture(Gdx.files.internal("data/hit_placeholder.png"));
    	//hit_button = new Sprite(hit_button_texture);
    	//hit_button.setSize(128, 64);
    	hit_button = ui_sprites[31];
    	hit_button.setPosition(hit_button_x, 10);
    	
    	//Texture stand_button_texture = new Texture(Gdx.files.internal("data/stand_placeholder.png"));
    	//stand_button = new Sprite(stand_button_texture);
    	//stand_button.setSize(128, 64);
    	stand_button = ui_sprites[33];
    	stand_button.setPosition(stand_button_x, 10);
    	
    	//Texture deal_button_texture = new Texture(Gdx.files.internal("data/deal_placeholder.png"));
    	//deal_button = new Sprite(deal_button_texture);
    	//deal_button.setSize(128, 64);
    	deal_button = ui_sprites[30];
    	deal_button.setPosition(deal_button_x, 10);
    	
    	//Texture bet_button_texture = new Texture(Gdx.files.internal("data/bet_placeholder.png"));
    	//bet_button = new Sprite(bet_button_texture);
    	//bet_button.setSize(128, 64);
    	//bet_button.setPosition(370, 10);
    	
    	//graphics
    	table_green = ui_sprites[0];
    	table_green.setPosition(0, 0);
    	
    	//dealer score
    	dealer_white_circle = ui_sprites[6];
    	dealer_blackjack_circle = ui_sprites[12];
    	dealer_red_white_circle = ui_sprites[8];
    	dealer_blue_white_circle = ui_sprites[10];
    	dealer_green_white_circle = ui_sprites[14];
    	
    	//player score
    	player_white_circle = ui_sprites[7];
    	player_blackjack_circle = ui_sprites[13];
    	player_red_white_circle = ui_sprites[9];
    	player_blue_white_circle = ui_sprites[11];
    	player_green_white_circle = ui_sprites[15];
    	
    	//split two score
    	split_white_circle = ui_sprites[19];
    	split_blackjack_circle = ui_sprites[22];
    	split_red_white_circle = ui_sprites[20];
    	split_blue_white_circle = ui_sprites[21];
    	split_green_white_circle = ui_sprites[23];
    	
    	//settings
    	settings_num_two = ui_sprites[53];
		settings_num_three = ui_sprites[54];
		settings_num_four = ui_sprites[55];
		settings_num_five = ui_sprites[56];
		settings_num_six = ui_sprites[57];
		settings_off = ui_sprites[58];
		settings_off_copy = ui_sprites[59];
		settings_on = ui_sprites[60];
		settings_on_copy = ui_sprites[61];
		settings_back_small = ui_sprites[62];
		settings_back_big = ui_sprites[63];
		
		settings_sound_back = ui_sprites[64];
		settings_deck_back = ui_sprites[65];
		settings_dealer_hit_back = ui_sprites[66];
		settings_card_dealt_back = ui_sprites[67];
		
		settings_sound_on = ui_sprites[68];
		settings_sound_on_copy = ui_sprites[69];
		settings_sound_off = ui_sprites[70];
		settings_sound_off_copy = ui_sprites[71];
		settings_deck_2_on = ui_sprites[72];
		settings_deck_2_off = ui_sprites[73];
		settings_deck_3_on = ui_sprites[74];
		settings_deck_3_off = ui_sprites[75];
		settings_deck_4_on = ui_sprites[76];
		settings_deck_4_off = ui_sprites[77];
		settings_deck_5_on = ui_sprites[78];
		settings_deck_5_off = ui_sprites[79];
		settings_deck_6_on = ui_sprites[80];
		settings_deck_6_off = ui_sprites[81];
		settings_dhit_on = ui_sprites[82];
		settings_dhit_on_copy = ui_sprites[83];
		settings_dhit_off = ui_sprites[84];
		settings_dhit_off_copy = ui_sprites[85];
		settings_dealt_on = ui_sprites[86];
		settings_dealt_on_copy = ui_sprites[87];
		settings_dealt_off = ui_sprites[88];
		settings_dealt_off_copy = ui_sprites[89];
		
		
    	
  	}
  	//UI INITIALIZE ////////////////////////////////
  	
  	
  	//UI DRAW //////////////////////////////////////
  	public void addHitStand(){
    	//hit stand button
        hit_button.draw(batch);
        stand_button.draw(batch);
    }
  	public void addDealBet(){
    	//hit stand button
        deal_button.draw(batch);
        //bet_button.draw(batch);
    }
  	public void addSplitButton(){
  		//split_button.draw(batch);
  		split_button2.setPosition(doubledown_x, doubledown_y - 100);
  		split_button2.draw(batch);
  	}
  	
    public void addGreenBackground(){
    	//add green background
        table_green.draw(batch);
    }
    
    
    public void addScore(int who,int x,int y, 
    		int player_sc,int dealer_sc){
    	
    	//default
    	int x_pos=0;
    	int dealer_y_pos=0;
    	int player_y_pos=0;
    	int ps = 0;
    	int ds = 0;
    	int ss = 0;
    	if(x == 0 && y == 0){
    		x_pos = x_offset;
    		dealer_y_pos = dealer_card_dealt_y + y_offset;
    		player_y_pos = player_card_dealt_y + y_offset;
    		ps = player_score;//this
        	ds = dealer_score;//this
    	}else{
    		x_pos = x;
    		dealer_y_pos = y;
    		player_y_pos = y;
    		ps = player_sc;//local
    		ss = player_sc;//local
        	ds = dealer_sc;//local
    	}
    			
    	switch(who){
    	case 0:
    		dealer_white_circle.setPosition(x_pos, dealer_y_pos);
        	dealer_white_circle.draw(batch);
        	ui_sprites_register[6]=2; // add 2 sprite register "2nd level" for bet menu
        	
        	dealer_score_sprites[ds].setPosition(x_pos +18,dealer_y_pos +17);
            dealer_score_sprites[ds].draw(batch);
            dealer_score_sprites_register[ds]=1;
    		break;
    	case 1:
    		player_white_circle.setPosition(x_pos, player_y_pos);
    		player_white_circle.draw(batch);
    		ui_sprites_register[7]=2; // add 2 sprite register "2nd level" for bet menu
    		
    		player_score_sprites[ps].setPosition(x_pos +18,player_y_pos +17);
            player_score_sprites[ps].draw(batch);
            player_score_sprites_register[ps]=1;
            break;
    	case 2:
    		split_white_circle.setPosition(x_pos, player_y_pos);
    		split_white_circle.draw(batch);
    		ui_sprites_register[19]=2; // add 2 sprite register "2nd level" for bet menu
    		
    		//System.out.println("===============================================================");
    		//System.out.println("ERROR split_score_sprites length: " + split_score_sprites.length);
    		//System.out.println("ERROR split_score_sprites[ss] ss=" + ss);
    		//System.out.println("===============================================================");
    		split_score_sprites[ss].setPosition(x_pos +18,player_y_pos +17);
            split_score_sprites[ss].draw(batch);
            split_score_sprites_register[ss]=1;
            break;
    	}
    	
    	
    }
    
    
    public void addScore21(int who,int x, int y){
    	
    	//default
    	int x_pos=0;
    	int dealer_y_pos=0;
    	int player_y_pos=0;
    	if(x == 0 && y == 0){
    		x_pos = x_offset;
    		dealer_y_pos = dealer_card_dealt_y + y_offset;
    		player_y_pos = player_card_dealt_y + y_offset;
    	}else{
    		x_pos = x;
    		dealer_y_pos = x;
    		player_y_pos = y;
    	}
    	
    	switch(who){
    	case 0:
    		dealer_white_circle.setPosition(x_pos, dealer_y_pos);
    		dealer_white_circle.draw(batch);
    		ui_sprites_register[6]=2; // add 2 sprite register "2nd level"
    		
    		dealer_score_sprites[31].setPosition(x_pos +18,dealer_y_pos +17);
    		dealer_score_sprites[31].draw(batch);
    		dealer_score_sprites_register[31]=1;
    		break;
    	case 1:
    		player_white_circle.setPosition(x_pos, player_y_pos);
    		player_white_circle.draw(batch);
    		ui_sprites_register[7]=2; // add 2 sprite register "2nd level"
    		
    		player_score_sprites[31].setPosition(x_pos +18,player_y_pos +17);
    		player_score_sprites[31].draw(batch);
    		player_score_sprites_register[31]=1;
    		break;
    	case 2:
    		split_white_circle.setPosition(x_pos, player_y_pos);
    		split_white_circle.draw(batch);
    		ui_sprites_register[19]=2; // add 2 sprite register "2nd level"
    		
    		split_score_sprites[31].setPosition(x_pos +18,player_y_pos +17);
    		split_score_sprites[31].draw(batch);
    		split_score_sprites_register[31]=1;
    		break;
    	}
    }
    
    
    public void addScoreBackground(int who,int what,int x, int y){
    	
		//default
		int x_pos=0;
		int dealer_y_pos=0;
		int player_y_pos=0;
		if(x == 0 && y == 0){
			x_pos = x_offset;
			dealer_y_pos = dealer_card_dealt_y + y_offset;
			player_y_pos = player_card_dealt_y + y_offset;
		}else{
			x_pos = x;
			dealer_y_pos = y;
			player_y_pos = y;
		}
    	switch(who){
    	case 0:
    		switch(what){
    		case 0:
    			dealer_blackjack_circle.setPosition(x_pos,dealer_y_pos);
    			dealer_blackjack_circle.draw(batch);
    			ui_sprites_register[12]=1;
    			break;
    		case 1:
    			dealer_blue_white_circle.setPosition(x_pos,dealer_y_pos);
    			dealer_blue_white_circle.draw(batch);
    			ui_sprites_register[10]=1;
    			break;
    		case 2:
    			dealer_green_white_circle.setPosition(x_pos,dealer_y_pos);
    			dealer_green_white_circle.draw(batch);
    			ui_sprites_register[14]=1;
    			break;
    		case 3:
    			dealer_red_white_circle.setPosition(x_pos,dealer_y_pos);
    			dealer_red_white_circle.draw(batch);
    			ui_sprites_register[8]=1;
    			break;
    		case 4:
    			dealer_red_white_circle.setPosition(x_pos,dealer_y_pos);
    			dealer_red_white_circle.draw(batch);
    			ui_sprites_register[8]=1;
    			break;
    		}
    		break;
    	case 1:
    		switch(what){
    		case 0:
    			player_blackjack_circle.setPosition(x_pos,player_y_pos);
    			player_blackjack_circle.draw(batch);
    			ui_sprites_register[13]=1;
    			break;
    		case 1:
    			player_blue_white_circle.setPosition(x_pos,player_y_pos);
    			player_blue_white_circle.draw(batch);
    			ui_sprites_register[11]=1;
    			break;
    		case 2:
    			player_green_white_circle.setPosition(x_pos,player_y_pos);
    			player_green_white_circle.draw(batch);
    			ui_sprites_register[15]=1;
    			break;
    		case 3:
    			player_red_white_circle.setPosition(x_pos,player_y_pos);
    			player_red_white_circle.draw(batch);
    			ui_sprites_register[9]=1;
    			break;
    		case 4:
    			player_red_white_circle.setPosition(x_pos,player_y_pos);
    			player_red_white_circle.draw(batch);
    			ui_sprites_register[9]=1;
    			break;
    		}
    		break;
    	case 2:
    		switch(what){
    		case 0:
    			split_blackjack_circle.setPosition(x_pos,player_y_pos);
    			split_blackjack_circle.draw(batch);
    			ui_sprites_register[22]=1;
    			break;
    		case 1:
    			split_blue_white_circle.setPosition(x_pos,player_y_pos);
    			split_blue_white_circle.draw(batch);
    			ui_sprites_register[21]=1;
    			break;
    		case 2:
    			split_green_white_circle.setPosition(x_pos,player_y_pos);
    			split_green_white_circle.draw(batch);
    			ui_sprites_register[23]=1;
    			break;
    		case 3:
    			split_red_white_circle.setPosition(x_pos,player_y_pos);
    			split_red_white_circle.draw(batch);
    			ui_sprites_register[20]=1;
    			break;
    		case 4:
    			split_red_white_circle.setPosition(x_pos,player_y_pos);
    			split_red_white_circle.draw(batch);
    			ui_sprites_register[20]=1;
    			break;
    		}
    		break;
    	}
    }
    
    public void addSplitHighlight(int x, int y){
    	split_back = ui_sprites[18];
    	split_back.setPosition(x, y);
    	split_back.draw(batch);
    }
    
    public void firstDealDraw(){
    	for(int i=0;i<Blackjack.cards_in_play_before_dealer.size();i++){
    		
    		
        	Card card = Blackjack.cards_in_play_before_dealer.get(i);
        	Sprite card_type = card.getSprite();
        	if(playerOne.gamelogic().getTurn(0) == 1){
	        	if(i == 3){
	        		card_type = face_card.getSprite();//face-down card
	        	}
        	}
        	card_type.draw(batch);
        }

    }
    
    public void redrawStaticCards(){
    	int cip = Blackjack.cards_in_play_before_dealer.size()-1;//-1 or you get the hitcard
    	for(int i=0;i<cip;i++){
        	Card card = Blackjack.cards_in_play_before_dealer.get(i);
        	Sprite card_type = card.getSprite();
        	if(playerOne.gamelogic().getTurn(0) == 1){
	        	if(i == 3){
	        		card_type = face_card.getSprite();//face-down card
	        	}
        	}
        	card_type.setPosition(card.getX(), card.getY());
        	card_type.draw(batch);
        }
    	
    	
    }
    
    
    //dealer flip , split screen
    public void redrawStaticCards2(){
    	int cip = Blackjack.cards_in_play_before_dealer.size();
    	for(int i=0;i<cip;i++){
        	Card card = Blackjack.cards_in_play_before_dealer.get(i);
        	card.getSprite().setPosition(card.getX(), card.getY());
        	
        	if(Blackjack.dealer_flipped){
        		card.getSprite().draw(batch);
        	}else{
        		if(i == 3){
	        		face_card.getSprite().draw(batch);//face-down card
	        	}else{
	        		card.getSprite().draw(batch);
	        	}
        	}
        }
    	
    	
    }
    
    
    ////////////////////////////////////////////////
    
    
    //UI INITIALIZE AND DRAW ///////////////////////
    
    public void addPush(int who, int x, int y){
    	
    	int x_pos = 0;
    	int dy_pos = 0;
    	int py_pos = 0;
    	
    	if(x == 0 && y == 0){
    		x_pos = dealer_score_graphic_x;
    		dy_pos = dealer_score_graphic_y;
    		py_pos = player_score_graphic_y;
    	}else{
    		x_pos = x;
    		dy_pos = y;
    		py_pos = y;
    	}
    	switch(who){
    	case 0:
    		copy_sprites[4].setPosition(x_pos, dy_pos);
    		copy_sprites[4].draw(batch);
    		copy_sprites_register[4]=1;
    		break;
    	case 1:
    		copy_sprites[22].setPosition(x_pos, py_pos);
    		copy_sprites[22].draw(batch);
    		copy_sprites_register[22]=1;
    		break;
    	case 2://above second split hand
    		copy_sprites[17].setPosition(x_pos, py_pos);
    		copy_sprites[17].draw(batch);
    		copy_sprites_register[17]=1;
    		break;
    	}
    	
    }

    public void addBust(int who,int x,int y){
    	
    	//default
    	int dealer_y_pos=0;
        int player_y_pos=0;
        int dealer_x_pos=0;
        int player_x_pos=0;
    	if(x == 0 && y == 0){
    		dealer_x_pos = dealer_score_graphic_x;
    		player_x_pos = player_score_graphic_x;
    		dealer_y_pos = dealer_score_graphic_y;
    		player_y_pos = player_score_graphic_y;
    	}else{
    		dealer_x_pos = x;
    		player_x_pos = x;
    	    dealer_y_pos = y;
    	    player_y_pos = y;
    	}
    			
    	
    	switch(who){
    	case 0:
    		copy_sprites[9].setPosition(dealer_x_pos, dealer_y_pos);
    		copy_sprites[9].draw(batch);
    		copy_sprites_register[9]=1;
    		break;
    	case 1:
    		copy_sprites[19].setPosition(player_x_pos,player_y_pos);
    		copy_sprites[19].draw(batch);
    		copy_sprites_register[19]=1;
    		break;
    	case 2://above hand 2
    		copy_sprites[14].setPosition(player_x_pos,player_y_pos);
    		copy_sprites[14].draw(batch);
    		copy_sprites_register[14]=1;
    		break;
    	}
    	
    }
    
    public void addBlackjack(int who){
    	
    	switch(who){
    	case 0:
    		copy_sprites[8].setPosition(dealer_blackjack_graphic_x, dealer_blackjack_graphic_y);
    		copy_sprites[8].draw(batch);
    		copy_sprites_register[8]=1;
    		break;
    	case 1:
    		copy_sprites[0].setPosition(player_blackjack_graphic_x, player_blackjack_graphic_y);
    		copy_sprites[0].draw(batch);
    		copy_sprites_register[0]=1;
    		break;
    	}
    }
    
    public void addWin(int who, int x, int y){
    	
    	int x_pos = 0;
    	int dy_pos = 0;
    	int py_pos = 0;
    	
    	if(x == 0 && y == 0){
    		x_pos = dealer_score_graphic_x;
    		dy_pos = dealer_score_graphic_y;
    		py_pos = player_score_graphic_y;
    	}else{
    		x_pos = x;
    		dy_pos = y;
    		py_pos = y;
    	}
    	
    	switch(who){
        	case 0:
        		copy_sprites[11].setPosition(x_pos, dy_pos);
        		copy_sprites[11].draw(batch);
        		copy_sprites_register[11]=1;
        		break;
        	case 1:
        		copy_sprites[21].setPosition(x_pos, py_pos);
        		copy_sprites[21].draw(batch);
        		copy_sprites_register[21]=1;
        		break;
        	case 2://above second split hand
        		copy_sprites[16].setPosition(x_pos, py_pos);
        		copy_sprites[16].draw(batch);
        		copy_sprites_register[16]=1;
        		break;
        }
    
    }

    public void addLose(int who, int x, int y){
    	
    	int x_pos = 0;
    	int dy_pos = 0;
    	int py_pos = 0;
    	
    	if(x == 0 && y == 0){
    		x_pos = dealer_score_graphic_x;
    		dy_pos = dealer_score_graphic_y;
    		py_pos = player_score_graphic_y;
    	}else{
    		x_pos = x;
    		dy_pos = y;
    		py_pos = y;
    	}
    	
    	switch(who){
        	case 0:
        		copy_sprites[10].setPosition(x_pos, dy_pos);
        		copy_sprites[10].draw(batch);
        		copy_sprites_register[10]=1;
        		break;
        	case 1:
        		copy_sprites[20].setPosition(x_pos, py_pos);
        		copy_sprites[20].draw(batch);
        		copy_sprites_register[20]=1;
        		break;
        	case 2:
        		copy_sprites[15].setPosition(x_pos, py_pos);
        		copy_sprites[15].draw(batch);
        		copy_sprites_register[15]=1;
        		break;
        }
    
    }
    
    //MENU
    public void addBetTab(){
    	//ui_sprites[16].setPosition(450, 0);
    	//ui_sprites[16].draw(batch);
    	
    	//HACK
    	//home_screen_viewed = false;
    	
    	//change the alpha on the bet button
        //bet_menu_bigger2.setAlpha(50.0f);
    			
    	bet_menu_bigger2.setPosition(248,0);
    	bet_menu_bigger2.draw(batch);
    	
    }
    ////////////////////////////////////////////////
    
    
    
    
    //InternalStorageMethods
    public void writeScoreInternal(int score){
    	internalStorage.write(score);
    }
    public String readScoreInternal(){
		return internalStorage.read();
    }
    
    
    //DATABASE METHODS
    public void updateDatabaseScore(){
    	//get score from database on force quit
        database.executeUpdate("INSERT OR REPLACE INTO blackjackscore VALUES (1,'1','" + player_one_total + "');");
    }
    public String getDatabaseScore(){
    	Result result = database.query("SELECT * FROM 'blackjackscore';");
    	String score = "0";
    	if (!result.isEmpty()){
            result.moveToNext();
            score = result.getString(result.getColumnIndex("player_score"));
        }
    	return score;
    }
    
    
    public void buyTokens(){
    	int split=0;
    	if(play_mode)split=1;
    	Blackjack.close_buybutton = false;
    	this.setScreen(new BillingScreen(this,split));
    	//no dispose here
    	//dispose called on BillingScreen
    }
    
    
    //BONUS TIMER //RIGHT NOW GOOD FOR 1 HOUR
    
    public String numFilter(int x){
    	String out="";
    	if(x == 60){
    		out = "00";
    	}else if(x < 10){
    		out = "0"+x;
    	}else{
    		out = ""+x;
    	}
    	return out;
    }
    
    
    
    
    
    public void test_deck(){
    	
       if(!test_mode){
	       /*
	       //split check 1
	       current_deck.add(31); //5s --- p2 first card
	       current_deck.add(38); //7h --- dealer first card
	       current_deck.add(37); //7d --- p1 first card
	       current_deck.add(27); //4s --- dealer second card
	       current_deck.add(44); //9c --- p1 second card
	       current_deck.add(31); //5s --- HIT p1 score=21
	       current_deck.add(43); //8s --- STAND HERE
	       current_deck.add(51); //10s -- Dealer hit 21
	       current_deck.add(20); //3c --- should not be called
	       */
	       
	       //score test
	       /*
	       current_deck.add(0); //Ac --- p2 first card
	       current_deck.add(38); //7h --- dealer first card
	       current_deck.add(37); //7d --- p1 first card
	       current_deck.add(27); //4s --- dealer second card
	       current_deck.add(44); //9c --- p1 second card
	       current_deck.add(37); //7d --- stand here
	       current_deck.add(41); //8d --- p2 second card
	       current_deck.add(41); //8d -- hit here and stand
	       current_deck.add(20); //3c --- should not be called
	       */
	       
    	   /*
	       current_deck.add(31); //5s --- p2 first card
	       current_deck.add(2); //Ah --- dealer first card
	       current_deck.add(26); //4h --- p1 first card
	       current_deck.add(40); //8c --- dealer second card
	       current_deck.add(41); //8d --- p1 second card
	       current_deck.add(43); //8s --- stand here
	       current_deck.add(51); //10s --- p2 second card
	       current_deck.add(27); //4s -- hit here and stand
	       current_deck.add(2); //3c --- should not be called
	       */
    	   
    	   /*
	       current_deck.add(42); //8h --- 
	       current_deck.add(37); //7d --- 
	       current_deck.add(24); //4c ---
	       current_deck.add(33); //6d ---
	       current_deck.add(0); //Ac ---
	       current_deck.add(30); //5h --- 
	       current_deck.add(51); //10s --- 
	       current_deck.add(27); //4s -- 
	       current_deck.add(2); //3c --- 
	       */
    	   
    	   /*
	       current_deck.add(42); //8h --- 
	       current_deck.add(2); //ah --- d1
	       current_deck.add(24); //4c ---
	       current_deck.add(24); //4c --- d2
	       current_deck.add(0); //Ac ---
	       current_deck.add(33); //6d --- 
	       current_deck.add(51); //10s --- 
	       current_deck.add(33); //6d -- 
	       current_deck.add(2); //3c --- 
	       */
    	   current_deck.add(6); //kh --- 
	       current_deck.add(10); //qh --- d1
	       current_deck.add(6); //kh ---
	       current_deck.add(9); //qd --- d2
	       current_deck.add(28); //5c ---
	       current_deck.add(38); //7h -- 
	       current_deck.add(29); //5d --- 
	       current_deck.add(21); //3d --- 
	       current_deck.add(2); //3c ---
	       
    	   
	       
		   for(int i=0;i<43;i++){
				current_deck.add(i);
		   }
	   
       }else{
    	   System.out.println("TEST MODE MUST BE OFF HERE");
       }

    }
	
    //draw the animated bet chips
    public void drawAnimatedChips(){
    	for(int i=0;i<chipArray.chipsInPlay.size();i++){
    		chipArray.chipsInPlay.get(i).getSprite().draw(batch);
    	}
    }
	
    
    //DATABASE RESULT INTERFACE ////////////////
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean moveToNext() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getColumnIndex(String name) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public float getFloat(int columnIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
}
