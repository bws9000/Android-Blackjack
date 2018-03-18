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

public class SettingsScreen implements Screen {
		
        final Blackjack game;

        OrthographicCamera camera;

        public SettingsScreen(final Blackjack gam) {
        	    
        		//Blackjack.screen_state = ;
        		Gdx.input.setCatchBackKey(true);
        	
                game = gam;

                camera = new OrthographicCamera();
                camera.setToOrtho(false, 480, 800);
                
                
        }

        @Override
        public void render(float delta) {
        		
                Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camera.update();
                game.batch.setProjectionMatrix(camera.combined);

                game.batch.begin();
                
                
                //background
                game.ui_sprites[47].setPosition(0, 0);
                game.ui_sprites[47].draw(game.batch);
                
                //sound settings
                game.settings_sound_back.setPosition(0,525);
                game.settings_sound_back.draw(game.batch);
                
                if(Blackjack.play_sounds){
                	game.settings_sound_on.setPosition(407,540);
                	game.settings_sound_on.draw(game.batch);
                	game.settings_sound_on_copy.setPosition(357,547);
            		game.settings_sound_on_copy.draw(game.batch);
                }else{
                	game.settings_sound_off.setPosition(408,540);
                	game.settings_sound_off.draw(game.batch);
                	game.settings_sound_off_copy.setPosition(342,543);
            		game.settings_sound_off_copy.draw(game.batch);
                }
                
                //dealer hit back settings
                game.settings_dealer_hit_back.setPosition(0,358);
                game.settings_dealer_hit_back.draw(game.batch);
                
                
                if(Blackjack.dealer_hits_soft_17){
                	game.settings_dhit_on.setPosition(407, 372);
                	game.settings_dhit_on.draw(game.batch);
                	game.settings_dhit_on_copy.setPosition(357, 379);
                	game.settings_dhit_on_copy.draw(game.batch);
                }else{
                	game.settings_dhit_off.setPosition(408, 372);
                	game.settings_dhit_off.draw(game.batch);
                	game.settings_dhit_off_copy.setPosition(342, 375);
                	game.settings_dhit_off_copy.draw(game.batch);
                }
                
                //card dealt settings
                game.settings_card_dealt_back.setPosition(0,293);
                game.settings_card_dealt_back.draw(game.batch);
                
                if(Blackjack.inline_card_deal){
                	game.settings_dealt_on.setPosition(407, 307);
                	game.settings_dealt_on.draw(game.batch);
                	game.settings_dealt_on_copy.setPosition(357, 314);
                	game.settings_dealt_on_copy.draw(game.batch);
                }else{
                	game.settings_dealt_off.setPosition(408, 307);
                	game.settings_dealt_off.draw(game.batch);
                	game.settings_dealt_off_copy.setPosition(342, 311);
                	game.settings_dealt_off_copy.draw(game.batch);
                }
                
                
                //decks settings
                game.settings_deck_back.setPosition(0,425);
                game.settings_deck_back.draw(game.batch);
                //2
                game.settings_num_two.setPosition(25, 435);
                game.settings_num_two.draw(game.batch);
                if(Blackjack.hmd_2){
                	game.settings_deck_2_on.setPosition(50, 436);
                	game.settings_deck_2_on.draw(game.batch);
                }else{
                	game.settings_deck_2_off.setPosition(51, 436);
                	game.settings_deck_2_off.draw(game.batch);
                }
                //3
                game.settings_num_three.setPosition(115, 435);
                game.settings_num_three.draw(game.batch);
                if(Blackjack.hmd_3){
                	game.settings_deck_3_on.setPosition(140, 436);
                	game.settings_deck_3_on.draw(game.batch);
                }else{
                	game.settings_deck_3_off.setPosition(141, 436);
                	game.settings_deck_3_off.draw(game.batch);
                }
                //4
                game.settings_num_four.setPosition(205, 435);
                game.settings_num_four.draw(game.batch);
                if(Blackjack.hmd_4){
                	game.settings_deck_4_on.setPosition(230, 436);
                	game.settings_deck_4_on.draw(game.batch);
                }else{
                	game.settings_deck_4_off.setPosition(231, 436);
                	game.settings_deck_4_off.draw(game.batch);
                }
                //5
                game.settings_num_five.setPosition(296, 435);
                game.settings_num_five.draw(game.batch);
                if(Blackjack.hmd_5){
                	game.settings_deck_5_on.setPosition(320, 436);
                	game.settings_deck_5_on.draw(game.batch);
                }else{
                	game.settings_deck_5_off.setPosition(321, 436);
                	game.settings_deck_5_off.draw(game.batch);
                }
                //6
                game.settings_num_six.setPosition(382, 435);
                game.settings_num_six.draw(game.batch);
                if(Blackjack.hmd_6){
                	game.settings_deck_6_on.setPosition(407, 436);
                	game.settings_deck_6_on.draw(game.batch);
                }else{
                	game.settings_deck_6_off.setPosition(408, 436);
                	game.settings_deck_6_off.draw(game.batch);
                }
                
                game.settingsFont.draw(game.batch, "Play Sounds",27 ,572);
                game.settingsFont.draw(game.batch, "How many decks",27 ,510);
                game.settingsFont.draw(game.batch, "Dealer hit soft 17 ",27 ,403);
                game.settingsFont.draw(game.batch, "Deal cards neatly ",27 ,339);
                
                //green back arrow
                game.greenbackarrow_one.setPosition(410, 245);
                game.greenbackarrow_one.draw(game.batch);
                
                //more games
                game.peeler1.setPosition(266,635);
                game.peeler1.draw(game.batch);
                
                game.batch.end();
                
              	
        	    if (Gdx.input.justTouched()) {
                    Vector3 touchpoint = new Vector3();
                    touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchpoint);
                    
                    
                    //more games
                    if( (touchpoint.x > 293) &&
                    		(touchpoint.y > 650 && touchpoint.y < Blackjack.screen_top)){
                    	
                    	//game.actionResolver.openAdActivity(game.isWifi);//applift
                    	((ActionResolver) game.actionResolver2).showOrLoadInterstital();
                    	
                    }
                    
                    
                    //sound
                    if( (touchpoint.x > 415 && touchpoint.x < 460) &&
                    		(touchpoint.y > 535 && touchpoint.y < 580)){
                    	
                    	if(Blackjack.play_sounds){
                    		Blackjack.play_sounds = false;
                    	}else{
                    		Blackjack.play_sounds = true;
                    	}
                    }
                    
                    //dealer hit
                    if( (touchpoint.x > 415 && touchpoint.x < 460) &&
                    		(touchpoint.y > 372 && touchpoint.y < 410)){
                    	
                    	if(Blackjack.dealer_hits_soft_17){
                    		Blackjack.dealer_hits_soft_17 = false;
                    	}else{
                    		Blackjack.dealer_hits_soft_17 = true;
                    	}
                    }
                    
                    //inline deal
                    if( (touchpoint.x > 415 && touchpoint.x < 460) &&
                    		(touchpoint.y > 297 && touchpoint.y < 344)){
                    	
                    	if(Blackjack.inline_card_deal){
                    		Blackjack.inline_card_deal = false;
                    	}else{
                    		Blackjack.inline_card_deal = true;
                    	}
                    }
                    
                    
                    //decks
                    //2
                    if( (touchpoint.x > 50 && touchpoint.x < 100) &&
                    		(touchpoint.y > 427 && touchpoint.y < 476)){
                    	
                    	if(Blackjack.hmd_2){
                    		Blackjack.hmd_2 = false;
                    	}else{
                    		Blackjack.hmd_2 = true;
                    		Blackjack.how_many_decks = 2;
                    	}
                    	checkDeck2();
                    	
                    }
                    //3
                    if( (touchpoint.x > 140 && touchpoint.x < 190) &&
                    		(touchpoint.y > 427 && touchpoint.y < 476)){
                    	
                    	if(Blackjack.hmd_3){
                    		Blackjack.hmd_3 = false;
                    	}else{
                    		Blackjack.hmd_3 = true;
                    		Blackjack.how_many_decks = 3;
                    	}
                    	checkDeck3();
                    	
                    }
                    //4
                    if( (touchpoint.x > 235 && touchpoint.x < 280) &&
                    		(touchpoint.y > 427 && touchpoint.y < 476)){
                    	
                    	if(Blackjack.hmd_4){
                    		Blackjack.hmd_4 = false;
                    	}else{
                    		Blackjack.hmd_4 = true;
                    		Blackjack.how_many_decks = 4;
                    	}
                    	checkDeck4();
                    	
                    }
                    //5
                    if( (touchpoint.x > 330 && touchpoint.x < 370) &&
                    		(touchpoint.y > 427 && touchpoint.y < 476)){
                    	
                    	if(Blackjack.hmd_5){
                    		Blackjack.hmd_5 = false;
                    	}else{
                    		Blackjack.hmd_5 = true;
                    		Blackjack.how_many_decks = 5;
                    	}
                    	checkDeck5();
                    }
                    
                    //6
                    if( (touchpoint.x > 410 && touchpoint.x < 450) &&
                    		(touchpoint.y > 427 && touchpoint.y < 476)){
                    	
                    	if(Blackjack.hmd_6){
                    		Blackjack.hmd_6 = false;
                    	}else{
                    		Blackjack.hmd_6 = true;
                    		Blackjack.how_many_decks = 6;
                    	}
                    	checkDeck6();
                    }
                    
                    //back button
                    if( (touchpoint.x > 395 && touchpoint.x < 460) &&
                    		(touchpoint.y > 235 && touchpoint.y < 290)){
                    	game.setScreen(new HomeScreen(game));
                    	checkAll();
                    }
                    
        	    }
        	    
