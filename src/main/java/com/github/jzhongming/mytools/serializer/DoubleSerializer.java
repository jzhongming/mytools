package com.github.jzhongming.mytools.serializer;

public class DoubleSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		outStream.WriteInt64(Double.doubleToLongBits((Double) obj));
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		return Double.longBitsToDouble(inStream.ReadInt64());
	}

}
