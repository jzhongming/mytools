package com.github.jzhongming.mytools.serializer;

public class BooleanSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteByte(((Boolean) obj) ? CCOutStream.ONE : CCOutStream.ZERO);

	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return (inStream.read() > 0);
	}

}
