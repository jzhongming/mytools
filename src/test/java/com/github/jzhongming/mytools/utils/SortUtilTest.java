package com.github.jzhongming.mytools.utils;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class SortUtilTest {
	
	private static final Integer[] A = {23,54,24,11,0,-1,1313,0};
	private static final int[] a = {23,54,24,11,0,-1,1313,0};
	
	private static Comparator<Integer> C = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	};
	
	@Test
	public void testBubbleSort1() {
		System.out.println();
		int[] t = Arrays.copyOf(a, a.length);
		for(int i=0; i<t.length; i++)
			System.out.print(t[i] + " ");
		
		SortUtil.bubbleSort(t);
		System.out.println();
		for(int i=0; i<t.length; i++)
			System.out.print(t[i] + " ");
	}
	
	@Test
	public void testBubbleSort2() {
		System.out.println();
		Integer[] t = Arrays.copyOf(A, A.length);
		for(int i=0; i<t.length; i++)
			System.out.print(t[i] + " ");
		
		SortUtil.bubbleSort(t, C);
		System.out.println();
		for(int i=0; i<t.length; i++)
			System.out.print(t[i] + " ");
	}
	
//	public static void main(String[] args) {
//		int LEN = 100;
//		Integer[] x = new Integer[LEN];
//		int[] xx = new int[LEN];
//		for (int i = 0; i < LEN; i++) {
//			x[i] = (int) (Math.random() * LEN);
//			xx[i] = x[i];
//			System.out.print(x[i] + " ");
//		}
//		Comparator<Integer> C = new Comparator<Integer>() {
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return o1 - o2;
//			}
//		};
//		
//		long s = System.currentTimeMillis();
//		SortUtil.bubbleSort(x, C);
//		SortUtil.quickSort(x, C);
//		SortUtil.shellSort(x, C);
//		SortUtil.insertSort(x, C);
//		SortUtil.selectSort(x, C);
//		SortUtil.bubbleSort(xx);
//		SortUtil.shellSort(xx);
//		SortUtil.insertSort(xx);
//		SortUtil.selectSort(xx);
//		SortUtil.quickSort(xx);
//		System.out.println("OK : " + (System.currentTimeMillis()-s) + " ms");
//		for (int i = 0; i < LEN; i++)
//			System.out.print(xx[i] + " ");
//	}
}
