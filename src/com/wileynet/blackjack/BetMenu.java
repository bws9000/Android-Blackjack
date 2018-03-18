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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.wileynet.blackjack.accessors.SpriteAccessor;

public class BetMenu implements Screen {
	
	final Blackjack game;
	private int where;
	Card c;
	///////
	
	//animated chips
	boolean draw_animated_chips = false;
	int chiptotal=0;
	int recursive_count=0;
	//===============================
	
	//chips
	private int chip_width_height=74;
	private int chips_open_x = 388;
	private int chips_closed_x = 488;
	private int chip5y = 700;
	private int chip10y = 600;
	private int chip25y = 500;
	private int chip50y = 400;
	private int chip100y = 300;
	
	private boolean c5_clicked = false;
	private boolean c10_clicked = false;
	private boolean c25_clicked = false;
	private boolean c50_clicked = false;
	private boolean c100_clicked = false;
	
	//controls
	private int bet_controls_closed_x = 488;
	private int bet_up_open_x = 390;
	private int bet_box_open_x = 384;
	private int bet_down_open_x = 390;
	private int betu_y = 199;
	private int betb_y = 150;
	private int betd_y = 88;
	
	private int chip_value = 5;//default
	private int default_bet = chip_value;
	private int bet_limit = 5000;
	
	
	Timeline cs;
	Timeline cs2;
	
	private final TweenManager tweenManager;
	private final TweenManager tweenManager2;
	
	OrthographicCamera camera;
	
	public BetMenu(final Blackjack gam, int w){
		
		Blackjack.screen_state = 8;
		Gdx.input.setCatchBackKey(true);
		
		where = w;
		Blackjack.bet_normal_or_split = w;
		//0=normal
		//1=split
		
		this.game = gam;
		
		tweenManager = new TweenManager();
		tweenManager2 = new TweenManager();
		
		//test
		//game.addTokens();
		
		//no bets above your bank
 	    if(Blackjack.player_one_last_bet_made > Blackjack.player_one_total){
 		   Blackjack.player_one_last_bet_made = Blackjack.player_one_total;
 	    }
		
		//first card of first hand seems to be set off-stage here
		//manual hack setposition
		
		if(Blackjack.cards_in_play_before_dealer.size() > 0){
			c =  new Card(Blackjack.cards_in_play_before_dealer.get(2).getType(),1,game);
			c.getSprite().setPosition(110, 340);
		}
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        
        
		        //chip positions
		        if(!game.bet_menu_state){
		        	//chips
		        	game.ui_sprites[24].setPosition(chips_closed_x, chip5y);
		    		game.ui_sprites[25].setPosition(chips_closed_x, chip10y);
		    		game.ui_sprites[26].setPosition(chips_closed_x, chip25y);
		    		game.ui_sprites[27].setPosition(chips_closed_x, chip50y);
		    		game.ui_sprites[28].setPosition(chips_closed_x, chip100y);
		    		
		    		//controls
		            game.ui_sprites[37].setPosition(bet_controls_closed_x, betu_y);//bet_up_arrow
		            game.ui_sprites[35].setPosition(bet_controls_closed_x, betb_y);//bet_black_box
		            game.ui_sprites[36].setPosition(bet_controls_closed_x, betd_y);//bet_down_arrow
		        	
		            //////// HACK SO IT DOESN'T LOAD AFTER HOME SCREEN VIEW
		            if(!Blackjack.home_screen_viewed){
		            	openBetMenu();
		            }
		            
		        }else{
		        	//chips
		        	game.ui_sprites[24].setPosition(chips_open_x, chip5y);
		    		game.ui_sprites[25].setPosition(chips_open_x, chip10y);
		    		game.ui_sprites[26].setPosition(chips_open_x, chip25y);
		    		game.ui_sprites[27].setPosition(chips_open_x, chip50y);
		    		game.ui_sprites[28].setPosition(chips_open_x, chip100y);
		    		
		    		//controls
		            game.ui_sprites[37].setPosition(bet_up_open_x, betu_y);//bet_up_arrow
		            game.ui_sprites[35].setPosition(bet_box_open_x, betb_y);//bet_black_box
		            game.ui_sprites[36].setPosition(bet_down_open_x, betd_y);//bet_down_arrow
		            
		            
		            closeBetMenu();
		        }
		        
        
        
	}
	
	public void resetClickChips(){
		this.c5_clicked = false;
		this.c10_clicked = false;
		this.c25_clicked = false;
		this.c50_clicked = false;
		this.c100_clicked = false;
	}
	
