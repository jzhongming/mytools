package com.github.jzhongming.mytools.utils;

import java.util.Comparator;
import java.util.Random;

public class SortUtil {
	private static final/* volatile */Random RND = new Random();

	public static <E> void quickSort(E[] array, Comparator<? super E> cmp) {
		qsort(array, 0, array.length - 1, cmp);
	}

	private static <E> void qsort(E[] array, int begin, int end,
			Comparator<? super E> cmp) {
		if (end > begin) {
			int index = partition(array, begin, end, cmp);
			qsort(array, begin, index - 1, cmp);
			qsort(array, index + 1, end, cmp);
		}
	}

	private static <E> int partition(E[] array, int begin, int end,
			Comparator<? super E> cmp) {
		int index = begin + RND.nextInt(end - begin + 1);
		E pivot = array[index];
		swap(array, index, end);
		for (int i = index = begin; i < end; ++i) {
			if (cmp.compare(array[i], pivot) <= 0) {
				swap(array, index++, i);
			}
		}
		swap(array, index, end);
		return (index);
	}

	private static void swap(Object[] array, int i, int j) {
		Object tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	public static int[] Two_Way_Merge_Sort(int[] A, int[] B) {
		int[] C = new int[A.length + B.length];
		int k = 0;
		int i = 0;
		int j = 0;
		while (i < A.length && j < B.length) {
			C[k++] = (A[i] < B[j]) ? A[i++] : B[j++];
		}
		while (i < A.length)
			C[k++] = A[i++];
		while (j < B.length)
			C[k++] = B[j++];
		return C;
	}

	// public static void main(String[] args) {
	// Integer[] A = {33,59,5,3,7,1};
	// Integer[] B = {98,4,21,0,6};
	// quickSort(A, new Comparator<Integer>() {
	//
	// @Override
	// public int compare(Integer o1, Integer o2) {
	// return o1 - o2;
	// }
	// });
	// quickSort(B, new Comparator<Integer>() {
	//
	// @Override
	// public int compare(Integer o1, Integer o2) {
	// return o1 - o2;
	// }
	// });
	// int[] C = Two_Way_Merge_Sort(ArrayUtils.toPrimitive(A),
	// ArrayUtils.toPrimitive(B));
	// for(int t : C) {
	// System.out.print(t + " ");
	// }
	// System.out.println("");
	// int c=0;
	// long t = System.currentTimeMillis();
	// for(int i=0; i<Integer.MAX_VALUE; ++i) {
	// c = (100 * (1+100)) / 2 ;
	// }
	// System.out.println((System.currentTimeMillis()-t) + " ms c = " + c);
	// long tt = System.currentTimeMillis();
	// for(int i=0; i<Integer.MAX_VALUE; ++i) {
	// c = (100 * (1+100))>>1 ;
	// }
	// System.out.println((System.currentTimeMillis()-tt) + " ms c = " + c);
	// }

	public static void main(String[] args) {
		Integer[] x = new Integer[100000];
		for (int i = 0; i < 100000; i++) {
			x[i] = (int) (Math.random() * 100000);
			System.out.println(x[i]);
		}
		for (int i = 0; i < x.length; i++) {
			for (int j = i; j > 0 && x[j - 1] > x[j]; j--) {
				swap(x, j - 1, j);
			}
		}
		for (int i = 0; i < 100; i++)
			System.out.print(x[i] + " ");
	}

	public static void swap(int[] array, int i, int j) {
		array[i] ^= array[j];
		array[j] ^= array[i];
		array[i] ^= array[j];
	}

}
