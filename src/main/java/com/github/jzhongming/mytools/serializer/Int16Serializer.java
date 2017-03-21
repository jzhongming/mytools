package com.github.jzhongming.mytools.serializer;

public class Int16Serializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteInt16((Short) obj);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return inStream.ReadInt16();
	}

}
