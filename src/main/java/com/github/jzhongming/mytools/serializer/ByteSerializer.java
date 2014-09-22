package com.github.jzhongming.mytools.serializer;

public class ByteSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteByte((Byte) obj);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return (byte) inStream.read();
	}

}
