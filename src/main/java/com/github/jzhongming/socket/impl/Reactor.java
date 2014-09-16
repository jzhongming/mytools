package com.github.jzhongming.socket.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反应器,每个服务端一个反应器
 * 
 * @author Alex
 * 
 */
public class Reactor implements Runnable, DispatcherEventListener {
	private static Logger logger = LoggerFactory.getLogger(Reactor.class);

	@Override
	public void run() {

	}

	@Override
	public void onRemoveSession(CCSessionImpl session) {

	}

	@Override
	public void onRegisterSession(CCSessionImpl session) {
		
	}
	
	public void write() {
		
	}
	
	public void read() {
		
	}

}
