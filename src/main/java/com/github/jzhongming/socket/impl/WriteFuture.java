package com.github.jzhongming.socket.impl;

import java.nio.ByteBuffer;

import com.github.jzhongming.socket.CCEvent;
import com.github.jzhongming.socket.CCSession;

public class WriteFuture extends AbsCCFuture {

	private ByteBuffer buffer;

	public WriteFuture(CCSession session, CCEvent event) {
		super(session, event);
	}

	public WriteFuture(CCSession session, CCEvent event, ByteBuffer buffer) {
		super(session, event);
		this.buffer = buffer;
	}

	@Override
	protected void complit() {
		event.inform();
	}

	ByteBuffer getBuffer() {
		return buffer;
	}

	void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

}