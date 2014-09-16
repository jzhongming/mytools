package com.github.jzhongming.mytools.scf.serializer;

public interface SerializerBase {
	
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception;

	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception;
}
