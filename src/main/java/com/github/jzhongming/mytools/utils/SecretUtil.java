package com.github.jzhongming.mytools.utils;

import java.math.BigInteger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class SecretUtil {
	
	private final static String HmacSHA256 = "HmacSHA256";
	private final static String HmacSHA512 = "HmacSHA512";

	public static byte[] MAC_SHA256(final byte[] key, final byte[] data) {
		try {
			Mac sha256_HMAC = Mac.getInstance(HmacSHA256);
			SecretKeySpec secret_key = new SecretKeySpec(key, HmacSHA256);
			sha256_HMAC.init(secret_key);
			return sha256_HMAC.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] MAC_SHA512(final byte[] key, final byte[] data) {
		try {
			Mac sha256_HMAC = Mac.getInstance(HmacSHA512);
			SecretKeySpec secret_key = new SecretKeySpec(key, HmacSHA512);
			sha256_HMAC.init(secret_key);
			return sha256_HMAC.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String data = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcm9tX3VzZXIiOiJCIiwidGFyZ2V0X3VzZXIiOiJBIn0";
		String key = "jerry";
		BigInteger sha = new BigInteger(MAC_SHA256(key.getBytes(), data.getBytes()));
		System.out.println(sha.toString(32));
	}
}
