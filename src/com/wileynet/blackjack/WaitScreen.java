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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class WaitScreen implements Screen {
	
	final Blackjack game;
    OrthographicCamera camera;
    boolean buyTokens = false;
    
    //Wait Screen for Google InApp Billing
    
	public WaitScreen(final Blackjack gam){
		
		this.game = gam;
		
		// create the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        
        float delay = 0.5f; // seconds
        Timer.schedule(new Task(){
            @Override
            public void run() {
            	
            	int split=0;
            	if(game.play_mode)split=1;
            	Blackjack.close_buybutton = true;
            	game.setScreen(new BillingScreen(game,split));
            	
            	
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
        
        //wait screen graphic
        game.ui_sprites[47].setPosition(0, 0);
        game.ui_sprites[47].draw(game.batch);
        
        
        //make sure back graphic shows up
        if(!buyTokens){
        	buyTokens = true;
        	
            //PURCHASE HERE
            game.actionResolver.buyTokens();
            
        }
        
        game.batch.end();
        
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
