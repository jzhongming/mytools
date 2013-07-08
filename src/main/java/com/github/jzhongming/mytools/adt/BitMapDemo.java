package com.github.jzhongming.mytools.adt;

import java.util.Arrays;
import java.util.Random;

/**
 * 给40亿个不重复的unsignedint的整数，没排过序的，
 * 然后再给一个数，如何快速判断这个数是否在那40亿个数当中？
 * @author sd
 *
 */
public class BitMapDemo {
	/**
	 * 解题思路，用bitmap（位图）的方式节约空间
	 * 
	 * 
	 * int共有32位(留一个符号位)。一个int可以表示 0～30之间的数字。可以申请一个数组，a[0]可以表示0～30
	 * a[1] 表示 31～61 以此类推。
	 */
	
	
	/**
	 * TODO tip:最高位留0	 * 这样只能保留到0～30 
	 * 每一个数字智能 存储31个数字
	 */
	
	/**
	 * test 100*32
	 * 申请一个数组
	 */
	int[] bitmap = new int[100];
	
	/**
	 * 将一连串的数字 初始化到 bitmap中
	 * @param numArr 给出的数字数组
	 * @param bitMap 待初始化的数组
	 */
	public static void initBitMap(int[] numArr , int[] bitMap){
		for(int i = 0 ; i < numArr.length ; i++){
			int index = getIndex(numArr[i]);
			int destnum = getMod(numArr[i]);
			System.out.println("src "+numArr[i]+"getindex:"+index+"get destnum"+destnum);
			adjustBitMap(destnum, bitMap, index);
		}
	}
	
	
	/**
	 * 算法主要做的工作
	 * 
	 * 将 0～30之间的数字存入到bitmap中。 最高位符号位置位为0（正数）
	 *  一个a[0]数字 用32位可以表示成 0100101010101010……要调整这个数字
	 *  
	 *  destnum 0 第2位，1 第3位---30 第 32 位
	 * @param destnum 要存入的数字（在 0 ～31之间）
	 * @param bitMap 
	 * @param index 数组索引
	 */
	public static void adjustBitMap(int destnum , int[] bitMap , int index){
		if(destnum <31 && destnum > -1){
			int numpre = bitMap[index];
			String binary_str = Integer.toBinaryString(numpre);
			binary_str = adjustBinaryStr(binary_str);
			char[] cArr = binary_str.toCharArray();
//			System.out.println(cArr.length);
			cArr[destnum+1] = '1';
			System.out.println("get binary str result "+String.valueOf(cArr));
			int num_result = Integer.valueOf(String.valueOf(cArr), 2);
			bitMap[index] = num_result;
		}
		
	}
	
	/**
	 * 将已知的二进制串改写成有32位的字符串
	 * @param srcBinaryStr
	 */
	public static String adjustBinaryStr(String srcBinaryStr){
		int str_length = srcBinaryStr.length();
		if(str_length < 32){
			for(int i = 0 ; i < 32-str_length ; i++){
				srcBinaryStr = "0"+srcBinaryStr;
			}
		}
		return srcBinaryStr;
	}
	
	/**
	 * 查找着数字是否在bitmap中
	 * @param destNum 要查找的数字
	 * @param bitMap 已经初始化的bitmap
	 * @return
	 */
	public static boolean findNum(int destNum , int[] bitMap){
		int index = getIndex(destNum);
		int mod = getMod(destNum);
		int numpre = bitMap[index];
		String binary_str = Integer.toBinaryString(numpre);
		binary_str = adjustBinaryStr(binary_str);
		char[] cArr = binary_str.toCharArray();
//		System.out.println(cArr.length);
		boolean flag = false;
		if(cArr[mod+1] == '1'){
			flag = true;
		}
//		System.out.println("get binary str result "+String.valueOf(cArr));
		int num_result = Integer.valueOf(String.valueOf(cArr), 2);
		bitMap[index] = num_result;
		return flag;
	}
	
	/**
	 * 获取 这个数字存储在bitmap中的索引位置（0～30 存在 a1中）
	 * @param srcNum
	 * @return
	 */
	public static int getIndex(int srcNum ){
		return srcNum / 31;
	}
	
	public static int getMod(int srcNum){
		return srcNum % 31;
	}
	
	/**
	 * 初始化 要存入bitmap中的数字
	 * @param numArr
	 */
	public static void initNumArr(int[] numArr){
		int limitNum = numArr.length;
		Random ran = new Random();
		for(int i = 0 ; i < limitNum ; i++){
			numArr[i] = ran.nextInt(limitNum);
		}
	}
	
	public static void main(String[] args){
//		String srcBinaryStr = "100";
//		srcBinaryStr = adjustBinaryStr(srcBinaryStr);
//		System.out.println(srcBinaryStr);
//		System.out.println("1111000000000000000000000000011".length());
//		int[] bitMap = new int[2];
//		int destnum = 2;
//		int index = 0;
//		adjustBitMap(0, bitMap, 1);
//		adjustBitMap(1, bitMap, 1);
//		adjustBitMap(2, bitMap, 1);
//		adjustBitMap(3, bitMap, 1);
//		adjustBitMap(29, bitMap, 1);
//		adjustBitMap(30, bitMap, 1);
//		System.out.println(Arrays.toString(bitMap));
//		System.out.println(Integer.toBinaryString(bitMap[1]));
//		int srcNum = 63;
//		int index = getIndex(srcNum);
//		int mod = getMod(srcNum);
//		System.out.println("index:"+index+"mod:"+mod);
		int searchNum = 56;
//		int searchNum = 66;
		int[] numArr = {19, 64, 45, 56, 0, 54, 28, 2, 23, 34, 40, 18, 54, 50, 49, 29, 20, 31, 47, 30, 24, 17, 50, 57, 33, 55, 21, 22, 27, 45, 3, 19, 17, 49, 24, 5, 15, 24, 27, 35, 6, 53, 9, 61, 4, 6, 12, 23, 52, 48, 39, 39, 21, 1, 11};
//		int[] numArr = new int[55];
//		initNumArr(numArr);
//		System.out.println(Arrays.toString(numArr));
		Arrays.sort(numArr);
//		System.out.println(Arrays.toString(numArr));
		int map_length = numArr.length % 32==0?numArr.length/32 : numArr.length/32+1;
		int[] bitMap = new int[map_length+1];
		initBitMap(numArr, bitMap);
//		System.out.println(Arrays.toString(bitMap));
		boolean find = findNum(searchNum, bitMap);
		System.out.println(find);
	}
	
	
}
