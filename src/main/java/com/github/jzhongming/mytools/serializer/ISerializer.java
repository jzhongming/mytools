package com.github.jzhongming.mytools.serializer;

public interface ISerializer {
	
	public abstract void WriteObject(Object obj, CCOutStream outStream) throws Exception;

	public abstract Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception;
}
