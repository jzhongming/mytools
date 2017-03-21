package com.github.jzhongming.socket;

/**
 * 组包接口
 * @author Alex
 *
 * @param <T>
 */
public interface CCPackage<T> {
	byte[] encode(T t);

	T decode(byte[] msg);
}
