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

import aurelienribon.tweenengine.Timeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.utils.Timer;
//import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class SplitScoreScreen implements Screen {
	
	final Blackjack game;
	Timeline cs;

    OrthographicCamera camera;
    int total_on_enter;
    int score;
    boolean score_finished = false;
    boolean database_updated = false;
	boolean zero_activated = false;
	boolean add_free_chips = false;
    int blinky;

    
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
    
    //private boolean cards_moved = false;
    private boolean hit_moved = false;
    private boolean flipped = false;
    
    //$$$ split bank
    boolean sp1_win = false;
    boolean sp1_lose = false;
    boolean sp1_bank_set = false;
    boolean sp2_win = false;
    boolean sp2_lose = false;
    boolean sp2_bank_set = false;
    
	
	public SplitScoreScreen(final Blackjack gam){
		
		Blackjack.screen_state = 7;
		Gdx.input.setCatchBackKey(true);
		
		this.game = gam;
		game.resetSpriteRegisters();
		
		Blackjack.player_one_total_before_change = game.split_animate_fake_score;
		
		score = Blackjack.player_one_total_before_change;
		
		
	    
	    

		
		game.split_animate_score_finished = false;
		
		//System.out.println(">>SCORE: " + score);
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

    	//dealer card 1
        dealer_card1 = Blackjack.cards_in_play_before_dealer.get(1).getSprite();
        
	}
	
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
  if(!Blackjack.home_screen_viewed){
	  
  
        //split 1 $$$bank
        if(sp1_win && !sp1_bank_set){
        	System.out.println("HAND ONE WIN");
        	if(Blackjack.split_hand_one_score > 21 || Blackjack.split_hand_one_score == 21){
        		if(game.dd_clicked_one){
        			if(Blackjack.split_hand_one_score == 21){
        				Blackjack.player_one_total += Blackjack.player_one_last_bet_made * 2;
        			}
        			game.dd_clicked_one = false;
        		}else{
        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made;
        		}
        		
        		
        	}else{
        		if(!game.dd_clicked_one){
        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made;
        		}else{
        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made * 2;
        			System.out.println("!!!!!!!!!!!! HAND ONE DD CLICKED !!!!!!!!!!");
        			game.dd_clicked_one = false;
        		}
	        	
        	}
        	sp1_bank_set = true;
        	
        }else if(sp1_lose && !sp1_bank_set){
        	System.out.println("HAND ONE LOSE");
        	if(Blackjack.split_hand_one_score > 21 || Blackjack.split_hand_one_score == 21){
        		if(game.dd_clicked_one){
        			//Blackjack.player_one_total -= Blackjack.player_one_last_bet_made * 2;
        			game.dd_clicked_one = false;
        		}
        		
        		
        	}else{
        		if(!game.dd_clicked_one){
        			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made;
        		}else{
        			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made * 2;
        			System.out.println("!!!!!!!!!!!! HAND ONE DD CLICKED !!!!!!!!!!");
        			game.dd_clicked_one = false;
        		}
	        	
        	}
        	sp1_bank_set = true;
        }
        
        
        //split 2 $$$bank
        if(sp2_win && !sp2_bank_set){
        	System.out.println("HAND TWO WIN");
        	if(!game.dd_clicked_two){
        		Blackjack.player_one_total += Blackjack.player_one_last_bet_made;
        	}else{
        		Blackjack.player_one_total += Blackjack.player_one_last_bet_made * 2;
        		System.out.println("!!!!!!!!!!!! HAND TWO DD CLICKED !!!!!!!!!!");
        		game.dd_clicked_two = false;
        	}
        	sp2_bank_set = true;
        	
        }else if(sp2_lose && !sp2_bank_set){
        	System.out.println("HAND TWO LOSE");
        	if(!game.dd_clicked_two){
        		Blackjack.player_one_total -= Blackjack.player_one_last_bet_made;
        	}else{
        		Blackjack.player_one_total -= Blackjack.player_one_last_bet_made * 2;
        		System.out.println("!!!!!!!!!!!! HAND TWO DD CLICKED !!!!!!!!!!");
        		game.dd_clicked_two = false;
        	}
        	sp2_bank_set = true;
        	
        }
        
  }//home viewed


        // tell the camera to update its matrices.
        camera.update();
        
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        
        drawSplitUi();

        
        //draw bet chips
        game.drawAnimatedChips();
        
        //add bet
        //game.betFont.draw(game.batch, ""+Blackjack.player_one_last_bet_made, 0,23);
        
        if(add_free_chips){
        	addfreechips();
        }
        
        game.batch.end();
        
        if(sp1_bank_set && sp2_bank_set){
    		game.dd_clicked_one = false;
    		game.dd_clicked_two = false;
        }
        
        //input
	    if (Gdx.input.justTouched()) {
	    	
	    	Vector3 touchpoint = new Vector3();
            touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchpoint);
            
	    	
	    		//deal button
	            if( (touchpoint.x > game.deal_button_x && touchpoint.x < game.deal_button_x + 141) &&
	            		(touchpoint.y > 10 && touchpoint.y < 60)  && !add_free_chips){
	            	   
	            	   
	            	   Blackjack.home_screen_viewed = false; // !!!! **** !!!! **** !!!!
	            	   
	            	   
	            	   Blackjack.split_detected = false;
	            	   game.play_mode = false;
	            	   game.reinitialize();
	            	   game.reinitializeSplitTwo();
	            	   game.resetSpriteRegisters();
	            	   
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
	                    game.updateGameStateSplit();
	                    
	                   Blackjack.split_detected = false;
	                   Blackjack.dealer_flipped = false;
	                   
	                   game.winding_played = false;
	                   
	           		   game.dd_clicked_one = false;
	        		   game.dd_clicked_two = false;
	        		
		  	    	   game.setScreen(new GameScreen(game));
		  	    	   
		  	    	   resetAgain();
		  	    	   
	            }
	            
	            //bet tab
	            if( (touchpoint.x > 440 && touchpoint.x < 480) &&
	            		(touchpoint.y > 350 && touchpoint.y < 460) && !add_free_chips){
	               
	               Blackjack.home_screen_viewed = false; // !!!! **** !!!! **** !!!!
	               
	               if(Blackjack.play_sounds){
						game.tick_sound.play();
					}
	               
	               game.setScreen(new BetMenu(game,1));
	               Blackjack.home_screen_viewed = true;
	               
	               
	  	    	   
	            }
	            
	            //buy chips
	            if( (touchpoint.x > Blackjack.buychips_x1 && 
	            		touchpoint.x < Blackjack.buychips_x2) &&
	            		(touchpoint.y > Blackjack.buychips_y1 && 
	            				touchpoint.y < Blackjack.buychips_y2)  && !add_free_chips){
	            	
	            	//buy
	            	
	            	//Blackjack.home_screen_viewed = false; // !!!! **** !!!! **** !!!!
	            	
	            	//game.buyTokens();
	            	
	            	
			    	
	            }
	            
	            
	            
	            //home button
	            if( (touchpoint.x > 421 && touchpoint.x < 475) &&
	            		(touchpoint.y > (Blackjack.screen_top-50)
	            				&& touchpoint.y < Blackjack.screen_top) && !add_free_chips){
	               //System.out.println("Home Clicked");
	               game.setScreen(new HomeScreen(game));
	               
	               
	            
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
	            
	    	
	            
	            
	            
	            
	            
	            
	    	    
	    	}//end touched
	    
	    
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
	
	/*
	private void animateScore(){
		
		int interval = Blackjack.player_one_last_bet_made / 5;
		
		if(!Blackjack.home_screen_viewed){
			
			//increment score to current total
			if(score < Blackjack.player_one_total){//win
				score = score + interval;
			}
			if(score > Blackjack.player_one_total){//lose
	        	score = score - interval;
	        }
			//deal button win or lose
			//System.out.println("SCORE: " + score);
			//System.out.println("PLAYER TOTAL: " + Blackjack.player_one_total);
			if(score == Blackjack.player_one_total){
				score_finished = true;
			}
		}else{
			//Blackjack.home_screen_viewed = false;
			score = Blackjack.player_one_total;
			score_finished = true;
		}
	        
	    if(score < 0){
	        score = 0;
	        Blackjack.player_one_total = 0;
	    }
	        
	    if(score == 0){
	        Blackjack.score_is_zero = true;
	    }

	}
	*/
	
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
			}
		}else{
			//Blackjack.home_screen_viewed = false;
			score = Blackjack.player_one_total;
			score_finished = true;
		}
        
        
        if(score < 0){
        	score = 0;
        	Blackjack.player_one_total = 0;
        }
        
        if(score == 0){
        	Blackjack.score_is_zero = true;
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
		int copy=0;
		
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
							//System.out.println(" ***Dealer Win Set");
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
			sp1_win = true;
		}else{
			//check bust
			if(ps1 > 21){
				game.addBust(2, x, y); // ??????
				sp1_lose = true;
			}else{
				//check push first
				if(ds == ps1){
					push = true;
				}
				//check win
				if(!push){
					if(ps1 > ds || ps1 == 21){
						game.addWin(2, x, y);
						sp1_win = true;
					}
				}
				//check push
				if(push){
					game.addPush(2, x, y);
				}
				//check lose
				if(ps1 < ds && !push && !dealer_bust){
					game.addLose(2, x, y);
					sp1_lose = true;
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
			sp2_win = true;
		}else{
			//check bust
			if(ps2 > 21){
				game.addBust(1, x, y);
				sp2_lose = true;
			}else{
				//check push first
				if(ds == ps2){
					push = true;
				}
				//check win
				if(!push){
					if(ps2 > ds || ps2 == 21){
						game.addWin(1, x, y);
						sp2_win = true;
					}
				}
				//check push
				if(push){
					game.addPush(1, x, y);
				}
				//check lose
				if(ps2 < ds && !push && !dealer_bust){
					game.addLose(1, x, y);
					sp2_lose = true;
				}
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
				game.addWin(2, sp1_score_pos_x, sp1_score_pos_y -26);
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
				game.addScoreBackground(1,2,p_x_pos,p_y_pos);
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
	
	
	public void drawSplitUi(){
		
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
        
        
        /*
        animateScore();
        game.arialblack2.draw(game.batch, ""+score, game.total_numbers_x , game.total_numbers_y);
        */
        
        animateScore();
        if(Blackjack.player_one_total == 0){
	        add_free_chips = true;
        }
        game.arialblack2.draw(game.batch, ""+score, game.total_numbers_x , game.total_numbers_y);

        
        
        if(Blackjack.what_split_hand == 1 && hit_moved && !flipped){
        	game.addSplitHighlight(43, 333);
        }else if(Blackjack.what_split_hand == 2 && !flipped){//2
        	game.addSplitHighlight(43, 333 - move_up);
        }
        
        
        //backgrounds
        if(hit_moved){
        	if(Blackjack.what_split_hand == 1){
        		drawBackgrounds(1);
        	}else if(Blackjack.what_split_hand == 2){
        		drawBackgrounds(2);
        	}
        	
        }
        
        //if(flipped){
    		game.addSplitHighlight(43, 533);
    	//}
        
        //dealer done
        if(Blackjack.dealer_done){
        	//dealer
        	game.addScoreBackground(0,endDealerBackground(),42,d_score_pos_y+3);
        	//drawDealerCards();
        	this.drawDealerCardsSplit();
        	drawDealerScores(42,d_score_pos_y+3);
        	
        }else{
        	//drawDealerCards();
        	//draw hit cards

        }
        
        
        endDealerCopy();
        
        

        	
        	//if(hit_moved){ this.addRegisteredSprites(0); }
        	
        	if(Blackjack.dealer_done){ game.addScoreBackground(1,
        			endSplitOneBackground(),45, sp1_score_pos_y);}
        	
        	redrawPlayerOneCards();
        	if(Blackjack.dealer_done){ drawSplitOneScores();}
        	if(Blackjack.dealer_done){ endSplitOneCopy();}
        	
        	
        	if(Blackjack.dealer_done){ game.addScoreBackground(2,
        			endSplitTwoBackground(),45,sp2_score_pos_y);}
        	
        	redrawPlayerTwoCards();
        	if(Blackjack.dealer_done){ drawSplitTwoScores();}
        	if(Blackjack.dealer_done){ endSplitTwoCopy();}
        	

        
    	if(Blackjack.dealer_done){
            game.addDealBet();
            game.addBetTab();
            
            //update database score
            if(!Blackjack.home_screen_viewed){
	        	if(!database_updated){
	        		System.out.println("Database Updated");
	        		game.updateDatabaseScore();
	        		if(Blackjack.application_type == 1){
	        			game.writeScoreInternal(Blackjack.player_one_total);
	        		}
		        	database_updated = true;
	        	}
            }
            
            //add win sound
    		if(sp1_win && !sp1_bank_set && game.winding_played == false ||
    				sp2_win && !sp2_bank_set && game.winding_played == false){
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
