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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
//import com.badlogic.gdx.utils.Timer;
//import com.badlogic.gdx.utils.Timer.Task;
import com.wileynet.blackjack.accessors.SpriteAccessor;

public class SplitScreen implements Screen {
	
	final Blackjack game;
	Timeline cs;
	private final TweenManager tweenManager;
	private boolean first_hit_done = false;

    OrthographicCamera camera;
    
    //buttons ( hit / stand ) //they both use same callback :complete  hit_callback
    private boolean click_once = false;
    
    
    Sprite dealer_card1;
    Sprite player_card1;
    Sprite player_card2;
    
    int move_up = 190;
    
    int sp1_score_pos_x = 46;
    int sp1_score_pos_y = 435;
    int sp2_score_pos_x = sp1_score_pos_x;
    int sp2_score_pos_y = sp1_score_pos_y - move_up;
    int d_score_pos_x = sp1_score_pos_x + move_up;
    int d_score_pos_y = sp1_score_pos_y + move_up;
    
    private boolean cards_moved = false;
    private boolean hit_moved = false;
    private boolean flipped = false;
	
	public SplitScreen(final Blackjack gam){
		Blackjack.screen_state = 6;
		
		this.game = gam;
		
		tweenManager = new TweenManager();
		Gdx.input.setCatchBackKey(true);
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
		
        game.split_animate_fake_score = Blackjack.player_one_total;
        game.score_fake = game.split_animate_fake_score;
        
        //animate
		//face down card
    	final int face_card_x = game.face_card.getX();
    	final int face_card_y = game.face_card.getY();
    	//dealer card 1
        dealer_card1 = Blackjack.cards_in_play_before_dealer.get(1).getSprite();
        final int dc1_x = Blackjack.cards_in_play_before_dealer.get(1).getX();
        final int dc1_y = Blackjack.cards_in_play_before_dealer.get(1).getY() + move_up;
        //player card 1
        player_card1 = Blackjack.cards_in_play_before_dealer.get(0).getSprite();
        final int pc1_x = Blackjack.cards_in_play_before_dealer.get(0).getX();
        //player card 2
        player_card2 = Blackjack.cards_in_play_before_dealer.get(2).getSprite();
        final int pc2_y = Blackjack.cards_in_play_before_dealer.get(2).getY() + move_up;
        
        //called one time
        if(!game.play_mode){ //set in game.addFirstSplitHit();

        	game.resetSpriteRegisters();
	            	
	            	//set xy destination
	            	game.face_card.setX(face_card_x);
	            	game.face_card.setY(face_card_y + move_up);
	            	dealer_card1.setX(dc1_x);
	            	dealer_card1.setY(dc1_y);
	            
			        cs = Timeline.createSequence();       	
			    	
			    	cs.push(Tween.to(game.face_card.getSprite(), SpriteAccessor.POS_XY,0.2f)
			    	.target(face_card_x,face_card_y + move_up)
			    	.ease(Circ.OUT));
			    	    	         	
			    	cs.push(Tween.to(dealer_card1, SpriteAccessor.POS_XY,0.2f)
			    	.target(dc1_x,dc1_y)
			    	.ease(Circ.OUT));
			    	
			    	cs.push(Tween.to(player_card2, SpriteAccessor.POS_XY,0.2f)//player card 2 move
					.target(pc1_x,pc2_y)//player card 1 x value
					.ease(Circ.OUT));
			    	
			    	cs.setCallback(first_move_callback);
			        cs.setCallbackTriggers(TweenCallback.COMPLETE);
			    	cs.start(tweenManager);
    	       
	        
	        
        }else{
        	
        	
        	if(!Blackjack.split_dealer_flip){
        		
	        	Card hitcard=null;
	        	Card previous_card=null;
	        	
	        	if(Blackjack.what_split_hand == 1){
		        	//SPLIT ONE
		        	int sp1 = Blackjack.split_one.size();
		        	hitcard = Blackjack.split_one.get(sp1 -1);//get the last card added
					previous_card = Blackjack.split_one.get(sp1 -2);
	        	}else if(Blackjack.what_split_hand == 2){
	        		//SPLIT TWO
		        	int sp2 = Blackjack.split_two.size();
		        	hitcard = Blackjack.split_two.get(sp2 -1);//get the last card added
					previous_card = Blackjack.split_two.get(sp2 -2);
	        	}
				
				
	    		float rotate_card = 0;// 0 no rotate
	    		int previous_y = previous_card.getY();
	    		int previous_x = previous_card.getX() + 33;
	    		/*SETTINGS*/
	    		if(!Blackjack.inline_card_deal){
	    			rotate_card = game.randFloat(-1.9f, 3.9f);
	    		}/*SETTINGS*/
	        	
	    		hitcard.setX(previous_x);
	    		hitcard.setY(previous_y);
	    		
	        	cs = Timeline.createSequence();
	        	
	        	//split one
	        		
	        		
	    		//first
	        	//set scale bigger
	        	cs.push(Tween.set(hitcard.getSprite(), SpriteAccessor.SCALE_XY)
	        			.ease(Circ.IN)
	                    .target(1.4f, 1.4f));
	        		
	        	//rotate it
	        	cs.push(Tween.set(hitcard.getSprite(), SpriteAccessor.ROTATION)
	                    .target(rotate_card));
	        		
	                   
	        		
	        	//animate
	            cs.push(Tween.to(hitcard.getSprite(), SpriteAccessor.POS_XY, 0.1f)
	            		.delay(0.2f)
	            		.ease(Circ.OUT)
	            		.target(previous_x, previous_y))
	            			
	            		.beginParallel()
	            			
	            //scale it back to normal
	            .push(Tween.to(hitcard.getSprite(), SpriteAccessor.SCALE_XY, 0.1f)
	            .target(1, 1)).end();
	    		
		    	
		    	cs.setCallback(hit_callback);
				cs.setCallbackTriggers(TweenCallback.START | 
						TweenCallback.COMPLETE);
				
				
				
    	        	cs.start(tweenManager);

    	        	
		    	
        	}else{
        		
        		
        		//flip it
        		dealerFlip();
        		
        		
    	            	
        		
		        		////////////////////////////DEALERS TURN //////////////////////////////////
		        		if(Blackjack.what_split_hand == 0 ){//you shouldn't be here if it wasnt'
		        		
		
						game.playerOneSplit.gamelogic().dealerHand(Blackjack.dealer_score,
								Blackjack.current_deck_array_index);
						
						
						if(Blackjack.dealer_cards.size() < 3){
							game.updateGameStateSplit();
						}
						
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
						   		int dealer_y = game.dealer_card_dealt_y + move_up;
						   		if(!Blackjack.inline_card_deal){
						   			rotate_card = game.randFloat(-2.0f, 2.9f);
						   			dealer_y = game.randInt(game.dealer_card_dealt_y + 180,
						   					game.dealer_card_dealt_y + 190);
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
						   
						   
						   float delay = 0.5f; // seconds
	                        Timer.schedule(new Task(){
	                            @Override
	                            public void run() {
	                            	cs.start(tweenManager);
	                            }
	                       }, delay);
	                        
	                        
						  }
						}
		        	    ////////////////////////////////////////////////////////////////////////////
        		
        		
        	}
        	
        } 	
		
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
                            	Blackjack.dealer_done = true; //
                            	game.updateGameStateSplit();
                            	
								////////////////////////////////////////////////////////////SPLIT SCORE SCREEN  	
								game.setScreen(new SplitScoreScreen(game));
								dispose();
                            }
                        }, delay);   	
                            	
                        
		                
                    	break;
    				}
    				}
 
	};
	
	
	private void dealerFlip(){
	        	
			cs = Timeline.createSequence();
			    	        	
			cs.push(Tween.to(game.face_card.getSprite(), SpriteAccessor.POS_XY,0.2f)
		    .delay(0.2f)
			.target(111,500)
			.ease(Circ.OUT))
			.beginParallel()
			.push(Tween.set(game.face_card.getSprite(), SpriteAccessor.SIZE_XY)
			.target(0,0)).end();
			    	    	      	        	
			cs.setCallback(dealer_flip_callback);
			cs.setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START);
			cs.start(tweenManager);
			        
			if(flipped){
			     Card card = Blackjack.cards_in_play_before_dealer.get(3);
			     cs = Timeline.createSequence();
			     cs.push(Tween.to(card.getSprite(), SpriteAccessor.SIZE_XY, 0.4f)
			     .target(Blackjack.card_width,Blackjack.card_height));
			     cs.start(tweenManager);
			}
        
	}
	
	
	private final TweenCallback dealer_flip_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                    flipped = true;
                    
                    if(Blackjack.dealer_cards.size() < 3){
                    	Blackjack.dealer_done = true;//!~
                    	game.updateGameStateSplit();
                    	/////////////////////////////////////////////// SPLIT SCORE SCREEN
                    	game.setScreen(new SplitScoreScreen(game));
		                dispose();
                    	
                    }
                	break;
				case START:
					//game.cardFlipSound.play();
                	break;
				}
		}

	};
	
	
	private void addFirstHit(){
		
		Card hitcard=null;
    	Card previous_card=null;
    	
    	if(Blackjack.what_split_hand == 1){
    		//SPLIT ONE
        	int sp1 = Blackjack.split_one.size();
    		//int sp2 = Blackjack.split_two.size();
        	hitcard = Blackjack.split_one.get(sp1 -1);//get the last card added
    		previous_card = Blackjack.split_one.get(sp1 -2);
    	}else if(Blackjack.what_split_hand == 2){
    		//SPLIT TWO
        	int sp1 = Blackjack.split_two.size();
    		//int sp2 = Blackjack.split_two.size();
        	hitcard = Blackjack.split_two.get(sp1 -1);//get the last card added
    		previous_card = Blackjack.split_two.get(sp1 -2);
    	}else{
    		//System.out.println("________________ERROR_________________" + Blackjack.what_split_hand);
    	}
    	
		
		float rotate_card = 0;// 0 no rotate
		int previous_y = previous_card.getY() + move_up;
		int previous_x = previous_card.getX();
		/*SETTINGS*/
		if(!Blackjack.inline_card_deal){
			rotate_card = game.randFloat(-1.9f, 3.9f);
		}/*SETTINGS*/
    	
		//set xy destination
		hitcard.setX(previous_x);
		hitcard.setY(previous_y);
		
    	cs = Timeline.createSequence();
    	
    	//split one
    		
    		
		//first
    	//set scale bigger
    	cs.push(Tween.set(hitcard.getSprite(), SpriteAccessor.SCALE_XY)
    			.ease(Circ.IN)
                .target(1.4f, 1.4f));
    		
    	//rotate it
    	cs.push(Tween.set(hitcard.getSprite(), SpriteAccessor.ROTATION)
                .target(rotate_card));
    		
               
    		
    	//animate
        cs.push(Tween.to(hitcard.getSprite(), SpriteAccessor.POS_XY, 0.1f)
        		.delay(0.2f)
        		.ease(Circ.OUT)
        		.target(previous_x, previous_y))
        			
        		.beginParallel()
        			
        //scale it back to normal
        .push(Tween.to(hitcard.getSprite(), SpriteAccessor.SCALE_XY, 0.1f)
        .target(1, 1)).end();
		
    	
    	cs.setCallback(hit_callback);
		cs.setCallbackTriggers(TweenCallback.START | 
				TweenCallback.COMPLETE);
    	cs.start(tweenManager);
	}
	
	
	
	private final TweenCallback first_move_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				switch (type) {
	                case COMPLETE:
	                				
	                				//Blackjack.what_split_hand = 1;
	                				game.addFirstSplitHit();//add first hit card to array
	            	            	addFirstHit();//animate it
	            	            	cards_moved = true;

	            	        break;
					}
				}

	 };
	
	
	
		
		
		
		private final TweenCallback hit_callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
					
				
					switch (type) {
	                case START:	
	            	        
	                	    //
	                		
		            	break;
	                case COMPLETE:
	                	
	                	
	                	//click_once = true;
	                	//first_hit_done = true;
	                	//hit_moved = true;
                	    game.updateGameStateSplit();
                	    if(Blackjack.play_sounds){
                	    	game.dealCardSound.play();
                	    }
	                	
		                
		                //set here and stand
                		if(Blackjack.what_split_hand == 1){
					        		
				                	if(Blackjack.split_hand_one_score > 21 || 
				                			Blackjack.split_hand_one_score == 21 ||
				                			game.dd_clicked_one){//bust,win
				                		
				                		
				                		float delay = 0.5f; // seconds
				                        Timer.schedule(new Task(){
				                            @Override
				                            public void run() {
						                		Blackjack.what_split_hand = 2;
						                		game.reinitializeSplitTwo();
						                		game.deal();
						        	  	    	game.updateGameStateSplit();
								                game.setScreen(new SplitScreen(game));
								                dispose();
				                            }
				                        }, delay);
						                
					            	     	
				                	}
				        }
                		
                		if(Blackjack.what_split_hand == 2){
				        	
                			if(!game.update_splitscreen_score_once){
                	        	//update current_fake_score based on hand
                	    		if(Blackjack.split_hand_one_score > 21){
                	    			if(!game.dd_clicked_one){
                	    				game.score_fake -= Blackjack.player_one_last_bet_made;
                	    			}else{
                	    				game.score_fake -= Blackjack.player_one_last_bet_made * 2;
                	    			}
                	    		}
                	    		
                	    		if(Blackjack.split_hand_one_score == 21){
                	    			if(!game.dd_clicked_one){
                	    				//game.score_fake += Blackjack.player_one_last_bet_made;
                	    			}else{
                	    				//game.score_fake += Blackjack.player_one_last_bet_made * 2;
                	    			}
                	    			System.out.println("HAND ONE WIN");
                	    		}
                	    		
                	    		game.update_splitscreen_score_once = true;
                        	}
					        
				        	if(Blackjack.split_hand_two_score > 21 || 
		                			Blackjack.split_hand_two_score == 21 ||
		                			game.dd_clicked_two){//bust,win
				                	
				        		System.out.println(">>>>>");
				        		System.out.println("split_hand_two_score: " + Blackjack.split_hand_two_score);
				        		System.out.println(">>>>>");
				                		
				                		float delay = 0.5f; // seconds
				                        Timer.schedule(new Task(){
				                            @Override
				                            public void run() {
						                		Blackjack.what_split_hand = 0;
						                		Blackjack.split_detected = false;
						                    	Blackjack.split_dealer_flip = true;
						                    	Blackjack.what_split_hand = 0; //////////////////// <----- * * *
						                    	game.reinitializeSplitTwo();
						        	  	    	game.updateGameStateSplit();
								                game.setScreen(new SplitScreen(game));
								                dispose();
				                            }
				                        }, delay);
						                
					            	    
				                	}
				         }
                		
                		
                		float delay = 0.5f; // seconds
                        Timer.schedule(new Task(){
                            @Override
                            public void run() {
                            	
		                		click_once = true;
			                	first_hit_done = true;
			                	hit_moved = true;
                            }
                        }, delay);
                        
		                
	                	break;
	                	
						  }
					}
			};
	
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        tweenManager.update(Gdx.graphics.getDeltaTime());

        // tell the camera to update its matrices.
        camera.update();
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
        game.addGreenBackground();
        game.c_control_panel_back_bottom.draw(game.batch);
        game.c_control_panel_back_top.draw(game.batch);
        //game.homebutton.draw(game.batch);
        
        
        //real score
        if(Blackjack.what_split_hand == 1){
        	game.arialblack2.draw(game.batch, ""+Blackjack.player_one_total, game.total_numbers_x , 
	        		game.total_numbers_y);
        }else{
        	animateScore();
            game.arialblack2.draw(game.batch, ""+game.split_animate_fake_score, game.total_numbers_x ,game.total_numbers_y);
        }	
        

        
        
        if(Blackjack.what_split_hand == 1 && hit_moved && !flipped){
        	game.addSplitHighlight(43, 333);
        	if(first_hit_done && Blackjack.split_one.size() < 3){
        		//add double down for everything
	    	    game.ui_sprites[44].setPosition(game.doubledown_x, game.doubledown_y + 188);
	    	    game.ui_sprites[44].draw(game.batch);
        	}
    	    
        }else if(Blackjack.what_split_hand == 2 && !flipped){//2
        	game.addSplitHighlight(43, 333 - move_up);
        	if(first_hit_done && Blackjack.split_two.size() < 3){
        		//add double down for everything
	    	    game.ui_sprites[44].setPosition(game.doubledown_x, game.doubledown_y);
	    	    game.ui_sprites[44].draw(game.batch);
        	}
        }
        
        
        //backgrounds
        if(hit_moved){
        	if(Blackjack.what_split_hand == 1){
        		drawBackgrounds(1);
        	}else if(Blackjack.what_split_hand == 2){
        		//drawBackgrounds(2);
        	}
        	
        }
        
        if(flipped){
    		game.addSplitHighlight(43, 533);
    	}
        
        //dealer done
        if(Blackjack.dealer_done){
        	//dealer
        	game.addScoreBackground(0,endDealerBackground(),42,d_score_pos_y+3);
        	drawDealerCards();
        	drawDealerScores(42,d_score_pos_y+3);
        	
        }else{
        	drawDealerCards();
        	//draw hit cards

        }
        

        player_card1.draw(game.batch);
        player_card2.draw(game.batch);
        //draw flipped card
        if(flipped){
        	Card card = Blackjack.cards_in_play_before_dealer.get(3);
        	card.getSprite().setPosition(138,540);
        	card.getSprite().draw(game.batch);
        	drawNewDealerCards();
        	if(Blackjack.dealer_done){ endDealerCopy(); }
        	
        }
        
        if(Blackjack.dealer_done){
    		//
    	}else{
    		//
    	}
       
        
        if(game.play_mode){//set here when hit button clicked
        	
        	if(hit_moved){ this.addRegisteredSprites(0); }
        	
        	if(Blackjack.dealer_done){ 
        		game.addScoreBackground(1,endSplitOneBackground(),45, sp1_score_pos_y);
        	}
        	
        	redrawPlayerOneCards();
        	if(Blackjack.dealer_done){ drawSplitOneScores();}
        	if(Blackjack.dealer_done){ endSplitOneCopy();}
        	
        	
        	if(Blackjack.dealer_done){ game.addScoreBackground(2,
        			endSplitTwoBackground(),45,sp2_score_pos_y);}
        	
        	redrawPlayerTwoCards();
        	if(Blackjack.dealer_done){ drawSplitTwoScores();}
        	if(Blackjack.dealer_done){ endSplitTwoCopy();}
        	
        }
        
        if(hit_moved){
        	
        	this.addRegisteredSprites(1);
        	this.addRegisteredSprites(2);
		
        	if(Blackjack.what_split_hand == 1)drawBust();
        	if(Blackjack.what_split_hand == 1)drawWin();
        	drawSplitOneScores();
        	drawSplitTwoScores();
        
        	//add controls
        	if(cards_moved || hit_moved){
        		game.addHitStand();
        	}
        }
    	
    	if(Blackjack.dealer_done){
            game.addDealBet();
            game.addBetTab();
    	}
    	
    	
    	//draw bet chips
        game.drawAnimatedChips();
        
        //add bet
        //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
        
        game.batch.end();
        
        
        
        //input
	    if (Gdx.input.justTouched()) {
	    	
	    	Vector3 touchpoint = new Vector3();
            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpoint);
            
	    	if(!Blackjack.dealer_done){
	            	
	    		
	    		//doubledown 1
	    		if(Blackjack.what_split_hand == 1 && hit_moved && !flipped){
	            	
	            	if(first_hit_done){
			            if( (touchpoint.x > game.doubledown_x && touchpoint.x < game.doubledown_x + 94) &&
			            		touchpoint.y > (game.doubledown_y + 188) && touchpoint.y < (game.doubledown_y + 188) + 54){
			            	   
			            	   game.doubledown_active_one = true;
			            	   game.dd_clicked_one = true;
			            	   
			            	   Blackjack.split_detected = false;
			            	   game.play_mode = true;
				  	    	   game.deal();
				  	    	   game.updateGameStateSplit();
				  	    	   game.setScreen(new SplitScreen(game));
				  	    	   dispose();
				  	    	   
			            }
	            	}
	    		}
	    		
	    		//doubledown 2
	    		if(Blackjack.what_split_hand == 2 && hit_moved && !flipped){
	            	
	            	if(first_hit_done){
			            if( (touchpoint.x > game.doubledown_x && touchpoint.x < game.doubledown_x + 94) &&
			            		touchpoint.y > (game.doubledown_y) && touchpoint.y < (game.doubledown_y) + 54){
			            	   
			            	   game.doubledown_active_two = true;
			            	   game.dd_clicked_two = true;
			            	   Blackjack.player_one_total = game.split_animate_fake_score;
			            	   
			            	   
			            	   Blackjack.split_detected = false;
			            	   game.play_mode = true;
				  	    	   game.deal();
				  	    	   game.updateGameStateSplit();
				  	    	   game.setScreen(new SplitScreen(game));
				  	    	   dispose();
				  	    	   
			            }
	            	}
	    		}
	    		
	    		
	    		
	            
		            //hit button
		            if( (touchpoint.x > game.hit_button_x && touchpoint.x < game.hit_button_x + 141) &&
		            		(touchpoint.y > 10 && touchpoint.y < 77 ) &&
		            		click_once ){
		            	   
		            	   Blackjack.player_one_total = game.split_animate_fake_score;
		            	   
		            	   Blackjack.split_detected = false;
		            	   game.play_mode = true;
		            	   
			  	    	   game.deal();
			  	    	   game.updateGameStateSplit();
			  	    	   game.setScreen(new SplitScreen(game));
			  	    	   dispose();
		            }
		        
		       
	            
	            //stand button
	            if( (touchpoint.x > game.stand_button_x && touchpoint.x < game.stand_button_x + 141) &&
	            		(touchpoint.y > 10 && touchpoint.y < 77) &&
	            		click_once){
	            	
	            	Blackjack.player_one_total = game.split_animate_fake_score;
	            	
	            	
	            	if(Blackjack.what_split_hand == 1){
	            		
	            		Blackjack.what_split_hand = 2;
	                	game.deal();
	    	  	    	game.updateGameStateSplit();
	                	game.setScreen(new SplitScreen(game));
	                	dispose();
	            		
	            	}else if(Blackjack.what_split_hand == 2){
	                	Blackjack.split_detected = false;
	                	Blackjack.split_dealer_flip = true;
	                	Blackjack.what_split_hand = 0; //////////////////// <----- * * *
	    	  	    	game.updateGameStateSplit();
	                	game.setScreen(new SplitScreen(game));
	                	dispose();
	            	}
	   	    	    
	            }
	            
	    	}else{
	    		
	    		
	    		//deal button
	            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
	            		(touchpoint.y > 10 && touchpoint.y < 60)){
	            	   
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
	            
	            //bet tab
	            if( (touchpoint.x > 440 && touchpoint.x < 480) &&
	            		(touchpoint.y > 350 && touchpoint.y < 460)){
	               
	               //System.out.println("Bet Clicked");
	               game.setScreen(new BetMenu(game,1));
	  	    	   dispose();
	            }
	            
	            //buy chips
	            if( (touchpoint.x > Blackjack.buychips_x1 && 
	            		touchpoint.x < Blackjack.buychips_x2) &&
	            		(touchpoint.y > Blackjack.buychips_y1 && 
	            				touchpoint.y < Blackjack.buychips_y2)){
	            	
	            	//buy
	            	//game.buyTokens();
			    	
	            }
	    		
	    	}
	    }
		
	    if (Gdx.input.isKeyPressed(Keys.BACK)){
	    	game.actionResolver.killMyProcess();
	    }
	    
	    if(Blackjack.score_is_zero){
	    	//game.buyTokens();
	    	Blackjack.score_is_zero = false;
            Blackjack.player_one_total = 600;
            Blackjack.player_one_total_before_change = 0;
            Blackjack.player_one_last_bet_made = 5;
            Blackjack.close_buybutton = true;
	    }
	}
	
	//for first deal
	private void animateScore(){
		
		int interval = Blackjack.player_one_last_bet_made / 5;
		
		if(game.score_fake < game.split_animate_fake_score){//lose
			game.split_animate_fake_score -= interval;
		}
		if(game.score_fake > game.split_animate_fake_score){//win
			game.split_animate_fake_score += interval;
        }

        if(game.score_fake < 0){
        	game.score_fake = 0;
        }
        
	}

	public void redrawPlayerOneCards(){
		
		int sp1 = Blackjack.split_one.size();
		
	    //split one
		for(int i=0;i<sp1;i++){
			Card card = Blackjack.split_one.get(i);
			card.getSprite().draw(game.batch);
			//System.out.println("one redraw: " + card.getName());
		}
		
	}

	public void redrawPlayerTwoCards(){
	
		int sp2 = Blackjack.split_two.size();
	
		//split two
		for(int i=0;i<sp2;i++){
			Card card = Blackjack.split_two.get(i);
			card.getSprite().draw(game.batch);
			//System.out.println("two redraw: " + card.getName());
		}
	
	}
	

	public int endDealerBackground(){
		
		int background=0;
		boolean push = false;
		boolean p1_bust = false;
		boolean p2_bust = false;
		boolean ds_bust = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps1 = Blackjack.split_hand_one_score;
		int ps2 = Blackjack.split_hand_two_score;
		if(ps1 > 21){ p1_bust = true; }
		if(ps2 > 21){ p2_bust = true; }
		if(ds > 21){ ds_bust = true; }

		
			if(p1_bust && p2_bust && ds < 22){
				background = 2;
			}else{
				
					//check bust
					if(ds > 21){
						background = 3;
					}else{
						//check push first
						if(ds == ps1){
							push = true;
						}else if(ds == ps2){
							push = true;
						}
						//check win
						if(ds > ps1 && !push || ds == 21){
							background = 2;
						}else if(ds > ps2 && !push || ds == 21){
							background = 2;
						}
						//check push
						if(push){
							background = 1;
						}
						//check lose
						if(ds < ps1 && !push && !p1_bust){
							background = 3;
						}else if(ds < ps2 && !push && !p2_bust){
							background = 3;
						}
					}
				}

		
		
		return background;
		
	}
	
	public int endSplitOneBackground(){
		
		int background=0;
		boolean push = false;
		boolean dealer_bust = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps1 = Blackjack.split_hand_one_score;
		if(ds > 21){
			dealer_bust = true;
		}
		
		if(ps1 < 22 && dealer_bust){
			background = 2;
		}else{
			//check bust
			if(ps1 > 21){
				background = 3;
			}else{
				//check push first
				if(ds == ps1){
					push = true;
				}
				//check win
				if(!push){
					if(ps1 > ds || ps1 == 21){
						background = 2;
					}
				}
				//check push
				if(push){
					background = 1;
				}
				//check lose
				if(ps1 < ds && !push && !dealer_bust){
					background = 3;
				}
			}
		}
		
		
		return background;
		
	}
	
	
	public int endSplitTwoBackground(){
		
		int background=0;
		boolean push = false;
		boolean dealer_bust = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps2 = Blackjack.split_hand_two_score;
		if(ds > 21){
			dealer_bust = true;
		}
		
		
		if(ps2 < 22 && dealer_bust){
			background = 2;
		}else{
			//check bust
			if(ps2 > 21){
				background = 3;
			}else{
				//check push first
				if(ds == ps2){
					push = true;
				}
				//check win
				if(!push){
					if(ps2 > ds || ps2 == 21){
						background = 2;
					}
				}
				//check push
				if(push){
					background = 1;
				}
				//check lose
				if(ps2 < ds && !push && !dealer_bust){
					background = 3;
				}
			}
		}
		
		return background;
		
	}
	
	
	public void endDealerCopy(){
		
		//0=dealer
		//1=player1
		//2=player2
		
		boolean push = false;
		boolean p1_bust = false;
		boolean p2_bust = false;
		boolean ds_bust = false;
		boolean p1_win = false;
		boolean p2_win = false;
		boolean d_lose = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps1 = Blackjack.split_hand_one_score;
		int ps2 = Blackjack.split_hand_two_score;
		
		if(ps1 > 21){ p1_bust = true; }
		if(ps2 > 21){ p2_bust = true; }
		if(Blackjack.split_hand_two_win || 
				Blackjack.split_hand_two_21){ p2_win = true; }
		if(Blackjack.split_hand_one_win || 
				Blackjack.split_hand_one_21){ p1_win = true; }
		
		if(Blackjack.split_dealer_lose){d_lose=true;}
		
		int x=42;
		int y=d_score_pos_y-22;
		//int copy=0;
		
			if(p1_bust && p2_bust && ds < 22){
				game.addWin(0, x, y);;
			}else{
				
					//check bust
					if(ds > 21){
						game.addBust(0, x, y);
					}else{
						//check push first
						if(ds == ps1){
							push = true;
						}else if(ds == ps2){
							push = true;
						}
						//check win
						if(Blackjack.split_dealer_win){
							System.out.println(" ***Dealer Win Set");
							game.addWin(0, x, y);
						}else{
							//System.out.println("  ***Dealer Win Not Set");
						}
						
						//check push
						if(push){
							game.addPush(0, x, y);
						}
						//check lose
						if(ds < ps1 && !push && !p1_bust){
							game.addLose(0, x, y);
						}else if(ds < ps2 && !push && !p2_bust){
							game.addLose(0, x, y);
						}
						
					}
				}
			
			
			}


	public void endSplitOneCopy(){
		
		//0=dealer
		//1=player1
		//2=player2
		
		boolean push = false;
		boolean dealer_bust = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps1 = Blackjack.split_hand_one_score;
		if(ds > 21){
			dealer_bust = true;
		}
		
		int x=44;
		int y = sp1_score_pos_y -25;
		
		if(ps1 < 22 && dealer_bust){
			game.addWin(2, x, y);
		}else{
			//check bust
			if(ps1 > 21){
				game.addBust(2, x, y); // ??????
			}else{
				//check push first
				if(ds == ps1){
					push = true;
				}
				//check win
				if(!push){
					if(ps1 > ds || ps1 == 21){
						game.addWin(2, x, y);
					}
				}
				//check push
				if(push){
					game.addPush(2, x, y);
				}
				//check lose
				if(ps1 < ds && !push && !dealer_bust){
					game.addLose(2, x, y);
				}
			}
		}
		
	}
	
	
	public void endSplitTwoCopy(){
		
		//0=dealer
		//1=player1
		//2=player2
		
		boolean push = false;
		boolean dealer_bust = false;
		
		int ds = Blackjack.split_dealer_score;
		int ps2 = Blackjack.split_hand_two_score;
		if(ds > 21){
			dealer_bust = true;
		}
		
		int x=44;
		int y= sp2_score_pos_y -25;
		
		if(ps2 < 22 && dealer_bust){
			game.addWin(1, x, y);
		}else{
			//check bust
			if(ps2 > 21){
				game.addBust(1, x, y);
			}else{
				//check push first
				if(ds == ps2){
					push = true;
				}
				//check win
				if(!push){
					if(ps2 > ds || ps2 == 21){
						game.addWin(1, x, y);
					}
				}
				//check push
				if(push){
					game.addPush(1, x, y);
				}
				//check lose
				if(ps2 < ds && !push && !dealer_bust){
					game.addLose(1, x, y);
				}
			}
		}
		
	}
	
	
	
	public void drawDealerCards(){
        dealer_card1.draw(game.batch);
        game.face_card.getSprite().draw(game.batch);
	}
	public void drawNewDealerCards(){
        int dcs = Blackjack.dealer_cards.size();
        for(int i=0;i<dcs;i++){
        	if(i > 1){
    	        Card card = Blackjack.dealer_cards.get(i);
    	        card.getSprite().draw(game.batch);
        	}
        }
	}
	
	
	public void drawSplitOneScores(){
		//if(Blackjack.split_hand_one_21){
			//game.addScore21(1, sp1_score_pos_x,sp1_score_pos_y);
		//}else{
			game.addScore(1, sp1_score_pos_x,sp1_score_pos_y,
					this.getPlayerScore(1),Blackjack.split_dealer_score);
		//}
	}
	public void drawSplitTwoScores(){
		if(Blackjack.what_split_hand == 2 || Blackjack.what_split_hand == 0){
			//if(Blackjack.split_hand_two_21){
				//game.addScore21(2, sp2_score_pos_x, sp2_score_pos_y);
			//}else{
				game.addScore(2, sp2_score_pos_x,sp2_score_pos_y,
						Blackjack.split_hand_two_score,Blackjack.split_dealer_score);
			//}
		}
	}
	public void drawDealerScores(int x, int y){
		//if(Blackjack.split_dealer_21){
			//game.addScore21(0, d_score_pos_x, d_score_pos_y);
		//}else{
			game.addScore(0, x,y,this.getPlayerScore(2),Blackjack.split_dealer_score);
		//}
	}
	
	
	public void drawBust(){
		//if(Blackjack.what_split_hand == 1){
			if(Blackjack.split_hand_one_bust){
				game.addBust(2, sp1_score_pos_x, sp1_score_pos_y -26);
        	}
		//}else if(Blackjack.what_split_hand == 2){
			if(Blackjack.split_hand_two_bust){
				game.addBust(1, sp2_score_pos_x, sp2_score_pos_y -26);
        	}
		//}
		
	}
	public void drawWin(){
		//if(Blackjack.what_split_hand == 1){
			if(Blackjack.split_hand_one_21){
				//game.addWin(2, sp1_score_pos_x, sp1_score_pos_y -26);
        	}
		//}else if(Blackjack.what_split_hand == 2){
			if(Blackjack.split_hand_two_21){
				game.addWin(1, sp2_score_pos_x, sp2_score_pos_y -26);
        	}
		//}
		
	}
	
	
	public void drawBackgrounds(int hand){
		
		int p_x_pos=0;
		int p_y_pos=0;
		
		if(hand == 1){

			p_x_pos = sp1_score_pos_x;
			p_y_pos = sp1_score_pos_y;
			
			if(Blackjack.split_hand_one_21){
				//game.addScoreBackground(1,2,p_x_pos,p_y_pos);
			}
			if(Blackjack.split_hand_one_bust){
				game.addScoreBackground(1,3,p_x_pos,p_y_pos);
			}
			
		}else if(hand == 2){//2

			p_x_pos = sp2_score_pos_x;
			p_y_pos = sp2_score_pos_y;
			
			if(Blackjack.split_hand_two_21){
				game.addScoreBackground(2,2,p_x_pos,p_y_pos);
			}
			if(Blackjack.split_hand_two_bust){
				game.addScoreBackground(2,3,p_x_pos,p_y_pos);
			}
			
		}
		
		
		/*
		
		//PUSH
        if(is_push){
        	//game.addScoreBackground(0,1,d_score_pos_x,d_score_pos_y);//dealer
    		//game.addScoreBackground(1,1,p_x_pos,p_y_pos);//player
        }else{
        	
        	//WIN
	        if(Blackjack.split_dealer_21){
	        	//game.addScoreBackground(0,2,d_score_pos_x,d_score_pos_y);//dealer
	        }
	        if(player_win){
	        	
	        	game.addScoreBackground(1,2,p_x_pos,p_y_pos);
	        	
	        	//win sound
	        	
	        	if(!player_21 && player_score == 21 &&
	        					Blackjack.dealer_flipped &&
	        						Blackjack.play_win_sound){
	        		
	        		game.winSound.play();
	        		Blackjack.play_win_sound = false;
	        		
	        	
	        }
	        
	        //LOSE
        	if(Blackjack.split_dealer_lose){
        		game.addScoreBackground(0,3,d_score_pos_x,d_score_pos_y);
        	}
        	if(player_lose){
        		game.addScoreBackground(1,3,p_x_pos,p_y_pos);
        	}
        	
        	//BUST
        	if(Blackjack.split_dealer_bust){
        		game.addScoreBackground(0,3,d_score_pos_x,d_score_pos_y);
        	}
        	if(player_bust){
        		game.addScoreBackground(player_who,3,p_x_pos,p_y_pos);
        	}

        }
		
		*/
        
	}
	
	
	
	public int getPlayerScore(int what){
		int ps =0;
		if(what == 1){
			ps = Blackjack.split_hand_one_score;
		}else if(what == 2){
			ps = Blackjack.split_hand_two_score;
		}
		return ps;
	}
	
	
	
	
	
	private void addRegisteredSprites(int what){
    	
		//0 ui_sprites
		//1 white_square
		//2 copy
		//3 push
		
		int ui_sr = game.ui_sprites_register.length;
    	int copy_sr = game.copy_sprites_register.length;
    	int dealer_score_sr = game.dealer_score_sprites_register.length;
    	int player_score_sr = game.player_score_sprites_register.length;
    	int split_score_sr = game.split_score_sprites_register.length;
    	
		switch(what){
			case 0:
				//backgrounds ?
				for(int i=0;i<ui_sr;i++){
		    		if(game.ui_sprites_register[i] == 1){
		    			game.ui_sprites[i].draw(game.batch);
		    		}
		    	}
				break;
			case 1:
				//white square
		    	for(int i=0;i<ui_sr;i++){
		    		if(game.ui_sprites_register[i] == 2){
		    			game.ui_sprites[i].draw(game.batch);
		    		}
		    	}
		    	break;
			case 2:
				for(int i=0;i<copy_sr;i++){
		    		if(game.copy_sprites_register[i] == 1){
		    			game.copy_sprites[i].draw(game.batch);
		    			//System.out.println("copysprite redrawn: " + i);
		    		}
		    	}
				break;
			case 3:
				//push
		    	if(Blackjack.is_push){
		    		//why is this here ? :: because push copy is the only copy that
		    		//exists at the same time on screen ...
		    		game.addPush(0,0,0);
		    		game.addPush(1,0,0);
		    	}
			case 4:
				for(int i=0;i<dealer_score_sr;i++){
		    		if(game.dealer_score_sprites_register[i] == 1){
		    			game.dealer_score_sprites[i].draw(game.batch);
		    		}
		    	}
				break;
			case 5:
				for(int i=0;i<player_score_sr;i++){
		    		if(game.player_score_sprites_register[i] == 1){
		    			game.player_score_sprites[i].draw(game.batch);
		    		}
		    	}
				break;
			case 6:
				for(int i=0;i<split_score_sr;i++){
		    		if(game.split_score_sprites_register[i] == 1){
		    			game.split_score_sprites[i].draw(game.batch);
		    		}
		    	}
				break;
		}
    	
	}
	
	
	
	

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
