package com.test.pulltorefresh;

import com.example.test_.R;
import com.test.pulltorefresh.BLPullLoadState.LoadStateDelegate;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BLPullFooterView extends RelativeLayout implements LoadStateDelegate{

	String TAG = "FootView";
	private Context mContext;
	private TextView text;
	
	public BLPullFooterView(Context context) {
		this(context,null);
	}
	public BLPullFooterView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public BLPullFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.foot_view, this, true);
		text = (TextView)findViewById(R.id.text);
		init();
	}
	
	public void init() {
	}
	
	
	@Override
	public void onLoading() {
		text.setText(R.string.loading);
	}
	@Override
	public void loadingReturn() {
		text.setText(R.string.pull_to_load);
	}
	@Override
	public void loadedAll() {
		text.setText(R.string.no_more_load);
	}
	
	public void setEmptyContent() {
		text.setText("");
	}
	
}
