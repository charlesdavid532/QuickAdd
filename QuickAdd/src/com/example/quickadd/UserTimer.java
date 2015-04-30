package com.example.quickadd;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class UserTimer {
	
	private final Context mContext;
	private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private int secs;
    private int mins;
    private Boolean mIsTimerRunning = false;
	
	public UserTimer(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		//Activity activity = (Activity)context;
		//timerValue = (TextView) activity.findViewById(R.id.user_time);
		timerValue = (TextView) ((Activity)context).findViewById(R.id.user_time);
	}
	
	public void start() {
		startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 1000);
        mIsTimerRunning = true;
	}
	
	public void stop() {
		customHandler.removeCallbacks(updateTimerThread);
		mIsTimerRunning = false;
	}
	
	public int getTimeInSeconds() {
		return ((mins*60)+secs);
	}
	
	public Boolean getIsTimerRunning() {
		return mIsTimerRunning;
	}

	public void setIsTimerRunning(Boolean mIsTimerRunning) {
		this.mIsTimerRunning = mIsTimerRunning;
	}

	private Runnable updateTimerThread = new Runnable() {
		        public void run() {
		            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
		            
		            updatedTime = timeSwapBuff + timeInMilliseconds;
		            secs = (int) (updatedTime / 1000);
		            mins = secs / 60;
		            secs = secs % 60;
		            //int milliseconds = (int) (updatedTime % 1000);
		            timerValue.setText("" + mins + ":"
		                    + String.format("%02d", secs));
		            customHandler.postDelayed(this, 1000);
		        }
    };

}
