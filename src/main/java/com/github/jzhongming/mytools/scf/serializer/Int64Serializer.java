package com.github.jzhongming.mytools.scf.serializer;

public class Int64Serializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		outStream.WriteInt64((Long) obj);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return inStream.ReadInt64();
	}

}
