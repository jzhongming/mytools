package com.github.jzhongming.mytools.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CCOutStream extends ByteArrayOutputStream {

	public static final byte ZERO = 0;
	public static final byte ONE = 1;

	private ConcurrentHashMap<Integer, Object> _RefPool = new ConcurrentHashMap<Integer, Object>();

	public void WriteByte(byte value) throws IOException {
		this.write(new byte[] { value });
	}

	public void WriteInt16(short value) throws IOException {
		byte[] buffer = ByteHelper.GetBytesFromInt16(value);
		this.write(buffer);
	}

	public void WriteInt32(int value) throws IOException {
		byte[] buffer = ByteHelper.GetBytesFromInt32(value);
		this.write(buffer);
	}

	public void WriteInt64(long value) throws IOException {
		byte[] buffer = ByteHelper.GetBytesFromInt64(value);
		this.write(buffer);
	}

	public boolean isRefWrited(Object obj) throws IOException {
		if (obj == null) {
			this.WriteByte(ONE);
			this.WriteInt32(ZERO);
			return true;
		}

		int refId = getRefId(obj);
		if (_RefPool.containsKey(refId)) {
			WriteByte(ONE);
			WriteInt32(refId);
			return true;
		} else {
			_RefPool.put(refId, obj);
			WriteByte(ZERO);
			WriteInt32(refId);
			return false;
		}
	}

	private AtomicInteger _refId = new AtomicInteger(1000);
	private ConcurrentHashMap<Object, Integer> _objMap = new ConcurrentHashMap<Object, Integer>();

	private int getRefId(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (_objMap.containsKey(obj) && obj == _objMap.get(obj)) {
			return _objMap.get(obj);
		} else {
			_objMap.put(obj, _refId.incrementAndGet());
			return _refId.get();
		}
	}
}
