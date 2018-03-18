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


public final class GameLogic {
	
	final Blackjack game;
	
	int test_var = 0;
	
	private int dealer_state = 0;
	private int player_state = 0;
	
	//split
	private int split_dealer_state = 0;
	private int split_player_state = 0;
	
	private int cards_in_play_before_dealer_size=0;
	private boolean game_over = false;
	

	public GameLogic(final Blackjack gam){	
		this.game = gam;
	}
	
	
    public void update(){
	    
		int dealer_score = 0;
		int player_score = 0;
		
		cards_in_play_before_dealer_size = Blackjack.cards_in_play_before_dealer.size();
		
    	int cip = cards_in_play_before_dealer_size;
    	
    	for(int i=0;i<cip;i++){
    		
    		Card card = Blackjack.cards_in_play_before_dealer.get(i);
    		
    		switch(card.getPlayerType()){
    		
    		case 0:
    			dealer_score += card.getValue();
    			break;
    		case 1:
    			player_score += card.getValue();
    			break;
    		}
    		
    	}
    	
    	//set scores
    	setScores(dealer_score,player_score);
    	
    	//set hard or soft hands
    	if(aceInHand(0) > 0){
    		Blackjack.dealer_soft_hand = true;
    	}
    	if(aceInHand(1) > 0){
    		Blackjack.player_soft_hand = true;
    	}
    	
    	//check for blackjack
    	checkBlackjack();
    	
    	//if blackjack DONE!
    	if(dealer_state == 1 || player_state == 1){
    		// do something else if blackjack ...
    	}else{
        	checkBust();
        	checkLose();
        	checkWin();
    	}
    	
    	detectSplit();
    	
    	
    	/*
		System.out.println("++++++++++++++++++++ CHECK GAME LOGIC +++++++++++++++++++++");
		System.out.println("CARDS IN PLAY SIZE: " + cards_in_play_before_dealer_size);
		System.out.println("DEALER SCORE: " + Blackjack.dealer_score);
		System.out.println("DEALER CARDS:");
		for(int i=0;i<Blackjack.dealer_cards.size();i++){
			System.out.println(">card: " + Blackjack.dealer_cards.get(i).getName());
		}
		System.out.println("PLAYER SCORE: " + Blackjack.player_score);
		System.out.println("PLAYER CARDS:");
		for(int i=0;i<Blackjack.player_cards.size();i++){
			System.out.println(">card: " + Blackjack.player_cards.get(i).getName());
		}
		System.out.println("-----------------------------------------------------");
    	System.out.println("Ace In Dealer Hand: " + aceInHand(0));
    	System.out.println("Ace In Player Hand: " + aceInHand(1));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("(0=PLAYING|1=BJ|3=WIN|4=LOSE|2=BUST");
		System.out.println("Dealer Game State: " + dealer_state);
		System.out.println("Player Game State: " + player_state);
		if(Blackjack.is_push){
			System.out.println("PUSH ?: TRUE");
		}else{
			System.out.println("PUSH ?: FALSE");
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Player Cards SIZE: " + Blackjack.player_cards.size());
		System.out.println("Dealer Cards SIZE: " + Blackjack.dealer_cards.size());
		if(game_over){
			System.out.println("Game Over: TRUE");
		}else{
			System.out.println("Game Over: FALSE");
		}
		if(Blackjack.split_detected){
			System.out.println(">>>>>>>>>>>>>>>>>>>>>");
			System.out.println("SPLIT DETECTED: TRUE");
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		*/
		
    }
    
    
    
