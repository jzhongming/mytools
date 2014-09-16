package com.github.jzhongming.mytools.scf.serializer;

import java.nio.charset.Charset;

public class StringSerializer implements SerializerBase {
	private Charset Encoder = Charset.forName("UTF-8");
	
	@Override
	public void WriteObject(Object obj, SCFOutStream outStream)
			throws Exception {
		if (outStream.WriteRef(obj)) {
			return;
		}
		byte[] buffer = obj.toString().getBytes(Encoder);
		byte[] bLen = ByteHelper.GetBytesFromInt32(buffer.length);
		byte[] bytes = new byte[buffer.length + 4];
		System.arraycopy(bLen, 0, bytes, 0, 4);
		System.arraycopy(buffer, 0, bytes, 4, buffer.length);
		outStream.write(bytes);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		int isRef = (byte) inStream.read();
		int hashcode = inStream.ReadInt32();
		if (isRef > 0) {
			Object obj = inStream.GetRef(hashcode);
			if (obj == null) {
				return "";
			}
			return obj;
		}
		int len = inStream.ReadInt32();
		if (len > TypeHelper.MAX_DATA_LEN) {
			throw new IllegalArgumentException("Data length overflow. max [1024 * 1024 * 10]");
		}
		if (len == 0) {
			inStream.SetRef(hashcode, "");
			return "";
		}
		byte[] buffer = new byte[len];
		inStream.SafeRead(buffer);
		String str = new String(buffer, Encoder);
		inStream.SetRef(hashcode, str);
		return str;
	}

}
