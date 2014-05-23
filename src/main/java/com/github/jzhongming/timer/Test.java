
package com.github.jzhongming.timer;




public class Test {
	private static final char[] HEXDUMP_TABLE = new char[256 * 4];

    static {
        final char[] DIGITS = "0123456789abcdef".toCharArray();
        for (int i = 0; i < 256; i ++) {
            HEXDUMP_TABLE[ i << 1     ] = DIGITS[i >>> 4 & 0x0F];
            HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i       & 0x0F];
        }
    }
    
    public static void main(String[] args) {
		for(char c : HEXDUMP_TABLE) {
			System.out.print(c);
			System.out.print(' ');
		}
	}
}
