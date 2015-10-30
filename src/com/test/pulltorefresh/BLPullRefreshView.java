package com.test.pulltorefresh;

import com.example.test_.R;
import com.test.pulltorefresh.BLPullRefreshState.RefreshStateDelegate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BLPullRefreshView extends RelativeLayout implements BLPullDelegate, RefreshStateDelegate{

	String TAG = "RefreshView";
	private Context mContext;
	private TextView text;
	RefreshStateDelegate delegate;
	
	public void setRefreshStateDelegate(RefreshStateDelegate delegate) {
		this.delegate = delegate;
	}

	private int mHeight = -1;
	
	public BLPullRefreshView(Context context) {
		this(context,null);
	}
	public BLPullRefreshView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BLPullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.refresh_view, this, true);
		text = (TextView) findViewById(R.id.text);
	}
	
	public void init() {
		
		measureView();
		mHeight = getMeasuredHeight();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
		params.topMargin = (int) -mHeight;
		setLayoutParams(params);
	}
	
	int getRefreshViewHeight(){
		return mHeight;
	}
	
	private void measureView() 
	{
		ViewGroup.LayoutParams p = getLayoutParams();
		if (p == null) 
		{
			p = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} 
		else 
		{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		
		measure(childWidthSpec, childHeightSpec);
	}
	@Override
	public void startPull() {
	}
	@Override
	public void onPulling(float offsetY) {
		float topMargin = offsetY - mHeight;
		setTopMargin(topMargin);
	}
	
	@Override
	public void stopPull(int state) {
		if(BLPullRefreshState.PULL_TO_REFRESH == state){
			setTopMargin(getTop(), -mHeight);
		}else if(BLPullRefreshState.REFRESHING == state){
			if(getTop() > 0){
				setTopMargin(getTop(),0);
			}
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

	@Override
	public void setPullToRefresh() {
		text.setText(R.string.pull_to_refresh);
	}
	@Override
	public void setReleaseToRefresh() {
		text.setText(R.string.release_to_refresh);
	}
	@Override
	public void setRefreshing() {
		text.setText(R.string.refreshing);
	}
	@Override
	public void stopPull() {}
	
	@Override
	public void refreshCompletely() {
		text.setText(R.string.refresh_complete);
		final float from = getTop();
		final int to = -mHeight;
		Animation animation = new Animation() {
			 @Override
			 protected void applyTransformation(float interpolatedTime, Transformation t)
			 {
				 setTopMargin(from + (int) ((to - from) * interpolatedTime));
			 }
		};
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				setPullToRefresh();
				if(delegate != null){
					delegate.setPullToRefresh();
				}
			}
		});
		animation.setDuration(300);
		startAnimation(animation);
		
	}
	
}
