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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BillingScreen implements Screen{
	
	final Blackjack game;
	private int where;
	private boolean buybutton_state = true;
	private boolean timer_activated = false;
	Card c;
	
	/*if bet tab is open*/
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
	private boolean bet_closed_clicked = false;
	/*if bet tab is open*/
	
	OrthographicCamera camera;
	
	public BillingScreen(final Blackjack gam,int w){
		Blackjack.screen_state = 9;
		
		
		where = w;
		Blackjack.bet_normal_or_split = w;
		//0=normal
		//1=split
		
		this.game = gam;
		//Blackjack.player_one_total = Integer.parseInt(game.getDatabaseScore());
		
		
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
            
            openBetMenu();
        	
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
        
        
        
        if(Blackjack.application_type == 1){
        	game.actionResolver.createConnection();
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
		game.ui_sprites[17].setPosition(444, 0);
	}
	
	public void closeBetMenu(){
		game.ui_sprites[17].setPosition(333, 0);
	}
	
	
	@Override
	public void render(float delta) {
		
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
        game.arialblack2.draw(game.batch, ""+Blackjack.player_one_total, game.total_numbers_x , 
        		game.total_numbers_y);
        
        
        
        if(where == 0){
        	addRegisteredSprites();
        }else{//1
        	game.ui_sprites[18].draw(game.batch);//highlight
        	addRegisteredSpritesFromSplit();
        }
        
        game.addDealBet();
        addBetMenu();
        addChips();
        addControls();
        
        
        
        if(Blackjack.score_is_zero)Blackjack.close_buybutton = false;
        if(buybutton_state && !Blackjack.close_buybutton){
	        //background
	        //game.ui_sprites[47].setPosition(0, 0);
	        //game.ui_sprites[47].draw(game.batch);
	        
	        //buynow graphic
        	game.ui_sprites[50].draw(game.batch);//SHADOW
	        game.ui_sprites[46].setPosition(0, 255);
	        game.ui_sprites[46].draw(game.batch);
        }
        
        
        //bonustimer
        if(Blackjack.score_is_zero){
        	timer_activated = true;
        	if(timer_activated){
        		timer_activated = false;
        		//game.timer.start();
        		startCountdown();
        	}
        	
        	game.ui_sprites[48].setPosition(0, 722);
        	game.ui_sprites[48].draw(game.batch);
        	game.bonusTimer.draw(game.batch, "1 minute" , 122,743);
        	
        }
        
        
        
        if(game.bet_menu_state && !bet_closed_clicked){
        	
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

        game.batch.end();
        
        
        if (Gdx.input.justTouched()) {
            Vector3 touchpoint = new Vector3();
            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpoint);
            
	            //close
		        if( (touchpoint.x > 355 && touchpoint.x < 455) &&
		        		(touchpoint.y > 465 && touchpoint.y < 555)){
		        	
		        	if(!Blackjack.score_is_zero){
		        		buybutton_state = false;
		        		Blackjack.close_buybutton = true;
		        	}
		        }
		        
			    //buynow
			    if( (touchpoint.x > 55 && touchpoint.x < 415) &&
			        		(touchpoint.y > 225 && touchpoint.y < 400)){
			       
			       if(Blackjack.application_type == 1){
			    	    
			    	    buybutton_state = false;
		            	game.setScreen(new WaitScreen(game));
			        	
			       }
				    	
			    }
			    
			    //close buynow
			    if( (touchpoint.x > 375 && touchpoint.x < 450) &&
			        		(touchpoint.y > 475 && touchpoint.y < 550)){
			    	    
			    	    buybutton_state = false;
		            	Blackjack.score_is_zero = false;
			            Blackjack.player_one_total = 50;
			            Blackjack.player_one_total_before_change = 0;
			            Blackjack.player_one_last_bet_made = 5;
			            Blackjack.close_buybutton = true;
				    	
			    }
			    
			    
			    
			    //deal button
	            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
	            		(touchpoint.y > 10 && touchpoint.y < 77)){
	  	    	  
	            	if(!Blackjack.score_is_zero){
	            		
	            	
		            	if(Blackjack.close_buybutton){
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
	            	}
	            	
	            	
	            }
	            
	            if(!game.bet_menu_state){
		            //bet tab is closed
	            	 if(!Blackjack.score_is_zero){
			            if( (touchpoint.x > 440 && touchpoint.x < 480) &&
			            		(touchpoint.y > 350 && touchpoint.y < 460)){
			               game.setScreen(new BetMenu(game,where));
			  	    	   dispose();
			            }
	            	 }
	            }
	            
	            //bet tab is open
	            if( (touchpoint.x > 333 && touchpoint.x < 363) &&
	            		(touchpoint.y > 350 && touchpoint.y < 460)){
	               
	               if(!Blackjack.score_is_zero){
		               if(Blackjack.close_buybutton){
			               game.bet_menu_state = true;
			               game.setScreen(new BetMenu(game,where));
			  	    	   dispose();
		               }
	               }
	               
	            }
	            
	            //buy chips
	            if( (touchpoint.x > Blackjack.buychips_x1 && 
	            		touchpoint.x < Blackjack.buychips_x2) &&
	            		(touchpoint.y > Blackjack.buychips_y1 && 
	            				touchpoint.y < Blackjack.buychips_y2)){
	            	
	            	int split=0;
	            	if(game.play_mode)split=1;
	            	Blackjack.close_buybutton = false;
	            	game.setScreen(new BillingScreen(game,split));
			    	
	            }
			    
	            if(game.bet_menu_state){
	            	
	            	if(!buybutton_state){
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
		    	
	        }
	        
	}
	
	public void startCountdown(){
		float delay = 60f; // seconds
        Timer.schedule(new Task(){
            @Override
            public void run() {
               if(Blackjack.score_is_zero){
	               Blackjack.score_is_zero = false;
	               Blackjack.player_one_total = 100;
	               Blackjack.player_one_total_before_change = 0;
	               Blackjack.player_one_last_bet_made = 5;
	               Blackjack.close_buybutton = true;
               }
            }
        }, delay);
	}
	
	
	private void chipClick(Vector3 t,int open_x,int chip_wh,int chipy,int what_chip){
    	if( (t.x > open_x && t.x < chips_open_x + chip_wh) &&
        		(t.y > chipy && t.y < chipy + chip_wh)){
           
           this.chip_value = what_chip;
           Blackjack.player_one_last_bet_made += this.chip_value;
           
           switch(what_chip){
	           case 5:
		           this.resetClickChips();
		           this.c5_clicked = true;
	           break;
	           case 10:
	        	   this.resetClickChips();
		           this.c10_clicked = true;
	           break;
	           case 25:
	        	   this.resetClickChips();
		           this.c25_clicked = true;
	           break;
	           case 50:
	        	   this.resetClickChips();
		           this.c50_clicked = true;
	           break;
	           case 100:
	        	   this.resetClickChips();
		           this.c100_clicked = true;
	           break;   
           }
           
        }
	}
	
	
	private void betClick(Vector3 t,int open_x,int wh,int by,boolean ud){
		
    	if( (t.x > open_x && t.x < open_x + wh) &&
        		(t.y > by && t.y < by + wh)){
           
    	   if(ud){
    		   Blackjack.player_one_last_bet_made += this.chip_value;
    	   }else{
    		   Blackjack.player_one_last_bet_made -= this.chip_value;
    	   }
    	   if(Blackjack.player_one_last_bet_made < default_bet){
    		   Blackjack.player_one_last_bet_made = default_bet;
    	   }else if(Blackjack.player_one_last_bet_made > this.bet_limit){
    		   Blackjack.player_one_last_bet_made = this.bet_limit;
    	   }
           //System.out.println("BET: " + this.bet_total);
    	   
    	   //no bets above your bank
    	   if(Blackjack.player_one_last_bet_made > Blackjack.player_one_total){
    		   Blackjack.player_one_last_bet_made = Blackjack.player_one_total;
    	   }
        }
	}
	
	
	
	
	private void addBetMenu(){
	    game.ui_sprites[17].draw(game.batch);
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
		//
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	
	/*
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		game.actionResolver.buyTokens();
		return true;
	}
	*/


}
