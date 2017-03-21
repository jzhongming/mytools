package com.github.jzhongming.mytools.serializer;

public class Int64Serializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteInt64((Long) obj);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return inStream.ReadInt64();
	}

}
