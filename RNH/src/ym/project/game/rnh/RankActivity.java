
package ym.project.game.rnh;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class RankActivity extends Activity {
	private TextView tv1, tv2;
	private String sql = "select min1, min2, sec1, sec2 from RnHrank order by totalTime desc;";
	private int rank;//순위
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.rank);
	        
	        tv1=(TextView)findViewById(R.id.Result01);
	        tv2=(TextView)findViewById(R.id.Result02);
	       
	        rank=0;
	        SQLiteOpenHelper dbHelper = new DBManager(this);
	    	SQLiteDatabase db = dbHelper.getReadableDatabase();
	    	Cursor cursor = db.rawQuery(sql, null);
	        String Result1 = "";
	        String Result2 = "";
	        while(cursor.moveToNext()){
	        	int min1 = cursor.getInt(0);
	        	int min2 = cursor.getInt(1);
	        	int sec1 = cursor.getInt(2);
	        	int sec2 = cursor.getInt(3);
	        	rank++;
	        	if(rank<=5)Result1 +=(rank+". "+min1+min2+":"+sec1+sec2+"\n");
	        	if(rank>5)Result2 +=(rank+". "+min1+min2+":"+sec1+sec2+"\n");
	        	if(rank==10)break;
	        	
	        }
	        
	        if(Result1.length()==0){
	        	tv1.setText("Empyt Set");
	        }else{
	        	tv1.setText(Result1); 
	        	
	        	tv2.setText(Result2); 
	        	
	        }
	        
	        cursor.close();
	        dbHelper.close();	        
	 }
	 
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
