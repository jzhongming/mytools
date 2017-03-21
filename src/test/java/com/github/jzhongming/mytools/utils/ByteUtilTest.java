package com.github.jzhongming.mytools.utils;

import org.junit.Assert;
import org.junit.Test;

public class ByteUtilTest {

	@Test
	public void intTest() {
		int o = 1023;
		byte[] bo =ByteUtil.intToBytesLittleEndian(o);
		Assert.assertTrue(MathUtil.isOdd(ByteUtil.bytesToIntLittleEndian(bo)));
		Assert.assertFalse(MathUtil.isEven(ByteUtil.bytesToIntLittleEndian(bo)));
		
		int e = 1024;
		byte[] be =ByteUtil.intToBytesBigEndian(e);
		Assert.assertTrue(MathUtil.isEven(ByteUtil.bytesToIntBigEndian(be)));
		Assert.assertFalse(MathUtil.isOdd(ByteUtil.bytesToIntBigEndian(be)));
	}
}
