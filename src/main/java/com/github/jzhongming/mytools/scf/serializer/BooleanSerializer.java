package com.github.jzhongming.mytools.scf.serializer;

public class BooleanSerializer implements SerializerBase {

	private static final byte ZERO = 0;
	private static final byte ONE = 1;

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		outStream.WriteByte(((Boolean) obj) ? ONE : ZERO);

	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return (inStream.read() > 0);
	}

}
