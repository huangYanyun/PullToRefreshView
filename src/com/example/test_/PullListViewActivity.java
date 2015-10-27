package com.example.test_;

import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.test.pulltorefresh.OnLoadingListener;
import com.test.pulltorefresh.OnRefreshListener;
import com.test.pulltorefresh.BLPullListViewController;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PullListViewActivity extends Activity{
	
	String TAG = "PullListViewActivity";
	BLPullListViewController controller;
	ListView listview;
	List<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_listview_activity);
		
		controller = (BLPullListViewController) findViewById(R.id.listview);
		listview = controller.getContentView();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		controller.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				Log.i(TAG, "onRefresh");
				controller.refreshCompletely();
				/*
				new Handler().postDelayed(new Runnable() {
				    
				    @Override
				    public void run() {
				        // TODO Auto-generated method stub
				    	
				    }
				}, 1000);
//				waitSeconds();
 * 
 */
			}
		});
		controller.setOnLoadingLintener(new OnLoadingListener(){

			@Override
			public void onLoading() {
				if(list.size() < 5){
					controller.makeLoadReturn();
				}
				else{
					controller.makeLoadedAll();
					Log.i("test===", "listview.count = "+listview.getCount());
					Log.i("test===", "listview.count = "+((AbsListView)listview).getCount());
					
				}
			}
		});
		
		
		listview.setAdapter(adapter);
		setData();
	}
	
	private void setData(){
		int size = list.size();
		for(int i = size; i < size + 5;i++){
			list.add("this is item "+i);
		}
		adapter.notifyDataSetChanged();
	}
	
	private void waitSeconds(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){

			@Override
			public void run() {
				//
			}
			
		};
		timer.schedule(task, 1000);
	}
}
