package com.github.jzhongming.mytools.utils;

public class MathUtil {
	private MathUtil() {
	}

	/** Archimede's constant PI, ratio of circle circumference to diameter. */
	public static final double PI = 105414357.0 / 33554432.0 + 1.984187159361080883e-9;

	/** Napier's constant e, base of the natural logarithm. */
	public static final double E = 2850325.0 / 1048576.0 + 8.254840070411028747e-8;

	/**
	 * 判断数字是否是偶数
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isEven(final int num) {
		return (num & 0x0001) == 0;
	}

	/**
	 * 判断数字是否是奇数
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isOdd(final int num) {
		return !isEven(num);
	}

	/**
	 * 反转指定数组
	 * 
	 * @param array
	 * @return
	 */
	public static int[] revertArray(int array[]) {
		if (array == null) {
			return new int[0];
		}
		for (int i = 0, j = array.length - 1; j > i; ++i, --j) {
			array[i] ^= array[j];
			array[j] ^= array[i];
			array[i] ^= array[j];
		}
		return array;
	}

	/**
	 * 反转指定数组
	 * 
	 * @param array
	 * @return
	 */
	public static char[] revertArray(char array[]) {
		if (array == null) {
			return new char[0];
		}
		for (int i = 0, j = array.length - 1; j > i; ++i, --j) {
			array[i] ^= array[j];
			array[j] ^= array[i];
			array[i] ^= array[j];
		}
		return array;
	}

	/**
	 * 指定值为负，则返回 -1；如果指定值为零，则返回 0；如果指定的值为正，则返回 1
	 * 
	 * @param i
	 * @return
	 */
	public static int signum(int i) {
		return (i >> 31) | (-i >>> 31);
	}

	/**
	 * 二分查找分组中数据，返回所在位置下标，-1为不存在
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	public static int binary_search(int array[], int value) {
		int low = 0, high = array.length - 1;

		while (low <= high) {
			int middle = low + ((high - low) >> 1);// 去除法，提高性能

			if (array[middle] > value) {
				high = middle - 1;
			} else if (array[middle] < value) {
				low = middle + 1;
			} else {
				return middle;
			}
		}
		return -1;
	}

	public static int nextPowerOfTwo(int value) {
		// The algorithm below does not work if value is zero:
		if (value == 0) {
			return 1;
		}

		// This uses the bit-twiddling hacks described on the Stanford site:
		int result = value - 1;
		result |= result >> 1;
		result |= result >> 2;
		result |= result >> 4;
		result |= result >> 8;
		result |= result >> 16;

		return ++result;
	}

	/**
	 * Returns true if the argument is a power of two.
	 * 
	 * @param n
	 *            the number to test
	 * @return true if the argument is a power of two
	 */
	public static boolean isPowerOfTwo(long n) {
		return (n > 0) && ((n & (n - 1)) == 0);
	}

	public static void main(String[] args) {
		System.out.println(MathUtil.isEven(-432422));
		System.out.println(MathUtil.isOdd(-432412));
		char tt[] = { 'a', 'b', 'c', 'd', 'e' };
		System.out.println(tt);
		revertArray(tt);
		System.out.println(tt);
		int it[] = { -434, 1, 3, 5, 9, 56, 3455, 23343 };
		System.out.println(binary_search(it, 56));
		for (int i = 0; i < 10; ++i) {
			System.out.println(i + " : " + nextPowerOfTwo(i) + " : " + isPowerOfTwo(nextPowerOfTwo(i)));
		}
	}
}
