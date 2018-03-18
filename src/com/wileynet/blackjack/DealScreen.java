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

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Circ;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.wileynet.blackjack.accessors.SpriteAccessor;

public class DealScreen implements Screen {
	
	//animated chips only five at beginning
	boolean draw_animated_chips = false;
	//===============================
	
	final Blackjack game;
	Timeline cs;
	Timeline cs2;
	private final TweenManager tweenManager;
	private boolean scorescreen_called = false;//hack

    OrthographicCamera camera;
    
    boolean draw_score_background = false;
    boolean draw_small_twinkle = false;
    boolean dealer_cards_dealt = false;
    
    
	public DealScreen(final Blackjack gam){
		   
		Blackjack.screen_state = 2;
		Gdx.input.setCatchBackKey(true);
		
		this.game = gam;
		
		//animated bet chips
		if(game.chipArray.chipsInPlay.size() < 1){
			game.chipArray.addChip(5);
		}
		
		//game.resetSpriteRegisters();
		game.dd_clicked_one = false;
		game.dd_clicked_two = false;
		
		tweenManager = new TweenManager();
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        
        //if only 5 set position of 5 chip that was added in homescreen
        if(game.chipArray.chipsInPlay.size() == 1){
        	for(int i=0;i<game.chipArray.chipsInPlay.size();i++){
        		if(!game.chipArray.chipsInPlay.get(i).getActive()){
        			sendChips();
        		}
        	}
        	
        }
        
        if(Blackjack.player_blackjack){
        	cs = Timeline.createSequence();
        	cs.push(Tween.to(game.copy_sprites[0], SpriteAccessor.SCALE_XY, 0.5f)
        	.target(1, 1));
            cs.setCallback(blackjack_callback);
            cs.setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START);
            cs.start(tweenManager);
        }
        if(Blackjack.dealer_blackjack){
        	cs = Timeline.createSequence();
        	cs.push(Tween.to(game.copy_sprites[0], SpriteAccessor.SCALE_XY, 0.5f)
        	.target(1, 1));
            cs.setCallback(blackjack_callback);
            cs.setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START);
            cs.start(tweenManager);
        }
        
        
        
        //////////////////////////// DEALERS TURN //////////////////////////////////
        if(Blackjack.current_player_playing == 0 ){
        	
        	/*
        	game.playerOne.gamelogic().dealerHand(Blackjack.dealer_score,
        			Blackjack.cards_in_play_before_dealer.size());
        	*/
        	game.playerOne.gamelogic().dealerHand(Blackjack.dealer_score,
        			Blackjack.current_deck_array_index);
        	
        	
        	if(Blackjack.dealer_cards.size() < 3){
        		game.updateGameState();
        		
        		
        	}else{
                
	        	//do it
	        	cs = Timeline.createSequence();
	        	int dcs = Blackjack.dealer_cards.size();
	        	if(dcs > 2){
	        		
	        		for(int i=0;i<dcs;i++){
	        			if(i>1){
	        				
	        				
	        				
	        				Card card = Blackjack.dealer_cards.get(i);
	                    	Sprite card_sprite = card.getSprite();
	                    	Card flipped_card = Blackjack.cards_in_play_before_dealer.get(3);
	                    	
	        				/*SETTINGS*/
	                		float rotate_card = 0;// 0 no rotate
	                		int dealer_y = game.dealer_card_dealt_y;
	                		if(!Blackjack.inline_card_deal){
	                			rotate_card = game.randFloat(-2.0f, 2.9f);
	                			dealer_y = game.randInt(game.dealer_card_dealt_y,game.dealer_card_dealt_y + 10);
	                		}/*SETTINGS*/
	                		
	                		//set card x,y
	                		int target_x=0;
	                		if(i==2){
	                			target_x = flipped_card.getX() + ((Blackjack.card_width / 8) * i);
	                		}else{
	                			Card last_card = Blackjack.dealer_cards.get(i-1);
	                			target_x = last_card.getX() + 35;
	                		}
	                		//int target_x = 90 + ((Blackjack.card_width / 8) * i );
	                		int target_y = dealer_y;
	                		card.setX(target_x);
	                		card.setY(target_y);
		                	
	                		
	                		//first
	                		//set scale bigger
	                		cs.push(Tween.set(card_sprite, SpriteAccessor.SCALE_XY)
	                				.ease(Circ.IN)
	        	                	.target(1.4f, 1.4f));
	                		
	                		//rotate it
	                		cs.push(Tween.set(card_sprite, SpriteAccessor.ROTATION)
	        	                	.target(rotate_card));
	                		
	        	               
	                		
	                		//animate
		                	cs.push(Tween.to(card_sprite, SpriteAccessor.POS_XY, 0.1f)
		                			.delay(0.2f)
		                			.ease(Circ.OUT)
		                			.setCallback(dealer_card_single_callback)
		                			.setCallbackTriggers(TweenCallback.START | 
		                					TweenCallback.COMPLETE)
		                			.target(target_x, target_y))
		                			
		                			.beginParallel()
		                			
		                	//scale it back to normal
		                	.push(Tween.to(card_sprite, SpriteAccessor.SCALE_XY, 0.1f)
		                	.target(1, 1))
		                	.end();
	        				
	        			}
	        		}
	        		cs.setCallback(dealer_card_all_callback);
	                cs.setCallbackTriggers(TweenCallback.COMPLETE);
	                
	                
	                //delay here also, fast !
	            	float delay = 0.1f; // seconds
	                Timer.schedule(new Task(){
	                    @Override
	                    public void run() {
	                    	cs.start(tweenManager);
	                    }
	                }, delay);
	                
	                
	        	}
	        }
        	
        }
        ////////////////////////////////////////////////////////////////////////////

        
	}
	
	private final TweenCallback dealer_card_single_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	if(Blackjack.play_sounds){
                		game.dealCardSound.play();
                	}
                	break;
				}
				}

	};
		
	private final TweenCallback dealer_card_all_callback = new TweenCallback() {
    		@Override
    		public void onEvent(int type, BaseTween<?> source) {
    				
    				switch (type) {
                    case COMPLETE:
                    	
                    	float delay = 0.5f; // seconds
                    	Timer.schedule(new Task(){
                            @Override
                            public void run() {
                            	dealer_cards_dealt = true;
                            	Blackjack.dealer_done = true;
                            	game.updateGameState();
                   	    	    
                            }
                        }, delay);
                    	break;
    				}
    				}
 
	};

	@Override
	public void render(float delta) {
		tweenManager.update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
        game.addGreenBackground();
        game.c_control_panel_back_bottom.draw(game.batch);
        game.c_control_panel_back_top.draw(game.batch);
        
        
        //score
        if(Blackjack.cards_in_play_before_dealer.size() < 1){
	        game.arialblack2.draw(game.batch, ""+ 
	        Blackjack.player_one_total, 
	        game.total_numbers_x , game.total_numbers_y);
        }else{
        	game.arialblack2.draw(game.batch, ""+ 
        	        Blackjack.player_one_total_before_change, 
        	        game.total_numbers_x , game.total_numbers_y);
        }
      	
        //draw bet chips
        game.drawAnimatedChips();
        
        
        /////////////////////////////////////////////////////////////////////// BLACKJACK SCORE BACKGROUND
        if(Blackjack.is_push){
    		//PUSH
    	}else{
    		if(Blackjack.dealer_blackjack){
    			game.addScoreBackground(0,0,0,0);
    			//game.addScoreBackground(1,3);
    		}
    		if(Blackjack.player_blackjack){
    			game.addScoreBackground(1,0,0,0);
    			//game.addScoreBackground(0,3);
    		}
    	}
        /////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        /////////////////////////////////////////////////////////////////////// WIN LOSE PUSH BUST BACKGROUND

        //PUSH
        if(Blackjack.is_push){
        	game.addScoreBackground(0,1,0,0);
    		game.addScoreBackground(1,1,0,0);
        }else{
        	
        	//WIN
	        if(Blackjack.dealer_win){
	        	game.addScoreBackground(0,2,0,0);
	        }
	        if(Blackjack.player_win){
	        	
	        	game.addScoreBackground(1,2,0,0);
	        	
	        	if(!Blackjack.player_blackjack &&
	        			Blackjack.player_score == 21 &&
	        				Blackjack.dealer_flipped &&
	        					Blackjack.play_win_sound){
	        		if(Blackjack.play_sounds){
	        			game.winSound.play();
	        		}
	        		Blackjack.play_win_sound = false;
	        		
	        	}
	        }
	        
	        //LOSE
        	if(Blackjack.dealer_lose){
        		game.addScoreBackground(0,3,0,0);
        	}
        	if(Blackjack.player_lose){
        		game.addScoreBackground(1,3,0,0);
        	}
        	
        	//BUST
        	if(Blackjack.dealer_bust){
        		game.addScoreBackground(0,3,0,0);
        	}
        	if(Blackjack.player_bust){
        		game.addScoreBackground(1,3,0,0);
        	}

        }
        ////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        ///////////////////////////////////////////////////////////////////////////// DRAW CARDS
        redrawStaticCards();
        if(Blackjack.current_player_playing == 0){
        	drawDealerCards();
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        //////////////////////////////////////////////////////////BLACKJACK COPY ANIMATION
        if(Blackjack.dealer_blackjack){
        	//game.addScore21(0);//add score here
        	game.addBlackjack(0);
        }
        if(Blackjack.player_blackjack){
        	//game.addScore21(1);//add score here
        	game.addBlackjack(1);
        }
        ///////////////////////////////////////////////////////////////////////////////////
        
        
        //////////////////////////////////////////////////////////////////////////// SCORES
	    if(Blackjack.dealer_score != 0 && 
	        		Blackjack.player_score != 0){
	    	
	        
	    	
	        if(Blackjack.dealer_cards.size() < 3){
	        	game.addScore(0,0,0,Blackjack.player_score,
	        			Blackjack.dealer_score);
        		game.addScore(1,0,0,Blackjack.player_score,
        				Blackjack.dealer_score);
	        	
	        	if(Blackjack.player_blackjack){
	        		if(!Blackjack.is_push){
	        			//game.addScore(0);
	        			game.addScore21(1,0,0);
	        		}else{
	        			//game.addScore(0);
	        			//game.addScore(1);
	        		}
	        	}
	        	
	        	//add yellow 21 to player 21score if not playerblackjack
	        	//and not push
	        	if(Blackjack.player_score == 21 &&
	        			!Blackjack.player_blackjack &&
	        			Blackjack.dealer_score != 21 &&
	        			Blackjack.dealer_flipped){
	        		//game.addScore(0);
	        		game.addScore21(1,0,0);
	        	}
	        	
	        	
	        	
	        	
	        }else{
	        	if(Blackjack.current_player_playing == 0 && dealer_cards_dealt){
	        		
		        	
		        	//add yellow 21 to player 21score if not playerblackjack
		        	//and not push
		        	if(Blackjack.player_score == 21 &&
		        			Blackjack.dealer_score != 21 &&
		        			Blackjack.dealer_done){
		        		game.addScore(0,0,0,Blackjack.player_score,
		        				Blackjack.dealer_score);
		        		game.addScore21(1,0,0);
		        	}else{
		        		game.addScore(0,0,0,Blackjack.player_score,
		        				Blackjack.dealer_score);
		        		game.addScore(1,0,0,Blackjack.player_score,
		        				Blackjack.dealer_score);
		        	}
		        	
		        	
	        	}
	        }
	    }
        ///////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        
        
        ///////////////////////////////////////////////////////////////////////// WIN LOSE PUSH BUST COPY
        //PUSH
        if(Blackjack.is_push){
        	game.addPush(0,0,0);
    		game.addPush(1,0,0);
        }else{
        	
        	//WIN
        	if(Blackjack.dealer_win || Blackjack.dealer_blackjack){
        		game.addWin(0,0,0);
        	}
        	if(Blackjack.player_win || Blackjack.player_blackjack){
        		game.addWin(1,0,0);
        	}
        	
        	//LOSE
        	if(Blackjack.dealer_lose){
        		game.addLose(0,0,0);
        	}
        	if(Blackjack.player_lose){
        		game.addLose(1,0,0);
        	}
        	
        	//BUST
        	if(Blackjack.dealer_bust){
        		game.addBust(0,0,0);
        	}
        	if(Blackjack.player_bust){
        		game.addBust(1,0,0);
        	}
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////	
        
        
        
        
        if(draw_small_twinkle){
        	drawSmallTwinkle();
        }

        if(!Blackjack.dealer_done){
	        game.addDealBet();
	        game.addBetTab();
	        game.homebutton.draw(game.batch);
        }
        
        
        //add bet
        //game.betFont.draw(game.batch, "5", 0,23);
        
        game.batch.end();
        
        
        if(!Blackjack.dealer_blackjack){
	        if(Blackjack.player_win || Blackjack.is_push || Blackjack.dealer_win){
	        	//goto score screen
	    		game.setScreen(new ScoreScreen(game));
		    	//dispose();
	        }
        }
        
        
        //input
	    if (Gdx.input.justTouched()) {
            Vector3 touchpoint = new Vector3();
            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpoint);
            
            
            //home button
            if( (touchpoint.x > 421 && touchpoint.x < 475) &&
            		(touchpoint.y > (Blackjack.screen_top-50)
            				&& touchpoint.y < Blackjack.screen_top)){
               //System.out.println("Home Clicked");
               game.setScreen(new HomeScreen(game));
            
            }
            
            
            //deal button
            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
            		(touchpoint.y > 10 && touchpoint.y < 77)){
            	
            	//set default bet
            	if(Blackjack.player_one_last_bet_made < 5){
            		Blackjack.player_one_last_bet_made = 5;
            	}
            	
            	//reset split scores
            	//game.dd_clicked_one = false;
            	//game.dd_clicked_two = false;
            	
            	
                game.reinitialize();
                //set shuffle
                if(Blackjack.current_deck.isEmpty()){
                	game.shuffle();
                	if(Blackjack.application_type==1){//android
                		game.actionResolver.showToast("Shuffling " + Blackjack.how_many_decks + " Decks",5000);
            	    }
                }
                //deal cards
                game.deal();
                //update score
                game.updateGameState();
                
                
               //game.setCardPositions();
               Blackjack.split_detected = false;
               
               if(Blackjack.current_deck.size() > 0){
            	   game.setScreen(new GameScreen(game));
               }else{
            	   game.setScreen(new MainMenuScreen(game));
               }
  	    	   //dispose();
            }
            
            
            //bet tab
            if( (touchpoint.x > 410 && touchpoint.x < 480) &&
            		(touchpoint.y > 330 && touchpoint.y < 480)){
               
            	if(Blackjack.play_sounds){
					game.tick_sound.play();
				}
               
               
               game.setScreen(new BetMenu(game,0));
  	    	   
            }
            
            //buy chips
            if( (touchpoint.x > Blackjack.buychips_x1 && 
            		touchpoint.x < Blackjack.buychips_x2) &&
            		(touchpoint.y > Blackjack.buychips_y1 && 
            				touchpoint.y < Blackjack.buychips_y2)){
            	
            	//buy
            	//game.buyTokens();
		    	
            }
            
            if(Blackjack.dealer_done){
	            //home button
	            if( (touchpoint.x > 421 && touchpoint.x < 475) &&
	            		(touchpoint.y > (Blackjack.screen_top-50)
	            				&& touchpoint.y < Blackjack.screen_top)){
	               //System.out.println("Home Clicked");
	               game.setScreen(new HomeScreen(game));
	            }
            }
            
	    }
		
	    if (Gdx.input.isKeyPressed(Keys.BACK)){
	    	//game.actionResolver.openAdActivity();//appnext
	    	game.actionResolver.killMyProcess();
	    }
	    
	    if(Blackjack.score_is_zero){
	    	//game.buyTokens();
	    	Blackjack.score_is_zero = false;
            Blackjack.player_one_total = 600;
            if(Blackjack.home_screen_viewed){
            	Blackjack.player_one_total_before_change = 600;
            	Blackjack.home_screen_viewed=false;
            }else{
            	Blackjack.player_one_total_before_change = 0;
            }
            Blackjack.player_one_last_bet_made = 5;
            Blackjack.close_buybutton = true;
	    }
	    
	    
	}
	
	
	
	private void sendChips(){
		
		draw_animated_chips = true;
		
		int yyy=0;
		
		//5
	    int start5y=18;
		int start5x=55;
		int count5=0;
		
		
		//stack interval
		int stack_interval=10;
		System.out.println("hi:" + game.chipArray.chipsInPlay.size()); 
			
			if(game.chipArray.chipsInPlay.size() > 0 && !Blackjack.chipsAnimationFinished){
					
				  
					//set positions 100
				
				    //sort stack ( NOT HERE )
					//game.chipArray.sortit();
					
					
					for(int i=0;i<game.chipArray.chipsInPlay.size();i++){
						//5
						if(game.chipArray.chipsInPlay.get(i).getType() == 5){
							count5++;
							yyy = (count5*stack_interval) + start5y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start5x);
						}
					}
					
					//System.out.println("::"+game.chipArray.chipsInPlay.size());
					
					cs2 = Timeline.createSequence();
					for(int i=0;i<game.chipArray.chipsInPlay.size();i++){		
						
						
						cs2.push(Tween.to(game.chipArray.chipsInPlay.get(i).getSprite(), SpriteAccessor.POS_XY,0.1f)
			        	.delay(0.1f)
			        	.setCallback(chip_sent)
			        	.ease(Circ.OUT)
			    	    .target(game.chipArray.chipsInPlay.get(i).getX(),
			    	    		game.chipArray.chipsInPlay.get(i).getY()));
			    	    
			    	}
					cs2.setCallback(all_chips_done);
					cs2.start(tweenManager);
					
			}
			
				
	}
	
	private final TweenCallback chip_sent = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	if(Blackjack.play_sounds){
                		game.poker_chips111.play();
                	}
                	break;
				}
				}

	};
	
	private final TweenCallback all_chips_done = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	Blackjack.chipsAnimationFinished = true;
                	break;
				}
				}

	};
	
	
	public void redrawStaticCards(){
    	int cip = Blackjack.cards_in_play_before_dealer.size();
    	for(int i=0;i<cip;i++){
        	Card card = Blackjack.cards_in_play_before_dealer.get(i);
        	card.getSprite().setPosition(card.getX(), card.getY());
        	card.getSprite().draw(game.batch);
        }
    	
    	
    }
	
	public void drawDealerCards(){
    	int dcs = Blackjack.dealer_cards.size();
    	for(int i=0;i<dcs;i++){
    		if(i > 1){
	        	Card card = Blackjack.dealer_cards.get(i);
	        	card.getSprite().draw(game.batch);
    		}
        }
    	
    }
	
	public void drawSmallTwinkle(){
		
		int twinkle_range_x_start = 300;
		int twinkle_range_x_to = 450;
		
		if(Blackjack.dealer_blackjack){
			
			//no stars for dealer
			
			/*
			int twinkle_range_y_to = 500;
			int twinkle_range_y_start = 300;
			
			int tx4 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty4 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			int tx5 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty5 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			int tx6 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty6 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			game.copy_sprites[5].setPosition(tx4, ty4);
			game.copy_sprites[6].setPosition(tx5, ty5);
			game.copy_sprites[7].setPosition(tx6, ty6);
			
			game.copy_sprites[5].draw(game.batch);
			game.copy_sprites[6].draw(game.batch);
			game.copy_sprites[7].draw(game.batch);
			*/
		}
		
		if(Blackjack.player_blackjack){
			
			int twinkle_range_y_to = 300;
			int twinkle_range_y_start = 100;
			
			int tx4 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty4 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			int tx5 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty5 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			int tx6 = game.randInt(twinkle_range_x_start, twinkle_range_x_to);
			int ty6 = game.randInt(twinkle_range_y_start, twinkle_range_y_to);
			
			game.copy_sprites[5].setPosition(tx4, ty4);
			game.copy_sprites[6].setPosition(tx5, ty5);
			game.copy_sprites[7].setPosition(tx6, ty6);
			
			game.copy_sprites[5].draw(game.batch);
			game.copy_sprites[6].draw(game.batch);
			game.copy_sprites[7].draw(game.batch);
		}
		
	}
	
	
    private final TweenCallback blackjack_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	
                	draw_small_twinkle = false;
                	
                	//goto score screen
                	if(!scorescreen_called){ // it only needs to be called once in case of split blackjack
	            		game.setScreen(new ScoreScreen(game));
	        	    	dispose();
	        	    	scorescreen_called = true;
                	}
        	    	
                	break;
                	
				case START:
					draw_small_twinkle = true;
					//draw_score_background = true;
					if(Blackjack.player_blackjack){
						if(Blackjack.play_sounds){
							game.blackjackSound.play();
						}
					}else if(Blackjack.dealer_blackjack &&
								!Blackjack.player_blackjack){
						if(Blackjack.play_sounds){
							game.bustSound.play();
						}
					}
                	break;
				}
		}

	};
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
