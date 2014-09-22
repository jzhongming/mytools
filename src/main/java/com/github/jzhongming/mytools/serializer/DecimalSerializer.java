package com.github.jzhongming.mytools.serializer;

import java.math.BigDecimal;


public class DecimalSerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		SerializerFactory.GetSerializer(String.class).WriteObject(obj.toString(), outStream);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		Object value = SerializerFactory.GetSerializer(String.class).ReadObject(inStream, String.class);
		return (value != null) ? new BigDecimal(value.toString()) : BigDecimal.ZERO;
	}

}
