package ym.project.game.rnh;

import java.util.Calendar;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.Display;
import android.view.SurfaceHolder;

import android.view.WindowManager;

public class GameThread extends Thread
{
	//Context context;
	private SurfaceHolder mHolder;
	public Bitmap wall[]= new Bitmap[6];
	public Bitmap bg[] = new Bitmap[2];
	public Bitmap sky;
	public Bitmap mainChr[][] = new Bitmap[6][4];
	public Bitmap road;
	public Bitmap ready, action;
	public Bitmap gameover;
	public Bitmap redBtn, greenBtn, blueBtn;
	public Bitmap bar;
	public Bitmap snake;
	public Bitmap recordWin;
	public Bitmap num[]=new Bitmap[10];
	
	private Resources res;
	
	public int displayWidth, dwh;
	private int dwhCheck1, dwhCheck2;
	public int displayHeight, dhh;
	private int wallSize, roadLoca;
	private int cw, ch , ww, wh;//캐릭터, 벽 중심
	private int bgMove1, bgMove2;
	private int wallColor1, wallColor2, wallColor3, wallColor4, wallColor5, wallColor6;//벽 칼라
	private int wallMove1, wallMove2, wallMove3, wallMove4, wallMove5, wallMove6;//벽 이동
	private int roadMove1, roadMove2;
	
	public int i, j; //캐릭터 비트맵 배열 변수
	private boolean flag; //쓰레드 플래그
	public boolean pauseFlag; //일시정지 플래그
	private boolean timeFlag;
	private int speed, msg;

	public int flagHandler;//일시정지플래그 핸들러
	
	public int readyAction;
	private int preMillisecond;//캐릭터 애니메이션을 위한 처음 받는 밀리세컨드
	private int Totaltime;//총 진행 시간(second)
	public int distance, barDistance;//뱀과 카멜레온 간격
	private int snakeSpeed, chameleonSpeed;//속도
	private int increase;//게임 시간이 증가함에 따라 게임속도 증가변수
	public boolean gameoverFlag;
	private Random ran = new Random();
	public int redX1,redX2, greenX1, greenX2, blueX1, blueX2, buttonY1, buttonY2;
	public int min1, min2, sec1, sec2;//게임 시간
	
	private SQLiteOpenHelper dbHelper;
	private SQLiteDatabase db;

	
	public GameThread(Context context, SurfaceHolder holder){
		mHolder = holder;
		
	
	
	}
	
