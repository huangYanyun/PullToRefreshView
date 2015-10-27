package com.example.test_;

import java.util.ArrayList;
import java.util.List;

import com.test.pulltorefresh.OnLoadingListener;
import com.test.pulltorefresh.OnRefreshListener;
import com.test.pulltorefresh.BLPullGridView;
import com.test.pulltorefresh.BLPullGridViewController;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class PullGridViewActivity extends Activity{
	
	String TAG = "PullListViewActivity";
	BLPullGridViewController controller;
	BLPullGridView listview;
	List<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_gridview_activity);
		
		controller = (BLPullGridViewController) findViewById(R.id.listview);
		listview = controller.getContentView();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		controller.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				waitFor();
				
			}
			
		});
		controller.setOnLoadingLintener(new OnLoadingListener(){

			@Override
			public void onLoading() {
//				setData();
				if(list.size() < 30){
//					setData();
					waitFor();
					controller.makeLoadReturn();
				}
				else{
					controller.makeLoadedAll();
				}
			}
		});
		
		listview.setNumColumns(5);
		listview.setAdapter(adapter);
		setData();
	}
	
	private void setData(){
		int size = list.size();
		for(int i = size; i < size + 40;i++){
			list.add("this is item "+i);
		}
		adapter.notifyDataSetChanged();
	}
	
	private void waitFor(){
		new Handler().postDelayed(new Runnable() {
		    
		    @Override
		    public void run() {
		    	controller.refreshCompletely();
		    }
		}, 1000);
	}
}
