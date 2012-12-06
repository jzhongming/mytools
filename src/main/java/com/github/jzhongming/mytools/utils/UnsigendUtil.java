package com.github.jzhongming.mytools.utils;

import java.nio.ByteBuffer;

/**
 * 向ByteBuffer对象中获取和存放无符号值的工具类。 这里所有的函数都是静态的，并且带有一个ByteBuffer参数。
 * 由于java不提供无符号原始类型，每个从缓冲区中读出的无符号值被升到比它大的 下一个基本数据类型中。
 * getUnsignedByte()返回一个short类型, getUnsignedShort( )
 * 返回一个int类型，而getUnsignedInt()返回一个long型。 由于没有基本类型来存储返回的数据，
 * 因此没有getUnsignedLong()。 如果需要，返回BigInteger的函数可以执行。 同样，存放函数要取一个大于它们所分配的类型的值。
 * putUnsignedByte取一个short型参数，等等。
 * */
public class UnsigendUtil {
	public static short getUnsignedByte(ByteBuffer bb) {
		return ((short) (bb.get() & 0xff));
	}

	public static void putUnsignedByte(ByteBuffer bb, int value) {
		bb.put((byte) (value & 0xff));
	}

	public static short getUnsignedByte(ByteBuffer bb, int position) {
		return ((short) (bb.get(position) & (short) 0xff));
	}

	public static void putUnsignedByte(ByteBuffer bb, int position, int value) {
		bb.put(position, (byte) (value & 0xff));
	}

	// ---------------------------------------------------------------
	public static int getUnsignedShort(ByteBuffer bb) {
		return (bb.getShort() & 0xffff);
	}

	public static void putUnsignedShort(ByteBuffer bb, int value) {
		bb.putShort((short) (value & 0xffff));
	}

	public static int getUnsignedShort(ByteBuffer bb, int position) {
		return (bb.getShort(position) & 0xffff);
	}

	public static void putUnsignedShort(ByteBuffer bb, int position, int value) {
		bb.putShort(position, (short) (value & 0xffff));
	}

	// ---------------------------------------------------------------
	public static long getUnsignedInt(ByteBuffer bb) {
		return ((long) bb.getInt() & 0xffffffffL);
	}

	public static void putUnsignedInt(ByteBuffer bb, long value) {
		bb.putInt((int) (value & 0xffffffffL));
	}

	public static long getUnsignedInt(ByteBuffer bb, int position) {
		return ((long) bb.getInt(position) & 0xffffffffL);
	}

	public static void putUnsignedInt(ByteBuffer bb, int position, long value) {
		bb.putInt(position, (int) (value & 0xffffffffL));
	}

}