	//////////////////////context 객체를 받아와서 비트맵에 뿌리는 함수
	public void setBitmap(Context context){
		res = context.getResources();
		
		
		//////////////////////해상도 크기
		Display display = 
			((WindowManager)context.getSystemService
					(context.WINDOW_SERVICE)).getDefaultDisplay();
		
		displayWidth = display.getWidth();
		displayHeight = display.getHeight();
		
		dwh = displayWidth/2;
		dhh = displayHeight/2;
		
		
		
		wall[0] = BitmapFactory.decodeResource(res, R.drawable.wall_red);
    	wall[1] = BitmapFactory.decodeResource(res, R.drawable.wall_green);
    	wall[2] = BitmapFactory.decodeResource(res, R.drawable.wall_blue);
    	wall[3] = BitmapFactory.decodeResource(res, R.drawable.wall_yellow);
    	wall[4] = BitmapFactory.decodeResource(res, R.drawable.wall_purple);
    	wall[5] = BitmapFactory.decodeResource(res, R.drawable.wall_mint);
    	
		
		wallSize = displayWidth/5;
		road = BitmapFactory.decodeResource(res, R.drawable.road);
		road = Bitmap.createScaledBitmap(road, displayWidth, displayHeight/20, true);
		roadMove2 = displayWidth;
		
		
		bg[0] = BitmapFactory.decodeResource(res, R.drawable.city1);
		bg[1] = BitmapFactory.decodeResource(res, R.drawable.city2);
		bg[0]= Bitmap.createScaledBitmap(bg[0], displayWidth, displayHeight/3, true);
		bg[1]= Bitmap.createScaledBitmap(bg[1], displayWidth, displayHeight/3, true);
		
		sky = BitmapFactory.decodeResource(res, R.drawable.sky);
		sky = Bitmap.createScaledBitmap(sky, displayWidth, displayHeight/5, true);
		
		flag = true;
		pauseFlag = true;	
		timeFlag=true;
		
		flagHandler = 0;
		

		bgMove2 = displayWidth;
		
		wallMove1 = 0;
		wallMove2 = wallSize;
		wallMove3 = wallSize*2;
		wallMove4 = wallSize*3;
		wallMove5 = wallSize*4;
		wallMove6 = wallSize*5;
		
		roadLoca=displayHeight/5*3+wallSize/6;
		
		mainChr[0][0] = BitmapFactory.decodeResource(res, R.drawable.char0201);
		mainChr[0][1] = BitmapFactory.decodeResource(res, R.drawable.char0202);
		mainChr[0][2] = BitmapFactory.decodeResource(res, R.drawable.char0203);
		mainChr[0][3] = BitmapFactory.decodeResource(res, R.drawable.char0204);
		mainChr[1][0] = BitmapFactory.decodeResource(res, R.drawable.char0101);
		mainChr[1][1] = BitmapFactory.decodeResource(res, R.drawable.char0102);
		mainChr[1][2] = BitmapFactory.decodeResource(res, R.drawable.char0103);
		mainChr[1][3] = BitmapFactory.decodeResource(res, R.drawable.char0104);
		mainChr[2][0] = BitmapFactory.decodeResource(res, R.drawable.char0301);
		mainChr[2][1] = BitmapFactory.decodeResource(res, R.drawable.char0302);
		mainChr[2][2] = BitmapFactory.decodeResource(res, R.drawable.char0303);
		mainChr[2][3] = BitmapFactory.decodeResource(res, R.drawable.char0304);
		mainChr[3][0] = BitmapFactory.decodeResource(res, R.drawable.char0401);
		mainChr[3][1] = BitmapFactory.decodeResource(res, R.drawable.char0402);
		mainChr[3][2] = BitmapFactory.decodeResource(res, R.drawable.char0403);
		mainChr[3][3] = BitmapFactory.decodeResource(res, R.drawable.char0404);
		mainChr[4][0] = BitmapFactory.decodeResource(res, R.drawable.char0501);
		mainChr[4][1] = BitmapFactory.decodeResource(res, R.drawable.char0502);
		mainChr[4][2] = BitmapFactory.decodeResource(res, R.drawable.char0503);
		mainChr[4][3] = BitmapFactory.decodeResource(res, R.drawable.char0504);
		mainChr[5][0] = BitmapFactory.decodeResource(res, R.drawable.char0601);
		mainChr[5][1] = BitmapFactory.decodeResource(res, R.drawable.char0602);
		mainChr[5][2] = BitmapFactory.decodeResource(res, R.drawable.char0603);
		mainChr[5][3] = BitmapFactory.decodeResource(res, R.drawable.char0604);
		
		getRanColor();
		
		ready = BitmapFactory.decodeResource(res, R.drawable.ready);
		ready = Bitmap.createScaledBitmap(ready, displayWidth, displayHeight, true);
		action = BitmapFactory.decodeResource(res, R.drawable.action);
		action = Bitmap.createScaledBitmap(action, displayWidth, displayHeight, true);
		//readyAction=0;
		
		gameover = BitmapFactory.decodeResource(res, R.drawable.gameover);
		gameover = Bitmap.createScaledBitmap(gameover, displayWidth, displayHeight, true);
		
		
		speed=(displayWidth/200);
		/////////check
		dwhCheck1=dwh-(wallSize/4);
		dwhCheck2=dwh+(wallSize/4);

		i=1;
		
		Totaltime=0;
		chameleonSpeed=1;
		snakeSpeed=2;

		redBtn = BitmapFactory.decodeResource(res, R.drawable.btn_red);
		greenBtn = BitmapFactory.decodeResource(res, R.drawable.btn_green);
		blueBtn = BitmapFactory.decodeResource(res, R.drawable.btn_blue);
		redBtn = Bitmap.createScaledBitmap(redBtn, displayWidth/3, displayWidth/6, true);
		greenBtn = Bitmap.createScaledBitmap(greenBtn, displayWidth/3, displayWidth/6, true);
		blueBtn = Bitmap.createScaledBitmap(blueBtn, displayWidth/3, displayWidth/6, true);
		
		redX1 = 0;
		redX2 = redBtn.getWidth();
		greenX1 = dwh-(greenBtn.getWidth()/2);
		greenX2 = greenX1+greenBtn.getWidth();
		blueX1 = displayWidth-blueBtn.getWidth();
		blueX2 = blueX1+blueBtn.getWidth();
		buttonY1 = roadLoca+road.getHeight();
		buttonY2 = buttonY1+redBtn.getHeight();
		
		bar = BitmapFactory.decodeResource(res, R.drawable.bar);
		bar = Bitmap.createScaledBitmap(bar, displayWidth, bar.getHeight(),true);
		snake=BitmapFactory.decodeResource(res, R.drawable.snake);
		
		distance= dwh-(bar.getWidth()/2)+200;
		
		gameoverFlag=false;
		
		
		recordWin = BitmapFactory.decodeResource(res, R.drawable.record);
		
		num[0] = BitmapFactory.decodeResource(res, R.drawable.num_0);
		num[1] = BitmapFactory.decodeResource(res, R.drawable.num_1);
		num[2] = BitmapFactory.decodeResource(res, R.drawable.num_2);
		num[3] = BitmapFactory.decodeResource(res, R.drawable.num_3);
		num[4] = BitmapFactory.decodeResource(res, R.drawable.num_4);
		num[5] = BitmapFactory.decodeResource(res, R.drawable.num_5);
		num[6] = BitmapFactory.decodeResource(res, R.drawable.num_6);
		num[7] = BitmapFactory.decodeResource(res, R.drawable.num_7);
		num[8] = BitmapFactory.decodeResource(res, R.drawable.num_8);
		num[9] = BitmapFactory.decodeResource(res, R.drawable.num_9);
		
		dbHelper = new DBManager(context);
		db = dbHelper.getWritableDatabase();
		
		
	}
    
	
	private void getRanColor(){
		
		wallColor1=ran.nextInt(6);
		wallColor2=ran.nextInt(6);
		wallColor3=ran.nextInt(6);
		wallColor4=ran.nextInt(6);
		wallColor5=ran.nextInt(6);
		wallColor6=ran.nextInt(6);

		
	}
	
