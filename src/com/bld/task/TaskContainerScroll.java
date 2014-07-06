package com.bld.task;

import java.util.LinkedList;

import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

/**
 * 用于Scrolls�?建立滚动缓存区任务容�?超量的任务不再被加载
 * */
@LargeTest
public class TaskContainerScroll extends TaskContainer {

	
	//使用LinkedList双向链表的理由是在顺序和倒叙移除元素时�?能较�?
	LinkedList<Task> tasks=new LinkedList<Task>();

	// �?��缓冲�?
	int maxSize = 30;

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void shutDownExecute() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object cacheData(Object parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object obtainData(Object result, Object parameter) throws Exception {
		// TODO Auto-generated method stub

		return null;
	}

	public Task currorTask;

	@Override
	public TaskContainer addTask(Task task) {
		// TODO Auto-generated method stub
		if (task == null)
			return this;
		if (tasks == null) {
			tasks = new LinkedList<Task>();
		};
		if(task.status!=TaskStatus.untreated){
			return this;
		}
		synchronized (tasks) {
			tasks.addFirst(task);
			if (tasks.size() > maxSize) {
				// 移除末尾的任�?
				Task outTask = tasks.removeLast();
				if (outTask.status == TaskStatus.untreated) {
					Log.i("过量任务滤过", "任务id=" + outTask.getTaskID());
					outTask.shutDownExecute();
					outTask.setWithout();
				}
			}
			currorTask = task;
		}
		return this;

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		currorTask.start(TaskPriority.max);
	}

	@Override
	public void start(TaskPriority priority) {
		// TODO Auto-generated method stub
		super.start(priority);
	}

//	@Override
//	public void cacheClear() {
//		super.cacheClear();
//		tasks = null;
//	}

}
