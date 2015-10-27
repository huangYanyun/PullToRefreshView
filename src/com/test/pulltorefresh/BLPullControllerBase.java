package com.test.pulltorefresh;

import java.util.ArrayList;
import java.util.List;
import com.example.test_.R;
import com.test.pulltorefresh.BLPullRefreshState.RefreshStateDelegate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;

public abstract class BLPullControllerBase extends RelativeLayout implements BLPullDelegate, RefreshStateDelegate{
	
	String TAG = "PullControllerBase";
	List<BLPullDelegate> mPullDelegates = new ArrayList<BLPullDelegate>();
	BLPullRefreshState refreshState;
	BLPullLoadState loadState;
	protected BLPullRefreshView mRefreshView;
	protected BLPullFooterView foot;
	public OnRefreshListener refreshListener;
	public OnLoadingListener loadingListener;
	OnPullViewScrollListener scrollListener;
	protected AbsListView absListView;
	int  maxGap = -1;
	final int AVAILABLE_ITEM = 5;
	protected Context mContext;
	
	public BLPullControllerBase(Context context) {
		this(context,null);
	}
	
	public BLPullControllerBase(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public BLPullControllerBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		refreshState = new BLPullRefreshState();
		setContentView();
		
		loadState = new BLPullLoadState();
		mRefreshView = (BLPullRefreshView)findViewById(R.id.refresh_view);
		mRefreshView.init();
		addPullDelegate(mRefreshView);
		refreshState.addRefreshStateDelegate(mRefreshView);
		int refreshViewHeight = mRefreshView.getRefreshViewHeight();
		maxGap = refreshViewHeight;
		setMaxGap(refreshViewHeight);
	}
	
	abstract protected void setContentView();
	
	abstract protected void setRefreshEnable(boolean enable);
	
	abstract protected void setMaxGap(int f);
	
	abstract protected void addFootView();
	
	abstract protected void resetSubView();
	
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}
	
	public void setOnLoadingListener(OnLoadingListener loadingListener) {
		this.loadingListener = loadingListener;
	}
	
	public void setScrollListener(OnPullViewScrollListener scrollListener) {
		this.scrollListener = scrollListener;
	}

	protected void addPullDelegate(BLPullDelegate delegate){
		if(!mPullDelegates.contains(delegate)){
			mPullDelegates.add(delegate);
		}
	}
	
	@Override
	public void startPull() {
		for(int i = 0; i < mPullDelegates.size();i++){
			mPullDelegates.get(i).startPull();
		}
	}

	@Override
	public void onPulling(float offsetY) {
		if(refreshState.isRefreshStatePullToRefresh()){
			if(offsetY >= maxGap){
				setReleaseToRefresh();
			}
		}else if(refreshState.isRefreshStateReleaseToRefresh()){
			if(offsetY < maxGap){
				setPullToRefresh();
			}
		}
		for(int i = 0; i < mPullDelegates.size();i++){
			mPullDelegates.get(i).onPulling(offsetY);
		}
	}

	@Override
	public void stopPull() {
		if(refreshState.isRefreshStateReleaseToRefresh()){
			setRefreshing();
		}
		for(int i = 0; i < mPullDelegates.size();i++){
			mPullDelegates.get(i).stopPull(refreshState);
		}
	}
	public void refreshCompletely(){
		resetSubView();
		mRefreshView.reset();
		refreshState.makePullToRefresh();
	}
	
	@Override
	public void setPullToRefresh() {
		refreshState.makePullToRefresh();
	}
	@Override
	public void setReleaseToRefresh() {
		refreshState.makeReleaseToRefresh();
	}
	@Override
	public void setRefreshing() {
		refreshState.makeRefreshing();
		if(refreshListener != null){
			refreshListener.onRefresh();
		}
	}
	@Override
	public void stopPull(BLPullRefreshState state) {}
	
	public void setOnLoadingLintener(OnLoadingListener listener){
		this.loadingListener = listener;
		absListView.setOnScrollListener(new OnScrollListener(){

			@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) 
				{
					if(scrollListener != null)
					{
						scrollListener.onScrollStateChanged(view, scrollState);
					}
				}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if((!loadState.loading) && loadState.hasMore && ((firstVisibleItem + visibleItemCount +AVAILABLE_ITEM) >= totalItemCount)){
					makeOnLoading();
				}
				if(scrollListener != null)
					{
						scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
					}
			}
			
		});
		addFootView();
		
	}

	private void makeOnLoading(){
		loadState.makeOnLoading();
		if(foot != null)
		{
			foot.onLoading();
		}
		if(loadingListener != null)
		{
			loadingListener.onLoading();
		}
		
	}
	public void makeLoadReturn(){
		loadState.makeLoadReturn();
		if(foot != null)
		{
			foot.loadingReturn();
		}
	}
	public void makeLoadedAll(){
		loadState.makeLoadedAll();
		if(foot != null)
		{
			if(absListView.getChildCount()  == absListView.getCount()){
				foot.setEmptyContent();
			}else{
				foot.loadedAll();
			}
		}
	}
	
}
