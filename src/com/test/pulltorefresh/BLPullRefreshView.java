package com.test.pulltorefresh;

import com.example.test_.R;
import com.test.pulltorefresh.BLPullRefreshState.RefreshStateDelegate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BLPullRefreshView extends RelativeLayout implements BLPullDelegate, RefreshStateDelegate,OnAnimationListener{

	String TAG = "RefreshView";
	private Context mContext;
	private BLPullRefreshAnimationView animationView;
	private ImageView image;
	ResetAnimationEndListener resetEndListener;

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
		animationView = (BLPullRefreshAnimationView) findViewById(R.id.animation_view);
		animationView.setAnimationListener(this);
		image = (ImageView) findViewById(R.id.image);
	}
	
	public void init() {
		
		measureView();
		mHeight = getMeasuredHeight();
		animationView.setParentHeight(mHeight);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
		params.topMargin = (int) -mHeight;
		setLayoutParams(params);
	}
	
	int getRefreshViewHeight(){
		return mHeight;
	}
	
	public void setResetEndListener(ResetAnimationEndListener resetEndListener) {
		this.resetEndListener = resetEndListener;
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
		animationView.drawCircle(offsetY,mHeight);
	}
	
	@Override
	public void stopPull(BLPullRefreshState state) {
		if(state.isRefreshStatePullToRefresh()){
			reset();
		}else if(state.isRefreshStateRefreshing()){
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
	
	public void reset(){
		animationView.reset();
		final int origTop = getTop();
		final int distTop = -mHeight;
		
		Animation animation = new Animation() {
			 @Override
			 protected void applyTransformation(float interpolatedTime, Transformation t)
			 {
				 setTopMargin(origTop + (int) ((distTop - origTop) * interpolatedTime));
			 }
		};
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				resetEndListener.animationEnd();
			}
			
		});
		animation.setDuration(300);
		startAnimation(animation);
	}
	@Override
	public void setPullToRefresh() {
		animationView.reset();
		animationView.setVisibility(View.VISIBLE);
		image.setVisibility(View.GONE);
	}
	@Override
	public void setReleaseToRefresh() {
	}
	@Override
	public void setRefreshing() {
	}
	@Override
	public void stopPull() {}
	
	@Override
	public void refreshCompletely() {
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
			}
		});
		animation.setDuration(300);
		startAnimation(animation);
		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void animationEnd() {
		animationView.setVisibility(View.GONE);
		image.setVisibility(View.VISIBLE);
		
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.5f, 1f);
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float animatedValue = (Float) animation.getAnimatedValue();
				image.setScaleX(animatedValue);
				image.setScaleY(animatedValue);
			}
		});
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
			}
		});
		valueAnimator.setDuration(350);
		valueAnimator.start();
	}
	
}
