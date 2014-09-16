package com.github.jzhongming.socket.impl;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.jzhongming.socket.CCFuture;
import com.github.jzhongming.socket.CCSession;

public class CCSessionImpl implements CCSession {
	/**sessionId*/
	private long id;
	
	/**属性映射表*/
	Map<String, Object> attributeMap = new ConcurrentHashMap<String, Object>();
	
	/**是否正式被关闭*/
	volatile boolean isClose;
	
	/**是否正在关闭中*/
	AtomicBoolean isCloseing = new AtomicBoolean(false);
	
	/**是否正在改变控制*/
	AtomicBoolean isChangeingControl = new AtomicBoolean(false);
	
	/**是否正在超时处理中超时处理中,false:还未开始超时处理,true:超时处理开始了*/
	AtomicBoolean isOverTimeHandleing = new AtomicBoolean(false);
	
	/**通道*/
	SocketChannel channel;
	
	/**选择键*/
	SelectionKey selectionKey;
	
	/**写消息时，是否为阻塞写*//*
	private boolean isblockWrite;
	
	*//**读消息时，是否阻塞读*//*
	private boolean isblockRead;
	
	*//**关闭会话时，是否阻塞关闭*//*
	private boolean isblockClose;*/
	
	/**最近读时间*/
	long lastReadTime = System.currentTimeMillis();
	
	/**最近写时间*/
	long lastWriteTime = System.currentTimeMillis();
	
	/**最近访问时间*/
	long lastAccessTime = System.currentTimeMillis();
	
	/**写队列*/
	ConcurrentLinkedQueue<CCFuture> writeQueue = new ConcurrentLinkedQueue<CCFuture>();
	
	/**检查超时*/
	final TimeOutCheckTask timeOutCheckTask;
	
	/**设置分发器*/
	Dispatcher dispatcher;
	
	/**关闭会话时的Future*/
	private CloseFuture closeFuture = new CloseFuture(this, new InformCCEvent());
	
	/**
	 * 分发器
	 */
	Reactor reactor;
	
	protected CCSessionImpl() {
		timeOutCheckTask = new TimeOutCheckTask();
	}
	protected CCSessionImpl(SocketChannel channel, Reactor reactor) {
		this.channel = channel;
		this.reactor = reactor;
		this.timeOutCheckTask = new TimeOutCheckTask();
	}
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public void addAttribute(String key, Object value) {
		attributeMap.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return attributeMap.get(key);
	}

	@Override
	public Object removeAttribute(String key) {
		return attributeMap.remove(key);
	}

	@Override
	public boolean isClosed() {
		return isClose;
	}

	@Override
	public boolean isCloseing() {
		return isCloseing.get();
	}

	@Override
	public CCFuture close() {
		return null;
	}

	@Override
	public int available() {
		throw new java.lang.UnsupportedOperationException("no implement");
	}

	@Override
	public CCFuture connect() {
		return null;
	}

	@Override
	public void read(WindowData[] msgs) {
		
	}

	@Override
	public WindowData read() {
		return null;
	}

	@Override
	public CCFuture write(WindowData msg) {
		return null;
	}
	
	void notifyTimeOut() {
		
	}
	
	SocketChannel getChannel() {
		return channel;
	}
	
	SelectionKey getSelectionKey(){
		return selectionKey;
	}
	
	void setSelectionKey(SelectionKey key) {
		this.selectionKey = key;
	}
	
	/**获得超时检查任务*/
	TimeOutCheckTask getCheckOverTime() {
		return timeOutCheckTask;
	}
	
	CloseFuture getCloseFuture() {
		return closeFuture;
	}
	
	/**
	 * 设置发布器
	 * @param dispatcher
	 */
	void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	
	/**定时检查是否超时*/
	private class TimeOutCheckTask extends TimerTask {

		@Override
		public void run() {
			
		}
		
	}

}
