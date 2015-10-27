package com.test.pulltorefresh;

import java.util.ArrayList;
import java.util.List;

import com.test.pulltorefresh.BLPullRefreshState.RefreshStateDelegate;

public class BLPullRefreshState{
	
	List<RefreshStateDelegate> mRefreshStateDelegates = new ArrayList<RefreshStateDelegate>();

	static int PULL_TO_REFRESH = 0;
	static int RELEASE_TO_REFRESH = 1;
	static int REFRESHING = 2;
	int refreshState = PULL_TO_REFRESH;
	
	public boolean isRefreshStatePullToRefresh(){
		return PULL_TO_REFRESH == refreshState;
	}
	public boolean isRefreshStateReleaseToRefresh(){
		return RELEASE_TO_REFRESH == refreshState;
	}
	public boolean isRefreshStateRefreshing(){
		return REFRESHING == refreshState;
	}
	
	interface RefreshStateDelegate{

		public void setPullToRefresh();
		public void setReleaseToRefresh();
		public void setRefreshing();
		public void refreshCompletely();
	}

	public void makeReleaseToRefresh() {
		refreshState = RELEASE_TO_REFRESH;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setReleaseToRefresh();
		}
	}
	
	public void makeRefreshing() {
		refreshState = REFRESHING;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setRefreshing();
		}
	}
	public void makePullToRefresh() {
		refreshState = PULL_TO_REFRESH;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setPullToRefresh();
		}
	}
	
	protected void addRefreshStateDelegate(RefreshStateDelegate delegate){
		if(!mRefreshStateDelegates.contains(delegate)){
			mRefreshStateDelegates.add(delegate);
		}
	}
	
}
