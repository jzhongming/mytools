package com.github.jzhongming.mytools.scf.serializer;

public class ByteSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		outStream.WriteByte((Byte) obj);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return (byte) inStream.read();
	}

}
