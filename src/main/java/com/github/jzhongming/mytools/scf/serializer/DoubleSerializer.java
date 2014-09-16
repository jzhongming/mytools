package com.github.jzhongming.mytools.scf.serializer;

public class DoubleSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		long value = Double.doubleToLongBits((Double) obj);
		outStream.WriteInt64(value);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return Double.longBitsToDouble(inStream.ReadInt64());
	}

}
