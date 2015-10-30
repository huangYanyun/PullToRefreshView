package com.test.pulltorefresh;

import com.test.pulltorefresh.BLPullRefreshState.RefreshStateDelegate;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class BLPullScrollView extends ScrollView implements RefreshStateDelegate, BLPullDelegate{
	
	String TAG = "PullListView";
	public int mLastMotionY = LASTMOTION_NOTSET;
	private int startY;
	public static final int LASTMOTION_NOTSET = -10000;
	
	BLPullDelegate pullDelegate;
	RefreshStateDelegate refreshStateDelegate;
	
	private int maxGap = -1;
	//设置下拉刷新开关,默认可以下拉刷新
	private boolean refreshEnable = true;

	public void setRefreshEnable(boolean enable) {
		this.refreshEnable = enable;
	}

	public void setRefreshStatusDelegate(RefreshStateDelegate refreshStatusChangedDelegate) {
		this.refreshStateDelegate = refreshStatusChangedDelegate;
	}
	
	public void setPullDelegate(BLPullDelegate pullDelegate) {
		this.pullDelegate = pullDelegate;
	}

	public BLPullScrollView(Context context) {
		this(context,null);
	}
	
	public BLPullScrollView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public BLPullScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	float lastX;
	float lastY;
	float distanceX;
	float distanceY;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			lastX = event.getRawX();
			lastY = event.getRawY();
			distanceX = distanceY = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			float currentX = event.getRawX();
			float currentY = event.getRawY();
			distanceX = Math.abs(currentX - lastX);
			distanceY = Math.abs(currentY - lastY);
			lastX = currentX;
			lastY = currentY;
			if(distanceX > distanceY){
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
			
		}
		return super.onInterceptTouchEvent(event);
	}
	
	private boolean isPull = false;
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			lastY = event.getRawY();
			mLastMotionY = (int) lastY;
			notifyStartPull();
			break;
		case MotionEvent.ACTION_MOVE:
			if(refreshEnable){
				isPull = notifyOnPulling(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			notifyStopPull();
			break;
		}
		if(isPull){
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private void notifyStartPull(){
		//记住listview本来的偏移量
		startY = getTop();
		broadcastStartPull();
	}
	
	private boolean notifyOnPulling(MotionEvent event){
		if(0 == getScrollY()){
			isPull = true;
			if (LASTMOTION_NOTSET == mLastMotionY)
			{
				mLastMotionY = (int) event.getRawY();
			}
			int offsetY = startY + (int) ((Math.log10((float) (event.getRawY() - mLastMotionY) / mHeight + DRAG_PARAM) - DRAG_LOG_PARAM) * mHeight);
			if(offsetY < 0){
				offsetY = 0;
				isPull = false;
			}
			broadcastPullingDown(offsetY);
			
		}else
		{
			//下面这句解决这个case: 手指按下时header尚未顶格，手指按下后向下拖，header顶格的瞬间跳变。
			mLastMotionY = (int) event.getRawY();
		}
		return isPull;
	}
	
	private void notifyStopPull(){
		broadcastStopPull();
	}
	
	private void broadcastStartPull(){
		if(pullDelegate != null){
			pullDelegate.startPull();
		}
	}

	private void broadcastPullingDown(int offsetY){
		if(pullDelegate != null){
			pullDelegate.onPulling(offsetY);
		}
	}
	
	private void broadcastStopPull(){
		if(pullDelegate != null){
			pullDelegate.stopPull();
		}
	}
	
	private void setTopMargin(float f) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)getLayoutParams();
		params.topMargin = (int) f;
		setLayoutParams(params);
	}
	private void setTopMargin(int from,int to){
		
		final int origTop = from;
		final int distTop = to;
		
		Animation animation = new Animation() {
			 @Override
			 protected void applyTransformation(float interpolatedTime, Transformation t)
			 {
				 setTopMargin(origTop + (int) ((distTop - origTop) * interpolatedTime));
			 }
		};
		animation.setDuration(300);
		startAnimation(animation);
	}
	
	private float mHeight = -1;
	final static float DRAG_PARAM = (float) 0.5;
	final static float DRAG_LOG_PARAM = (float) Math.log10(DRAG_PARAM);
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		super.onLayout(changed, l, t, r, b);
		if(changed && (mHeight == -1)){
			mHeight = getMeasuredHeight();
		}
	}
	
	public void setMaxGap(int gap) {
		this.maxGap = gap;
	}

	@Override
	public void setRefreshing() {
		setTopMargin(getTop(),maxGap);
	}

	public void reset() {
		mLastMotionY = LASTMOTION_NOTSET;
		setTopMargin(getTop(),0);
	}

	@Override
	public void setPullToRefresh() {
	}
	
	@Override
	public void setReleaseToRefresh() {}


	@Override
	public void startPull() {}

	@Override
	public void onPulling(float offsetY) {
		setTopMargin(offsetY);
	}

	@Override
	public void stopPull(int state) {
		if(BLPullRefreshState.PULL_TO_REFRESH == state){
			reset();
		}else if(BLPullRefreshState.REFRESH_COMPLETET == state){
			if(getTop() > maxGap){
				setTopMargin(getTop(),maxGap);
			}
		}
	}

	@Override
	public void stopPull() {}
	
	@Override
	public void refreshCompletely() {
		reset();
	}
	
}
