package com.example.test_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	
	String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button list = (Button) findViewById(R.id.listview);
		Button grid = (Button) findViewById(R.id.gridview);
		Button scroll = (Button) findViewById(R.id.scrollview);
		
		list.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PullListViewActivity.class);
				startActivity(intent);
			}
			
		});
		grid.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PullGridViewActivity.class);
				startActivity(intent);
			}
			
		});
		scroll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, PullScrollViewActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
