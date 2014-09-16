package com.github.jzhongming.mytools.scf.serializer;

public class Int32Serializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		outStream.WriteInt32((Integer) obj);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return inStream.ReadInt32();
	}

}
