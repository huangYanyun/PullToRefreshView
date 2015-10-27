package com.test.pulltorefresh;

import android.widget.AbsListView;

public interface OnPullViewScrollListener {

	public void onScrollStateChanged(AbsListView view, int scrollState);
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
