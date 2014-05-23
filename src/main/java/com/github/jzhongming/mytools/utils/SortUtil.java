package com.github.jzhongming.mytools.utils;

import java.util.Comparator;

public class SortUtil {
//	private static final/* volatile */Random RND = new Random();

	private static <E> void swap(E[] array, int i, int j) {
		E tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private static void swap(int[] array, int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private static <E> int partition(E[] array, int begin, int end,
			Comparator<? super E> cmp) {
//		int index = begin + RND.nextInt(end - begin + 1);
		int index = begin + ((end-begin)>>1);
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

	private static <E> void qsort(E[] array, int begin, int end, Comparator<? super E> cmp) {
		if (begin < end) {
			int index = partition(array, begin, end, cmp);
			qsort(array, begin, index - 1, cmp);
			qsort(array, index + 1, end, cmp);
		}
	}
	
	//不稳定 nlgn O(n)2
	public static <E> void quickSort(E[] array, Comparator<? super E> cmp) {
		qsort(array, 0, array.length - 1, cmp);
	}
	
	private static int partition(int[] array, int begin, int end) {
		int index = begin + ((end-begin)>>1);
//		int index = begin + RND.nextInt(end - begin + 1);
		int pivot = array[index];
		swap(array, index, end);
		for (int i = index = begin; i < end; ++i) {
			if (array[i]<=pivot) {
				swap(array, index++, i);
			}
		}
		swap(array, index, end);
		return (index);
	}

	private static void qsort(int[] array, int begin, int end) {
		if (begin < end) {
			int index = partition(array, begin, end);
			qsort(array, begin, index - 1);
			qsort(array, index + 1, end);
		}
	}
	
	public static void quickSort_1(int[] array) {
		qsort(array, 0, array.length-1);
	}
	
	public static void quickSort_2(int[] sort, int begin, int end) {
		if (begin < end) {
			int left = begin, right = end, pivot = sort[left];
			while (left < right) {
				while (left < right && sort[right] > pivot)
					right--;
				if (left < right) {
					sort[left] = sort[right];
					left++;
				}
				while (left < right && sort[left] < pivot)
					left++;
				if (left < right) {
					sort[right] = sort[left];
					right--;
				}
			}
			sort[left] = pivot;
			quickSort_2(sort, begin, left - 1);
			quickSort_2(sort, left + 1, end);
		}
	}
	
	//稳定的 O(n)2
	public static <E> void bubbleSort(E[] array, Comparator<? super E> cmp) {
		boolean exchange;
		for (int i = 0, capacity = array.length; i < capacity; i++) {
			exchange = false;
			for(int j=capacity-1; j>i; j--) {
				if(cmp.compare(array[j-1], array[j])>0) {
					swap(array, j, j-1);
					exchange = true;
				}
			}
			if(exchange == false) {
				return;
			}
		}
	}
	
	public static void bubbleSort(int[] array) {
		boolean exchange;
		for (int i = 0, capacity = array.length; i < capacity; i++) {
			exchange = false;
			for(int j=capacity-1; j>i; j--) {
				if(array[j-1]> array[j]) {
					swap(array, j-1, j);
					exchange = true;
				}
			}
			if(exchange == false) {
				return;
			}
		}
	}
	
	// 稳定 O(n)2
	public static <E> void insertSort(E[] array, Comparator<? super E> cmp) {
		E temp;
		for (int i = 1, j, n = array.length; i < n; i++) {
			temp = array[i];
			for (j = i; (j > 0) && (cmp.compare(array[j - 1], temp) > 0); j--) {
				array[j] = array[j - 1];
			}
			array[j] = temp;
		}
	}

	// 稳定 O(n)2
	public static void insertSort(int[] array) {
		int temp;
		for (int i = 1, j, n = array.length; i < n; i++) {
			temp = array[i];
			for (j = i; (j > 0) && (array[j - 1] > temp); j--) {
				array[j] = array[j - 1];
			}
			array[j] = temp;
		}
	}
	
	// 不稳定 O(n)2
	public static <E> void selectSort(E[] array, Comparator<? super E> cmp) {
		int min, n = array.length;
		for (int i = 0; i < n - 1; i++) {
			min = i;
			// 查找最小值
			for (int j = i + 1; j < n; j++)
				if (cmp.compare(array[min], array[j]) > 0)
					min = j;
			// 交换
			if (min != i) {
				swap(array, min, i);
			}
		}
	}

	//不稳定 O(n)2
	public static void selectSort(int[] array) {
		int n = array.length-1;
		for (int i = 0, min; i < n; i++) {
			min = i;
			// 查找最小值
			for (int j = i + 1; j <= n; j++)
				if (array[min] > array[j])
					min = j;
			// 交换
			if (min != i) {
				swap(array, min, i);
			}
		}
	}
	
	//不稳定 O(n)-O(n)2
	public static <E> void shellSort(E[] array, Comparator<? super E> cmp) {
		E temp;
		int n = array.length;
		for (int d = n >> 1; d >= 1; d >>= 1) {
			for (int i = d, j; i < n; i++) {
				temp = array[i];
				for (j = i - d; (j >= 0) && (cmp.compare(array[j], temp) > 0); j -= d) {
					array[j + d] = array[j];
				}
				array[j + d] = temp;
			}
		}
	}

	//不稳定 O(n)-O(n)2
	public static void shellSort(int[] array) {
		int temp, n = array.length;
		for (int d = n >> 1; d >= 1; d >>= 1) {
			for (int i = d, j; i < n; i++) {
				temp = array[i];
				for (j = i - d; (j >= 0) && (array[j] > temp); j -= d) {
					array[j + d] = array[j];
				}
				array[j + d] = temp;
			}
		}
	}
	
	public static void mergeSort(int[] array) {
		
		
	}
	
	/**
	 * 将有序的array[s...m],array[m+1...e]合并成array[s...e]
	 * @param array
	 * @param s (start Index)
	 * @param m (middle Index)
	 * @param e (end Index)
	 */
	public static void twoWayMerge(int[] array, int s, int m, int e) {
		int i = s, j = m+1, k=0;
		int[] C = new int[e+1];
		while(i<=m && j<=e) {
			C[k++] = (array[i] < array[j]) ? array[i++] : array[j++];
		}
		System.arraycopy(C, 0, array, 0, e);
	}

//	稳定 nlgn
	public static int[] Two_Way_Merge_Sort(int[] A, int[] B) {
		int[] C = new int[A.length + B.length];
		int k = 0, i = 0, j = 0;
		
		while (i < A.length && j < B.length) {
			C[k++] = (A[i] < B[j]) ? A[i++] : B[j++];
		}
		while (i < A.length)
			C[k++] = A[i++];
		while (j < B.length)
			C[k++] = B[j++];
		
		return C;
	}
	public static void main(String[] args) {
		int[] array = {1,3,5,7, 2,4,6,8,9};
		insertSort(array);
//		quickSort_1(array);
		for(int a : array) {
			System.out.println(a);
		}
	}
}
