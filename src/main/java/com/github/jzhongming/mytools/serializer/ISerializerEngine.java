package com.github.jzhongming.mytools.serializer;

/**
 * 序列化引擎接口
 * 
 * @author Jack.J
 * 
 */
public interface ISerializerEngine {
	byte[] Serialize(Object obj) throws Exception;

	Object Derialize(byte[] buffer, Class<?> type) throws Exception;
}
