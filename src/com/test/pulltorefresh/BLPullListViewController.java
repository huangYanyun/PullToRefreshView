package com.test.pulltorefresh;

import com.example.test_.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class BLPullListViewController extends BLPullControllerBase{
	
	private BLPullListView listview;

	public BLPullListViewController(Context context) {
		this(context,null);
	}
	public BLPullListViewController(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BLPullListViewController(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected void setContentView(){
		LayoutInflater.from(mContext).inflate(R.layout.pull_listview, this, true);
		
		listview = (BLPullListView)findViewById(R.id.abslistview);
		
		listview.setPullDelegate(this);
		listview.setRefreshStatusDelegate(this);
		addPullDelegate(listview);
		refreshState.addRefreshStateDelegate(listview);
		
		absListView = listview;
	}
	@Override
	protected void setRefreshEnable(boolean enable) {
		listview.setRefreshEnable(enable);
	}
	@Override
	protected void setMaxGap(int f) {
		listview.setMaxGap(f);
	}
	
	public BLPullListView getContentView() {
		return listview;
	}
	@Override
	protected void addFootView() {
		foot = new BLPullFooterView(mContext);
		listview.addFooterView(foot);
	}
	@Override
	protected void resetSubView() {
		listview.reset();
	}

}
