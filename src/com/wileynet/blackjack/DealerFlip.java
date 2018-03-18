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

public class DealerFlip implements Screen {
	
	final Blackjack game;
	Timeline cs;
	private final TweenManager tweenManager;

    OrthographicCamera camera;
    
    boolean draw_black_circle = false;
    boolean draw_small_twinkle = false;
    
    int flip_card_x = 0;
    int flip_card_y = 0;
    int timer = 0;
    
    
	public DealerFlip(final Blackjack gam){
		Blackjack.screen_state = 4;
		Gdx.input.setCatchBackKey(true);
		
		this.game = gam;
		//game.resetSpriteRegisters();
		//game.actionResolver.showToast("flip screen",2000);
		
		tweenManager = new TweenManager();
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        
        
        
        if(!Blackjack.player_bust){
	        float delay = 0.5f; // seconds
	        Timer.schedule(new Task(){
	            @Override
	            public void run() {
	                	
	    	        	cs = Timeline.createSequence();
	    	        	
	    	        	cs.push(Tween.to(game.face_card.getSprite(), SpriteAccessor.POS_XY,0.2f)
	    	    	    .target(175,300)
	    	    	    .ease(Circ.OUT));
	    	    	    //.beginParallel()
	    	    	    //.push(Tween.set(game.face_card.getSprite(), SpriteAccessor.SIZE_XY)
	    	    	    //.target(0,0)).end();
	    	    	    
	    	        	
	    	            cs.setCallback(flip_callback);
	    	            cs.setCallbackTriggers(TweenCallback.COMPLETE | TweenCallback.START);
	    	            cs.start(tweenManager);
	            	
	            }
	        }, delay);
        }else{
        	//bust
        	//update $$$score
        	Blackjack.player_one_total_before_change = Blackjack.player_one_total;
    		if(!game.doubledown){
    			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made;
    		}else{
    			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made * 2;
    			game.doubledown = false;
    		}
    		if(Blackjack.player_one_total < 0)Blackjack.player_one_total = 0;
    		
    		System.out.println("BUST:::");
    		System.out.println("total:" + Blackjack.player_one_total + 
    				" BET:" + Blackjack.player_one_last_bet_made);
    		
    		//goto score screen
    		//game.setScreen(new ScoreScreen(game));
	    	//dispose();
        }
        
        
        
       /*
       if(Blackjack.dealer_flipped){
    	    
    	    //doesn't seem to work well on android PAUSE
        	Card card = Blackjack.cards_in_play_before_dealer.get(3);
    		cs = Timeline.createSequence();
        	cs.push(Tween.to(card.getSprite(), SpriteAccessor.SIZE_XY, 0.4f)
        	.target(145,203));
            cs.start(tweenManager);
           
       }
       */
        
        if (Gdx.input.isKeyPressed(Keys.BACK)){
	    	game.actionResolver.killMyProcess();
	    }
        
	}

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
        
        //score
        game.arialblack2.draw(game.batch, ""+Blackjack.player_one_total, game.total_numbers_x , 
        		game.total_numbers_y);
        
        
        //Add Bust and Deal
        if(Blackjack.player_bust){
        	game.addScoreBackground(1,3,0,0);
        	game.addScore(1,0,0,
        			Blackjack.player_score,
        			Blackjack.dealer_score);
	    	
        }
        
        game.redrawStaticCards2();
        
        
        //bust copy on top of cards
        if(Blackjack.player_bust){
        	if(Blackjack.play_bust_sound){
        		if(Blackjack.play_sounds){
        			game.bustSound.play();
        		}
    			Blackjack.play_bust_sound = false;
    		}
        	game.addBust(1,0,0);
        	game.addDealBet();//add deal bet controls
        }
        
        
        if(Blackjack.dealer_flipped){
        	Card card = Blackjack.cards_in_play_before_dealer.get(3);
        	card.getSprite().draw(game.batch);
        }
        
        
        //draw bet chips
        game.drawAnimatedChips();
        
        
        //add bet
        //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
        
        game.batch.end();
        
        
        if(Blackjack.player_bust){
        	//goto score screen
    		game.setScreen(new ScoreScreen(game));
	    	//dispose();
	    	
	    	
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
	
	
	
	
    private final TweenCallback flip_callback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
				
				
				switch (type) {
                case COMPLETE:
                	
                	
                	game.playerOne.gamelogic().updateAfterDealer();	// Update Score
                	
                	//set dealer done
                	if(Blackjack.dealer_cards.size() < 3){
                		Blackjack.dealer_done = true;
                	}
                	
                	Blackjack.dealer_flipped = true;
                	float delay = 0.5f; // seconds
                    Timer.schedule(new Task(){
                        @Override
                        public void run() {
                        	Blackjack.play_win_sound = true;
                        	game.setScreen(new DealScreen(game));
                        	//dispose();
                        }
                    }, delay);
                    
                	break;
				case START:
					//game.cardFlipSound.play();
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
