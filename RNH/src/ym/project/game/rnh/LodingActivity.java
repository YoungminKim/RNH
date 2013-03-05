package ym.project.game.rnh;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LodingActivity extends Activity {
	private TimerTask tt;
	private Animation animation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loding);
        
        final Intent i = new Intent(this, Main.class);
        
        tt=new TimerTask(){

    		@Override
    		public void run() {
    			animation = AnimationUtils.loadAnimation(LodingActivity.this, R.anim.loding);
    			finish();
    			startActivity(i);
    		}	
        };
        Timer t = new Timer();  
        t.schedule(tt, 1000);
    }
    
    
  

   
}