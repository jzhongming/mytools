package com.github.jzhongming.mytools.utils;

import java.util.Arrays;

import org.junit.Test;

/**
 * 洗牌算法测试
 * @author Administrator
 *
 */
public class ShuffleTest {
	private static final int[] card = {'A','B','C','D','E','F','G','H','I','J'};
	private static int[][] count;
	private static final int SHUFFLECOUNT = 2000000;
	
	private static void shuffle1() {
		int[] array = Arrays.copyOf(card, 10);
		MathUtil.Shuffle(array);
		for(int i=0; i<10; i++) {
			count[array[i]-'A'][i]++;
		}
	}
	
//	private static void shuffle2() {
//		int[] array = Arrays.copyOf(card, 10);
//		MathUtil.Shuffle2(array);
//		for(int i=0; i<10; i++) {
//			count[array[i]-65][i]++;
//		}
//	}
	
	private static void show() {
		for(int i=0; i<10; i++) {
			if(i == 0) {
				System.out.print("      ");
				for(int c : card)
					System.out.print((char)c + "      ");
				
				System.out.println("\n-------------------------------------------------------------------------");
			}
			for(int j=0; j<10; j++) {
				if(j == 0) {
					System.out.print((i)+" | ");
				}
				System.out.print(count[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	@Test
	public void testShuffle() {
		count = new int[card.length][card.length];
		for(int i=0; i<SHUFFLECOUNT; i++) {
			shuffle1();
		}
		show();
		System.out.println("------------------------------- 华丽分割线 -------------------------------");
//		count = new int[card.length][card.length];
//		for(int i=0; i<SHUFFLECOUNT; i++) {
//			shuffle2();
//		}
//		show();
	}
}
