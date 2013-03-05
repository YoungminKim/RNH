package ym.project.game.rnh;
	
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main  extends Activity implements OnTouchListener
{

	private ImageView startBtn, helpBtn, rankBtn;
	private Animation animation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startBtn = (ImageView)findViewById(R.id.Start_btn);
        helpBtn = (ImageView)findViewById(R.id.Help_btn);
        rankBtn = (ImageView)findViewById(R.id.Rank_btn);
        
        startBtn.setOnTouchListener(this);
        helpBtn.setOnTouchListener(this);
        rankBtn.setOnTouchListener(this);	        
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()){
		case R.id.Start_btn: startGame();break;
		case R.id.Help_btn: help();break;
		case R.id.Rank_btn: rank();break;
		}
		return false;
	}
	private void startGame() {
		animation = AnimationUtils.loadAnimation(this, R.anim.loding);
		Intent i = new Intent(this, RNHActivity.class);
		startActivity(i);
		finish();
	}
	private void help() {
		animation = AnimationUtils.loadAnimation(this, R.anim.loding);
		Intent i = new Intent(this, HelpActivity.class);
		startActivity(i);
		finish();
	}
	private void rank(){
		animation = AnimationUtils.loadAnimation(this, R.anim.loding);
		Intent i = new Intent(this, RankActivity.class);
		startActivity(i);
		finish();
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event){//뒤로가기 버튼을 눌렀을 때
    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
    		if (KeyCode == KeyEvent.KEYCODE_BACK) {
    			//Toast.makeText(Main.this, "event : "+ event, 1).show();
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			builder.setMessage("Are you sure you want to quit?");
    			
    			DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which ==-1) finish();
					}
				};
    			
    			builder.setPositiveButton("Yes", listner);
    			builder.setNegativeButton("No", null);
    			builder.show();
    			
    			
    			return true;
    		}
    	}
    	
    	return super.onKeyDown(KeyCode, event);
    }

	    
}


