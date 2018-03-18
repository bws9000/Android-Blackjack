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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MainMenuScreen implements Screen {
//public class MainMenuScreen implements Screen, InputProcessor {
		
        final Blackjack game;

        OrthographicCamera camera;

        public MainMenuScreen(final Blackjack gam) {
        		Blackjack.screen_state = 0;
        		
                game = gam;

                camera = new OrthographicCamera();
                camera.setToOrtho(false, 480, 800);
                
                Gdx.input.setCatchBackKey(true);
                
                //Gdx.input.setInputProcessor(this);
                
                //game.actionResolver.openAdActivity(); //ad
                
                //delay here also, too fast !
            	float delay = 1.5f; // seconds
                Timer.schedule(new Task(){
                    @Override
                    public void run() {
                    	game.setScreen(new HomeScreen(game));
                        //dispose();
                    }
                }, delay);

        }

        @Override
        public void render(float delta) {
        		
                Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 0.00f);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                camera.update();
                game.batch.setProjectionMatrix(camera.combined);

                game.batch.begin();

                game.ui_sprites[45].draw(game.batch);
                
                game.batch.end();
                
                
                if (Gdx.input.isTouched()) {
                	Vector3 touchpoint = new Vector3();
                    touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchpoint);
                    
                	if( (touchpoint.x > 0 && touchpoint.x < 500) &&
                    		(touchpoint.y > 0 && touchpoint.y < 254)){
                		game.setScreen(new HomeScreen(game));
                	}
                	if( (touchpoint.x > 0 && touchpoint.x < 500) &&
                    		(touchpoint.y > 455 && touchpoint.y < 650)){
                		game.setScreen(new HomeScreen(game));
                	}
                    
                }
                
               
                if (Gdx.input.isKeyPressed(Keys.BACK)){
                	//game.actionResolver.openAdActivity();//appnext
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
		
		
		/*
		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			game.setScreen(new HomeScreen(game));
            return true;
		}
		*/

}
