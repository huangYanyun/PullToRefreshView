package com.test.pulltorefresh;

import com.example.test_.R;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

public class BLPullRefreshAnimationView extends View{
	
	String TAG = "RefreshAnimationView";
	private Context mContext;
	/**
	 * 屏幕宽度
	 */
	private float screenWidth;
	/**
	 * 圆半径
	 */
	private float radius;
	/**
	 * 圆最大半径，超过此半径开始画椭圆
	 */
	private float maxRadius;
	/**
	 * 圆心x坐标
	 */
	private float circleX;
	/**
	 * 圆心y坐标
	 */
	private float circleY;
	/**
	 * 父容器高度
	 * @param context
	 */
	private int parentHeight;
	/**
	 * 画图定位(left,top,right,bottom)
	 */
	private RectF rectF;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 正在动画时不响应pull动画
	 */
	private boolean isAnimation = false;
	private OnAnimationListener animationListener;
	
	public void setAnimationListener(OnAnimationListener animationListener) {
		this.animationListener = animationListener;
	}
	public void setParentHeight(int parentHeight) {
		this.parentHeight = parentHeight;
	}
	public BLPullRefreshAnimationView(Context context) {
		this(context,null);
	}
	public BLPullRefreshAnimationView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BLPullRefreshAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}
	private void init(){
		maxRadius = DensityUtil.dip2px(mContext, 18);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		rectF = new RectF();
		
		mPaint = new Paint();
		mPaint.setColor(getResources().getColor(R.color.bl_refresh_circle));
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL); // 实心

		
	}
	public void drawCircle(float offsetY, int mHeight) {
		
		//正在动画不再画圈
		if(isAnimation){
			return;
		}
		
		if(offsetY >= 0){
			radius = offsetY / 2;
			circleX = screenWidth / 2;
			circleY = parentHeight - offsetY / 2;
			postInvalidate();
		}
		if(offsetY >= parentHeight){
			radius = parentHeight / 2;
			startAnimation();
		}
	}
	
	@SuppressLint("NewApi")
	private void startAnimation() {

		ValueAnimator valueAnimator = ValueAnimator.ofFloat(parentHeight / 2, parentHeight - maxRadius);
		valueAnimator.setInterpolator(new DecelerateInterpolator(0.4f));
		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				isAnimation = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if(animationListener != null){
					animationListener.animationEnd();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				isAnimation = false;
			}
		});
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				circleY = (Float) animation.getAnimatedValue();
				radius = Math.max(maxRadius, parentHeight - circleY);
				postInvalidate();
			}
		});

		AnimatorSet as = new AnimatorSet();
		as.setDuration(200);
//		as.play(valueAnimator).with(bottomAnima);
		as.play(valueAnimator);
		as.start();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (radius >= maxRadius) {
			rectF.set(circleX - maxRadius, circleY - radius, circleX + maxRadius, circleY + radius);
			canvas.drawOval(rectF, mPaint);
		} else {
			canvas.drawCircle(circleX, circleY, radius, mPaint);
		}
	}
	
	public void reset() {
		isAnimation = false;
	}

}
