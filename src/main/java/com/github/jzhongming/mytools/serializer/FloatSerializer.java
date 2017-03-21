package com.github.jzhongming.mytools.serializer;

public class FloatSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteInt32(Float.floatToIntBits((Float) obj));
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return Float.intBitsToFloat(inStream.ReadInt32());
	}

}
