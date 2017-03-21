package com.github.jzhongming.mytools.serializer;

public class NullSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteInt32(0);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return null;
	}

}
