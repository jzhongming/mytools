package com.github.jzhongming.mytools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * @author Alex (j.zhongming@gmail.com)
 */
public class MD5Filter {
	private final double maxToken = Math.pow(2, 127);
	int nodeNum = 4;
	public double min = 0 * maxToken / nodeNum;
	public double max = 2 * maxToken / nodeNum;

	private MD5Filter() {

	}

	public static BigInteger getToken(final String strKey) {
		if (null == strKey || strKey.isEmpty()) {
			return BigInteger.ZERO;
		}
		BigInteger hash = new BigInteger("");
		try {
			byte[] bytes = strKey.getBytes("UTF-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(bytes);
			hash = new BigInteger(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash.abs();
	}

	public static boolean beFiltered(final String strKey, final double Md5Min,
			final double Md5Max) {
		double token = getToken(strKey).doubleValue();
		if (token < Md5Min || token > Md5Max)
			return true;
		else
			return false;
	}

	public static BigInteger md5(final String key) {
		if (null == key || key.isEmpty()) {
			return BigInteger.ZERO;
		}

		byte[] result = hash("MD5", key.getBytes());
		BigInteger hash = new BigInteger(result);
		return hash.abs();
	}

	public static byte[] hash(final String type, final byte[]... data) {
		byte[] result = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(type);
			for (byte[] block : data) {
				messageDigest.update(block);
			}
			result = messageDigest.digest();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String getMd5ByFile(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
			byteBuffer.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	public static String getMd5ByFile2(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int length = -1;
			while ((length = in.read(buffer)) != -1) {
				md5.update(buffer, 0, length);
			}
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}

		return value;
	}
	
	public static void main(String[] args) {
		String s = MD5Filter.getMd5ByFile2(new File("C:\\Users\\Administrator\\Desktop\\aaa.txt"));
		System.out.println(s);
	}

}
