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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wileynet.blackjack.db.DatabaseDesktop;
import com.wileynet.blackjack.internal.InternalDesktopStorage;

public class Main {
	
	static boolean isWifi = false; //dummy not in use anyway ...
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Blackjack";
		cfg.width = 480;
		cfg.height = 800;
		
		Object objdummy = new Object();
		
		//new LwjglApplication(new Blackjack(), cfg);
		new LwjglApplication(new Blackjack(new DatabaseDesktop(),
				new InternalDesktopStorage(),0,new ActionResolverDesktop(),isWifi, objdummy),cfg);
	}
	
}