    public void updateAfterDealer(){
    	
    	//dealer and player scores should already be set
    	
  
    	checkBlackjack();
    	if(dealer_state == 1 || player_state == 1){
    		// do something else if blackjack ...
    	}else{
        	checkBust();
        	checkLose();
        	checkWin();
    	}
    	
    	//check for push here
    	if(Blackjack.dealer_score < 22 &&
		    	Blackjack.dealer_score == Blackjack.player_score && Blackjack.dealer_done){
    			
    		    
    		    	if(Blackjack.dealer_flipped){
    		    		Blackjack.is_push = true;
    		    	}
    		   
		    	
		}
    	
    	
        System.out.println("");
		System.out.println("++++++++++++++++++++ CHECK AFTER DEALER +++++++++++++++++++++");
		System.out.println("CARDS IN PLAY SIZE: " + cards_in_play_before_dealer_size);
		System.out.println("DEALER SCORE: " + Blackjack.dealer_score);
		System.out.println("DEALER CARDS:");
		for(int i=0;i<Blackjack.dealer_cards.size();i++){
			System.out.println(">card: " + Blackjack.dealer_cards.get(i).getName());
		}
		System.out.println("PLAYER SCORE: " + Blackjack.player_score);
		System.out.println("PLAYER CARDS:");
		for(int i=0;i<Blackjack.player_cards.size();i++){
			System.out.println(">card: " + Blackjack.player_cards.get(i).getName());
		}
		System.out.println("-----------------------------------------------------");
    	System.out.println("Ace In Dealer Hand: " + aceInHand(0));
    	System.out.println("Ace In Player Hand: " + aceInHand(1));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("(0=PLAYING|1=BJ|3=WIN|4=LOSE|2=BUST");
		System.out.println("Dealer Game State: " + dealer_state);
		System.out.println("Player Game State: " + player_state);
		if(Blackjack.is_push){
			System.out.println("PUSH ?: TRUE");
		}else{
			System.out.println("PUSH ?: FALSE");
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if(Blackjack.dealer_soft_hand){
 			 System.out.println("Dealer Soft Hand: TRUE");
 		 }else{
 			System.out.println("Dealer Soft Hand: FALSE");
 		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Player Cards SIZE: " + Blackjack.player_cards.size());
		System.out.println("Dealer Cards SIZE: " + Blackjack.dealer_cards.size());
		if(game_over){
			System.out.println("Game Over: TRUE");
		}else{
			System.out.println("Game Over: FALSE");
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
				
				//$$$$
				
					System.out.println("UPDATE $$$$$");
					Blackjack.player_one_total_before_change = Blackjack.player_one_total;
					
					switch(player_state){
					case 0:
						break;
					case 1: //blackjack
						//update score
						if(!Blackjack.is_push){
			        		if(!game.doubledown){
			        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made * 3;//3:1 odds
			        		}else{
			        			//probably never gets called you can't get double down on a blackjack ....
			        			Blackjack.player_one_total += (Blackjack.player_one_last_bet_made * 2) * 3;
			        			game.doubledown = false;
			        		}
						}
						break;
					case 2://BUST
						//set on DealerFlip.java
						break;
					case 3://win
						//update score
		        		if(!game.doubledown){
		        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made;
		        		}else{
		        			Blackjack.player_one_total += Blackjack.player_one_last_bet_made * 2;
		        			game.doubledown = false;
		        		}
						break;
					case 4:
						//update score
		        		if(!game.doubledown){
		        			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made;
		        		}else{
		        			Blackjack.player_one_total -= Blackjack.player_one_last_bet_made * 2;
		        			game.doubledown = false;
		        		}
		        		if(Blackjack.player_one_total < 0)Blackjack.player_one_total = 0;
						break;
					default:
						System.out.println(" ++NOTHING ERROR++");
						break;
					}
					
					//push
					if(Blackjack.is_push){
						//update score
		        		//Blackjack.player_one_total += Blackjack.player_one_last_bet_made;
		        		System.out.println("PUSH:::");
		        		System.out.println("total:" + Blackjack.player_one_total + 
		        				" BET:" + Blackjack.player_one_last_bet_made);
					}
				
		
    }
	
    
    private void checkBlackjack(){
    	

    		//check blackjack push
    		if(Blackjack.dealer_score == 21 && Blackjack.player_score == 21 &&
    				getTurn(0) == 1){
    			Blackjack.is_push = true;
    			// dont check setDealerState(1);
    			dealer_state = 1;
    			// dont check setPlayerState(1);
    			player_state = 1;
    		}else{
    			
    			//dealer
    			if(Blackjack.dealer_score == 21 && getTurn(0) == 1){
    	    			//dont check setDealerState(1);
    			    	dealer_state = 1;
    	    			//dont check setPlayerState(4);
    			    	player_state = 4;
    			}
    		    //player
    			if(Blackjack.player_score == 21 && getTurn(1) == 1){
    	    			//dont check setDealerState(4);
    					dealer_state = 4;
    	    			//dont check setPlayerState(1);
    					player_state = 1;
    			}
    			
    	}
	    
    	
    }
    
    private void checkWin(){
    	
    		
    	if(Blackjack.is_push){
			setDealerState(3);
			setPlayerState(3);
    	}else{
    		//dealer
	    	if(Blackjack.dealer_score < 22 &&
	    			Blackjack.player_score < Blackjack.dealer_score){
    			setDealerState(3);
	    	}
	    	
    		//player
	    	if(Blackjack.player_score < 22 &&
	    			Blackjack.player_score > Blackjack.dealer_score){
    			setPlayerState(3);
	    	}
    	}

    	
    }
    
    private void checkLose(){
    		
    		//dealer
		    if(Blackjack.dealer_score < Blackjack.player_score &&
		    	player_state != 2){
    			setDealerState(4);
		    }
	
		    //player
		    if(Blackjack.player_score < Blackjack.dealer_score &&
		    	dealer_state !=2 ){
    			setPlayerState(4);
		    }
    	
    }
    
    
    private void checkBust(){
    	
    	//player
		if(Blackjack.player_score > 21){
			//setPlayerState(2); //don't check
			player_state = 2;
		}
		    
    	//dealer
		if(Blackjack.dealer_score > 21){
			setPlayerState(3);
			setDealerState(2);
		}
    	
    	
    }
    
    public int getDealerState(){
    	return dealer_state;
    }
    public int getPlayerState(){
    	return player_state;
    }
    public int getSplitDealerState(){
    	return split_dealer_state;
    }
    public int getSplitPlayerState(){
    	return split_player_state;
    }
    
    private void setPlayerState(int state){
    		if(!Blackjack.dealer_flipped){
    			player_state =0;
    		}else{
    			player_state = state;
    		}
    }
    private void setDealerState(int state){
    		if(!Blackjack.dealer_flipped){
    			dealer_state =0;
    		}else{
    			dealer_state = state;
    		}
    }
    
    private void setSplitPlayerState(int state){
		if(!Blackjack.dealer_flipped){
			split_player_state =0;
		}else{
			split_player_state = state;
		}
    }
	private void setSplitDealerState(int state){
			if(!Blackjack.dealer_flipped){
				split_dealer_state =0;
			}else{
				split_dealer_state = state;
			}
	}

    
    public void setScores(int ds,int ps){
    	//probably need to reconfigure this one ...
    	//so far it works ...
    	
    	//dealer
    	if(aceInHand(0) > 1){
    		int check = 11+(aceInHand(0)-1);
    		if((ds + check) > 21 ){
    			ds += check-10;
    		}else{
    			ds += check;
    		}
    		
    	}else if(aceInHand(0) == 1){
    		if((ds + 11) > 21){
    			ds += 1;
    		}else{
    			ds += 11;
    		}
    	}
    		
    	//player
    	if(aceInHand(1) > 1){
    		int check = 11+(aceInHand(1)-1);
    		if((ps + check) > 21 ){
    			ps += check-10;
    		}else{
    			ps += check;
    		}
    		
    	}else if(aceInHand(1) == 1){
    		if((ps + 11) > 21){
    			ps += 1;
    		}else{
    			ps += 11;
    		}
    	}
    	
    	Blackjack.dealer_score = ds;
    	Blackjack.player_score = ps;
    	
    }
    
    //what turn are we on
    public int getTurn(int who){
    	
    	int ds;
    	if(who == 0){
    		ds = Blackjack.dealer_cards.size();
    	}else{
    		ds = Blackjack.player_cards.size();
    	}
    	
    	int turn=0;
    	
    	switch(ds){
    	case 2:
    		turn = 1;
    		break;
    	case 3:
    		turn = 2;
    		break;
    	case 4:
    		turn = 3;
    		break;
    	case 5:
    		turn = 4;
    		break;
    	case 6:
    		turn = 5;
    		break;
    	case 7:
    		turn = 6;
    		break;
    	case 8:
    		turn = 7;
    		break;
    	case 9:
    		turn = 8;
    		break;
    	default:
    		//
    		break;
    	}
    	return turn;
    }
    
    
    //how many aces in your hand
  	public int aceInHand(int playerType){
  		
    	/* ACE REFERENCE
  		0 = ace of clubs
        1 = ace of diamonds
        2 = ace of hearts
        3 = ace of spades
		*/
  		
  		//how many ace in hand
  		int ace=0;
  		if(playerType == 1){
	  		int pcs = Blackjack.player_cards.size();
	  		for(int i=0;i<pcs;i++){
	  			Card card = Blackjack.player_cards.get(i);
	  			if(card.getType() < 4){
	  				ace++;
	  			}
	  		}
  		}else if(playerType == 0){
  			int pcs = Blackjack.dealer_cards.size();
	  		for(int i=0;i<pcs;i++){
	  			Card card = Blackjack.dealer_cards.get(i);
	  			if(card.getType() < 4){
	  				ace++;
	  			}
	  		}
  		}
  		
  		return ace;
  	}
  	
  	
  	
  	//////////////////////////////////////// DEALER TURN //////////////////////////////////////
  	public void dealerHand(int ds, int index){
  			
  			boolean csh = checkSoftHand();
  			
		  	if(!csh && ds > 16 || ds == 21 || ds > 21){
		  		return;
		  	}else{
		  		 
		  		 //hit
		  		 ///////////////////////////////////////////////////////////////////////////////
		  		 /*
		  		 Card dealer_card;
		  		 if(Blackjack.test_mode){
		  			 //TEST
		  			 if(test_var == 0){
		  				 //dealer_card = new Card(Blackjack.current_deck.get(21),0); //3d
		  				 dealer_card = new Card(Blackjack.current_deck.get(32),0); //kc
		  			 }else if(test_var == 1){
		  				//dealer_card = new Card(Blackjack.current_deck.get(22),0); //3
		  				dealer_card = new Card(Blackjack.current_deck.get(5),0); //kd
		  			 }else{
		  				dealer_card = new Card(Blackjack.current_deck.get(index),0);
		  			 }
		  			 test_var++;
		  			 Blackjack.dealer_cards.add(dealer_card);
		  			 
		  		 }else{
		  			//LIVE
		  			dealer_card = new Card(Blackjack.current_deck.get(index),0);
		  			Blackjack.dealer_cards.add(dealer_card);
		  		 }
		  		 */
		  		 ////////////////////////////////////////////////////////////////////////////////
		  		 index++;
		  		 Blackjack.current_deck_array_index++;
		  		 Card dealer_card = new Card(Blackjack.current_deck.get(index),0,game);
	  			 Blackjack.dealer_cards.add(dealer_card);
	  			 
		  		 System.out.println("==========================================");
		  		 System.out.println("New Dealer Card: " + dealer_card.getName());
		  	
		  		 int score = setDealerScore(dealer_card);
		  		 
		  		 System.out.println("New Card Initial Score: " + dealer_card.getValue() );
		  		 System.out.println("New Dealer Score: " + score);
		  		 System.out.println("==========================================");
		  		 
		  		 Blackjack.dealer_score = score;
		  		 dealerHand(score,index);
		  	     return;
		  	     
		  	}
  		
  	}
  	
  	private boolean checkSoftHand(){
  		//HIT ON SOFT 17
  		boolean check=false;
  		if(Blackjack.dealer_soft_hand){
  			if(aceInHand(0) > 0 && Blackjack.dealer_score == 17){
  					check=true; //hit
  			}
  		}
  		return check;
  	}
  	
  	
  	public int setDealerScore(Card card){
    	
  		int current_total = 0;
  		
  		
  		//first get current score
  		for(int i=0;i<Blackjack.dealer_cards.size();i++){
  			Card c = Blackjack.dealer_cards.get(i);
  			current_total += c.getValue(); //includes card argument
  		}
  		
  		//ace check
  		if(aceInHand(0) > 1){
	  		
  			int ace_value = 0; //all aces start at 0 value
  			int check = (aceInHand(0) * 11);
  			if(check > 21){
  				if(aceInHand(0) < 3){
  					if((current_total + 12) > 21){
  						ace_value = 2;
  					}else{
  						ace_value = 12;
  					}
  				}else{
  					ace_value = aceInHand(0);
  				}
  				Blackjack.dealer_soft_hand = true;
  			}
  			current_total += ace_value;
	  			
	  		
  		}else if(aceInHand(0) == 1){
  			
  			if((current_total + 11) > 21){
    			current_total += 1;
    			Blackjack.dealer_soft_hand = true;
    		}else{
    			current_total += 11;
    		}
  			
  		}else{
  			//current_total already set
  		}
  		
  		return current_total;
  		
    }
  	
  	
  	
    //test for split
  	public void detectSplit(){
  		
  		int turn = getTurn(0);
  		if(turn == 1){
	  		Card card1 = Blackjack.player_cards.get(0);
	  		Card card2 = Blackjack.player_cards.get(1);
	  		
	  		int c1 = card1.getGroup();
	  		int c2 = card2.getGroup();
	  		if( c1 == c2){
	  			System.out.println("C1: " + c1);
	  			System.out.println("C2: " + c2);
	  			Blackjack.split_detected = true;
	  		}
	  		
  		}
  		
  	}
  	
  	
  	/////////////////////////////// SPLIT //////////////////////////////////////
  	public void updateSplit(){
	    
  		ArrayList<Card> split = null;
  		ArrayList<Card> dealer_hand = Blackjack.dealer_cards;
  		
  		if(Blackjack.what_split_hand == 1){
  			split = Blackjack.split_one;
  		}else{
  			split = Blackjack.split_two;
  		}
  		
		int split_dealer_score = 0;
		int split_player_score = 0;
		
		int split_hand_size = split.size();
		int dealer_hand_size = dealer_hand.size();
    	
		//player
    	for(int i=0;i<split_hand_size;i++){
    		Card card = split.get(i);
    		split_player_score += card.getValue();
    	}
    	//dealer
    	for(int i=0;i<dealer_hand_size;i++){
    		Card card = dealer_hand.get(i);
    		split_dealer_score += card.getValue();
    	}
    	
    	//set scores
    	setSplitScores(split_dealer_score,
    			split_player_score);
    	
    	
    	//check for 21 // no blackjack in split
    	checkSplit21();
    	
    	//if 21 DONE!
    	if(split_dealer_state == 1 || split_player_state == 1){
    		// do something else if blackjack ...
    	}else{
        	checkSplitBust();
        	checkSplitLose();
        	checkSplitWin();
    	}
    	
    	
    	
    	//check for push here
    	if(Blackjack.what_split_hand == 1){
	    	if(split_dealer_score < 22 &&
			    	split_dealer_score == split_player_score && Blackjack.dealer_done){
	    			
	    		    
	    		    	if(Blackjack.dealer_flipped){
	    		    		Blackjack.hand_one_push = true;
	    		    	}
	    		   
			    	
			}
    	}else{//2
    		if(split_dealer_score < 22 &&
			    	split_dealer_score == split_player_score && Blackjack.dealer_done){
	    			
	    		    
	    		    	if(Blackjack.dealer_flipped){
	    		    		Blackjack.hand_two_push = true;
	    		    	}
	    		   
			    	
			}
    	}
    	
    	
    	/*
    	///////////check
    	int player_score =0;
    	boolean split_push=false;
    	ArrayList<Card> player_cards=null;
    	if(Blackjack.what_split_hand == 1){
    		player_score = Blackjack.split_hand_one_score;
    		split_push = Blackjack.hand_one_push;
    		player_cards = Blackjack.split_one;
    		System.out.println("++++++++++++++++++++ SPLIT GAME HAND ONE +++++++++++++++++++++");
    	}else{
    		player_score = Blackjack.split_hand_two_score;
    		split_push = Blackjack.hand_two_push;
    		player_cards = Blackjack.split_two;
    		System.out.println("++++++++++++++++++++ SPLIT GAME HAND TWO +++++++++++++++++++++");
    	}
    	
		System.out.println("DEALER SCORE: " + Blackjack.split_dealer_score);
		System.out.println("DEALER CARDS:");
		for(int i=0;i<Blackjack.dealer_cards.size();i++){
			System.out.println(">card: " + Blackjack.dealer_cards.get(i).getName());
		}
		System.out.println("PLAYER SCORE: " + player_score);
		System.out.println("PLAYER CARDS:");
		for(int i=0;i<player_cards.size();i++){
			System.out.println(">card: " + player_cards.get(i).getName());
		}
		System.out.println("-----------------------------------------------------");
    	System.out.println("Ace In Dealer Hand: " + aceInSplitHand(0));
    	System.out.println("Ace In Player Hand: " + aceInSplitHand(1));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("(0=PLAYING|1=21|3=WIN|4=LOSE|2=BUST");
		System.out.println("Dealer Game State: " + split_dealer_state);
		System.out.println("Player Game State: " + split_player_state);
		if(split_push){
			System.out.println("PUSH ?: TRUE");
		}else{
			System.out.println("PUSH ?: FALSE");
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("Player Cards SIZE: " + player_cards.size());
		System.out.println("Dealer Cards SIZE: " + Blackjack.dealer_cards.size());
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		
		/*
		System.out.println(">>>>>>>>> SPLIT HAND ONE: ");
		if(Blackjack.split_hand_one_21){
			System.out.println("SPLIT HAND ONE 21");
		}else if(Blackjack.split_hand_one_bust){
			System.out.println("SPLIT HAND ONE BUST");
		}else if(Blackjack.split_hand_one_lose){
			System.out.println("SPLIT HAND ONE LOSE");
		}else if(Blackjack.split_hand_one_win){
			System.out.println("SPLIT HAND ONE WIN");
		}
		System.out.println(">>>>>>>>> SPLIT HAND TWO: ");
		if(Blackjack.split_hand_two_21){
			System.out.println("SPLIT HAND TWO 21");
		}else if(Blackjack.split_hand_two_bust){
			System.out.println("SPLIT HAND TWO BUST");
		}else if(Blackjack.split_hand_two_lose){
			System.out.println("SPLIT HAND TWO LOSE");
		}else if(Blackjack.split_hand_two_win){
			System.out.println("SPLIT HAND TWO WIN");
		}
		*/
		
    }
  	
  	public void setSplitScores(int ds,int ps){

    	//dealer
    	if(aceInSplitHand(0) > 1){
    		int check = 11+(aceInSplitHand(0)-1);
    		if((ds + check) > 21 ){
    			ds += check-10;
    		}else{
    			ds += check;
    		}
    		
    	}else if(aceInSplitHand(0) == 1){
    		if((ds + 11) > 21){
    			ds += 1;
    		}else{
    			ds += 11;
    		}
    	}
    		
    	//player
    	if(aceInSplitHand(1) > 1){
    		int check = 11+(aceInSplitHand(1)-1);
    		if((ps + check) > 21 ){
    			ps += check-10;
    		}else{
    			ps += check;
    		}
    		
    	}else if(aceInSplitHand(1) == 1){
    		if((ps + 11) > 21){
    			ps += 1;
    		}else{
    			ps += 11;
    		}
    	}
    	
    	
    	
        Blackjack.split_dealer_score = ds;
        
        if(Blackjack.what_split_hand == 1){
        	Blackjack.split_hand_one_score = ps;
  		}else{//2
  			Blackjack.split_hand_two_score = ps;
  		}
        
        ///the rest//////////////////////////////////////
        int dsc = Blackjack.split_dealer_score;
		int ps1 = Blackjack.split_hand_one_score;
		int ps2 = Blackjack.split_hand_two_score;
		
		System.out.println("DEALER SCORE: " + dsc);
		System.out.println("PLAYER SCORE: " + ps1);
		System.out.println("PLAYER 2 SCORE: " + ps2);
		
		
		if(Blackjack.dealer_done){
			if(dealer_win(dsc,ps1,ps2)){
				Blackjack.split_dealer_win = true;
			}
		}
		
		
    	
    }
  	
  	private boolean dealer_win(int ds,int p1s, int p2s){
  		
  		boolean result = false;
  		boolean push_bust = false;
  		boolean p1bust = false;
  		boolean p2bust = false;
  		 
  		//check bust
  		if(p1s > 21){ p1bust = true;}
  		if(p2s > 21){ p2bust = true;}
  		
  		
  			//check push
  			if(ds == p1s){
  				push_bust = true;
  			}
  			if(ds == p2s){
  				push_bust = true;
  			}
  			if(ds > 21){
  				push_bust = true;
  			}
  		
  		if(ds > p1s && !push_bust || ds > p2s && !push_bust){
			if(p2s > 21 && p2s > 21){
				result = true;
				System.out.println("DWIN SET HERE 1");
			}else if(p2s < ds && p1s < ds){
				result = true;
				System.out.println("DWIN SET HERE 2");
			}else if(p1bust && p2s < ds){
				result = true;
				System.out.println("DWIN SET HERE 3");
			}else if(p2bust && p1s < ds){
				result = true;
				System.out.println("DWIN SET HERE 4");
			}
		}
  		
  		return result;
  	}
  	
  	
    //how many aces in your hand
  	public int aceInSplitHand(int playerType){
  		
    	/* ACE REFERENCE
  		0 = ace of clubs
        1 = ace of diamonds
        2 = ace of hearts
        3 = ace of spades
		*/
  		
  		ArrayList<Card> split = null;
  		if(Blackjack.what_split_hand == 1){
  			split = Blackjack.split_one;
  		}else{//2
  			split = Blackjack.split_two;
  		}
  		
  		int ace=0;
  		if(playerType == 1){
	  		int pcs = split.size();
	  		for(int i=0;i<pcs;i++){
	  			Card card = split.get(i);
	  			if(card.getType() < 4){
	  				ace++;
	  			}
	  		}
  		}else if(playerType == 0){
  			int pcs = Blackjack.dealer_cards.size();
	  		for(int i=0;i<pcs;i++){
	  			Card card = Blackjack.dealer_cards.get(i);
	  			if(card.getType() < 4){
	  				ace++;
	  			}
	  		}
  		}
  		
  		return ace;
  	}
  	
  	
  	
  	private void checkSplit21(){
    	
  		int player_score = 0;
  		
  		if(Blackjack.what_split_hand == 1){
  			player_score = Blackjack.split_hand_one_score;
  		}else{//2
  			player_score = Blackjack.split_hand_two_score;
  		}

		//check blackjack push
		if(Blackjack.split_dealer_score == 21 && 
				player_score == 21){
			
			if(Blackjack.what_split_hand == 1){
				Blackjack.hand_one_push = true;
			}else{
				Blackjack.hand_two_push = true;
			}
			split_dealer_state = 1;
			split_player_state = 1;
			
		}else{
			
			//dealer
			if(Blackjack.split_dealer_score == 21){
			    	split_dealer_state = 1;
			}
		    //player
			if(player_score == 21){
					split_player_state = 1;
			}
			
		}
    
  	}
  	
  	
  	//what turn are we on
    public int getSplitTurn(int who){
    	
    	int ds;
    	if(who == 0){
    		ds = Blackjack.dealer_cards.size();
    	}else{
    		
    		if(Blackjack.what_split_hand == 1){
    			ds = Blackjack.split_one.size();
    		}else{
    			ds = Blackjack.split_two.size();
    		}
    	}
    	
    	int turn=0;
    	
    	switch(ds){
    	case 2:
    		turn = 1;
    		break;
    	case 3:
    		turn = 2;
    		break;
    	case 4:
    		turn = 3;
    		break;
    	case 5:
    		turn = 4;
    		break;
    	case 6:
    		turn = 5;
    		break;
    	case 7:
    		turn = 6;
    		break;
    	case 8:
    		turn = 7;
    		break;
    	case 9:
    		turn = 8;
    		break;
    	default:
    		//
    		break;
    	}
    	return turn;
    }
  	
    
    private void checkSplitBust(){
    	
    	int player_score = 0;
    	
    	if(Blackjack.what_split_hand == 1){
    		player_score = Blackjack.split_hand_one_score;
    	}else{//2
    		player_score = Blackjack.split_hand_two_score;
    	}
    	
    	//player
		if(player_score > 21){
			split_player_state = 2;
		}
		    
    	//dealer
		if(Blackjack.split_dealer_score > 21){
			setSplitPlayerState(3);
			setSplitDealerState(2);
		}
    	
    	
    }
    
    private void checkSplitLose(){
		
    	int player_score = 0;
    	
    	if(Blackjack.what_split_hand == 1){
    		player_score = Blackjack.split_hand_one_score;
    	}else{//2
    		player_score = Blackjack.split_hand_two_score;
    	}
    	
		//dealer
	    if(Blackjack.split_dealer_score < player_score &&
	    	split_player_state != 2){
			setSplitDealerState(4);
	    }

	    //player
	    if(player_score < Blackjack.split_dealer_score &&
	    	split_dealer_state !=2 ){
			setSplitPlayerState(4);
	    }
	
    }
    
    
    private void checkSplitWin(){
    	
    	boolean hand_push = false;
    	if(Blackjack.what_split_hand == 1){
    		hand_push = Blackjack.hand_one_push;
    	}else{//2
    		hand_push = Blackjack.hand_two_push;
    	}
    	int player_score = 0;
    	if(Blackjack.what_split_hand == 1){
    		player_score = Blackjack.split_hand_one_score;
    	}else{//2
    		player_score = Blackjack.split_hand_two_score;
    	}
    	
		
    	if(hand_push){
			setSplitDealerState(3);
			setSplitPlayerState(3);
    	}else{
    		//dealer
	    	if(Blackjack.split_dealer_score < 22 &&
	    			player_score < Blackjack.split_dealer_score){
    			setSplitDealerState(3);
	    	}
	    	
    		//player
	    	if(player_score < 22 &&
	    			player_score > Blackjack.split_dealer_score){
    			setSplitPlayerState(3);
    			if(Blackjack.what_split_hand == 1){
    	    		Blackjack.split_hand_one_win = true;
    	    	}else{//2
    	    		Blackjack.split_hand_two_win = true;
    	    	}
	    	}
    	}

    	
    }
  	
  	

}
