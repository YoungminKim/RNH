package ym.project.game.rnh;



import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements Callback {
	
	GameThread mThread;
	Context context;
	AttributeSet attr;

	public GameView(Context context, AttributeSet attr) {
		
		super(context, attr);
		init();
		
		
	}
	public void init(){
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
		mThread = new GameThread(context, holder);

	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	
		mThread.start();
	}

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		boolean retry = true;
		while(retry){

			mThread.threadClose();				
			
			for(int i=0; i<6; i++){
				mThread.wall[i].recycle();
			}
			mThread.bg[0].recycle();
			mThread.bg[1].recycle();
			mThread.sky.recycle();
			for(int i=0; i<6; i++){
				for(int j=0; j<4;j++){
					mThread.mainChr[i][j].recycle();
				}
			}
			mThread.road.recycle();
			mThread.ready.recycle();
			mThread.action.recycle();
			mThread.gameover.recycle();
			
			mThread.redBtn.recycle();
			mThread.greenBtn.recycle();
			mThread.blueBtn.recycle();
			mThread.bar.recycle();
			mThread.snake.recycle();
			mThread.recordWin.recycle();
			for(int i=0; i<10; i++){
				mThread.num[i].recycle();
			}
			
			
			System.gc();


			
			retry=false;			
		}
	}
    
    public GameThread getThread() {
        return mThread;
    }
}


