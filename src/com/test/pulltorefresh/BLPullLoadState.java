package com.test.pulltorefresh;

public class BLPullLoadState{
	
	public boolean hasMore = true;
	public boolean loading = false;
	
	interface LoadStateDelegate{
		public void onLoading();
		public void loadingReturn();
		public void loadedAll();
	}

	public void makeOnLoading() {
		loading = true;
	}

	public void makeLoadReturn() {
		loading = false;
	}

	public void makeLoadedAll() {
		hasMore = false;
	}
	
}
