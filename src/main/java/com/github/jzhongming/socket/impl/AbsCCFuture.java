package com.github.jzhongming.socket.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import com.github.jzhongming.socket.CCEvent;
import com.github.jzhongming.socket.CCFuture;
import com.github.jzhongming.socket.CCSession;

public abstract class AbsCCFuture implements CCFuture {

	private CCSession session;
	final CCEvent event;

	/** 是否被取消 */
	private AtomicBoolean isCancel;

	/** 异常类型 */
	private Throwable throwable;

	public AbsCCFuture(CCSession session, CCEvent event) {
		this.session = session;
		this.event = event;
	}

	@Override
	public CCSession getSession() {
		return session;
	}

	@Override
	public boolean isError() {
		return (this.throwable == null);
	}

	@Override
	public boolean isCannel() {
		return isCancel.get();
	}

	@Override
	public void cancel() {
		isCancel.compareAndSet(false, true);
	}

	@Override
	public Throwable getThrowable() {
		return this.throwable;
	}

	@Override
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public void await() {
		try {
			event.waitforEver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void await(long timeout) {
		try {
			event.waitforMilliSeconds(timeout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void complit();

}
