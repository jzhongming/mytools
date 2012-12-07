package com.github.jzhongming.mytools.utils;

import java.io.UnsupportedEncodingException;

public class ByteUtil {
	private ByteUtil() {
	}

	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static byte[] getBytes(final String k) {
		if (k == null || k.isEmpty()) {
			throw new IllegalArgumentException("Key must not be blank");
		}
		try {
			return k.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getString(final byte[] bytes) {
		try {
			return new String(bytes, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void byte2hex(final byte b, final StringBuilder buf) {
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}
	
	public static final short bytesToShortBigEndian(final byte[] buf) {
		return bytesToShortBigEndian(buf, 0);
	}
	
	public static final short bytesToShortBigEndian(final byte[] buf, final int offset) {
		return (short) (((buf[0 + offset] << 8) & 0xff00) | (buf[1 + offset] & 0xff));
	}

	public static final short bytesToShortLittleEndian(final byte[] buf) {
		return bytesToShortLittleEndian(buf, 0);
	}
	
	public static final short bytesToShortLittleEndian(final byte[] buf, final int offset) {
		return (short) (buf[0 + offset] & 0xff | ((buf[1 + offset] << 8) & 0xff00));
	}
	
	public static final int bytesToIntBigEndian(final byte[] buf) {
		return bytesToIntBigEndian(buf, 0);
	}

	public static final int bytesToIntBigEndian(final byte[] buf, final int offset) {
		return ((buf[0 + offset] << 24) & 0xff000000)
				| ((buf[1 + offset] << 16) & 0xff0000)
				| ((buf[2 + offset] << 8) & 0xff00) | (buf[3 + offset] & 0xff);
	}
	
	public static final int bytesToIntLittleEndian(final byte[] buf) {
		return bytesToIntLittleEndian(buf, 0);
	}

	public static final int bytesToIntLittleEndian(final byte[] buf, final int offset) {
		return (buf[0 + offset] & 0xff) | ((buf[1 + offset] << 8) & 0xff00)
				| ((buf[2 + offset] << 16) & 0xff0000)
				| ((buf[3 + offset] << 24) & 0xff000000);
	}
	
	public static final long bytesToLongBigEndian(final byte buf[]) {
		return bytesToLongBigEndian(buf, 0);
	}

	public static final long bytesToLongBigEndian(final byte buf[], final int offset) {
		return (((long) buf[0 + offset] << 56   ) & 0xff00000000000000L)
				| (((long) buf[1 + offset] << 48) & 0x00ff000000000000L)
				| (((long) buf[2 + offset] << 40) & 0x0000ff0000000000L)
				| (((long) buf[3 + offset] << 32) & 0x000000ff00000000L)
				| (((long) buf[4 + offset] << 24) & 0x00000000ff000000L)
				| (((long) buf[5 + offset] << 16) & 0x0000000000ff0000L)
				| (((long) buf[6 + offset] << 8 ) & 0x000000000000ff00L)
				| ((long) buf[7 + offset] & 0xffL);
	}

	public static final long bytesToLongLittleEndian(final byte[] buf) {
		return bytesToLongLittleEndian(buf, 0);
	}
	
	public static final long bytesToLongLittleEndian(final byte[] buf, final int offset) {
		return ((long) buf[0 + offset] &            0x00000000000000ffL)
				| (((long) buf[1 + offset] << 8 ) & 0x000000000000ff00L)
				| (((long) buf[2 + offset] << 16) & 0x0000000000ff0000L)
				| (((long) buf[3 + offset] << 24) & 0x00000000ff000000L)
				| (((long) buf[4 + offset] << 32) & 0x000000ff00000000L)
				| (((long) buf[5 + offset] << 40) & 0x0000ff0000000000L)
				| (((long) buf[6 + offset] << 48) & 0x00ff000000000000L)
				| (((long) buf[7 + offset] << 56) & 0xff00000000000000L);
	}

	public static final byte[] shortToBytesBigEndian1(final short n) {
		byte[] buf = new byte[2];
		buf[0] = (byte) ((n & 0xff00) >> 8);
		buf[1] = (byte) ( n & 0x00ff);
		return buf;
	}

	public static final byte[] intToBytesBigEndian1(final int n) {
		byte[] buf = new byte[4];
		buf[0] = (byte) ((n & 0xff000000) >> 24);
		buf[1] = (byte) ((n & 0x00ff0000) >> 16);
		buf[2] = (byte) ((n & 0x0000ff00) >> 8);
		buf[3] = (byte) ( n & 0x000000ff);
		return buf;
	}

	public static final byte[] longToBytesBigEndian1(final long n) {
		byte[] buf = new byte[8];
		buf[0] = (byte) ((n & 0xff00000000000000L) >> 56);
		buf[1] = (byte) ((n & 0x00ff000000000000L) >> 48);
		buf[2] = (byte) ((n & 0x0000ff0000000000L) >> 40);
		buf[3] = (byte) ((n & 0x000000ff00000000L) >> 32);
		buf[4] = (byte) ((n & 0x00000000ff000000L) >> 24);
		buf[5] = (byte) ((n & 0x0000000000ff0000L) >> 16);
		buf[6] = (byte) ((n & 0x000000000000ff00L) >> 8);
		buf[7] = (byte) ( n & 0x00000000000000ffL);
		return buf;
	}
	
	public static final byte[] shortToBytesLittleEndian1(final short n) {
		byte[] buf = new byte[2];
		buf[0] = (byte) ((short) n & 0x00ff);
		buf[1] = (byte) (((short) n & 0xff00) >> 8);
		return buf;
	}
	
	public static final byte[] intToBytesLittleEndian(final int n) {
		byte[] buf = new byte[4];
		buf[0] = (byte) ( n & 0x000000ff);
		buf[1] = (byte) ((n & 0x0000ff00) >> 8);
		buf[2] = (byte) ((n & 0x00ff0000) >> 16);
		buf[3] = (byte) ((n & 0xff000000) >> 24);
		return buf;
	}

	public static final byte[] longToBytesLittleEndian(final long n) {
		byte[] buf = new byte[8];
		buf[0] = (byte) ( n & 0x00000000000000ffL);
		buf[1] = (byte) ((n & 0x000000000000ff00L) >> 8);
		buf[2] = (byte) ((n & 0x0000000000ff0000L) >> 16);
		buf[3] = (byte) ((n & 0x00000000ff000000L) >> 24);
		buf[4] = (byte) ((n & 0x000000ff00000000L) >> 32);
		buf[5] = (byte) ((n & 0x0000ff0000000000L) >> 40);
		buf[6] = (byte) ((n & 0x00ff000000000000L) >> 48);
		buf[7] = (byte) ((n & 0xff00000000000000L) >> 56);
		return buf;
	}
	
}
