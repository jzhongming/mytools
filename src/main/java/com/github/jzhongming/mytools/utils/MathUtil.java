package com.github.jzhongming.mytools.utils;

/**
 * 数学相关工具类
 * 
 * @author j.zhongming@gmail.com
 * 
 */
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
		return (num & 0x0001) == 1;
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

	public static int[] revertArray(int array[], int begin, int end) {
		if (array == null) {
			return new int[0];
		}
		if (begin > end || end > array.length) {
			throw new IllegalArgumentException();
		}
		for (int i = begin, j = end; j > i; ++i, --j) {
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
			int middle = low + ((high - low) >> 1);// 减小Int溢出 (x+y)/2 ==
													// (x&y)+((x^y)>>1) ==
													// ((x-y)>>1)+y

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

	/**
	 * 返回start到end之前的随机数，不包括end
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int random(int start, int end) {
		return (int) (Math.random() * (end - start)) + start;
	}

	/**
	 * Returns true if the argument is a power of two. 判断一个整数是不是2的幂,对于一个数 x >=
	 * 0，判断他是不是2的幂
	 * 
	 * @param n
	 *            the number to test
	 * @return true if the argument is a power of two
	 */
	public static boolean isPowerOfTwo(long n) {
		return ((n & (n - 1)) == 0) && (n != 0);
	}

	public static int nextPowerOfTwo(int value) {
		// If the value isPowerOfTwo, return the value
		if (isPowerOfTwo(value)) {
			return value;
		}

		// This uses the bit-twiddling hacks described on the Stanford site:
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;

		return value + 1;
	}

	/**
	 * 交换数组对应位置数据
	 * 
	 * @param array
	 * @param i
	 * @param j
	 */
	public static void swap(int[] array, int i, int j) {
		if (i == j)
			return;

		array[i] ^= array[j];
		array[j] ^= array[i];
		array[i] ^= array[j];
	}

	/**
	 * 洗牌问题，模拟洗牌，修正概率问题
	 * 
	 * @param array
	 */
	public static void Shuffle(int[] array) {
		int N = array.length - 1;
		for (int i = 0; i <= N; i++) {
			swap(array, (N - i), (int) (Math.random() * (N - i + 1)));// 修正概率问题
		}
	}

	/**
	 * 洗牌问题，模拟洗牌，概率错误
	 * 
	 * @param array
	 */
	// public static void Shuffle2(int[] array) {
	// // int N = array.length;
	// // for (int i = 0; i <= N; i++) {
	// // swap(array, i, (int) (Math.random() * (N - i + 1 )) + i);
	// // }
	//
	// for(int idx=0; idx<array.length; idx++) {
	// swap(array, (int)(Math.random() * N), (int)(Math.random() * N));
	// }
	// }

	public static boolean nextPermutation(int[] array) {
		int i = array.length-2;
		while(i >= 0 && array[i] > array[i+1])
			--i;
		
		if(i<0) {
			revertArray(array);
			return true;
		} else {
			int j = i + 2;
			while(j<array.length && array[j] > array[i])
				++j;
			--j;
			
			swap(array, i, j);
			revertArray(array, i+1, array.length-1);
			return (i==j);
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println(MathUtil.isEven(-432422));
		System.out.println(MathUtil.isOdd(-432412));
		char tt[] = { 'a', 'b', 'c', 'd', 'e' };
		System.out.println(tt);
		revertArray(tt);
		System.out.println(tt);
		int it[] = { -434, 1, 3, 5, 9, 56, 3455, 23343 };
		System.out.println("binary_search index:" + binary_search(it, 56));
		for (int i = 0; i < 10; ++i) {
			System.out.println(i + " : " + nextPowerOfTwo(i) + " : " + isPowerOfTwo(nextPowerOfTwo(i)));
		}
		System.out.println(PI);
		System.out.println(E);
		int b = Integer.MAX_VALUE, a = 99999;
		System.out.println((a + b) / 2);
		System.out.println((a & b) + ((a ^ b) >> 1));
		System.out.println(b + ((a - b) >> 1));
		int[] itt = { 1,2,3};
		while (!nextPermutation(itt)) {
			for (int i : itt)
				System.out.print(i);
			System.out.println();
		}
	}
}
