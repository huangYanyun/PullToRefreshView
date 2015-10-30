package com.test.pulltorefresh;

import java.util.ArrayList;
import java.util.List;

public class BLPullRefreshState{
	
	List<RefreshStateDelegate> mRefreshStateDelegates = new ArrayList<RefreshStateDelegate>();

	static int PULL_TO_REFRESH = 0;
	static int RELEASE_TO_REFRESH = 1;
	static int REFRESHING = 2;
	static int REFRESH_COMPLETET = 3;
	public int refreshStateFlag = PULL_TO_REFRESH;
	
	public boolean isRefreshStatePullToRefresh(){
		return PULL_TO_REFRESH == refreshStateFlag;
	}
	public boolean isRefreshStateReleaseToRefresh(){
		return RELEASE_TO_REFRESH == refreshStateFlag;
	}
	public boolean isRefreshStateRefreshing(){
		return REFRESHING == refreshStateFlag;
	}
	public boolean isRefreshStateRefreshComplete(){
		return REFRESH_COMPLETET == refreshStateFlag;
	}
	
	interface RefreshStateDelegate{

		public void setPullToRefresh();
		public void setReleaseToRefresh();
		public void setRefreshing();
		public void refreshCompletely();
	}

	public void makeReleaseToRefresh() {
		refreshStateFlag = RELEASE_TO_REFRESH;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setReleaseToRefresh();
		}
	}
	
	public void makeRefreshing() {
		refreshStateFlag = REFRESHING;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setRefreshing();
		}
	}
	public void makePullToRefresh() {
		refreshStateFlag = PULL_TO_REFRESH;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).setPullToRefresh();
		}
	}
	public void makeRefreshCompletet() {
		refreshStateFlag = REFRESH_COMPLETET;
		for(int i = 0; i < mRefreshStateDelegates.size();i++){
			mRefreshStateDelegates.get(i).refreshCompletely();
		}
	}
	
	protected void addRefreshStateDelegate(RefreshStateDelegate delegate){
		if(!mRefreshStateDelegates.contains(delegate)){
			mRefreshStateDelegates.add(delegate);
		}
	}
	
}
