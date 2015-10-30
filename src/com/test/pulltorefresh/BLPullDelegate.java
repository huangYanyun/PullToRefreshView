package com.test.pulltorefresh;

public interface BLPullDelegate {
	
	public void startPull();
	public void onPulling(float offsetY);
	public void stopPull(int state);
	public void stopPull();

}
