package com.bld.task;

import java.util.ArrayList;
import java.util.LinkedList;

import android.util.Log;

import com.bld.task.Task.OnSystemFinishListen;
import com.bld.task.Task.TaskStatus;

public class TaskQueue implements Runnable, OnSystemFinishListen {
	static String debug = "TaskQueue";
	@SuppressWarnings("unchecked")
	// 在等待的任务队列
	 static LinkedList<Task> tasks_wait = new LinkedList<Task>();

	public static class TaskQueueExpection extends Exception{
		TaskQueueExpection(String detailMessage) {
			super(detailMessage);
			// TODO Auto-generated constructor stub
		}
		
	};
	
	// 正在执行的任�?
	 static ArrayList<Task> tasks_running = new ArrayList<Task>();
	// 是否持续运行
	public static boolean isRun=true;
	// 队列线程
	private static Thread taskQueueThread;
	// runnable保证线程安全
	private static TaskQueue runnable = new TaskQueue();;
	// �?��线程�?
	static int ThreadMaxNum = 1;

	// 如果队列线程为空或�?停止则重新开�?
	public static void serivesRun() {
		// TODO Auto-generated method stub
		boolean isCanSeriver=false;
		synchronized (tasks_running) {
			isCanSeriver=tasks_running.size() < ThreadMaxNum;
		}
		if (isCanSeriver && taskQueueThread == null
				|| (taskQueueThread != null && taskQueueThread
						.getState() == Thread.State.TERMINATED)) {

			taskQueueThread = new Thread(runnable);
			taskQueueThread.start();
			

		}

	}
	
	//获取正在执行的任务数
	public static int getRunningTaskCount() {
		synchronized (TaskQueue.tasks_running) {
			return TaskQueue.tasks_running.size();
		}
	}
	//设置�?��任务�?
	public static void setThreadMaxNum(int num) {
		if (num <= 0) {
			TaskQueue.ThreadMaxNum = 1;
			return;
		}
		if (num > 10) {
			TaskQueue.ThreadMaxNum = 10;
			return;
		}
		TaskQueue.ThreadMaxNum = num;
	}

	// 线程�?如果等待队列的任务数不为空，并且当前线程数字少于�?��线程�?
	public static boolean taskRun() {
		synchronized (tasks_wait) {
			synchronized (tasks_running) {
				return !tasks_wait.isEmpty()
						&& tasks_running.size() < ThreadMaxNum;
			}
		}
	}
    //�?��新线�?
	@Override
	public void run() {
		// 线程�?如果等待队列的任务数不为空，并且当前线程数字少于�?��线程�?
        Task t;
		if((t=getWaittingTask())!=null)
		{
			t.setOnSystemFinishListen(runnable);
			t.threadRun();
		}

	}
	
	 //递归 避免新开线程   唤醒等待中的任务 但此方案会�?成java.lang.StackOverflowError
	@Deprecated
	private void notifyWaitingTask()
	{
		Task newTask;
		if((newTask=getWaittingTask())!=null)
		{
			newTask.setOnSystemFinishListen(runnable);
			newTask.run();
		}
	}
	
	private  Task getWaittingTask()
	{
		Task t=null;
		while (isRun && taskRun()) {
			// 添加带执行中动�?数组�?
			synchronized (tasks_wait) {
				// 从等待任务的队列中获取并移除此列表的头（第一个元素）
				t = tasks_wait.poll();
				Log.i(debug, "队列的任务数" + tasks_wait.size());
				// 如果h为空则从队列重新取对象或者任务绑定的状�?变化�?
				if (t == null || t.status == TaskStatus.without) {
					Log.i(debug, "任务取消 编号" + t!=null?String.valueOf(t.getTaskID()):"空任务");
					continue;
				}
			}
			synchronized (tasks_running) {
				tasks_running.add(t);
			}
			Log.i(debug, "正在执行任务" + tasks_running.size() + "/上限"
					+ ThreadMaxNum);

			return t;
		}
		return t;
	}
	

	@Override
	public void OnSystemFinish(Task t, Object data) {
		// TODO Auto-generated method stub
		synchronized (tasks_running) {
			// 从处理中的动态数组中移除此任�?
			tasks_running.remove(t);
			Log.i(debug, "执行队列中移除任务taskid=" + t.taskID);
			// 通知执行后续未处理的任务
			Log.i(debug, "正在执行任务" + tasks_running.size() + "/上限"
					+ ThreadMaxNum);
            
			if(t.getTaskObservable()!=null)
			{
				t.getTaskObservable().deleteObserver(t);
			}
			// 移除此名字映�?
			if (t.getSingletonName() != null) {
				Task.getNameTask().remove(t.getSingletonName());
			}
			//通知任务�?��的任务组
			t.notifyTaskGroup();
		}
		
		//唤醒等待中的任务  并开启新线程处理
         runnable.run();
         	
        //递归 避免新开线程   唤醒等待中的任务 
        //但此方案在大量任务时会�?成java.lang.StackOverflowError
        //递归 堆栈也不能提高�?能故此抛�?
        //runnable.notifyWaitingTask();
		synchronized (tasks_wait) {

			// 如果等待队列中的任务
			if (tasks_wait.isEmpty())
				taskQueueThread = null;
		}
		//onFinishListen后回�?
//		if(t.onFinishListen==null){
//		   t.cacheClear();
//		}
	}

}
