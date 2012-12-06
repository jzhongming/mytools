package com.github.jzhongming.mytools.utils;

import java.io.UnsupportedEncodingException;

/**
 * Convert number to bytes[] and vice versa.
 * 
 * @author Alex (j.zhongming@gmail.com)
 */

public class ByteUtil {
	private ByteUtil() {

	}

	private static final String DEFAULT_CHARSET_NAME = "UTF-8";

	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static byte[] getBytes(final String k) {
		if (k == null || k.isEmpty()) {
			throw new IllegalArgumentException("Key must not be blank");
		}
		try {
			return k.getBytes(DEFAULT_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getString(final byte[] bytes) {
		try {
			return new String(bytes, DEFAULT_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void byte2hex(final byte b, final StringBuffer buf) {
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}

	public static void putShort(byte[] bb, final short s, final int index) {
		bb[index] = (byte) (s >> 8);
		bb[index + 1] = (byte) (s);
	}

	public static short getShort(byte[] bb, final int index) {
		return (short) (((bb[index] << 8) | bb[index + 1] & 0xff));
	}

	public static void putInt(byte[] bb, final int x, final int index) {
		bb[index + 0] = (byte) (x >> 24);
		bb[index + 1] = (byte) (x >> 16);
		bb[index + 2] = (byte) (x >> 8);
		bb[index + 3] = (byte) (x >> 0);
	}

	public static int getInt(byte[] bb, final int index) {
		return (int) ((((bb[index + 0] & 0xff) << 24)
				| ((bb[index + 1] & 0xff) << 16)
				| ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
	}

	public static void putLong(byte[] bb, final long x, final int index) {
		bb[index + 0] = (byte) (x >> 56);
		bb[index + 1] = (byte) (x >> 48);
		bb[index + 2] = (byte) (x >> 40);
		bb[index + 3] = (byte) (x >> 32);
		bb[index + 4] = (byte) (x >> 24);
		bb[index + 5] = (byte) (x >> 16);
		bb[index + 6] = (byte) (x >> 8);
		bb[index + 7] = (byte) (x >> 0);
	}

	public static long getLong(byte[] bb, final int index) {
		return ((((long) bb[index + 0] & 0xff) << 56)
				| (((long) bb[index + 1] & 0xff) << 48)
				| (((long) bb[index + 2] & 0xff) << 40)
				| (((long) bb[index + 3] & 0xff) << 32)
				| (((long) bb[index + 4] & 0xff) << 24)
				| (((long) bb[index + 5] & 0xff) << 16)
				| (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));

	}

	public static String getString(byte[] bb, final int offset, final int limit) {
		return new String(bb, offset, limit);
	}

	public static void putString(byte[] src, byte[] bb, final int offset,
			final int limit) {
		System.arraycopy(src, 0, bb, offset, limit);
	}

	private final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999,
			9999999, 99999999, 999999999, Integer.MAX_VALUE };

	private final static byte[] DigitTens = { '0', '0', '0', '0', '0', '0',
			'0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1',
			'1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3',
			'3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4',
			'4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
			'5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7',
			'7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8',
			'8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9',
			'9', '9', '9', };

	private final static byte[] DigitOnes = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', };

	private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	// Requires positive x
	public static final int stringSize(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	// Requires positive x
	public static final int stringSize(long x) {
		long p = 10;
		for (int i = 1; i < 19; i++) {
			if (x < p)
				return i;
			p = 10 * p;
		}
		return 19;
	}

	public static void getBytes(long i, int index, byte[] buf) {
		long q;
		int r;
		int pos = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Get 2 digits/iteration using longs until quotient fits into an int
		while (i > Integer.MAX_VALUE) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[--pos] = DigitOnes[r];
			buf[--pos] = DigitTens[r];
		}

		// Get 2 digits/iteration using ints
		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;
			// really: r = i2 - (q * 100);
			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[--pos] = DigitOnes[r];
			buf[--pos] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i2 <= 65536, i2);
		for (;;) {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
			buf[--pos] = digits[r];
			i2 = q2;
			if (i2 == 0)
				break;
		}
		if (sign != 0) {
			buf[--pos] = sign;
		}
	}

	/**
	 * Places characters representing the integer i into the character array
	 * buf. The characters are placed into the buffer backwards starting with
	 * the least significant digit at the specified index (exclusive), and
	 * working backwards from there.
	 * 
	 * Will fail if i == Integer.MIN_VALUE
	 */
	public static void getBytes(int i, int index, byte[] buf) {
		int q, r;
		int pos = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Generate two digits per iteration
		while (i >= 65536) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[--pos] = DigitOnes[r];
			buf[--pos] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i <= 65536, i);
		for (;;) {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
			buf[--pos] = digits[r];
			i = q;
			if (i == 0)
				break;
		}
		if (sign != 0) {
			buf[--pos] = sign;
		}
	}
}
