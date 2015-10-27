package com.test.pulltorefresh;

public interface BLPullDelegate {
	
	public void startPull();
	public void onPulling(float offsetY);
	public void stopPull(BLPullRefreshState state);
	public void stopPull();

}
