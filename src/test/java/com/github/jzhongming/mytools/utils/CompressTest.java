package com.github.jzhongming.mytools.utils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompressTest {

	private static String str = "";
	private static final int SIZE = 1024*1024*2;
	
	@BeforeClass
	public static void start() {
		StringBuilder sbd = new StringBuilder();
		for(int i=0; i< SIZE; i++) {
			sbd.append((char)MathUtil.random(20000, 40000));
		}
		str = sbd.toString();
	}
	
	@Test
	public void zipCompressTest() {
		byte[] b = CompressUtil.zipCompress(str.getBytes());
		b = CompressUtil.zipDecompress(b);
//		System.out.println(new String(b));
		Assert.assertEquals(str, new String(b));
	}
	
	@Test
	public void gzipCompressTest() {
		byte[] b = CompressUtil.gzipCompress(str.getBytes());
		b = CompressUtil.gzipDecompress(b);
//		System.out.println(new String(b));
		Assert.assertEquals(str, new String(b));
	}
	
	@Test
	public void compressTest() throws Exception {
		byte[] b = CompressUtil.compress(str.getBytes());
		b = CompressUtil.uncompress(b);
//		System.out.println(new String(b));
		Assert.assertEquals(str, new String(b));
	}
}
