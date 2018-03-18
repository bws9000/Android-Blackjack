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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.math.Vector3;
import com.wileynet.blackjack.accessors.SpriteAccessor;

public class Hit implements Screen{
	
	final Blackjack game;
	private final TweenManager tweenManager;
	private Timeline cs;
	OrthographicCamera camera;
	Card hitcard;
	private boolean hit_once = false;
	
	boolean hit_animate_end=false;
	
	public Hit(final Blackjack gam){
		Blackjack.screen_state = 3;
		Gdx.input.setCatchBackKey(true);
		
		this.game = gam;
		
		//game.actionResolver.showToast("hit screen",2000);
		
		//game.resetSpriteRegisters();
		
		tweenManager = new TweenManager();
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
		
		//hitcard = Blackjack.cards_in_play_before_dealer.get(Blackjack.current_deck_array_index);//hitcard
        hitcard = Blackjack.cards_in_play_before_dealer.get(Blackjack.cards_in_play_before_dealer.size()-1);//hitcard
		//game.card_sprites[hitcard.getType()].setPosition(220, (Blackjack.screen_top + 110));
        hitcard.getSprite().setPosition(220, (Blackjack.screen_top + 110));
		
		cs = Timeline.createSequence();
		
		
			//get the last card added to ui
		    int cip = Blackjack.cards_in_play_before_dealer.size()-1;//-1 or you get the hitcard
		    Card user_card = null;
		    //just roll through them all getting the last card owned by player 1
			for(int i=0;i<cip;i++){
				Card card = Blackjack.cards_in_play_before_dealer.get(i);
				if(card.getPlayerType() == 1){
					user_card = Blackjack.cards_in_play_before_dealer.get(i);
				}
			}
			
			int lastcard_targetx = user_card.getX();
			int lastcard_targety = user_card.getY();
			
			//System.out.println("x: " + user_card.getX());
			//System.out.println("y: " + user_card.getY());
			
			//set card x,y
    		int target_x = lastcard_targetx + 32;
    		int target_y = lastcard_targety;
    		hitcard.setX(target_x);
    		hitcard.setY(target_y);
    		
    		/*SETTINGS*/
    		float rotate_card = 0;// 0 no rotate
    		int player_y = hitcard.getY();
    		int player_x = hitcard.getX();
    		if(!Blackjack.inline_card_deal){
    			rotate_card = game.randFloat(-1.9f, 3.9f);
    		}/*SETTINGS*/
    		
    		
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
        			.delay(0.0f)
        			.ease(Circ.OUT)
        			.setCallback(hitcallback)
        			.setCallbackTriggers(TweenCallback.START | 
        					TweenCallback.COMPLETE)
        			.target(player_x, player_y))
        			
        			.beginParallel()
        			
        	//scale it back to normal
        	.push(Tween.to(hitcard.getSprite(), SpriteAccessor.SCALE_XY, 0.1f)
        	.target(1, 1)).end();
        	
    		
        cs.setCallback(hit_sequence_end_callback);
        cs.setCallbackTriggers(TweenCallback.COMPLETE);
        
        
        
        //delay hit also, fast !
        
    	float delay = 0.1f; // seconds
        Timer.schedule(new Task(){
            @Override
            public void run() {
            	cs.start(tweenManager);
            }
        }, delay);
        
		
        //cs.start(tweenManager);
	}
	
	@Override
	public void render(float delta) {
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
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
		
		
		if(hit_animate_end){
			
			//WIN
			if(Blackjack.player_win){
				game.setScreen(new DealerFlip(game));
	        	//dispose();
			}
			
			//BUST
			if(Blackjack.player_bust){
				game.setScreen(new DealerFlip(game));
	        	//dispose();
			}
		
		}
		

    	game.redrawStaticCards();
    	if(hit_animate_end){
    		if(game.playerOne.gamelogic().getPlayerState() == 0){
    			
    			if(Blackjack.player_score == 21 || 
        				Blackjack.player_score > 21){
        			Blackjack.current_player_playing =0;
        			game.setScreen(new DealerFlip(game));
        		}else{
        			
        			if(!game.doubledown){
	        			game.addScore(1,0,0,
	        					Blackjack.player_score,
	        					Blackjack.dealer_score);
        			}
        			
        		}
    		}
    	}
		//draw hitcard
    	hitcard.getSprite().draw(game.batch);
    	
    	//add controls
    	game.addHitStand();
        
    	
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
            
            
	            //hit button
	            if( (touchpoint.x > game.hit_button_x && touchpoint.x < game.hit_button_x + 141) &&
	            		(touchpoint.y > 10 && touchpoint.y < 77 &&
	            				hit_once)){
	            	
	            
	            	   
		               Blackjack.current_player_playing = 1;
		               Blackjack.split_detected = false;
		  	    	   game.deal();
		  	    	   game.updateGameState();
		  	    	   game.setScreen(new Hit(game));
		  	    	   //dispose();
	            }
	        
	       
            
            //stand button
            if( (touchpoint.x > game.stand_button_x && touchpoint.x < game.stand_button_x + 141) &&
            		(touchpoint.y > 10 && touchpoint.y < 77)){
               
            	
            	Blackjack.current_player_playing = 0;
            	Blackjack.split_detected = false;
            	game.setScreen(new DealerFlip(game));
            	//dispose();
   	    	    
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
	
	
	private final TweenCallback hitcallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	if(Blackjack.play_sounds){
                		game.dealCardSound.play();
                	}
                		Blackjack.play_bust_sound = true;
                		
                		//if doubledown
                		if(game.doubledown && !Blackjack.player_bust){
                			
                			Blackjack.current_player_playing = 0;
                        	Blackjack.split_detected = false;
                        	
                        	//delay hit also, fast !
                        	float delay = 0.2f; // seconds
                            Timer.schedule(new Task(){
                               @Override
                                public void run() {
                                	game.setScreen(new DealerFlip(game));
                                	//dispose();
                                }
                            }, delay);
                            
                			
                		}
                		
                	break;
				}
				}

		};
		
		private final TweenCallback hit_sequence_end_callback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
					
					
					switch (type) {
	                case COMPLETE:
	                		game.updateGameState();
	                		hit_animate_end = true;
	                		hit_once = true;
	                	break;
					}
					}

			};
		
	 @Override
     public void show() {
     		//Gdx.input.setInputProcessor(this);
     	    // start the playback of the background music
             // when the screen is shown
             //rainMusic.play();
     }

     @Override
     public void hide() {
     	//Gdx.input.setInputProcessor(null);
     }

     @Override
     public void pause() {
     }

     @Override
     public void resume() {
     }

     @Override
     public void dispose() {
     	//Gdx.input.setInputProcessor(null);
     }

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
