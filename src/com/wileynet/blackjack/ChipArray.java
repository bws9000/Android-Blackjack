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

public class ChipArray {
	//animated chips
	
	final Blackjack game;
	
	public ArrayList<Chip>chipsInPlay = new ArrayList<Chip>();
	
	private int max_chip_stack = 4;
	
	private int c100;
	private int c50;
	private int c25;
	private int c10;
	private int c5;
	
	public ChipArray(final Blackjack gam){
		this.game = gam;
	}
	
	public void addChip(int type){
		
		if(!checkChipTotal()){
			
			switch(type){
				case 100:
					if(c100 <= max_chip_stack){
						chipsInPlay.add(new Chip(1,100,game));
						c100++;
					}
					break;
				case 50:
					if(c50 <= max_chip_stack){
						chipsInPlay.add(new Chip(1,50,game));
						c50++;
					}
					break;
				case 25:
					if(c25 <= max_chip_stack){
						chipsInPlay.add(new Chip(1,25,game));
						c25++;
					}
					break;
				case 10:
					if(c10 <= max_chip_stack){
						chipsInPlay.add(new Chip(1,10,game));
						c10++;
					}
					break;
				case 5:
					if(c5 <= max_chip_stack){
						chipsInPlay.add(new Chip(1,5,game));
						c5++;
					}
					break;
			}
		}
	}
	
	//basically cosmetic
	public void deleteChip(int type){
		for(int i=0;i<chipsInPlay.size();i++){
			if(chipsInPlay.get(i).getType() == type){
				chipsInPlay.remove(i);
			}
		}
	}
	
	
	// is the chip total greater
	public boolean checkChipTotal(){
		
		int t = 0;
		
		for(int i=0;i<chipsInPlay.size();i++){
			t += chipsInPlay.get(i).getType();
		}
		
		if(t >= Blackjack.player_one_total){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	// is the chip total greater
    public int getChipTotal(){
			
			int t = 0;
			
			for(int i=0;i<chipsInPlay.size();i++){
				t += chipsInPlay.get(i).getType();
			}
			
			return t;
			
	}
    
	
	public void sortit(){
		
		//so chips don't overlap when stacked
		
		ArrayList<Chip>new100 = new ArrayList<Chip>();
		ArrayList<Chip>new50 = new ArrayList<Chip>();
		ArrayList<Chip>new25 = new ArrayList<Chip>();
		ArrayList<Chip>new10 = new ArrayList<Chip>();
		ArrayList<Chip>new5 = new ArrayList<Chip>();
		
		for(int i=0;i<chipsInPlay.size();i++){
		    
			if(chipsInPlay.get(i).getType() == 100){
				new100.add(chipsInPlay.get(i));
			}
			if(chipsInPlay.get(i).getType() == 50){
				new50.add(chipsInPlay.get(i));
			}
			if(chipsInPlay.get(i).getType() == 25){
				new25.add(chipsInPlay.get(i));
			}
			if(chipsInPlay.get(i).getType() == 10){
				new10.add(chipsInPlay.get(i));
			}
			if(chipsInPlay.get(i).getType() == 5){
				new5.add(chipsInPlay.get(i));
			}
		}
		
		
		chipsInPlay.clear();
		
		
		for(int i=0;i<new100.size();i++){
			chipsInPlay.add(new100.get(i));
		}
		for(int i=0;i<new50.size();i++){
			chipsInPlay.add(new50.get(i));
		}
		for(int i=0;i<new25.size();i++){
			chipsInPlay.add(new25.get(i));
		}
		for(int i=0;i<new10.size();i++){
			chipsInPlay.add(new10.get(i));
		}
		for(int i=0;i<new5.size();i++){
			chipsInPlay.add(new5.get(i));
		}
		
	}
	
	public ArrayList<Chip> getChips(){
		sortit();
		return this.chipsInPlay;
	}
	
	public void clearChipArray(){
		
		
		c100=0;
		c50=0;
		c25=0;
		c10=0;
		c5=0;
		
		
		chipsInPlay.clear();
	}
	
	public void resetChipCount(){
		
		c100=0;
		c50=0;
		c25=0;
		c10=0;
		c5=0;
		
	}
	
}
