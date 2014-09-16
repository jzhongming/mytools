package com.github.jzhongming.mytools.scf.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class SCFOutStream extends ByteArrayOutputStream {

	private static final byte ZERO = 0;
	private static final byte ONE = 1;

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

	public boolean WriteRef(Object obj) throws IOException {
		if (obj == null) {
			this.WriteByte(ONE);
			this.WriteInt32(ZERO);
			return true;
		}

		int objHashcode = obj.hashCode();
		if (_RefPool.containsKey(objHashcode)) {
			WriteByte(ONE);
			WriteInt32(objHashcode);
			return true;
		} else {
			_RefPool.put(objHashcode, obj);
			WriteByte(ZERO);
			WriteInt32(objHashcode);
			return false;
		}
	}
}
