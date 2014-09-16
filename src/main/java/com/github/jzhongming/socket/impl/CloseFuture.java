package com.github.jzhongming.socket.impl;

import com.github.jzhongming.socket.CCEvent;
import com.github.jzhongming.socket.CCSession;

public class CloseFuture extends AbsCCFuture {

	public CloseFuture(CCSession session, CCEvent event) {
		super(session, event);
	}

	@Override
	protected void complit() {
		event.inform();
	}
	
}
