package com.github.jzhongming.mytools.jwt;

import java.math.BigInteger;

import com.github.jzhongming.mytools.utils.Base64;
import com.github.jzhongming.mytools.utils.Base64Util;
import com.github.jzhongming.mytools.utils.SecretUtil;

import org.codehaus.jackson.map.ObjectMapper;

public class Jwt {

	public static String getToken(final String key, TokenInfo tinfo) {
		Header h = tinfo.getHeader();
		Payload p = tinfo.getPayload();
		StringBuffer data = new StringBuffer(Base64Util.encodeBytes(h.toJson().getBytes()) + "." + Base64Util.encodeBytes(p.toJson().getBytes()));
		byte[] b = SecretUtil.MAC_SHA256(key.getBytes(), data.toString().getBytes());
		if (null == b || b.length == 0) {
			return null;
		}

		BigInteger bb = new BigInteger(b);
		data.append(".").append(bb.toString(32));
		return Base64Util.encodeBytes(data.toString().getBytes());
	}

	public static boolean checkToken(final String key, final String token) {
		try {
			TokenInfo tinfo = parseTokenInfo(token);
			if (tinfo.getPayload().getExp() < System.currentTimeMillis()) {
				System.out.println(tinfo.getPayload().getExp());
				System.out.println(System.currentTimeMillis());
				return false;
			}
			return token.equals(getToken(key, tinfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static TokenInfo parseTokenInfo(final String token) throws Exception {
		String t = new String(Base64.decodeFast(token));
		String[] args = t.split("[.]");
		if (args.length != 3) {
			throw new Exception("error token");
		}

		Header h = new ObjectMapper().readValue(Base64.decodeFast(args[0]), Header.class);
		Payload p = new ObjectMapper().readValue(Base64.decodeFast(args[1]), Payload.class);
		return new TokenInfo(h, p);
	}

	public static void main(String[] args) throws Exception {
		Header header = new Header("JTW", "HmacSHA256");
		Payload payload = new Payload();
		payload.setAud("Jerry");
		payload.setSub("CBS");
		payload.setIss("CBS");
		payload.setExp(System.currentTimeMillis());
		payload.setIat(System.currentTimeMillis());
		payload.setNbf(System.currentTimeMillis());
		payload.setExt("TEST");

		TokenInfo tinfo = new TokenInfo(header, payload);
		String token = Jwt.getToken("jerry", tinfo);
		System.out.println(token);
		System.out.println(parseTokenInfo(token));
		System.out.println(checkToken("jerry", token));
	}

	public static class TokenInfo {
		private final Header header;
		private final Payload payload;

		public TokenInfo(final Header header, final Payload payload) {
			this.header = header;
			this.payload = payload;
		}

		public Header getHeader() {
			return header;
		}

		public Payload getPayload() {
			return payload;
		}

		@Override
		public String toString() {
			return "TokenInfo [header=" + header + ", payload=" + payload + "]";
		}

	}

}
