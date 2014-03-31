package com.github.jzhongming.mytools.utils;

public final class NumericConvertUtil {

	public static final int BASE_16 = 16;
	public static final int BASE_32 = 32;
	public static final int BASE_64 = 64;

	/**
	 * 在进制表示中的字符集合
	 */
	private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '-', '_' };

	/**
	 * 将十进制的数字转换为指定进制的字符串
	 * 
	 * @param n
	 *            十进制的数字
	 * @param base
	 *            指定的进制
	 * @return
	 */
	public static String toBaseString(long n, int base) {
		long num = (n < 0) ? ((0x7fffffffL << 1) + n) : n;
		int charPos = 32;
		char[] buf = new char[32];
		while ((num / base) > 0) {
			buf[--charPos] = digits[(int) (num % base)];
			num /= base;
		}
		buf[--charPos] = digits[(int) (num % base)];
		return new String(buf, charPos, (32 - charPos));
	}

	/**
	 * 将其它进制的数字（字符串形式）转换为十进制的数字
	 * 
	 * @param str
	 *            其它进制的数字（字符串形式）
	 * @param base
	 *            指定的进制
	 * @return
	 */
	public static long toDecimal(String str, int base) {
		char[] buf = new char[str.length()];
		str.getChars(0, str.length(), buf, 0);
		long num = 0;
		for (int i = 0, len=buf.length-1; i <= len; i++) {
			for (int j = 0; j < digits.length; j++) {
				if (digits[j] == buf[i]) {
					num += j * Math.pow(base, len-i);
					break;
				}
			}
		}
		return num;
	}

	public static void main(String[] args) {
		String ss = NumericConvertUtil.toBaseString(12345, BASE_64);
		System.out.println(ss);

		System.out.println(NumericConvertUtil.toDecimal(ss, BASE_64));

		String s = "";
		for (char c : digits) {
			s += c;
		}
		System.out.println(s);
		System.out.println(s.matches("[\\w|\\-]{1,64}"));
	}
}