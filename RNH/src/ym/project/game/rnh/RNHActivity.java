package ym.project.game.rnh;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class RNHActivity extends Activity {
    /** Called when the activity is first created. */
	
	private GameView gv;
	private GameThread gThread;
	private TimerTask timetask;
	private Timer time;
	private boolean redState, blueState, greenState;
	private PowerManager pm;
	private WakeLock wakeLock;
	private MediaPlayer player;//BGM

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);   

        gv = (GameView)findViewById(R.id.GameView);
        pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeAlways");
       
        gv.mThread.setBitmap(this.getApplicationContext());
        
        gThread = gv.getThread();
        player=MediaPlayer.create(this, R.raw.yeizon);
        player.start();
        player.setLooping(true);
        
        
        timetask=new TimerTask(){

    		@Override
    		public void run() {
    			try {
    				
    				gThread.readyAction=0;
					Thread.sleep(2000);
					gThread.readyAction=1;
					gThread.flagHandler=1;
					Thread.sleep(1000);
					gThread.readyAction=2;
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
    			

    		}	
        };
       
        time = new Timer();  
        time.schedule(timetask, 1);
       
      
    }
    
    protected void onResume() {
		super.onResume();
		wakeLock.acquire();
	}
  
    protected void onPause() {
		super.onPause();
		if(wakeLock.isHeld()){
			wakeLock.release();
		}
	}
    
    public void onDestroy(){
    	super.onDestroy();
    	if(player!=null){
    		player.release();
    		player=null;
    	}
    }
		
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int pureaction = action & MotionEvent.ACTION_MASK;
		int touchX1 = (int)event.getX();	
		int touchY1 = (int)event.getY();
		int touchX2 = 0;
		int touchY2 = 0;
		if(event.getPointerCount() >1){
			touchX2 = (int)event.getX(1);	
			touchY2 = (int)event.getY(1);
		 }

		
			switch(pureaction){	
			case 0:{/////DOWN	
				if(touchY1>=gThread.buttonY1&&touchY1<=gThread.buttonY2){
					if(touchX1>=gThread.redX1&&touchX1<=gThread.redX2){
						redState=true;
						gThread.i=0;
					}
					if(touchX1>=gThread.greenX1&&touchX1<=gThread.greenX2){
						greenState=true;
						gThread.i=1;
					}
					if(touchX1>=gThread.blueX1&&touchX1<=gThread.blueX2){
						blueState=true;
						gThread.i=2;
					}		
				}
				
			}
				break;
			case 1:{////////////UP
				redState=false;
				blueState=false;
				greenState=false;

				
			}
			break;
			case 5:////////////PDOWN
				
				if(touchY2>=gThread.buttonY1&&touchY2<=gThread.buttonY2){
					if(touchX2>=gThread.redX1&&touchX2<=gThread.redX2){
						if(blueState){
							gThread.i=4;
						}else if(greenState){
							gThread.i=3;
						}
						
					}
					if(touchX2>=gThread.greenX1&&touchX2<=gThread.greenX2){
						if(blueState){
							gThread.i=5;
						}else if(redState){
							gThread.i=3;
						}
					}
					if(touchX2>=gThread.blueX1&&touchX2<=gThread.blueX2){
						if(greenState){
							gThread.i=5;
						}else if(redState){
							gThread.i=4;
						}
					}	
				}
				
			break;
			case 6:{////////////PUP
				redState=false;
				blueState=false;
				greenState=false;
				
			}
			break;
		}
	
		return true;
	}

	public boolean onKeyDown(int KeyCode, KeyEvent event){//뒤로가기 버튼을 눌렀을 때
    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
    		if (KeyCode == KeyEvent.KEYCODE_BACK) {
    			
    			gThread.threadPause(false);
    			gThread.flagHandler=0;
    			player.pause();

    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("Are you want you go to the main?");
    			
    			DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which ==-1){
							Intent i = new Intent(RNHActivity.this, Main.class);
							gThread.threadPause(false);
							finish();
							startActivity(i);
						}else{
							if(!gThread.gameoverFlag){
								gThread.flagHandler=1;
								gThread.threadPause(true);
								player.start();
							}
						}
					}
				};
				
				DialogInterface.OnCancelListener listner2 = new OnCancelListener() {
	
					@Override
					public void onCancel(DialogInterface dialog) {
						if(!gThread.gameoverFlag){
							gThread.flagHandler=1;
							gThread.threadPause(true);
							player.start();
						}
					}
				};
				builder.setOnCancelListener(listner2);	
    			builder.setPositiveButton("Yes", listner);
    			builder.setNegativeButton("No", listner);
    			builder.show();
    
    			return true;
    		}
    	}
    	return super.onKeyDown(KeyCode, event);
    }   
}