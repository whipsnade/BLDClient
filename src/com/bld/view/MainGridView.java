package com.bld.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ProgressBar;

public class MainGridView extends GridView {
	
	public interface OnScrollListener {
		void onBottom(int scrollY);

		void onTop(int scrollY);

		void onScroll(int scrollY);

		void onAutoScroll(int scrollY);

		void onUpdata(int scrollY);

		// 上滑
		void onScrollUP(int scrollY);

		// 下滑
		void onScrollDOWN(int scrollY);
		
		void onScrollMid(int scrollY);
	}

	private OnScrollListener onScrollListener;
	private boolean canLoading = true;
	Integer tasksOver = 0;
	int tasksLength;
	private ProgressBar loadingbar;
	
	
	
	public MainGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MainGridView(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	
	public OnScrollListener getOnScrollListener() {
		return onScrollListener;
	}

	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

}
