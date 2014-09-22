/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jzhongming.mytools.serializer;

public class ByteHelper {

	public static short ToInt16(byte[] buffer) throws IllegalArgumentException {
		if (buffer.length < 2) {
			throw new IllegalArgumentException();
		}
		short int16 = 0;
		int16 = (short) (buffer[0] & 0xff);
		int16 |= ((short) buffer[1] << 8) & 0xff00;
		return int16;
	}

	public static int ToInt32(byte[] buffer) throws IllegalArgumentException {
		if (buffer.length < 4) {
			throw new IllegalArgumentException();
		}
		int int32 = 0;
		int32 = buffer[0] & 0xff;
		int32 |= ((int) buffer[1] << 8) & 0xff00;
		int32 |= ((int) buffer[2] << 16) & 0xff0000;
		int32 |= ((int) buffer[3] << 24) & 0xff000000;
		return int32;
	}

	public static long ToInt64(byte[] buffer) throws IllegalArgumentException {
		if (buffer.length < 8) {
			throw new IllegalArgumentException();
		}
		long int64 = 0;
		int64 = buffer[0] & 0xffL;
		int64 |= ((long) buffer[1] << 8) & 0xff00L;
		int64 |= ((long) buffer[2] << 16) & 0xff0000L;
		int64 |= ((long) buffer[3] << 24) & 0xff000000L;
		int64 |= ((long) buffer[4] << 32) & 0xff00000000L;
		int64 |= ((long) buffer[5] << 40) & 0xff0000000000L;
		int64 |= ((long) buffer[6] << 48) & 0xff000000000000L;
		int64 |= ((long) buffer[7] << 56);
		return int64;
	}

	public static byte[] GetBytesFromInt16(short value) {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) value;
		buffer[1] = (byte) (value >> 8);
		return buffer;
	}

	public static byte[] GetBytesFromInt32(int value) {
		byte[] buffer = new byte[4];
		for (int i = 0; i < 4; i++) {
			buffer[i] = (byte) (value >> (8 * i));
		}
		return buffer;
	}

	public static byte[] GetBytesFromInt64(long value) {
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) {
			buffer[i] = (byte) (value >> (8 * i));
		}
		return buffer;
	}

	public static byte[] GetBytesFromChar(char ch) {
		int temp = (int) ch;
		byte[] b = new byte[2];
		for (int i = b.length - 1; i >= 0; i--) {
			b[i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	// public static char getCharFromBytes(byte[] b) {
	// int s = 0;
	// if (b[0] > 0) {
	// s += b[0];
	// } else {
	// s += 256 + b[0];
	// }
	// s *= 256;
	// if (b[1] > 0) {
	// s += b[1];
	// } else {
	// s += 256 + b[1];
	// }
	// char ch = (char) s;
	// return ch;
	// }

	public static char getCharFromBytes(final byte[] b) {
		int s = 0;
		s += (b[0] > 0) ? b[0] : (256 + b[0]);
		s <<= 8;
		s += (b[1] > 0) ? b[1] : (256 + b[1]);
		return (char) s;
	}

}
