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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ScoreScreen implements Screen {
	
	final Blackjack game;
	int score;
	
	boolean score_finished = false;
	boolean database_updated = false;
	boolean zero_activated = false;
	boolean add_free_chips = false;
	
	int blinky;

    OrthographicCamera camera;
    
	public ScoreScreen(final Blackjack gam){
		Blackjack.screen_state = 5;
		Gdx.input.setCatchBackKey(true);
		
		this.game = gam;
		game.resetSpriteRegisters();
		
		//game.actionResolver.showToast("score screen",2000);
		
		game.dd_clicked_one = false;
		game.dd_clicked_two = false;
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        
        score = Blackjack.player_one_total_before_change;
        
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
	        // tell the camera to update its matrices.
	        camera.update();
	        
	        game.batch.setProjectionMatrix(camera.combined);
	        game.batch.begin();
	        
	        drawGameUi();
	        
	        animateScore();
	        if(Blackjack.player_one_total == 0){
	        	
	        	//pause in animateScore
	        	add_free_chips = true;
	        	game.arialblack2.draw(game.batch, "0", game.total_numbers_x , game.total_numbers_y);
	        	
	        }else{
	        	game.arialblack2.draw(game.batch, ""+score, game.total_numbers_x , game.total_numbers_y);
	        }
	        
	        //draw bet chips
            game.drawAnimatedChips();
            
            //add bet
            //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
            
            if(add_free_chips){
            	addfreechips();
            }
            

			game.batch.end();
		
		
        
        //input
	    if (Gdx.input.justTouched() && score_finished) {
            Vector3 touchpoint = new Vector3();
            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpoint);
            
            
            //deal button
            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
            		(touchpoint.y > 10 && touchpoint.y < 77) && !add_free_chips){
            	
                game.reinitialize();
                //set shuffle
                if(Blackjack.current_deck.isEmpty()){
                	game.shuffle();
                	if(Blackjack.application_type==1){//android
                		game.actionResolver.showToast("Shuffling",5000);
            	    }
                }
                //deal cards
                game.deal();
                //update score
                game.updateGameState();
                game.winding_played = false;
                
                
               //game.setCardPositions();
               Blackjack.split_detected = false;
               game.setScreen(new GameScreen(game));
  	    	   dispose();
            }
            
            
            //bet tab
            if( (touchpoint.x > 440 && touchpoint.x < 480) &&
            		(touchpoint.y > 350 && touchpoint.y < 460) && !add_free_chips){
               
            	if(Blackjack.play_sounds){
					game.tick_sound.play();
				}
            	
               game.setScreen(new BetMenu(game,0));
  	    	   dispose();
            }
            
            //buy chips
            if( (touchpoint.x > Blackjack.buychips_x1 && 
            		touchpoint.x < Blackjack.buychips_x2) &&
            		(touchpoint.y > Blackjack.buychips_y1 && 
            				touchpoint.y < Blackjack.buychips_y2) && !add_free_chips){
            	
            	//buy
            	//game.buyTokens();
		    	
            }
            
            //addfreechips
            if( (touchpoint.x > 0 && 
            		touchpoint.x < 455) &&
            		(touchpoint.y > 200 && 
            				touchpoint.y < 600) && add_free_chips){
            	
            	
            	if(Blackjack.application_type == 1){
            		//game.actionResolver.openAdActivity(game.isWifi);//applift
            		if(Blackjack.enable_admob){
            			
            			//ADMOB BAN
            			//((ActionResolver) game.actionResolver2).showOrLoadInterstital();
            			
            		}
            		game.writeScoreInternal(600);
            		
            	}else if(Blackjack.application_type == 0){
            		game.actionResolver.openUri("http://casinogames.wileynet.com/out.php");
            	}
            	
            	add_free_chips = false;
            	
		    	
            }
            
            
            //home button
            if( (touchpoint.x > 421 && touchpoint.x < 475) &&
            		(touchpoint.y > (Blackjack.screen_top-50)
            				&& touchpoint.y < Blackjack.screen_top) && !add_free_chips){
               //System.out.println("Home Clicked");
               game.setScreen(new HomeScreen(game));
            }
            
	    }
	    
	    if (Gdx.input.isKeyPressed(Keys.BACK)){
	    	//game.actionResolver.openAdActivity();//appnext
	    	game.actionResolver.killMyProcess();
	    }
	    
	    if(Blackjack.score_is_zero){
        	//game.buyTokens();
	    	game.writeScoreInternal(600);
	    	
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
	
	private void addfreechips(){
    	//addfreechips
		game.ui_sprites[50].draw(game.batch);//SHADOW
    	game.ui_sprites[100].setPosition(0, 265);
    	game.ui_sprites[100].draw(game.batch);
	}
	
	
	private void animateScore(){
		
		if(Blackjack.player_one_total == 0){
			
			//delay
			float delay = 1.5f;
	        Timer.schedule(new Task(){
	            @Override
	            public void run() {
	            	zero_activated = true;
	            	doAnimateScore();
	            	
	            }
	        }, delay);
		}else{
			doAnimateScore();
		}
        
		
        
	}
	
	private void doAnimateScore(){
		
		int down_interval = Blackjack.player_one_last_bet_made / 5;
		int up_interval = Blackjack.player_one_last_bet_made / 5;
		if(zero_activated){
			up_interval = 1;
		}
    	
    	if(!Blackjack.home_screen_viewed){
			//increment score to current total
			if(score < Blackjack.player_one_total){//win
				score = score + up_interval;
				if(Blackjack.play_sounds){
					game.dingding.play();
				}
			}
			if(score > Blackjack.player_one_total){//lose
	        	score = score - down_interval;
	        	if(Blackjack.play_sounds){
	        		game.dingding.play();
	        	}
	        }
			//deal button win or lose
			if(score == Blackjack.player_one_total){
				score_finished = true;
				game.dingding.stop();
			}
		}else{
			Blackjack.home_screen_viewed = false;
			score = Blackjack.player_one_total;
			score_finished = true;
			game.dingding.stop();
		}
        
        
        if(score < 0){
        	score = 0;
        	Blackjack.player_one_total = 0;
        }
        
        if(score == 0){
        	Blackjack.score_is_zero = true;
        }
        
	}
	
	
	
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
	
	
	public void drawGameUi(){
		
		//draw last game ui state
		//which is basically everything DealScreen result is
		
		game.addGreenBackground();
        game.c_control_panel_back_bottom.draw(game.batch);
        game.c_control_panel_back_top.draw(game.batch);
        game.homebutton.draw(game.batch);
        
        
        //blink chip top
        blinky += Gdx.app.getGraphics().getDeltaTime() + 1;
        if(blinky % 2 == 0 && blinky < 90000){
        	game.ui_sprites[49].setPosition(0, 755);
            game.ui_sprites[49].draw(game.batch);
        }
        
        //System.out.println("SCORE SCREEN ENTERED");
      		
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
	        		Blackjack.play_win_sound = false; // DEEP WIN SOUND
	        		
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

        
        if(score_finished){
    		
        	game.addDealBet();
        	game.addBetTab();
        	
        	//update database score
        	if(!database_updated){
        		game.updateDatabaseScore();
        		if(Blackjack.application_type == 1){
        			game.writeScoreInternal(Blackjack.player_one_total);
        		}
	        	database_updated = true;
        	}
        	
        	//add win sound
    		if(Blackjack.player_win && game.winding_played == false){
    			//keeping sound play in render method
    			//seems to help failure rate, when in sub methods it failed sometimes i.e drawGameUi()
    			//or animateScore()
    			if(Blackjack.play_sounds){
    				game.winding2.play();
    			}
    			game.winding_played = true;
    			score_finished = false;
    		}
    		
        }
        
	}

}