        	    /*
        	    if (Gdx.input.isKeyPressed(Keys.BACK)){
        	    	game.setScreen(new HomeScreen(game));
        	    	checkAll();
        	    	//game.actionResolver.killMyProcess();
        	    }
        	    */
                
        }
        
        public void checkAll(){
        	if(!Blackjack.hmd_2 && !Blackjack.hmd_3 && !Blackjack.hmd_4 &&
        		!Blackjack.hmd_5 && !Blackjack.hmd_6){
        		Blackjack.hmd_6 = true;
        		Blackjack.how_many_decks = 6;
        	}
        }
        public void checkDeck6(){
        	if(Blackjack.hmd_6)
        		Blackjack.hmd_5 = false;
        		Blackjack.hmd_4 = false;
        		Blackjack.hmd_3 = false;
        		Blackjack.hmd_2 = false;
        }
        public void checkDeck5(){
        	if(Blackjack.hmd_5)
        		Blackjack.hmd_6 = false;
        		Blackjack.hmd_4 = false;
        		Blackjack.hmd_3 = false;
        		Blackjack.hmd_2 = false;
        }
        public void checkDeck4(){
        	if(Blackjack.hmd_4)
        		Blackjack.hmd_6 = false;
        		Blackjack.hmd_5 = false;
        		Blackjack.hmd_3 = false;
        		Blackjack.hmd_2 = false;
        }
        public void checkDeck3(){
        	if(Blackjack.hmd_3)
        		Blackjack.hmd_6 = false;
        		Blackjack.hmd_4 = false;
        		Blackjack.hmd_5 = false;
        		Blackjack.hmd_2 = false;
        }
        public void checkDeck2(){
        	if(Blackjack.hmd_2)
        		Blackjack.hmd_6 = false;
        		Blackjack.hmd_4 = false;
        		Blackjack.hmd_3 = false;
        		Blackjack.hmd_5 = false;
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