	private void getRanColor(int msg){
		
		switch(msg){
		case 1:wallColor1=ran.nextInt(6);;break;
		case 2:wallColor2=ran.nextInt(6);;break;
		case 3:wallColor3=ran.nextInt(6);;break;
		case 4:wallColor4=ran.nextInt(6);;break;
		case 5:wallColor5=ran.nextInt(6);;break;
		case 6:wallColor6=ran.nextInt(6);;break;
		
		}
	}
	

	public void run(){
	
		while(flag){
		
			if(flagHandler!=0){
				pauseFlag = true;	
			}
			if(pauseFlag){
	
				Canvas canvas = null;
				canvas = mHolder.lockCanvas();	
				
				Calendar calendar;
				
				calendar = Calendar.getInstance();
				int millisecond;
				
				millisecond=calendar.get(Calendar.MILLISECOND);
				
				
				try {
					synchronized (mHolder) {
						
						if(Totaltime%10==0){
							increase=Totaltime/30;
						}
						
						
						getSize();
						canvas.drawBitmap(sky, 0, 0, null);
						scrollCity(canvas);
						scrollWall(canvas);
						scrollbar(canvas);
						charAni(canvas, millisecond);
						checkWall(millisecond);
						scrollRoad(canvas);
						getSecond(millisecond);
						btn(canvas);
						
						if(flagHandler==0){
							if(readyAction==0){
								canvas.drawBitmap(ready, 0, 0, null);
							}
							pauseFlag = false;
						}else if(readyAction==1){
							canvas.drawBitmap(action, 0, 0, null);
						}
						
						if(distance>=(dwh+(bar.getWidth()/2))-100){	
							
							min1=(Totaltime/60)/10;
							min2=(Totaltime/60)%10;
							sec1=(Totaltime%60)/10;
							sec2=(Totaltime%60)%10;
							canvas.drawBitmap(gameover, 0, 0, null);
							canvas.drawBitmap(recordWin, dwh-(recordWin.getWidth()/2), dhh-(recordWin.getHeight())/2, null);
							canvas.drawBitmap(num[min1],dwh-num[min1].getWidth()*3, dhh, null);
							canvas.drawBitmap(num[min2],dwh-num[min1].getWidth()*2, dhh, null);
							canvas.drawBitmap(num[sec1],dwh+num[sec1].getWidth(), dhh, null);
							canvas.drawBitmap(num[sec2],dwh+num[sec2].getWidth()*2, dhh, null);
							ContentValues cv = new ContentValues();
							cv.put("min1", min1);
							cv.put("min2", min2);
							cv.put("sec1", sec1);
							cv.put("sec2", sec2);
							cv.put("totaltime", Totaltime);
							db.insert("RnHrank", null, cv);
							dbHelper.close();
							
							flagHandler=0;
							pauseFlag = false;
							gameoverFlag=true;
							
						}
						
						
					}
					
				} catch (Exception e) {
					
				}finally{
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
	
	private void getSecond(int millisecond) {
		if(millisecond<50&&timeFlag){
			Totaltime++;
			timeFlag=false;
		}
		if(millisecond>950){
			timeFlag=true;
		}
	}

	private void scrollCity(Canvas canvas){							
		canvas.drawBitmap(bg[0], bgMove1, 0, null);
		canvas.drawBitmap(bg[1], bgMove2, 0, null);
		bgMove1-=3;
		bgMove2-=3;
		
		if(bgMove1<=-displayWidth){
			bgMove1=displayWidth;		
		}
		if(bgMove2<=-displayWidth){			
			bgMove2=displayWidth;	
		}
	
	}
	private void scrollbar(Canvas canvas) {
		canvas.drawBitmap(bar, dwh-(bar.getWidth()/2), 0, null);
		canvas.drawBitmap(snake, distance, 0, null);	
	}

	private void getSize(){
		wall[wallColor1]=Bitmap.createScaledBitmap(wall[wallColor1], wallSize, displayHeight/2, true);
		wall[wallColor2]=Bitmap.createScaledBitmap(wall[wallColor2], wallSize, displayHeight/2, true);
		wall[wallColor3]=Bitmap.createScaledBitmap(wall[wallColor3], wallSize, displayHeight/2, true);
		wall[wallColor4]=Bitmap.createScaledBitmap(wall[wallColor4], wallSize, displayHeight/2, true);
		wall[wallColor5]=Bitmap.createScaledBitmap(wall[wallColor5], wallSize, displayHeight/2, true);
		wall[wallColor6]=Bitmap.createScaledBitmap(wall[wallColor6], wallSize, displayHeight/2, true);
		
		ww = wall[wallColor1].getWidth()/2;
		wh = wall[wallColor1].getHeight()/2;

		
	}
	private void scrollWall(Canvas canvas){
		
	
		canvas.drawBitmap(wall[wallColor1], wallMove1, displayHeight/5, null);
		canvas.drawBitmap(wall[wallColor2], wallMove2, displayHeight/5, null);
		canvas.drawBitmap(wall[wallColor3], wallMove3, displayHeight/5, null);
		canvas.drawBitmap(wall[wallColor4], wallMove4, displayHeight/5, null);
		canvas.drawBitmap(wall[wallColor5], wallMove5, displayHeight/5, null);
		canvas.drawBitmap(wall[wallColor6], wallMove6, displayHeight/5, null);
		
		wallMove1-=(speed+increase);
		wallMove2-=(speed+increase);
		wallMove3-=(speed+increase);
		wallMove4-=(speed+increase);
		wallMove5-=(speed+increase);
		wallMove6-=(speed+increase);
		
		if(wallMove1<=-wallSize){
			wallMove1=displayWidth;
			msg=1;
			getRanColor(msg);
		}
		if(wallMove2<=-wallSize){
			wallMove2=displayWidth;
			msg=2;
			getRanColor(msg);
		}
		if(wallMove3<=-wallSize){
			wallMove3=displayWidth;
			msg=3;
			getRanColor(msg);
		}
		if(wallMove4<=-wallSize){
			wallMove4=displayWidth;
			msg=4;
			getRanColor(msg);
		}
		if(wallMove5<=-wallSize){
			wallMove5=displayWidth;
			msg=5;
			getRanColor(msg);
		}
		if(wallMove6<=-wallSize){
			wallMove6=displayWidth;
			msg=6;
			getRanColor(msg);
		}
	}
	
	private void charAni(Canvas canvas, int millisecond) {
	
		int sum=0;
		sum=preMillisecond-millisecond;
		if(sum<0)sum=sum*-1;
		if(sum>50){
			preMillisecond=millisecond;
			j++;
			if(j==4)j=0;
		}
		
		cw = mainChr[i][j].getWidth()/2;
		ch = mainChr[i][j].getHeight();
		
		canvas.drawBitmap(mainChr[i][j], dwh-cw, roadLoca-ch, null);
	}
	
	private void checkWall(int millisecond){

		if(wallMove1<=dwhCheck2&&wallMove1>=dwhCheck1){
			checkColor(wallColor1);
		}else if(wallMove2<=dwhCheck2&&wallMove2>=dwhCheck1){
			checkColor(wallColor2);
		}else if(wallMove3<=dwhCheck2&&wallMove3>=dwhCheck1){
			checkColor(wallColor3);
		}else if(wallMove4<=dwhCheck2&&wallMove4>=dwhCheck1){
			checkColor(wallColor4);
		}else if(wallMove5<=dwhCheck2&&wallMove5>=dwhCheck1){
			checkColor(wallColor5);	
		}else if(wallMove6<=dwhCheck2&&wallMove6>=dwhCheck1){
			checkColor(wallColor6);
		}
	}
	
	private void checkColor(int wallColor){
		if(wallColor==i){
			distance=distance-chameleonSpeed;
		}else{
			distance=distance+snakeSpeed;
		}
	}
	
	private void scrollRoad(Canvas canvas){
		canvas.drawBitmap(road, roadMove1, roadLoca, null);
		canvas.drawBitmap(road, roadMove2, roadLoca, null);
		roadMove1 -= speed;
		roadMove2 -= speed; 
		if(roadMove1<=-displayWidth){
			roadMove1 = displayWidth;
		}
		if(roadMove2<=-displayWidth){
			roadMove2 = displayWidth;
		}
	}
	private void btn(Canvas canvas){
		canvas.drawBitmap(redBtn, redX1, buttonY1, null);
		canvas.drawBitmap(greenBtn, greenX1, buttonY1, null);
		canvas.drawBitmap(blueBtn, blueX1,buttonY1, null);
	}

	
	public void threadClose(){
		flag = false;
	}
	
	public void threadPause(Boolean pauseFlag){
		this.pauseFlag = pauseFlag;
	}

}