package com.bld.task;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;



public  class TaskViewHolder {
	String debug=getClass().getSimpleName();
	private View rootView;
	protected Handler activityHandler;
	//任务�?
	protected TaskObservable taskObservable=new TaskObservable();
	protected Context context;


	public TaskViewHolder(TaskActivity activity,int resxml) {
		rootView = (LayoutInflater.from(activity).inflate(resxml, null));
		activityHandler = new Handler(activity);
		activity.setContentView(rootView);
		context=activity;
		
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public View getRootView() {
		return rootView;
	}

	public void setRootView(View rootView) {
		this.rootView = rootView;
	}

	public TaskObservable getTaskObservable() {
		return taskObservable;
	}

	public void setTaskObservable(TaskObservable taskObservable) {
		this.taskObservable = taskObservable;
	}

	public Handler getActivityHandler() {
		return activityHandler;
	}

	public void setActivityHandler(Handler activityHandler) {
		this.activityHandler = activityHandler;
	}

	public void cacheClear() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					taskObservable.setChanged();
					taskObservable.notifyObservers();
					rootView = null;
					activityHandler = null;
					context=null;
					taskObservable = null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}) {
		};
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	public void addObserver(Task task) {
		// TODO Auto-generated method stub
		taskObservable.addObserver(task);
	}


	
	

}
