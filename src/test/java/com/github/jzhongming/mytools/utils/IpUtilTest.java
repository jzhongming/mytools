package com.github.jzhongming.mytools.utils;

import org.junit.Assert;
import org.junit.Test;

public class IpUtilTest {

	private static final String IP1 = "192.168.113.119";
	private static final long LONG1 = 3232264567L;
	private static final int INT1 = -1062702729;

	@Test
	public void ipToLongTest() {
		long iplong1 = IpUtil.ipToLong(IP1);
		String ip1 = IpUtil.longToIp(iplong1);
		Assert.assertEquals(IP1, ip1);
	}

	@Test
	public void longToIpTest() {
		String ip1 = IpUtil.longToIp(LONG1);
		long iplong1 = IpUtil.ipToLong(ip1);
		Assert.assertEquals(LONG1, iplong1);
	}

	@Test
	public void ipToIntTest() {
		int ipint1 = IpUtil.ipToInt(IP1);
		String ip1 = IpUtil.intToIp(ipint1);
		Assert.assertEquals(IP1, ip1);
	}

	@Test
	public void intToIpTest() {
		String ip1 = IpUtil.intToIp(INT1);
		int ipint1 = IpUtil.ipToInt(ip1);
		Assert.assertEquals(INT1, ipint1);
	}

	@Test
	public void isIpFormate() {
		Assert.assertTrue(IpUtil.isIpFormat(IP1));
		Assert.assertTrue(IpUtil.isIpFormat("0.0.0.1"));
		Assert.assertTrue(IpUtil.isIpFormat("1.23.45.67"));
		Assert.assertTrue(IpUtil.isIpFormat("123.123.123.123"));
		Assert.assertTrue(IpUtil.isIpFormat("255.255.255.255"));

		Assert.assertFalse(IpUtil.isIpFormat("255.05.005.255"));
		Assert.assertFalse(IpUtil.isIpFormat("255.255.255.256"));
		Assert.assertFalse(IpUtil.isIpFormat("a.b.c.d"));
		Assert.assertFalse(IpUtil.isIpFormat("255.255.255"));
		Assert.assertFalse(IpUtil.isIpFormat("123.456.789.012"));
		Assert.assertFalse(IpUtil.isIpFormat("0.0.0.0"));
	}

	@Test
	public void IntervalIpArrTest() {
		long[] ips = IpUtil.getIntervalIpArr(IP1 + "-" + "192.168.113.120");
		Assert.assertEquals(LONG1, ips[0]);
		ips = IpUtil.getIntervalIpArr(IP1 + "-" + "127.0.0.1");
		Assert.assertEquals(LONG1, ips[1]);
	}
	
	@Test
	public void IntervalIpFormatTest() {
		Assert.assertTrue(IpUtil.isIntervalIpFormat(IP1 + "-" + "192.168.113.120"));
		Assert.assertTrue(IpUtil.isIntervalIpFormat(IP1 + "-" + "127.0.0.1"));
	}
}