	public void openBetMenu(){
		
		//chips_5 24
		//chips_10 25
		//chips_25 26
		//chips_50 27
		//chips_100 28
		   
		//game.ui_sprites[17].setPosition(444, 0);//TWEEN
		game.bet_menu_bigger2_close.setPosition(248, 0);
		
    	//open it
    	Tween.to(game.bet_menu_bigger2_close, SpriteAccessor.POS_XY,0.4f)// menu back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(133,0)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[24], SpriteAccessor.POS_XY,0.4f) //5 chip
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(chips_open_x,chip5y)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[25], SpriteAccessor.POS_XY,0.4f) //5 chip
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(chips_open_x,chip10y)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[26], SpriteAccessor.POS_XY,0.4f) //5 chip
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(chips_open_x,chip25y)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[27], SpriteAccessor.POS_XY,0.4f) //5 chip
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(chips_open_x,chip50y)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[28], SpriteAccessor.POS_XY,0.4f) //5 chip
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(chips_open_x,chip100y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    //bet controls
	    Tween.to(game.ui_sprites[37], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_up_open_x,betu_y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[35], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_box_open_x,betb_y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[36], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_down_open_x,betd_y)
	    .setCallback(open_callback)//CALLBACK HERE
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	   
	}
	
	
	
	public void closeBetMenu(){
		
		sendChips();
		
		//game.ui_sprites[17].setPosition(333, 0);//TWEEN
		game.bet_menu_bigger2_close.setPosition(133, 0);
    	
		//close it
    	Tween.to(game.bet_menu_bigger2_close, SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(248,0)
	    .setCallback(close_callback)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
    	
    	
    	Tween.to(game.ui_sprites[24], SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(chips_closed_x,chip5y)
	    .start(tweenManager);
    	
    	Tween.to(game.ui_sprites[25], SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(chips_closed_x,chip10y)
	    .start(tweenManager);
    	
    	Tween.to(game.ui_sprites[26], SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(chips_closed_x,chip25y)
	    .start(tweenManager);
    	
    	Tween.to(game.ui_sprites[27], SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(chips_closed_x,chip50y)
	    .start(tweenManager);
    	
    	Tween.to(game.ui_sprites[28], SpriteAccessor.POS_XY,0.4f) //back
    	.delay(0.2f)
    	.ease(Circ.OUT)
	    .target(chips_closed_x,chip100y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
    	
    	//bet controls
	    Tween.to(game.ui_sprites[37], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_controls_closed_x,betu_y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[35], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_controls_closed_x,betb_y)
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    Tween.to(game.ui_sprites[36], SpriteAccessor.POS_XY,0.4f)
	    .delay(0.2f)
	    .ease(Circ.OUT)
	    .target(bet_controls_closed_x,betd_y)
	    .setCallback(close_callback)//CALLBACK HERE
        .setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START)
	    .start(tweenManager);
	    
	    
	    //put the chips on the table
	    //sendChips();
	    
	}
	
	private void sendChips(){
		
		draw_animated_chips = true;
		
		int yyy=0;
		
		//100
		int start100y=44;
		int start100x=77;
		int count100=0;
		
		//50
	    int start50y=44;
		int start50x=44;
		int count50=0;
		
		//25
	    int start25y=33;
		int start25x=22;
		int count25=0;
		
		//10
	    int start10y=22;
		int start10x=33;
		int count10=0;
		
		//5
	    int start5y=18;
		int start5x=55;
		int count5=0;
		
		
		//stack interval
		int stack_interval=5;
		
			
			if(game.chipArray.chipsInPlay.size() > 0){
					
				     
					//set positions 100
				
				    //sort stack
					game.chipArray.sortit();
					
					
					for(int i=0;i<game.chipArray.chipsInPlay.size();i++){
						
						
						//100
						if(game.chipArray.chipsInPlay.get(i).getType() == 100){
							count100++;
							yyy = (count100*stack_interval) + start100y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start100x);
						}
						//50
						if(game.chipArray.chipsInPlay.get(i).getType() == 50){
							count50++;
							yyy = (count50*stack_interval) + start50y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start50x);
						}
						//25
						if(game.chipArray.chipsInPlay.get(i).getType() == 25){
							count25++;
							yyy = (count25*stack_interval) + start25y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start25x);
						}
						//10
						if(game.chipArray.chipsInPlay.get(i).getType() == 10){
							count10++;
							yyy = (count10*stack_interval) + start10y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start10x);
						}
						//5
						if(game.chipArray.chipsInPlay.get(i).getType() == 5){
							count5++;
							yyy = (count5*stack_interval) + start5y;
							game.chipArray.chipsInPlay.get(i).setY(yyy);
							game.chipArray.chipsInPlay.get(i).setX(start5x);
						}
						
					}
					
					
					
					cs2 = Timeline.createSequence();
					for(int i=0;i<game.chipArray.chipsInPlay.size();i++){		
					
						chiptotal += game.chipArray.chipsInPlay.get(i).getType();
						if(chiptotal <= Blackjack.player_one_last_bet_made){ // LESS OK .. MORE NO OK
							//if not already active(don't need)
							if(!game.chipArray.chipsInPlay.get(i).getActive()){
								
									cs2.push(Tween.to(game.chipArray.chipsInPlay.get(i).getSprite(), SpriteAccessor.POS_XY,0.1f)
						        	.delay(0.1f)
						        	.setCallback(chip_sent)
						        	.ease(Circ.OUT)
						    	    .target(game.chipArray.chipsInPlay.get(i).getX(),
						    	    		game.chipArray.chipsInPlay.get(i).getY()));
						    	    
									//set active
						    		game.chipArray.chipsInPlay.get(i).setActive();
							}
						}
			    		
			    	}
					cs2.setCallback(all_chips_done);
					cs2.start(tweenManager2);
					
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
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        tweenManager.update(Gdx.graphics.getDeltaTime());
        tweenManager2.update(Gdx.graphics.getDeltaTime());
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
    	
        game.addGreenBackground();
        
        if(game.bet_menu_state){
        	game.ui_sprites[50].draw(game.batch);//SHADOW
        }
    	
        game.c_control_panel_back_bottom.draw(game.batch);
        game.c_control_panel_back_top.draw(game.batch);
        game.homebutton.draw(game.batch);
        
        //score
        game.arialblack2.draw(game.batch, ""+Blackjack.player_one_total, game.total_numbers_x , 
        		game.total_numbers_y);
        
        if(where == 0){
        	addRegisteredSprites();
        }else{//1
        	game.ui_sprites[18].draw(game.batch);//highlight
        	addRegisteredSpritesFromSplit();
        }
        
        if(!game.bet_menu_state){
        	if(Blackjack.chipsAnimationFinished){
        		game.addDealBet();
        	}
        }
        
        //animated chips
        if(draw_animated_chips || Blackjack.chipsAnimationFinished){
	    	game.drawAnimatedChips();
	    }
        
        if(game.bet_menu_state){
        	game.ui_sprites[50].draw(game.batch);//SHADOW
        }
        
        
        addBetMenu();
        
        
        
        addChips();
        addControls();
        
        if(game.bet_menu_state && !game.bet_closed_clicked){
        	
        	int bx = bet_box_open_x;
        	
        	if(Blackjack.player_one_last_bet_made < 10){
        		bx += 32;
        	}else if(Blackjack.player_one_last_bet_made > 9 && Blackjack.player_one_last_bet_made < 100){
        		bx += 23;
        	}else if(Blackjack.player_one_last_bet_made > 99 && Blackjack.player_one_last_bet_made < 1000){
        		bx += 15;
        	}else if(Blackjack.player_one_last_bet_made > 999 && Blackjack.player_one_last_bet_made < 10000){
        		bx += 7;
        	}
        	
        	//DRAW BET FONT
        	//if 0 bank 0 bet
        	
            if(Blackjack.player_one_total == 0){
            	Blackjack.player_one_last_bet_made = 0;
            	game.font32blue.draw(game.batch, ""+Blackjack.player_one_last_bet_made, bx , betb_y + 42);
            }else{
	            if(Blackjack.player_one_last_bet_made == 0){
	            	game.font32blue.draw(game.batch, ""+chip_value, bx , betb_y + 42);//default bet
	            	Blackjack.player_one_last_bet_made = chip_value;
	            }else{
	            	game.font32blue.draw(game.batch, ""+Blackjack.player_one_last_bet_made, bx , betb_y + 42);
	            }
            }
            
            
            
        }else{
        	//closed clicked
        }
        
        
        //add bet
        //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
        
        game.batch.end(); //END DRAW
        
        
        	
        
	        //input
		    if (Gdx.input.justTouched()) {
	            Vector3 touchpoint = new Vector3();
	            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	            camera.unproject(touchpoint);
	            
	       
	        
	        	
	            if(Blackjack.chipsAnimationFinished){
			            //deal button
			            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
			            		(touchpoint.y > 10 && touchpoint.y < 77)){
			  	    	 
			            	
				            	if(!game.bet_menu_state){
				            	
					                if(where == 0){
					                	
							                game.reinitialize();
							                //set shuffle
							                game.shuffle();
							                //deal cards
							                game.deal();
							                //update score
							                game.updateGameState();
							                
							                
							               //game.setCardPositions();
							               game.setScreen(new GameScreen(game));
							  	    	   dispose();
							  	    	   
					                }else{//split
					                	
					             	   	   Blackjack.split_detected = false;
						            	   game.play_mode = false;
						            	   game.reinitialize();
						            	   game.reinitializeSplitTwo();
						            	   game.resetSpriteRegisters();
						            	   
						                    //set shuffle
						                    if(Blackjack.current_deck.isEmpty()){
						                    	game.shuffle();
						                    }
						                    //deal cards
						                    game.deal();
						                    //update score
						                    game.updateGameState();
						                    game.updateGameStateSplit();
						                    
						                   Blackjack.split_detected = false;
						                   Blackjack.dealer_flipped = false;
						                  
							  	    	   game.setScreen(new GameScreen(game));
							  	    	   dispose();
							  	    	   
					                }
					                
				            	}
				            	
				            	
				            	resetAgain();
				            	
			            }
			            
	            //Blackjack.chipsAnimationFinished = false;
	            }
		        
	            
	            
	            //BET TAB
	            if(!game.bet_menu_state){
		            
	            	//bet tab is closed

	            	
	            	if( (touchpoint.x > 410 && touchpoint.x < 480) &&
	                		(touchpoint.y > 330 && touchpoint.y < 480)){
		               
	            		

	            		if(Blackjack.play_sounds){
							game.tick_sound.play();
						}
	            		
		               //////// HACK SO IT DOESN'T LOAD AFTER HOME SCREEN VIEW
		               Blackjack.home_screen_viewed = false;
		               game.setScreen(new BetMenu(game,where));
		  	    	   
		            }
		            
	
		            
		            //home button
		            if( (touchpoint.x > 421 && touchpoint.x < 475) &&
		            		(touchpoint.y > (Blackjack.screen_top-50)
		            				&& touchpoint.y < Blackjack.screen_top)){
		              
		               game.setScreen(new HomeScreen(game));
		            }
		            
		            
	            }
	            
	            
	            
	            
	            //bet tab is open
	            if( (touchpoint.x > 290 && touchpoint.x < 373) &&
	            		(touchpoint.y > 330 && touchpoint.y < 480)){
	               
	            	
	            	if(Blackjack.play_sounds){
						game.tick_sound.play();
					}
	               
	               
	               
	               
	               this.autoChipAdd();
	               game.setScreen(new BetMenu(game,where));
	               
	  	    	  
	            }
	            
	            //buy chips
	            if( (touchpoint.x > Blackjack.buychips_x1 && 
	            		touchpoint.x < Blackjack.buychips_x2) &&
	            		(touchpoint.y > Blackjack.buychips_y1 && 
	            				touchpoint.y < Blackjack.buychips_y2)){
	            	
	            	//buy
	            	//game.buyTokens();
			    	
	            }
	            
	            
	            
	            
	            
	            
	            
	            if(game.bet_menu_state){
	            	
	            	//more free games bottom
		            if( (touchpoint.x > 375 && 
		            		touchpoint.x < 480) &&
		            		(touchpoint.y > 0 && 
		            				touchpoint.y < 85)){
		            	
		            	if(Blackjack.application_type == 1){
                    		//game.actionResolver.openAdActivity(game.isWifi);//applift
		            		((ActionResolver) game.actionResolver2).showOrLoadInterstital();
                    	}else if(Blackjack.application_type == 0){
                    		game.actionResolver.openUri("http://casinogames.wileynet.com/out.php");
                    	}
		            	
				    	
		            }
		            
	            	//chips
	            	this.chipClick(touchpoint, chips_open_x, chip_width_height, chip5y,5);
	            	this.chipClick(touchpoint, chips_open_x, chip_width_height, chip10y,10);
	            	this.chipClick(touchpoint, chips_open_x, chip_width_height, chip25y,25);
	            	this.chipClick(touchpoint, chips_open_x, chip_width_height, chip50y,50);
	            	this.chipClick(touchpoint, chips_open_x, chip_width_height, chip100y,100);
	            	
	            	//bet up/down
	            	this.betClick(touchpoint, bet_up_open_x, 65, betu_y,true);//up
	            	this.betClick(touchpoint, bet_down_open_x, 65, betd_y,false);//down
		            
	            }
	            
	            
		    }
		    
		    
        
        
	    
	    if (Gdx.input.isKeyPressed(Keys.BACK)){
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
            
            game.chipArray.chipsInPlay.clear();
	    }  
		
	}
	
	private void resetAgain(){
		//test
		Blackjack.player_blackjack = false;
		Blackjack.dealer_blackjack = false;
		Blackjack.dealer_flipped = false;
	    Blackjack.split_dealer_flip = false;
	    Blackjack.dealer_soft_hand = false;
	    Blackjack.player_soft_hand = false;
	    Blackjack.dealer_done = false;
	    Blackjack.is_push = false;
	    Blackjack.first_deal_before_hit = false;
	    Blackjack.dealer_blackjack = false;
	    Blackjack.player_blackjack = false;
	    Blackjack.split_dealer_21 = false;
	    Blackjack.split_hand_one_21 = false;
	    Blackjack.split_hand_two_21 = false;
	    
	    //BUST
	    Blackjack.dealer_bust = false;
	    Blackjack.player_bust = false;
	    Blackjack.split_dealer_bust = false;
	    Blackjack.split_hand_one_bust = false;
	    Blackjack.split_hand_two_bust = false;
	    
	    //LOSE
	    Blackjack.dealer_lose = false;
	    Blackjack.player_lose = false;
	    Blackjack.split_dealer_lose = false;
	    Blackjack.split_hand_one_lose = false;
	    Blackjack.split_hand_two_lose = false;
	    
	    //WIN
	    Blackjack.dealer_win = false;
	    Blackjack.player_win = false;
	    Blackjack.split_dealer_win = false;
	    Blackjack.split_hand_one_win = false;
	    Blackjack.split_hand_two_win = false;
	    //PLAYER SPLIT
	    Blackjack.split_detected = false;
	    
	    game.score_fake=0;
	    game.split_animate_fake_score = 0;
	    game.split_animate_score_finished = false;
	    game.update_splitscreen_score_once = false;
	    Blackjack.home_screen_viewed = false;
	    game.dd_split_one_last_bet = 0;
	    game.dd_split_two_last_bet = 0;
	}
	
	private void autoChipAdd(){

		//if closed tab with no bet clicked 5 added
		if(game.chipArray.chipsInPlay.size() < 1 && Blackjack.player_one_last_bet_made == 5){
			//game.chipArray.addChip(5);
		}else{
		
			int lastbetmade = Blackjack.player_one_last_bet_made;
			int bigchipclick = game.chipArray.getChipTotal();
			int addchips = 0;
			
			if(lastbetmade > bigchipclick){
				addchips = lastbetmade - bigchipclick;
			}else if(lastbetmade < bigchipclick){
				game.chipArray.chipsInPlay.clear();
				game.chipArray.resetChipCount(); //RESET CHIP STACK COUNT
				addchips = lastbetmade;
			}
			
			System.out.println("");
			System.out.println("- - - - - - - - - - - - - - - ");
			System.out.println("last bet made : " + lastbetmade);
			System.out.println("big chip click: " + bigchipclick);
			System.out.println("addchips: " + addchips);
			System.out.println("- - - - - - - - - - - - - - - ");
			System.out.println("");
			
			//check for exact one chip match
			if(lastbetmade == 5 && game.chipArray.chipsInPlay.size() < 1){
				game.chipArray.addChip(5);
			}else if(lastbetmade == 10){
				game.chipArray.addChip(10);
			}else if(lastbetmade == 25){
				game.chipArray.addChip(25);
			}else if(lastbetmade == 50){
				game.chipArray.addChip(50);
			}else if(lastbetmade == 100){
				game.chipArray.addChip(100);
			}else{
				
				generateChips(addchips);
			}
			
			
		}
	}
	
	
	
	private void generateChips(int d){
		
		//if chips cleared use total
		if(game.chipArray.chipsInPlay.size() < 1){
			d = Blackjack.player_one_last_bet_made;
		}
		
		//fake it
		int r1 = game.randInt(1, 2);
		int r2 = game.randInt(1, 3);
		
		//how many 100s
		int newhundreds=0;
		int onehuns = 0;
		
		if(d == 100){ 
			onehuns = 1;
		}else{
			onehuns = d / 100;
		}
		
		if(onehuns > 0){
			newhundreds = onehuns * 100;
		}
		d = Blackjack.player_one_last_bet_made-newhundreds;
		
		
		System.out.println("onehuns: "+onehuns);
		System.out.println("d: "+d);
		
		
		//add hundreds
		for(int i=0;i<onehuns;i++){
			game.chipArray.addChip(100);
		}
		System.out.println("chips array size: " + game.chipArray.chipsInPlay.size());
		
		
		
				// * * * little ones
		
				//add 5
				if(d == 5){
					game.chipArray.addChip(5);
				}
				
				//add 10
				if(d == 10){
					switch(r1){
					case 1:
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(10);
						break;
					}
				}
				
				//add 15
				if(d == 15){
					switch(r1){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				//add 20
				if(d == 20){
					switch(r2){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(10);
						break;
					case 3:
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				//add 25
				if(d == 25){ game.chipArray.addChip(25);}
				
				
				//add 30
				if(d == 30){
					switch(r1){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				
				//add 35
				if(d == 35){
					switch(r1){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						break;
					}
				}
				
				
				//add 40
				if(d == 40){
					switch(r1){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					}
				}
		
		
				if(d == 45){
					switch(r1){
					case 1:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(25);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 50){
					switch(r1){
					case 1:
						game.chipArray.addChip(50);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						break;
					}
				}
				
				if(d == 55){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 60){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 65){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						game.chipArray.addChip(10);
						break;
					}
				}
				
				if(d == 70){
					switch(r1){
					case 1:
						game.chipArray.addChip(50);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(10);
						game.chipArray.addChip(25);
						game.chipArray.addChip(5);
						game.chipArray.addChip(10);
						break;
					}
				}
				
				if(d == 75){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(25);
						break;
					}
				}
				
				if(d == 80){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(25);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 85){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						break;
					}
				}
				
				if(d == 90){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 95){
					switch(r1){
					case 1:
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					case 2:
						game.chipArray.addChip(50);
						game.chipArray.addChip(25);
						game.chipArray.addChip(10);
						game.chipArray.addChip(5);
						game.chipArray.addChip(5);
						break;
					}
				}
				
				if(d == 100){
					switch(r1){
					case 1:
						game.chipArray.addChip(50);
						game.chipArray.addChip(50);
						break;
					case 2:
						game.chipArray.addChip(100);
						break;
					}
				}
				
			
		
		
		

	}
	
	
	private void chipClick(Vector3 t,int open_x,int chip_wh,int chipy,int what_chip){
    	if( (t.x > open_x && t.x < chips_open_x + chip_wh) &&
        		(t.y > chipy && t.y < chipy + chip_wh)){
           
           this.chip_value = what_chip;
           Blackjack.player_one_last_bet_made += this.chip_value;
           
           if(Blackjack.player_one_last_bet_made < Blackjack.player_one_total){
           
	           switch(what_chip){
		           case 5:
			           this.resetClickChips();
			           this.c5_clicked = true;
			           this.checkIfAnythingClicked(true);//run check to reset
			           game.chipArray.addChip(5);
				   	   if(Blackjack.play_sounds){game.poker_chips111.play();}
		           break;
		           case 10:
		        	   this.resetClickChips();
			           this.c10_clicked = true;
			           this.checkIfAnythingClicked(true);//run check to reset
			           game.chipArray.addChip(10);
			           if(Blackjack.play_sounds){game.poker_chips111.play();}
		           break;
		           case 25:
		        	   this.resetClickChips();
			           this.c25_clicked = true;
			           this.checkIfAnythingClicked(true);//run check to reset
			           game.chipArray.addChip(25);
			           if(Blackjack.play_sounds){game.poker_chips111.play();}
		           break;
		           case 50:
		        	   this.resetClickChips();
			           this.c50_clicked = true;
			           this.checkIfAnythingClicked(true);//run check to reset
			           game.chipArray.addChip(50);
			           if(Blackjack.play_sounds){game.poker_chips111.play();}
		           break;
		           case 100:
		        	   this.resetClickChips();
			           this.c100_clicked = true;
			           this.checkIfAnythingClicked(true);//run check to reset
			           game.chipArray.addChip(100);
			           if(Blackjack.play_sounds){game.poker_chips111.play();}
		           break;   
	           }
           }else{
        	   
        	   game.actionResolver.showToast("You only have " + Blackjack.player_one_total + " in chips.",5000);
           }
           
           //no bets above your bank
    	   if(Blackjack.player_one_last_bet_made > Blackjack.player_one_total){
    		   Blackjack.player_one_last_bet_made = Blackjack.player_one_total;
    	   }
           
        }
	}
	
	//animated chips check
	private void checkIfAnythingClicked(boolean ud){
		
		//if reclick bet after bet clear everything
        if(Blackjack.chipsAnimationFinished && game.chipArray.chipsInPlay.size() > 0){
     	   Blackjack.chipsAnimationFinished = false;
     	   //game.chipArray.chipsInPlay.clear();
        }
        
		
	}
	
	private void betClick(Vector3 t,int open_x,int wh,int by,boolean ud){
		
		
    	if( (t.x > open_x && t.x < open_x + wh) &&
        		(t.y > by && t.y < by + wh)){
    		
    		if(Blackjack.play_sounds){game.poker_chips111.play();}
           
    	   if(ud){// up/down
    		   
    		   if(Blackjack.player_one_last_bet_made < Blackjack.player_one_total){
	    		   Blackjack.player_one_last_bet_made += this.chip_value;
	    		   this.checkIfAnythingClicked(true);//run check to reset
	    		   Blackjack.chipsAnimationFinished = true;
    		   }else{
    			   game.actionResolver.showToast("You only have " + Blackjack.player_one_total + " in chips.",5000);
    		   }
    		   
    	   }else{
    		   Blackjack.player_one_last_bet_made -= this.chip_value;
    		   this.checkIfAnythingClicked(false);//run check to reset
    		   Blackjack.chipsAnimationFinished = true;
    		   //if(this.c5_clicked)game.chipArray.deleteChip(5);
    		   //if(this.c10_clicked)game.chipArray.deleteChip(10);
    		   //if(this.c25_clicked)game.chipArray.deleteChip(25);
    		   //if(this.c50_clicked)game.chipArray.deleteChip(50);
    		   //if(this.c100_clicked)game.chipArray.deleteChip(100);
    	   }
    	   
    	   if(Blackjack.player_one_last_bet_made < default_bet){
    		   Blackjack.player_one_last_bet_made = default_bet;
    	   }else if(Blackjack.player_one_last_bet_made > this.bet_limit){
    		   Blackjack.player_one_last_bet_made = this.bet_limit;
    	   }
           
    	   
    	   //no bets above your bank
    	   if(Blackjack.player_one_last_bet_made > Blackjack.player_one_total){
    		   Blackjack.player_one_last_bet_made = Blackjack.player_one_total;
    	   }
        }
	}

	private void addBetMenu(){
	    //game.ui_sprites[17].draw(game.batch);
		
		if(!game.bet_menu_state){
			game.bet_menu_bigger2.draw(game.batch);
		}else{
			game.bet_menu_bigger2_close.draw(game.batch);
		}
	}
	
	private void addChips(){
		
		game.ui_sprites[42].setPosition(chips_open_x, chip5y);//5 glow
		game.ui_sprites[39].setPosition(chips_open_x, chip10y);//10 glow
		game.ui_sprites[40].setPosition(chips_open_x, chip25y);//25 glow
		game.ui_sprites[41].setPosition(chips_open_x, chip50y);//50 glow
		game.ui_sprites[38].setPosition(chips_open_x, chip100y);//100 glow
		
		if(!this.c5_clicked)game.ui_sprites[24].draw(game.batch);
		else game.ui_sprites[42].draw(game.batch);
		
		if(!this.c10_clicked)game.ui_sprites[25].draw(game.batch);//10
		else game.ui_sprites[39].draw(game.batch);//10 glow
		
		if(!this.c25_clicked)game.ui_sprites[26].draw(game.batch);//25
		else game.ui_sprites[40].draw(game.batch);//25 glow
		
		if(!this.c50_clicked)game.ui_sprites[27].draw(game.batch);//50
		else game.ui_sprites[41].draw(game.batch);//50 glow
		
		if(!this.c100_clicked)game.ui_sprites[28].draw(game.batch);//100
		else game.ui_sprites[38].draw(game.batch);//100 glow
		
	}
	private void addControls(){
	    game.ui_sprites[35].draw(game.batch);
	    game.ui_sprites[36].draw(game.batch);
	    game.ui_sprites[37].draw(game.batch);
	}
	
	
	private void addRegisteredSpritesFromSplit(){
    	
    	int ui_sr = game.ui_sprites_register.length;
    	int copy_sr = game.copy_sprites_register.length;
    	
    	for(int i=0;i<ui_sr;i++){
    		if(game.ui_sprites_register[i] == 1){
    			game.ui_sprites[i].draw(game.batch);
    		}
    	}
    	
    	c.getSprite().draw(game.batch);//hack
    	redrawStaticCardsSplit();
    	
    	
    	drawDealerCardsSplit();
    	
    	//white square
    	for(int i=0;i<ui_sr;i++){
    		if(game.ui_sprites_register[i] == 2){
    			if(checkPushEnd()){
    				//probably need to remove
    				//game.ui_sprites_register[15]=0;//split one background
    				//game.ui_sprites_register[23]=0;//split two background
    			}
    			game.ui_sprites[i].draw(game.batch);
    		}
    	}
    	
    	for(int i=0;i<copy_sr;i++){
    		if(game.copy_sprites_register[i] == 1){
    			if(checkPushEnd()){
    				//probably need to remove
    				//game.copy_sprites_register[16]=0;// s1 win
    				//game.copy_sprites_register[21]=0;// s2 win
    			}
    			game.copy_sprites[i].draw(game.batch);
    		}
    	}
    	

    	game.dealer_score_sprites[Blackjack.split_dealer_score].draw(game.batch);
    	game.player_score_sprites[Blackjack.split_hand_one_score].draw(game.batch);
    	game.split_score_sprites[Blackjack.split_hand_two_score].draw(game.batch);
    	
    	
	}

	private boolean checkPushEnd(){
		boolean result = false;
		if(Blackjack.split_hand_one_score == Blackjack.split_dealer_score ||
				Blackjack.split_hand_two_score == Blackjack.split_dealer_score){
			result = true;
		}
		return result;
	}
	
	private void addRegisteredSprites(){
    	
    	int ui_sr = game.ui_sprites_register.length;
    	int copy_sr = game.copy_sprites_register.length;
    	int dealer_score_sr = game.dealer_score_sprites_register.length;
    	int player_score_sr = game.player_score_sprites_register.length;
    	
    	
    	for(int i=0;i<ui_sr;i++){
    		if(game.ui_sprites_register[i] == 1){
    			game.ui_sprites[i].draw(game.batch);
    		}
    	}
    	
    	redrawStaticCards();
    	
    	
    	//white square
    	for(int i=0;i<ui_sr;i++){
    		if(game.ui_sprites_register[i] == 2){
    			game.ui_sprites[i].draw(game.batch);
    		}
    	}
    	
    	drawDealerCards();
    	
    	for(int i=0;i<copy_sr;i++){
    		if(game.copy_sprites_register[i] == 1){
    			game.copy_sprites[i].draw(game.batch);
    		}
    	}
    	//push
    	if(Blackjack.is_push){
    		//why is this here ? :: because push copy is the only copy that
    		//exists at the same time on screen ...
    		game.addPush(0,0,0);
    		game.addPush(1,0,0);
    	}
    	for(int i=0;i<dealer_score_sr;i++){
    		if(game.dealer_score_sprites_register[i] == 1){
    			game.dealer_score_sprites[i].draw(game.batch);
    		}
    	}
    	for(int i=0;i<player_score_sr;i++){
    		if(game.player_score_sprites_register[i] == 1){
    			game.player_score_sprites[i].draw(game.batch);
    		}
    	}
    	
    	
    	
	}
	
	private void redrawStaticCards(){
    	int cip = Blackjack.cards_in_play_before_dealer.size();
    	for(int i=0;i<cip;i++){
        	Card card = Blackjack.cards_in_play_before_dealer.get(i);
        	card.getSprite().setPosition(card.getX(), card.getY());
        	card.getSprite().draw(game.batch);
        }
    	
    	
    }
	
	private void redrawStaticCardsSplit(){
		
		int s1 = Blackjack.split_one.size();
    	for(int i=0;i<s1;i++){
    		
        	Card card = Blackjack.split_one.get(i);
        	card.getSprite().setPosition(card.getX(), card.getY());
        	card.getSprite().draw(game.batch);
        }
    	
    	int s2 = Blackjack.split_two.size();
    	
    	for(int i=0;i<s2;i++){
        	Card card = Blackjack.split_two.get(i);
        	card.getSprite().setPosition(card.getX(), card.getY());
        	card.getSprite().draw(game.batch);
        }
    	
    	
    }
	
	private void drawDealerCards(){
    	int dcs = Blackjack.dealer_cards.size();
    	for(int i=0;i<dcs;i++){
    		if(i > 1){
	        	Card card = Blackjack.dealer_cards.get(i);
	        	card.getSprite().draw(game.batch);
    		}
        }
    	
    	
    }
	
	private void drawDealerCardsSplit(){
    	
		int dcs = Blackjack.dealer_cards.size();
    			
    	for(int i=0;i<dcs;i++){
    		if(i==1){
    			//dealer card one
    	        Blackjack.cards_in_play_before_dealer.get(1).getSprite().draw(game.batch);
    		}
	        	Card card = Blackjack.dealer_cards.get(i);
	        	card.getSprite().draw(game.batch);
    		
        }
    }
	
	
	private final TweenCallback open_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
				case START:
        			game.bet_menu_state = true;
        			break;
                case COMPLETE:
                		game.bet_closed_clicked = false;
                		if(Blackjack.play_sounds){
                			game.pokersoundroom.play();
                		}
                	break;
				}
				}

	};
	
	private final TweenCallback close_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				switch (type) {
				case START:
            			game.bet_closed_clicked = true;
            	break;
                case COMPLETE:
                		game.bet_menu_state = false;
                		if(Blackjack.play_sounds){
                			game.pokersoundroom.stop();
                		}
                		//game.bet_menu_bigger2_close.setAlpha(0.0f);
                		//sendChips();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
