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

public class HomeScreen implements Screen {
		
        final Blackjack game;

        OrthographicCamera camera;

        public HomeScreen(final Blackjack gam) {
        		//Blackjack.screen_state = 10;
        		Gdx.input.setCatchBackKey(true);
        		
                game = gam;

                camera = new OrthographicCamera();
                camera.setToOrtho(false, 480, 800);
                
                
                
        		
                //game.appnextads.buttonAddMoreGamesLeft();
                //game.actionResolver.openAdActivity();
                
        }

        @Override
        public void render(float delta) {
        		
                Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camera.update();
                game.batch.setProjectionMatrix(camera.combined);

                game.batch.begin();

                game.ui_sprites[47].draw(game.batch);
                game.home_screen.draw(game.batch);
                
                game.peeler1.setPosition(266,635);
                game.peeler1.draw(game.batch);
                
                //author
                game.settingsFont.draw(game.batch, "Game Developer: " , 90 , 575);
                game.settingsFont.draw(game.batch, "Burt Wiley Snyder " , 90 , 550);
                game.settingsFont.draw(game.batch, "armorsoft@gmail.com" , 90 , 525);
                game.settingsFont.draw(game.batch, "github.com/bws9000" , 90 , 500);
                
                game.batch.end();
                

                
                //input
        	    if (Gdx.input.justTouched()) {
                    Vector3 touchpoint = new Vector3();
                    touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchpoint);
                    
                    //more games
                    if( (touchpoint.x > 293) &&
                    		(touchpoint.y > 650 && touchpoint.y < Blackjack.screen_top)){
                    	
                    	if(Blackjack.application_type == 1){
                    		//game.actionResolver.openAdActivity(game.isWifi);//applift
                    		((ActionResolver) game.actionResolver2).showOrLoadInterstital();
                    	}else if(Blackjack.application_type == 0){
                    		game.actionResolver.openUri("http://casinogames.wileynet.com/out.php");
                    	}
                    	
                    }
                    
                    
                    //settings
                    if( (touchpoint.x > 85 && touchpoint.x < 400) &&
                    		(touchpoint.y > 354 && touchpoint.y < 444)){
                    	
                    	game.setScreen(new SettingsScreen(game));
                    	
                    }
                    
                    
                    //play button
                    if( (touchpoint.x > 85 && touchpoint.x < 400) &&
                    		(touchpoint.y > 254 && touchpoint.y < 344)){
                    	
                    	//System.out.println("play");
                    	//game.setScreen(new DealScreen(game));
                    	Blackjack.home_screen_viewed = true;
                    	Blackjack.chipsAnimationFinished = true;
                    	
                    	switch(Blackjack.screen_state){
                    	case 0:
                    		//Blackjack.home_screen_viewed = true;
                    		game.setScreen(new DealScreen(game));
                		case 1:
                			//game.setScreen(new GameScreen(game));
                			break;
                		case 2:
                			//Blackjack.home_screen_viewed = true;
                			game.setScreen(new DealScreen(game));
                			break;
                		case 3:
                			//game.setScreen(new Hit(game));
                			break;
                		case 4:
                			//game.setScreen(new DealerFlip(game));
                			break;
                		case 5:
                			//Blackjack.home_screen_viewed = true;
                			game.setScreen(new ScoreScreen(game));
                			break;
                		case 6:
                			//Blackjack.home_screen_viewed = true;
                			game.setScreen(new SplitScreen(game));
                			break;
                		case 7:
                			//Blackjack.home_screen_viewed = true;
                			game.setScreen(new SplitScoreScreen(game));
                			break;
                		case 8:
                			//Blackjack.home_screen_viewed = true;
                			game.setScreen(new BetMenu(game,Blackjack.bet_normal_or_split));
                			break;
                		case 9:
                			//game.setScreen(new BillingScreen(this,bet_normal_or_split));
                			break;
                		}
                    }
                    
                    
        	    }
        	    
        	    if (Gdx.input.isKeyPressed(Keys.BACK)){
        	    	game.actionResolver.killMyProcess();
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
