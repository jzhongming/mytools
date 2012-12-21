package com.github.jzhongming.mytools.utils;

import org.junit.Test;

public class HexUtilTest {

	@Test
	public void testHexenCoding() {
		char[] c = HexUtil.encodeHex("monster中华人民共和国".getBytes());
		for(char item: c) {
			System.out.print(item);
		}
		byte[] b = HexUtil.decodeHex(c);
		System.out.println(new String(b));
	}
}
//6d6f6e73746572e4b8ade58d8ee4babae6b091e585b1e5928ce59bbd
//6d6f6e73746572e4b8ade58d8ee4babae6b091e585b1e5928ce59bbd