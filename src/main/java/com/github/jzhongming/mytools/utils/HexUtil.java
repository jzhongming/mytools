package com.github.jzhongming.mytools.utils;

import java.nio.charset.Charset;

public class HexUtil {
	public static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

	private static final char[] DIGITS_LOWER = 
		{ '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static final char[] DIGITS_UPPER = 
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private final Charset charset;
	
	public HexUtil() {
		// use default encoding
		this.charset = DEFAULT_CHARSET;
	}

	public HexUtil(Charset charset) {
		this.charset = charset;
	}

	public HexUtil(String charsetName) {
		this(Charset.forName(charsetName));
	}
	
    public Charset getCharset() {
        return this.charset;
    }

    public String getCharsetName() {
        return this.charset.name();
    }
    
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int len = data.length;
        char[] out = new char[len << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
    
    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }
    
    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }
    
    public byte[] encode(byte[] array) {
        return encodeHexString(array).getBytes(this.getCharset());
    }
    
    public Object encode(Object object) {
        try {
            byte[] byteArray = object instanceof String ?
                                   ((String) object).getBytes(this.getCharset()) : (byte[]) object;
            return encodeHex(byteArray);
        } catch (ClassCastException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
	
    public static byte[] decodeHex(char[] data) throws IllegalArgumentException {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }
    
    public byte[] decode(byte[] array) throws IllegalArgumentException {
        return decodeHex(new String(array, this.getCharset()).toCharArray());
    }
    
    public Object decode(Object object) throws IllegalArgumentException {
        try {
            char[] charArray = (object instanceof String)? ((String) object).toCharArray() : (char[]) object;
            return decodeHex(charArray);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
	@Override
    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}
