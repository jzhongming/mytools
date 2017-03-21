package com.github.jzhongming.socket;

import java.util.concurrent.TimeUnit;

/**
 * 事件通知接口
 * 
 * @author Alex
 */
public interface CCEvent {
	// 通知接口
	void inform();
	
	//等待接口
	void waitforEver() throws Exception;

	// 等待接口
	boolean waitforMilliSeconds(long time) throws Exception;

	// 等待接口
	boolean waitfor(long time, TimeUnit timeUnit) throws Exception;
}
