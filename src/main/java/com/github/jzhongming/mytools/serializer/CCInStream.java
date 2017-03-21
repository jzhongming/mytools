package com.github.jzhongming.mytools.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CCInStream extends ByteArrayInputStream {

	private ConcurrentHashMap<Integer, Object> _RefPool = new ConcurrentHashMap<Integer, Object>();

	public CCInStream(byte[] buf) {
		super(buf);
	}

	public CCInStream(byte[] buffer, int offset, int length) {
		super(buffer, offset, length);
	}

	public void SafeRead(byte[] buffer) throws IllegalArgumentException, IOException {
		if (this.read(buffer) != buffer.length) {
			throw new IllegalArgumentException("Stream error: buffer is error length");
		}
	}

	public Object GetRef(int refId) {
		return _RefPool.get(refId);
	}

	public void SetRef(int refId, Object obj) {
		_RefPool.putIfAbsent(refId, obj);
	}

	public short ReadInt16() throws Exception {
		byte[] buffer = new byte[2];
		if (this.read(buffer) != 2) {
			throw new IllegalArgumentException();
		}
		return ByteHelper.ToInt16(buffer);
	}

	public int ReadInt32() throws Exception {
		byte[] buffer = new byte[4];
		if (this.read(buffer) != 4) {
			throw new IllegalArgumentException();
		}
		return ByteHelper.ToInt32(buffer);
	}

	public long ReadInt64() throws Exception {
		byte[] buffer = new byte[8];
		if (this.read(buffer) != 8) {
			throw new IllegalArgumentException();
		}
		return ByteHelper.ToInt64(buffer);
	}
}
