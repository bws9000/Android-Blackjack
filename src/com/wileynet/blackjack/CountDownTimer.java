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

import com.badlogic.gdx.utils.TimeUtils;

public class CountDownTimer{
    private long start;
    private long secsToWait;

    public CountDownTimer(long secsToWait){
        this.secsToWait = secsToWait;
    }

    public void start(){
        start = TimeUtils.millis() / 1000;
    }

    public boolean hasCompleted(){
        return TimeUtils.millis() / 1000 - start >= secsToWait;
    }
}