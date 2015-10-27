package com.test.pulltorefresh;

import com.example.test_.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class BLPullGridViewController extends BLPullControllerBase{
	
	private BLPullGridView view;

	public BLPullGridViewController(Context context) {
		this(context,null);
	}
	public BLPullGridViewController(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BLPullGridViewController(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected void setContentView(){
		LayoutInflater.from(mContext).inflate(R.layout.pull_gridview, this, true);
		
		view = (BLPullGridView)findViewById(R.id.abslistview);
		
		view.setPullDelegate(this);
		view.setRefreshStatusDelegate(this);
		addPullDelegate(view);
		refreshState.addRefreshStateDelegate(view);
		
		absListView = view;
	}
	@Override
	protected void setRefreshEnable(boolean enable) {
		view.setRefreshEnable(enable);
	}
	@Override
	protected void setMaxGap(int f) {
		view.setMaxGap(f);
	}
	
	public BLPullGridView getContentView() {
		return view;
	}
	@Override
	protected void addFootView() {
		foot = new BLPullFooterView(mContext);
		view.addFooterView(foot);
	}
	@Override
	protected void resetSubView() {
		view.reset();
	}

}
