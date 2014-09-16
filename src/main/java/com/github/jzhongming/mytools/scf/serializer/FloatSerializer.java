package com.github.jzhongming.mytools.scf.serializer;

public class FloatSerializer implements SerializerBase {

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		int value = Float.floatToIntBits((Float) obj);
		outStream.WriteInt32(value);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		return Float.intBitsToFloat(inStream.ReadInt32());
	}

}
