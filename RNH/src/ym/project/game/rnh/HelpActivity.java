package ym.project.game.rnh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class HelpActivity extends Activity {
	
	private ImageView helpImg, backBtn, nextBtn;
	private int helpImgs []={
			R.drawable.help1,R.drawable.help2,R.drawable.help3,R.drawable.help4,
			R.drawable.help5,R.drawable.help6,R.drawable.help7, R.drawable.help8,
			R.drawable.help9};
	
	private int i;//이미지 체크
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.help);  
	        
	        helpImg = (ImageView)findViewById(R.id.HelpImg);
	        backBtn = (ImageView)findViewById(R.id.Backbtn);
	        nextBtn = (ImageView)findViewById(R.id.NextBtn);
	        
	        
	        backBtn.setOnTouchListener(listner);
	        nextBtn.setOnTouchListener(listner);
	 }
	 View.OnTouchListener listner = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
				case R.id.Backbtn:{
					i--;
					if(i<0){
						Intent i = new Intent(HelpActivity.this, Main.class);
		    			startActivity(i);
		    			finish();
					}else{
						helpImg.setImageResource(helpImgs [i]);	
					}
					
				}
				break;
				
				case R.id.NextBtn:{
					i++;
					if(i>8){
						Intent i = new Intent(HelpActivity.this, Main.class);
		    			startActivity(i);
		    			finish();
					}else{
						helpImg.setImageResource(helpImgs [i]);	
					}
					
				}
				break;
			}
			return false;
		}
	};
	
	public boolean onKeyDown(int KeyCode, KeyEvent event){//뒤로가기 버튼을 눌렀을 때
    	if (event.getAction() == KeyEvent.ACTION_DOWN) {
    		if (KeyCode == KeyEvent.KEYCODE_BACK) {
    			Intent i = new Intent(this, Main.class);
    			startActivity(i);
    			finish();
    		}		
    	}
    	
    	return super.onKeyDown(KeyCode, event);
    }

}
