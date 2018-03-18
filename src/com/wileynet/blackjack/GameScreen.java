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
import com.wileynet.blackjack.accessors.SpriteAccessor;


//public class GameScreen implements Screen, InputProcessor{
public class GameScreen implements Screen{

        
	    final Blackjack game;
	    
	    private final TweenManager tweenManager;
	    //InputMultiplexer inputMultiplexer;

        OrthographicCamera camera;
        
        static int playonce;
        
        
        int dealer_card_dealt=0;
        int player_card_dealt=0;
        
        
        Timeline cs;
        boolean deal_animate_end = false;
        
       
        //SHOULD BE RENAMED FirstDealScreen
        public GameScreen(final Blackjack gam) {
        	Blackjack.screen_state = 1;
        	
        	Gdx.input.setCatchBackKey(true);
        	
        		this.game = gam;
        		//game.resetSpriteRegisters();
        		
	        		//interstitial ads
	        		if(Blackjack.application_type==1){//android
	        			Blackjack.hand_count++;
	        			if(Blackjack.hand_count == Blackjack.show_ads_per_hands){
	        				if(Blackjack.whatad == 0){
	        					//game.admob.showAd();
	        					//game.actionResolver.openAdActivity();//appnext
	        					Blackjack.whatad = 1;
	        				}else{
	        					//game.actionResolver.openAdActivity();//appnext
	        					//game.admob.showAd();
	        					Blackjack.whatad = 0;
	        				}
	        	    		Blackjack.hand_count = 0;
	        	    	}
	        	    }
        		
        		// !!!!!!!!!IMPORTANT reset the face-flip card
        		game.face_card.getSprite().setPosition(220, (Blackjack.screen_top + 110));
        		game.face_card.getSprite().setSize(Blackjack.card_width,Blackjack.card_height);
        		
        	
        		tweenManager = new TweenManager();
        	
                // create the camera
                camera = new OrthographicCamera();
                camera.setToOrtho(false, 480, 800);
                
                
                
                //check if play mode isn't "SPLIT"

                if(Blackjack.current_deck_array_index < 4){
                	if(Blackjack.play_sounds){	
                		game.shuffleSound.play();
                	}
                	if(Blackjack.application_type==1){//android
                		game.actionResolver.showToast("Shuffling " + Blackjack.how_many_decks + " Decks",5000);
            	    }
                		
                	float delay = 2.5f; // seconds
                    Timer.schedule(new Task(){
	                    @Override
	                    public void run() {
	                            startDeal();
	                    }
                    }, delay);
                }else{
                	startDeal();
                }
                    	
                    	
                //scale
                //BLACKJACK ( if do not set after "BlackJack" if next deal is "BlackJack"
                //animation will start at last scale.
                game.copy_sprites[0].setScale(1.5f, 1.5f);
                
                
                
                
                
        }
        
        
        public void startDeal(){
        	//no cards in play= initial deal
        	
        	
        	//game.actionResolver.showToast("gs-sd",2000);
        	
        	
        	//if(!Blackjack.cards_in_play_before_dealer.isEmpty()){
        	
        	
        		//animate initial deal
                cs = Timeline.createSequence();
                // 3rd index card in cards_in_play_before_dealer should be a face-up dealer card ...
                //game.actionResolver.showToast("size:"+Blackjack.cards_in_play_before_dealer.size(),2000);
                for(int i=0;i<Blackjack.cards_in_play_before_dealer.size();i++){
                	
                	Card card = Blackjack.cards_in_play_before_dealer.get(i);
                	Sprite card_sprite = card.getSprite();
                	
                	if(i == 3){
                		card_sprite = game.face_card.getSprite();//face-down card
                	}
                	
                	if(card.getPlayerType() == 0){
                		
                		/*SETTINGS*/
                		float rotate_card = 0;// 0 no rotate
                		int dealer_y = game.dealer_card_dealt_y;
                		if(!Blackjack.inline_card_deal){
                			rotate_card = game.randFloat(-2.0f, 2.9f);
                			dealer_y = game.randInt(game.dealer_card_dealt_y,game.dealer_card_dealt_y + 10);
                		}/*SETTINGS*/
                    	
                		
                		//set card x,y
                		int target_x = 90 + ((Blackjack.card_width / 8) * i );
                		int target_y = dealer_y;
                		card.setX(target_x);
                		card.setY(target_y);
                		if(i == 3){
                    		game.face_card.setX(target_x);
                    		game.face_card.setY(target_y);
                    	}
	                	
                		
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
	                			.setCallback(dealer_deal_callback)
	                			.setCallbackTriggers(TweenCallback.START | 
	                					TweenCallback.COMPLETE)
	                			.target(target_x, target_y))
	                			
	                			.beginParallel()
	                			
	                	//scale it back to normal
	                	.push(Tween.to(card_sprite, SpriteAccessor.SCALE_XY, 0.1f)
	                	.target(1, 1))
	                	.end();
	                	
	                			
	                	
                	}else{
                		
                		/*SETTINGS*/
                		float rotate_card = 0;// 0 no rotate
                		int player_y = game.player_card_dealt_y;;
                		if(!Blackjack.inline_card_deal){
                			rotate_card = game.randFloat(-2.0f, 2.9f);
                			player_y = game.randInt(game.player_card_dealt_y,game.player_card_dealt_y + 10);
                		}/*SETTINGS*/
                    	
                		
                		//set card x,y
                		int target_x = 110 + ((Blackjack.card_width / 8) * i );
                		int target_y = player_y;
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
	                			.setCallback(player_deal_callback)
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
                cs.setCallback(update_score_callback);
                cs.setCallbackTriggers(TweenCallback.COMPLETE);
                
                
                //delay here also, too fast !
                
                
            	float delay = 0.1f; // seconds
                Timer.schedule(new Task(){
                    @Override
                    public void run() {
                    	cs.start(tweenManager);
                    }
                }, delay);
            	
                //cs.start(tweenManager);
                
        	}
        //}
        

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
	            //game.homebutton.draw(game.batch);
	            
	            
	            
	            //score
	            game.arialblack2.draw(game.batch, ""+Blackjack.player_one_total, game.total_numbers_x , 
	            		game.total_numbers_y);
	            //game.addBetTab();
	            
	            game.firstDealDraw();
	            
	            
	            
	            //add player score
	            if(player_card_dealt == 2 && !Blackjack.dealer_blackjack
	            		&& !Blackjack.player_blackjack){
	            	game.addScore(1,0,0,Blackjack.player_score,
	            			Blackjack.dealer_score);
	            }
	            
	            
	            
	            
	            /////////////////////////////////////////////////////////////////// BLACKJACK
	            //add dealer blackjack
	            if(Blackjack.dealer_blackjack && Blackjack.player_blackjack && deal_animate_end){
	            	game.setScreen(new DealerFlip(game));
	            	//dispose();
	            }else{
		            if(Blackjack.dealer_blackjack && deal_animate_end){
		            	game.setScreen(new DealerFlip(game));
		            	//dispose();
		            }
		            //add player blackjack
		            else if(Blackjack.player_blackjack && deal_animate_end){
		            	game.setScreen(new DealerFlip(game));
		            	//dispose();
		            }
	            }
	            /////////////////////////////////////////////////////////////////// BLACKJACK
	            
	            
	            
	            if(deal_animate_end){
		            //draw buttons
		            if(Blackjack.first_deal_before_hit || game.playerOne.gamelogic().getPlayerState() ==0){
		            	
		            	game.addHitStand();
		            	
		            	//add double down for everything
					    game.ui_sprites[44].setPosition(game.doubledown_x, game.doubledown_y);
					    game.ui_sprites[44].draw(game.batch);
					    
		            	if(Blackjack.split_test_mode){
	            			game.addSplitButton();
	            		}else if(Blackjack.split_detected){
	            			game.addSplitButton();
		                }
		            	
		            }
	            }
	            
	            
	            //draw bet chips
	            game.drawAnimatedChips();
	            
	            //add bet
	            //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
	            
        	    
	            game.batch.end();
	            
	            
	            //input
	    	    if (Gdx.input.justTouched() && deal_animate_end) {
	                Vector3 touchpoint = new Vector3();
	                touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	                camera.unproject(touchpoint);
	                
	                if(deal_animate_end){
		                //doubledown
		                if( (touchpoint.x > game.doubledown_x && touchpoint.x < game.doubledown_x + 94) &&
		                		(touchpoint.y > game.doubledown_y  && touchpoint.y < game.doubledown_y + 52)){
		      	    	     
		                	 //same as hit buton with a doubledown flag
		                	 game.doubledown = true;
		                	 
		                	 Blackjack.first_deal_before_hit = true;
		                	 Blackjack.split_detected = false;
			      	    	 game.deal();
			      	    	 game.updateGameState();
			      	    	 game.setScreen(new Hit(game));
			      	    	 //dispose();
		                	 
		                }
	                }
	                
	                //hit button
	                if( (touchpoint.x > game.hit_button_x && touchpoint.x < game.hit_button_x + 141) &&
	                		(touchpoint.y > 10 && touchpoint.y < 77)){
	      	    	   
	                   //if(deal_animate_end){
	                	   //current_player_playing should be 1 here
	                	   Blackjack.first_deal_before_hit = true;
	                	   Blackjack.split_detected = false;
		      	    	   game.deal();
		      	    	   game.updateGameState();
		      	    	   game.setScreen(new Hit(game));
		      	    	   //dispose();
	                   //}
	                }
	                
	                //stand button
	                if( (touchpoint.x > game.stand_button_x && touchpoint.x < game.stand_button_x + 141) &&
	                		(touchpoint.y > 10 && touchpoint.y < 77)){
	                	
	                	System.out.println("Deck Index: " + Blackjack.current_deck_array_index );
	                	Blackjack.current_player_playing = 0;
	                	Blackjack.split_detected = false;
	                	game.setScreen(new DealerFlip(game));
		            	//dispose();
	       	    	    
	                }
	                
	                
	                
	                //split
	                if(Blackjack.split_test_mode){
	                		
	                	if( (touchpoint.x > game.doubledown_x && touchpoint.x < game.doubledown_x + 94) &&
			                	(touchpoint.y > game.doubledown_y - 100 && touchpoint.y < game.doubledown_y - 52)){
		                	
	                		Blackjack.split_detected = false;
	                		Blackjack.what_split_hand = 1;
	                		Blackjack.split_dealer_flip = false;
	                		game.reinitializeSplitTwo();
	                		game.resetSpriteRegisters();
		       	    	    game.setScreen(new SplitScreen(game));
		       	    	    
		       	    
		       	    	    
		                }
	                }else if(Blackjack.split_detected){
	                	
	                	if( (touchpoint.x > game.doubledown_x && touchpoint.x < game.doubledown_x + 94) &&
			                	(touchpoint.y > game.doubledown_y - 100 && touchpoint.y < game.doubledown_y - 52)){
		                	
	                		
	                		Blackjack.split_detected = false;
	                		Blackjack.what_split_hand = 1;
	                		Blackjack.split_dealer_flip = false;
	                		game.reinitializeSplitTwo();
	                		game.resetSpriteRegisters();
		       	    	    game.setScreen(new SplitScreen(game));
		       	    	    //dispose();
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
        
        
        //Tween Callback
        private final TweenCallback dealer_deal_callback = new TweenCallback() {
    		@Override
    		public void onEvent(int type, BaseTween<?> source) {
    				
    				
    				switch (type) {
                    case COMPLETE:
                    		if(Blackjack.play_sounds){
                    			game.dealCardSound.play();
                    		}
                    		dealer_card_dealt++;
                    	break;
    				}
    				}
 
    		};
       private final TweenCallback player_deal_callback = new TweenCallback() {
        		@Override
        		public void onEvent(int type, BaseTween<?> source) {
        				
        				
        				switch (type) {
                        case COMPLETE:
                        	if(Blackjack.play_sounds){
                        		game.dealCardSound.play();
                        	}
                        		player_card_dealt++;
                        	break;
        				}
        				}
     
        		};
       private final TweenCallback update_score_callback = new TweenCallback() {
            		@Override
            		public void onEvent(int type, BaseTween<?> source) {
            				
            				//update score after deal(timeline sequence) finished
            				switch (type) {
                            case COMPLETE:
                            		game.updateGameState();
                            		deal_animate_end=true;
                            	break;
            				}
            				}
         
       };
        
        public void resize(int width, int height) {
        }

        @Override
        public void show() {
        		//Gdx.input.setInputProcessor(this);
        	    // start the playback of the background music
                // when the screen is shown
                //rainMusic.play();
        }

        @Override
        public void hide() {
        }

        @Override
        public void pause() {
        	
        }

        @Override
        public void resume() {
        }

        @Override
        public void dispose() {
        }
        
}
