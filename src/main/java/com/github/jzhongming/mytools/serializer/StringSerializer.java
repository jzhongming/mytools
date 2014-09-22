package com.github.jzhongming.mytools.serializer;

public class StringSerializer implements ISerializer {
	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (outStream.isRefWrited(obj)) {
			return;
		}
		byte[] buffer = obj.toString().getBytes(TypeHelper.ENCODER);
		byte[] bLen = ByteHelper.GetBytesFromInt32(buffer.length);
		byte[] bytes = new byte[buffer.length + 4];
		System.arraycopy(bLen, 0, bytes, 0, 4);
		System.arraycopy(buffer, 0, bytes, 4, buffer.length);
		outStream.write(bytes);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
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
			throw new IllegalArgumentException("Data length overflow. max [1024 * 1024 * 100]");
		}
		if (len == 0) {
			inStream.SetRef(hashcode, "");
			return "";
		}
		byte[] buffer = new byte[len];
		inStream.SafeRead(buffer);
		String str = new String(buffer, TypeHelper.ENCODER);
		inStream.SetRef(hashcode, str);
		return str;
	}

}
