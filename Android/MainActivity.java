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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;

import android.widget.RelativeLayout;

import android.widget.RelativeLayout.LayoutParams;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wileynet.blackjack.db.DatabaseAndroid;
import com.wileynet.blackjack.internal.InternalAndroidStorage;

public class MainActivity extends AndroidApplication implements ActionResolver {
	
	public RelativeLayout layout;
	public AdView admobView;
	
	//admob
	private boolean enable_admob = true;
	//private static final String AD_UNIT_ID_BANNER = "";
	private static final String AD_UNIT_ID_BANNER = "";
	//private static final String AD_UNIT_ID_INTERSTITIAL = "";
	private static final String AD_UNIT_ID_INTERSTITIAL = "";
	
	public AdView adView;
	public View gameView;
	public InterstitialAd interstitialAd;
	
	
	//InnApp Billing
    Activity rootActivity = this;
    boolean status = false;
    boolean isWifi = false;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	if(isWifi()){
    		isWifi = true;
    	}
    	 
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        //cfg.useGL20 = false;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        
        if(!enable_admob){
        	
        	/*New Game*/
            initialize(new Blackjack(new DatabaseAndroid(this.getBaseContext()),
            		new InternalAndroidStorage(this.getBaseContext()),1, 
            		
            		new ActionResolverAndroid(this.getBaseContext(),
            				new DatabaseAndroid(this.getBaseContext()),
            				new InternalAndroidStorage(this.getBaseContext()),rootActivity),isWifi, 
            				this),cfg);
            
        }else{
            
        	 // Do the stuff that initialize() would do for you
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	
	        layout = new RelativeLayout(this);
	        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
	        		RelativeLayout.LayoutParams.MATCH_PARENT);
	        layout.setLayoutParams(params);
	
	        admobView = createAdView();
	        layout.addView(admobView);
	        View gameView = createGameView(cfg);
	        layout.addView(gameView);
	
	        setContentView(layout);
	        startAdvertising(admobView);
	        
	        interstitialAd = new InterstitialAd(this);
	        interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
	        interstitialAd.setAdListener(new AdListener() {
	          @Override
	          public void onAdLoaded() {
	            //Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
	            showOrLoadInterstital();
	          }
	          @Override
	          public void onAdClosed() {
	            //Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
	          }
	        });
	        
        }
        
        	    
        
    } 
    
	//admob
	private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID_BANNER);
        adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
        		LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
      }

      private View createGameView(AndroidApplicationConfiguration cfg) {
        
        /*New Game*/
        gameView = initializeForView(new Blackjack(new DatabaseAndroid(this.getBaseContext()),
        		new InternalAndroidStorage(this.getBaseContext()),1, 
        		
        		new ActionResolverAndroid(this.getBaseContext(),
        				new DatabaseAndroid(this.getBaseContext()),
        				new InternalAndroidStorage(this.getBaseContext()),rootActivity),isWifi,
        				this),cfg);
        
        
        
        
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
        		LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ABOVE, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
      }

      private void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
      }
      
      @Override
      public void showOrLoadInterstital() {
        try {
          runOnUiThread(new Runnable() {
            public void run() {
              if (interstitialAd.isLoaded()) {
                interstitialAd.show();
                //Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
              }
              else {
                AdRequest interstitialRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(interstitialRequest);
                //Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
              }
            }
          });
        } catch (Exception e) {
        }
      }

      @Override
      public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
      }

      @Override
      public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
      }

      @Override
      public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
      }
      
      
	
	   //is wifi ? AT START OF APP
	   public boolean isWifi(){
		   
		   boolean wifi = false;
		   
		   ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		   NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		   if (mWifi.isConnected()) {
		       wifi = true;
		   }
		   return wifi;
	   }

	@Override
	public void createConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buyTokens() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openAdActivity(boolean isWifi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getAndroidTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void countdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void killMyProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showToast(CharSequence message, int toastDuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showShortToast(CharSequence toastMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLongToast(CharSequence toastMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAlertBox(String alertBoxTitle, String alertBoxMessage,
			String alertBoxButtonText) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showConfirmAlert(String alertBoxTitle, String alertBoxMessage,
			String positiveButton, String negativeButton) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmexit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitLevel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showMyList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openUri(String url) {
		// TODO Auto-generated method stub
		
	}
	
	
    
}
