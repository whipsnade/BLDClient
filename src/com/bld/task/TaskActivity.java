package com.bld.task;




import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class TaskActivity extends Activity implements Handler.Callback {


	String debug=getClass().getSimpleName();
	protected TaskViewHolder taskViewHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(taskViewHolder!=null && taskViewHolder.getTaskObservable()!=null)
		{
			taskViewHolder.getTaskObservable().setChanged();
			taskViewHolder.getTaskObservable().notifyObservers();
		}
		super.onDestroy();
		
	}
	
	public TaskViewHolder getTaskViewHolder() {
		return taskViewHolder;
	}


	public void setTaskViewHolder(TaskViewHolder taskViewHolder) {
		this.taskViewHolder = taskViewHolder;
	}

	

}