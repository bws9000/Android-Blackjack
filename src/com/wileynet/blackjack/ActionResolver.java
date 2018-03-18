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

//Android Interface
public interface ActionResolver {
	   
	   public void showOrLoadInterstital();
	
	   //INAPP BILLING METHODS///////////////////////////////////////////////////////
	   public void createConnection();
	   public void buyTokens();
	   public void callFinish();
	   
	
	   //ANDROID METHODS ////////////////////////////////////////////////////////////
	   public void openAdActivity(boolean isWifi);
	   public long getAndroidTime();
	   public void countdown();
	   
	   public void killMyProcess();
	   
	   public void showToast(CharSequence message, int toastDuration);
	   
	   public void showShortToast(CharSequence toastMessage);
	   
	   public void showLongToast(CharSequence toastMessage);
	   
	   public void showAlertBox(String alertBoxTitle, 
			   String alertBoxMessage, String alertBoxButtonText);
	   
	   public void showConfirmAlert(String alertBoxTitle, 
			   String alertBoxMessage, String positiveButton, String negativeButton);
	   
	   public void confirmexit();
	   
	   public void submitLevel();
	   
	   public void showMyList();
	   
	   public void openUri(String url);

	   
}
