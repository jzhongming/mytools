package com.github.jzhongming.mytools.utils;

public final class IpUtil {

	public static final IpUtil instance = new IpUtil();

	private IpUtil() {
	}

	// ip格式 0~255.0~255.0~255.1~255
	private static final String REGEX_IP = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])";
	// ip区间 (格式为 ip-ip，例如：211.151.74.1-211.151.74.100)
	private static final String REGEX_INTERVAL_IP = REGEX_IP + "-" + REGEX_IP;

	/**
	 * 字符型IP转换成long型
	 * 
	 * @param ip
	 * @return
	 */
	public static Long ipToLong(String ip) {
		long result = 0;
		if (isIpFormat(ip)) {
			String section[] = ip.split("\\.");
			if (section.length == 4) {
				result += Long.parseLong(section[0]) << 24;
				result += Long.parseLong(section[1]) << 16;
				result += Long.parseLong(section[2]) << 8;
				result += Long.parseLong(section[3]);
			}
		}
		return result;
	}

	/**
	 * 将long型IP转换成字符型
	 * 
	 * @param value
	 * @return
	 */
	public static String longToIp(long value) {
		if (value < 1)
			throw new IllegalArgumentException(value + "is not a IP long value.");

		StringBuilder ip = new StringBuilder();
		ip.append(String.valueOf((value >>> 24))).append("."); // 直接右移24位
		ip.append(String.valueOf((value & 0x00FFFFFF) >>> 16)).append("."); // 将高8位置0，然后右移16位
		ip.append(String.valueOf((value & 0x0000FFFF) >>> 8)).append("."); // 将高16位置0，然后右移8位
		ip.append(String.valueOf((value & 0x000000FF))); // 将高24位置0
		return ip.toString();
	}

	public static int ipToInt(String ip) {
		String[] ipAry = ip.split("\\.");
		if (ipAry.length != 4) {
			throw new IllegalArgumentException(ip + "is not a IP formate.");
		}
		byte[] ipBuf = new byte[4];
		for (int i = 0; i < 4; i++) {
			ipBuf[i] = (byte) Integer.parseInt(ipAry[i]);
		}

		return ByteUtil.bytesToIntBigEndian(ipBuf);
	}

	public static String intToIp(int value) {
		StringBuilder sbIP = new StringBuilder();
		sbIP.append((value >> 24) & 0xFF).append(".").append((value >> 16) & 0xFF).append(".").append((value >> 8) & 0xFF).append(".").append(value & 0xFF);
		return sbIP.toString();
	}

	/**
	 * 根据传入的ip区间，
	 * 
	 * @param intervalIp
	 * @return 返回的数组有两个元素，第一个是较小的，第二的是较大的
	 */
	public static long[] getIntervalIpArr(String intervalIp) {
		if (!isIntervalIpFormat(intervalIp))
			throw new IllegalArgumentException("interval ip format error !");

		String[] ips = intervalIp.split("-");
		long ip1 = ipToLong(ips[0]);
		long ip2 = ipToLong(ips[1]);

		return new long[] { Math.min(ip1, ip2), Math.max(ip1, ip2) };
	}

	public static boolean isIpFormat(String ip) {
		return ip != null && ip.length() > 0 && ip.matches(REGEX_IP);
	}

	public static boolean isIpArrFormat(String[] ips) {
		if (ips == null || ips.length < 1)
			return false;

		for (String ip : ips) {
			if (!isIpFormat(ip))
				return false;
		}

		return true;
	}

	public static boolean isIntervalIpFormat(String intervalIp) {
		return intervalIp != null && intervalIp.length() > 0 && intervalIp.matches(REGEX_INTERVAL_IP);
	}
	
}
