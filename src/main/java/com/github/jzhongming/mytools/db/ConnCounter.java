package com.github.jzhongming.mytools.db;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器Bean
 * @author Alex (j.zhongming@gmail.com)
 *
 */
public class ConnCounter {
	private String name;  // 计数器名称 
	private static final int MAX_BLOCKED = 100; //计数器最大连接 阻塞数
	private final AtomicInteger counter = new AtomicInteger(0); //原子操作递增
	
	private int maxBlocked = MAX_BLOCKED;    // 数据库是否达到阻塞值

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AtomicInteger getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter.set(counter);
	}

	public int getMaxBlocked() {
		return maxBlocked;
	}

	public void setMaxBlocked(int maxBlocked) {
		this.maxBlocked = maxBlocked;
	}

	/**
	 * 当前计数器是否到达阻塞数，如果达到，不可使用
	 * @return boolean 没有达到返回True，达到不可用False
	 */
	public boolean enter() {
		if (maxBlocked > counter.get()) {
			counter.incrementAndGet();
			return true;
		}
		return false;
	}
	
	public void outer() {
		counter.decrementAndGet();
	}

	@Override
	public String toString() {
		return name + "|" + counter.intValue() + "|" + maxBlocked;
	}
}
