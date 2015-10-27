package com.example.test_;

import java.util.ArrayList;

import java.util.List;

import com.test.pulltorefresh.OnRefreshListener;
import com.test.pulltorefresh.BLPullScrollView;
import com.test.pulltorefresh.BLPullScrollViewController;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PullScrollViewActivity extends Activity{
	
	String TAG = "PullListViewActivity";
	BLPullScrollViewController controller;
	BLPullScrollView listview;
	LinearLayout linear;
	int MAX_LOAD = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_scrollview_activity);
		
		controller = (BLPullScrollViewController) findViewById(R.id.listview);
		linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		listview = controller.getScrollView();
		controller.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				Log.i(TAG, "onRefresh");
				new Handler().postDelayed(new Runnable() {
				    
				    @Override
				    public void run() {
				        // TODO Auto-generated method stub
				    	setData();
						controller.refreshCompletely();
				    }
				}, 1000);
				
			}
			
		});
		/*
		controller.setOnLoadingLintener(new OnLoadingListener(){

			@Override
			public void onLoading() {
				setData();
				if(list.size() < 100){
					controller.makeLoadReturn();
				}
				else{
					controller.makeLoadedAll();
				}
			}
		});
		*/
		
		setData();
	}
	
	private void setData(){
		if(MAX_LOAD < 4){
			for(int i = 0; i < 15; i ++){
				TextView text = new TextView(this);
				text.setText("this is item "+ i);
				text.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,100));
				linear.addView(text);
			}
			MAX_LOAD = MAX_LOAD + 1;
			listview.removeAllViews();
			listview.addView(linear);
		}
	}
}
