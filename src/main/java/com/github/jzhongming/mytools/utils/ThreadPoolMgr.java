package com.github.jzhongming.mytools.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 多线程池的管理类
 * 
 * @author Alex (j.zhongming@gmail.com)
 */
public enum ThreadPoolMgr {
	INSTANCE;
	
	/**
	 * 启用固定线程池，固定线程为100
	 */
	private static ExecutorService executor = Executors.newFixedThreadPool(10);

	private ThreadPoolMgr() {

	}

	/**
	 * 添加异步任务
	 * 
	 * @param task
	 *            任务线程
	 */
	public void addTask(final Thread task) throws Exception {
		this.addTask(Thread.NORM_PRIORITY, task);
	}

	/**
	 * 添加任务记录
	 * 
	 * @param threadPriority
	 *            线程优先级
	 * @param task
	 *            任务线程
	 * 
	 * @see Thread.MAX_PRIORITY, Thread.MIN_PRIORITY, Thread.NORM_PRIORITY
	 */
	public void addTask(final int threadPriority, final Thread task) throws Exception {
		int tp = (threadPriority < 5) ? Thread.MIN_PRIORITY : (threadPriority == 5) ? Thread.NORM_PRIORITY
				: Thread.MAX_PRIORITY;
		task.setPriority(tp);
		executor.execute(task);
	}

	/**
	 * 添加任务记录
	 * 
	 * @param task
	 */
	public void addTask(final Runnable task) throws Exception {
		assert (task != null);
		executor.execute(task);
	}

	/**
	 * 添加Future异常返回结果的任务
	 * 
	 * @param task
	 * @throws Exception
	 */
	public Future<?> submit(FutureTask<?> task) throws RuntimeException {
		assert (task != null);
		return executor.submit(task);
	}

	/**
	 * 停止管理者所有的线程池
	 * 
	 * @throws InterruptedException
	 */
	public void stop() throws InterruptedException {
		executor.shutdown();
	}

	/**
	 * 试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
	 * 
	 * @return
	 */
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	/**
	 * 如果关闭后所有任务都已完成，则返回 true。
	 * 
	 * @return
	 */
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	// public static void main(String[] args) throws Exception {
	// FutureTask<String> future = new FutureTask<String>(new Callable<String>()
	// {// 使用Callable接口作为构造参数
	// public String call() {
	// System.out.println("call");
	// return "Call";
	// // 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
	// }
	// });
	// ThreadPoolMgr.INSTANCE.submit(future);
	// Thread.sleep(1000);
	// System.out.println(future.get());
	// }

}
