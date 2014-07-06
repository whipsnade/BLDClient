package com.bld.task;

import java.util.HashMap;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public abstract class Task implements Runnable, Observer {
	String debug = getClass().getSimpleName();


	static final Handler publicModuleHandler = new Handler();
	

	
	class OnFinishListenRunnable implements Runnable
	{
		
		
		private OnFinishListenRunnable(Task task) {
			super();
			this.task = task;
			publicModuleHandler.post(this);
		}
		protected Task task;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task.getOnFinishListen().OnFinish(task, task.getResult());
		}
	}
	
	class OnProgressListenRunnable implements Runnable
	{	
		
		OnProgressListenRunnable(Task task,int progress,Object data) {
			super();
			this.task = task;
			this.progress=progress;
			this.data=data;
			publicModuleHandler.post(this);
		}
		protected Task task;
		protected int progress;
		protected Object data;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			task.getOnProgressListen().onProgress(task, progress, data);
		}
	}
	

	
	
	public static enum TaskPriority {
		max, min;
	}

	/** 鍗曚緥 鍙互鎻愰珮鎬ц兘 */
	protected final static Exception withoutException = new Exception(
			"The state is without");

	// 鍚嶅瓧鏄犲皠
	private static HashMap<String, Task> nameTasks;

	public static HashMap<String, Task> getNameTask() {
		if (nameTasks == null) {
			nameTasks = new HashMap<String, Task>();
		}
		return nameTasks;

	}

	public Task setSingletonName(String singletonName) {
		this.singletonName = singletonName;
		return this;
	}

	public String getSingletonName() {
		return singletonName;
	}

	public interface OnStartListen {
		void onStart(Task t);
	}

	public interface OnProgressListen {
		void onProgress(Task task, int progress, Object data);
	}

	public interface OnFinishListen {
		void OnFinish(Task task, Object data);
	}

	public interface OnSystemStartListen {
		void onSystemStart(Task task);
	}

	public interface OnSystemFinishListen {
		void OnSystemFinish(Task t, Object data);
	}

	/** 璇锋眰鍙傛暟 */
	protected Object parameter;
	/** 浠诲姟寮?鐩戝惉 */
	protected OnStartListen onStartListen;
	/** 浠诲姟杩涘害鐩戝惉 */
	protected OnProgressListen onProgressListen;
	/** 浠诲姟瀹屾垚鐩戝惉 */
	protected OnFinishListen onFinishListen;
	/** 浠诲姟鍦ㄩ槦鍒椾腑瀹屾垚 鐩戝惉 */
	protected OnSystemStartListen onSystemStartListen;
	/** 浠诲姟鍦ㄩ槦鍒椾腑寮? 鐩戝惉 */
	protected OnSystemFinishListen onSystemFinishListen;
	/** 鐢ㄤ簬浠诲姟瀹屾垚鍚庡彂閫佹秷鎭?*/
	protected Handler handler;
	/** 缁撴灉 */
	protected Object result;
	/** 浠诲姟缂栧彿鏍囩ず */
	protected int taskID = -1;
	/** 浠诲姟鍚嶅瓧鏍囩ず */
	/** 璁剧疆姝や换鍔″悕鏄惁涓哄崟渚嬶紝鍗曚緥妯″紡涓嬶紝濡傛灉鐩稿悓鍚嶅瓧鐨勪换鍔℃湭鎵ц瀹岋紝鍒欐棤娉曟坊鍔犳柊浠诲姟 */
	protected String singletonName;

	/** 淇濆瓨涓?釜瀵硅薄 */
	protected Object tag;
	/** 鑾峰緱褰撳墠鑷韩绾跨▼鐨勫紩鐢?鍦╰hreadRun鏂规硶 */
	protected Thread thread;
	/** 閲嶈繛娆℃暟 */
	protected int tryAgainCount = 1;
	/** 閲嶈繛闂撮殧 */
	protected int tryAgainTime = 1000;
	/** 瑙嗗浘缁戝畾 */
	protected TaskViewHolder taskViewHolder;

	/** 浠诲姟瑙傚療鑰?*/
	protected TaskObservable taskObservable;

	/** 榛樿浼樺厛绾т綆 */
	protected TaskPriority priority = TaskPriority.min;
	protected Context context;

	
	
	/** 浠诲姟瀹瑰櫒 */
	protected TaskContainer taskContainer;
	
	/** 浠诲姟缁?*/
	protected TaskGroup taskGroup;
	
	//閫氱煡鎵?湪瀹瑰櫒
	protected final void notifyTaskGroup()
	{
		if(taskGroup!=null)
		{
			taskGroup.notifyTaskGroup(this);
		}
	}

	public TaskContainer getTaskContainer() {
		return taskContainer;
	}

	public void setTaskContainer(TaskContainer taskContainer) {
		this.taskContainer = taskContainer;
	}

	public TaskGroup getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
	}

	protected Task() {
	}

	protected Task(TaskObservable taskObservable) {
		super();
		taskObservable.addObserver(this);
	}

	protected Task(TaskViewHolder taskViewHolder) {
		super();
		this.taskViewHolder = taskViewHolder;
		this.handler = taskViewHolder.getActivityHandler();
		this.taskViewHolder.getTaskObservable().addObserver(this);
		this.context = taskViewHolder.getContext();
		this.taskObservable = this.taskViewHolder.getTaskObservable();
	}

	// 浠诲姟鐘舵?
	public static enum TaskStatus {
		// 鏈鐞?鍑洪敊 瀹屾垚 鎵ц涓?鎺掗櫎
		untreated, wait,error, finsh, running, without;
	}

	/** 鐘舵? */
	TaskStatus status = TaskStatus.untreated;

	public void setWithout() {
		this.status = TaskStatus.without;
	}

	public void remove() {
		this.status = TaskStatus.without;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}


	
	/** 鍚姩绾跨▼ */
	public void start() {
		if (this.priority == null)
			this.priority = TaskPriority.min;
		
		synchronized (TaskQueue.tasks_wait) {
			if (getSingletonName() != null
					&& Task.getNameTask().get(this.getSingletonName()) != null) {
				Log.i("TaskQueueManager", this.getSingletonName() + "");
				this.setWithout();
			} else {
				Task.getNameTask().put(this.getSingletonName(), this);

			}

			switch (priority) {
			case min:
				TaskQueue.tasks_wait.remove(this);
				TaskQueue.tasks_wait.add(this);
				break;
			case max:
				TaskQueue.tasks_wait.remove(this);
				TaskQueue.tasks_wait.addFirst(this);
				break;
			default:
				break;
			}
			// 鍚姩姝ゆ湇鍔?
			TaskQueue.serivesRun();
		}
		
	}

	/** 鍚姩绾跨▼ */
	public void start(TaskPriority priority) {
		
		
		this.priority = priority;
		status=TaskStatus.wait;
		start();
	}

	public TaskObservable getTaskObservable() {
		return taskObservable;
	}

	public void setTaskObservable(TaskObservable taskObservable) {
		this.taskObservable = taskObservable;
	}

	/** 鍚姩绾跨▼ */
	final void threadRun() {
		thread = new Thread(this);
		thread.start();
	}

	// 涓柇Execute鏂规硶
	public abstract void shutDownExecute();

	public abstract Object cacheData(Object parameter);

	// 绂佹琚噸鍐?
	public final Object Execute() throws Exception {
		// TODO Auto-generated method stub
		if (onStartListen != null)
			onStartListen.onStart(this);

		// 闃熷垪涓洖璋?
		if (onSystemStartListen != null)
			onSystemStartListen.onSystemStart(this);
		// 鐘舵?浠庢湭澶勭悊鏀瑰彉涓哄鐞嗕腑
		status = TaskStatus.running;

		// 鑾峰彇鏈?悗涓?鏄惁閿欒
		Exception exception = null;
		// 鏄惁鏈夌紦瀛樻暟鎹鏋滄病鏈?
		if ((result = cacheData(parameter)) == null) {

			// 澶辫触閲嶈仈娆℃暟
			for (int i = 0; i < tryAgainCount; i++) {
				try {
					// 濡傛灉鐘舵?鏀瑰彉涓烘帓闄ゅ垯璺冲嚭澶辫触閲嶈仈
					if (status == TaskStatus.without) {
						break;
					}
					exception = null;
					result = obtainData(result, parameter);
					System.out.println("result=" + result);
					break;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if ((exception = e) == withoutException) {
						break;
					}
					e.printStackTrace();
					try {
						Thread.sleep(tryAgainTime);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		// 濡傛灉鏈?悗涓?浠嶇劧澶辫触鍒欐姏鍑?
		if (exception != null) {
			throw exception;
		}
		 if(onProgressListen!=null)
         {
			 new OnProgressListenRunnable(this, 100, result);
         }

		// 濡傛灉鐘舵?鏀瑰彉涓哄鐞嗗畬浣嗕笉閫氱煡
		if (status != TaskStatus.without) {

			if (onFinishListen != null) {
				//瀹屾垚鐩戝惉骞跺皢缁撴灉鍔犲叆鍒颁富绾跨▼
				new OnFinishListenRunnable(this);
			}
			;

			if (handler != null) {
				handler.obtainMessage(taskID, result).sendToTarget();
			}

		}
		if (onSystemFinishListen != null) {
			onSystemFinishListen.OnSystemFinish(this, result);
		}
		status = TaskStatus.finsh;
		return result;
	}

	public abstract Object obtainData(Object result, Object parameter)
			throws Exception;

	@Override
	public void update(Observable observable, Object data) {
		// 绉婚櫎瑙傚療
		observable.deleteObserver(this);
		// 涓柇 鍋滄鍏抽棴杩炴帴
		this.shutDownExecute();
		this.setWithout();
		if (this.thread != null) {
			this.thread.interrupt();
		}
		// 閿欒灏濊瘯娆℃暟涓?
		this.tryAgainCount = 0;
	};

	@Override
	public void run() {

		try {
			Execute();
		} catch (Exception e) {
			e.printStackTrace();
			status = TaskStatus.error;

			// 濡傛灉鐘舵?鏀瑰彉涓哄鐞嗗畬浣嗕笉閫氱煡
			if (status != TaskStatus.without) {
				

				if (handler != null) {
					handler.obtainMessage(taskID, e).sendToTarget();
				}
				
				if (onFinishListen != null) {
					//灏嗙粨鏋滃姞鍏ュ埌涓荤嚎绋?
					new OnFinishListenRunnable(this);
				}

			}
			if (onSystemFinishListen != null) {
				onSystemFinishListen.OnSystemFinish(this, e);
			}
		}

	}

//	public void cacheClear() {
//
//		if (taskOverCache) {
//			if (parameter != null)
//				parameter = null;
//			if (onStartListen != null)
//				onStartListen = null;
//			if (onProgressListen != null)
//				onProgressListen = null;
//			if (onFinishListen != null)
//				onFinishListen = null;
//			if (onSystemStartListen != null)
//				onSystemStartListen = null;
//			if (onSystemFinishListen != null)
//				onSystemFinishListen = null;
//			if (handler != null)
//				handler = null;
//			if (result != null)
//				result = null;
//			if (singletonName != null)
//				singletonName = null;
//			if (tag != null)
//				tag = null;
//			if (thread != null)
//				thread = null;
//			if (taskViewHolder != null)
//				taskViewHolder = null;
//			if (taskObservable != null)
//				taskObservable = null;
//			if (context != null)
//				context = null;
//			if (priority != null)
//				priority = null;
//		}
//	}



	public TaskViewHolder getTaskViewHolder() {
		return taskViewHolder;
	}

	public void setTaskViewHolder(TaskViewHolder taskViewHolder) {
		this.taskViewHolder = taskViewHolder;
	}

	public Object getTag() {
		return tag;
	}

	public Task setTag(Object tag) {
		this.tag = tag;
		return this;
	}

	public Thread getThread() {
		return thread;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public Object getParameter() {
		return parameter;
	}

	public Task setParameter(Object parameter) {
		this.parameter = parameter;
		return this;
	}

	public OnStartListen getOnStartListen() {
		return onStartListen;
	}

	public Task setOnStartListen(OnStartListen onStartListen) {
		this.onStartListen = onStartListen;
		return this;
	}

	public OnProgressListen getOnProgressListen() {
		return onProgressListen;
	}

	public Task setOnProgressListen(OnProgressListen onProgressListen) {
		this.onProgressListen = onProgressListen;
		return this;
	}

	public OnFinishListen getOnFinishListen() {
		return onFinishListen;
	}

	public Task setOnFinishListen(OnFinishListen onFinishListen) {
		this.onFinishListen = onFinishListen;
		return this;
	}

	public OnSystemStartListen getOnSystemStartListen() {
		return onSystemStartListen;
	}

	public OnSystemFinishListen getOnSystemFinishListen() {
		return onSystemFinishListen;
	}

	public void setOnSystemFinishListen(
			OnSystemFinishListen onSystemFinishListen) {
		this.onSystemFinishListen = onSystemFinishListen;
	}

	public Handler getHandler() {
		return handler;
	}

	public Task setHandler(Handler handler) {
		this.handler = handler;
		return this;
	}

	public int getTaskID() {
		return taskID;
	}

	public Task setTaskID(int taskID) {
		this.taskID = taskID;
		return this;
	}

	public Object getResult() {
		return result;
	}

	public int getTryAgainCount() {
		return tryAgainCount;
	}

	public Task setTryAgainCount(int tryAgainCount) {
		this.tryAgainCount = tryAgainCount;
		return this;
	}

	public int getTryAgainTime() {
		return tryAgainTime;
	}

	private Task setTryAgainTime(int tryAgainTime) {
		this.tryAgainTime = tryAgainTime;
		return this;
	}

}
