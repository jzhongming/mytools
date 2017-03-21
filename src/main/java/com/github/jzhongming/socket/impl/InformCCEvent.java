package com.github.jzhongming.socket.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.github.jzhongming.socket.CCEvent;

/**
 * 默认通知事件实现
 * @author Administrator
 *
 */
public class InformCCEvent implements CCEvent {
	private final CountDownLatch cdLatch;

	public InformCCEvent() {
		cdLatch = new CountDownLatch(1);
	}

	public InformCCEvent(int waitCount) {
		cdLatch = new CountDownLatch(waitCount);
	}

	@Override
	public void inform() {
		cdLatch.countDown();
	}

	@Override
	public boolean waitfor(long time, TimeUnit timeUnit) throws InterruptedException {
		return cdLatch.await(time, timeUnit);
	}

	@Override
	public boolean waitforMilliSeconds(long time) throws InterruptedException {
		return waitfor(time, TimeUnit.MILLISECONDS);
	}

	@Override
	public void waitforEver() throws Exception {
		cdLatch.await();
	}
	
}
