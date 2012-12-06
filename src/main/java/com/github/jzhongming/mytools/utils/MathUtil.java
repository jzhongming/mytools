package com.github.jzhongming.mytools.utils;

public class MathUtil {
	private MathUtil() {

	}

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
		for (int i=0, j=array.length-1; j > i;++i,--j) {
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
		for (int i=0, j=array.length-1; j > i;++i,--j) {
			array[i] ^= array[j];
			array[j] ^= array[i];
			array[i] ^= array[j];
		}
		return array;
	}

	public static void main(String[] args) {
		System.out.println(MathUtil.isEven(-432422));
		System.out.println(MathUtil.isOdd(-432412));
		char tt[] = { 'a', 'b', 'c', 'd', 'e' };
		System.out.println(tt);
		revertArray(tt);
		System.out.println(tt);
	}
}
