package com.github.jzhongming.socket.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class DataConverter {
	private static final Map<String, CharsetEncoder> encoders = new HashMap<String, CharsetEncoder>();
	private static final Map<String, CharsetDecoder> decoders = new HashMap<String, CharsetDecoder>();

	/**
	 * converts the given byte size in a textual representation
	 * 
	 * @param bytes
	 *            the bytes to convert
	 * @return the formated String representation of the bytes
	 */
	public static String toFormatedBytesSize(long bytes) {
		if (bytes > (5 * 1000 * 1000)) {
			return (bytes / 1000000) + " mb";

		} else if (bytes > (10 * 1000)) {
			return (bytes / 1000) + " kb";

		} else {
			return bytes + " bytes";
		}
	}

	/**
	 * converts the given time in a textual representation
	 * 
	 * @param time
	 *            the time to convert
	 * @return the formated String representation of the date
	 */
	public static String toFormatedDate(long time) {
		return new SimpleDateFormat("MMM.dd HH:mm").format(new Date(time));
	}

	/**
	 * converts the given time in a RFC822 conform date representation
	 * 
	 * @param time
	 *            the time to convert
	 * @return the formated String representation in a RFC822 conform date
	 *         representation
	 */
	public static String toFormatedRFC822Date(long time) {
		return newRFC822DateFomat().format(time);
	}

	/**
	 * converts a RFC822 date string into a date object
	 * 
	 * @param rfc822DateString
	 *            the rfc822 string
	 * @return the date object
	 */
	public static Date toDate(String rfc822DateString) {
		try {
			return newRFC822DateFomat().parse(rfc822DateString);
		} catch (ParseException pe) {
			throw new RuntimeException(pe.toString());
		}
	}

	private static SimpleDateFormat newRFC822DateFomat() {
		SimpleDateFormat df = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss z", Locale.US);
		df.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("GMT+0")));
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df;
	}

	/**
	 * converts the given duration in a textual representation
	 * 
	 * @param duration
	 *            the duration to convert
	 * @return the formated String representation of the duration
	 */
	public static String toFormatedDuration(long duration) {

		if (duration < 5 * 1000) {
			return duration + " millis";

		} else if (duration < (60 * 1000)) {
			return ((int) (duration / 1000)) + " sec";

		} else if (duration < (60 * 60 * 1000)) {
			return ((int) (duration / (60 * 1000))) + " min";

		} else if (duration < (24 * 60 * 60 * 1000)) {
			return ((int) (duration / (60 * 60 * 1000))) + " h";

		} else {
			return ((long) (duration / (24 * 60 * 60 * 1000))) + " d";
		}
	}

	/**
	 * converts the given String into a ByteBuffer
	 * 
	 * @param s
	 *            the String to convert
	 * @param encoding
	 *            the encoding to use
	 * @return the String as ByteBuffer
	 */
	public static ByteBuffer toByteBuffer(String s, String encoding) {
		try {
			return ByteBuffer.wrap(s.getBytes(encoding));
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}

	/**
	 * converts the given ByteBuffer into String by using UTF-8 encoding
	 * 
	 * @param buffer
	 *            the ByteBuffer to convert
	 * @return the ByteByuffer as String
	 */
	public static String toString(ByteBuffer buffer) throws UnsupportedEncodingException {
		return toString(buffer, "UTF-8");
	}

	/**
	 * converts the given ByteBuffer array into String by using UTF-8 encoding
	 * 
	 * @param buffer
	 *            the ByteBuffer arrayto convert
	 * @return the ByteByuffer as String
	 */
	public static String toString(ByteBuffer[] buffer) throws UnsupportedEncodingException {
		return toString(buffer, "UTF-8");
	}

	/**
	 * converts the given ByteBuffer into String repesentation
	 * 
	 * @param buffer
	 *            the ByteBuffer to convert
	 * @param encoding
	 *            the encoding to use
	 * @return the ByteByuffer as String
	 */
	public static String toString(ByteBuffer buffer, String encoding) throws UnsupportedEncodingException {
		try {
			CharsetDecoder decoder = decoders.get(encoding);
			if (decoder == null) {
				Charset charset = Charset.forName(encoding);
				decoder = charset.newDecoder();
				decoders.put(encoding, decoder);
				encoders.put(encoding, charset.newEncoder());
			}

			return decoder.decode(buffer).toString();

		} catch (CharacterCodingException cce) {
			RuntimeException re = new RuntimeException("coding exception for `" + encoding + "` occured: " + cce.toString(), cce);
			throw re;
		}
	}

	/**
	 * converts the given ByteBuffer into a hex string
	 * 
	 * @param buffer
	 *            the ByteBuffer to convert
	 * @return the hex string
	 */
	public static String toHexString(ByteBuffer buffer) {

		if (buffer == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		while (buffer.hasRemaining()) {
			String hex = Integer.toHexString(0x0100 + (buffer.get() & 0x00FF)).substring(1);
			sb.append((hex.length() < 2 ? "0" : "") + hex + " ");
		}

		return sb.toString();
	}

	/**
	 * converts the given list of ByteBuffers into a String by using UTF-8
	 * encoding
	 * 
	 * @param buffers
	 *            the list of ByteBuffer to convert
	 * @return the ByteByuffer as String
	 */
	public static String toString(List<ByteBuffer> buffers) throws UnsupportedEncodingException {
		return toString(buffers.toArray(new ByteBuffer[buffers.size()]), "UTF-8");
	}

	/**
	 * converts the given list of ByteBuffers into a String
	 * 
	 * @param buffers
	 *            the list of ByteBuffer to convert
	 * @param encoding
	 *            the encoding to use
	 * @return the ByteByuffer as String
	 */
	public static String toString(List<ByteBuffer> buffers, String encoding) throws UnsupportedEncodingException {
		return toString(buffers.toArray(new ByteBuffer[buffers.size()]), encoding);
	}

	/**
	 * converts the given array of ByteBuffers into String
	 * 
	 * @param buffers
	 *            the array of ByteBuffer to convert
	 * @param encoding
	 *            the encoding to use
	 * @return the ByteByuffer as String
	 */
	public static String toString(ByteBuffer[] buffers, String encoding) throws UnsupportedEncodingException {
		return new String(toBytes(buffers), encoding);
	}

	/**
	 * print the bytebuffer as limited string
	 * 
	 * @param buffers
	 *            the buffers to print
	 * @param encoding
	 *            the encoding to use
	 * @param maxOutSize
	 *            the max size to print
	 * 
	 * @return the ByteBuffers as string representation
	 */
	public static String toString(ByteBuffer[] buffers, String encoding, int maxOutSize) throws UnsupportedEncodingException {
		String s = toString(buffers, encoding);
		if (s.length() > maxOutSize) {
			s = s.substring(0, maxOutSize) + " [output has been cut]";
		}
		return s;
	}

	/**
	 * merges a ByteBuffer array into a (direct) ByteBuffer
	 * 
	 * @param buffers
	 *            the ByteBuffer array to merge
	 * @return the single ByteBuffer
	 */
	public static ByteBuffer toByteBuffer(ByteBuffer[] buffers) {
		if (buffers.length == 0) {
			return ByteBuffer.allocate(0);
		}

		if (buffers.length == 1) {
			return buffers[0];
		}

		byte[] bytes = toBytes(buffers);
		return ByteBuffer.wrap(bytes);
	}

	/**
	 * converts a single byte to a byte buffer
	 * 
	 * @param b
	 *            the byte
	 * @return the ByteBuffer which contains the single byte
	 */
	public static ByteBuffer toByteBuffer(byte b) {
		ByteBuffer buffer = ByteBuffer.allocate(1).put(b);
		buffer.flip();

		return buffer;
	}

	/**
	 * converts a byte array to a byte buffer
	 * 
	 * @param bytes
	 *            the byte array
	 * @return the ByteBuffer which contains the bytes
	 */
	public static ByteBuffer toByteBuffer(byte[] bytes) {
		return ByteBuffer.wrap(bytes);
	}

	/**
	 * converts a byte array to a byte buffer
	 * 
	 * @param bytes
	 *            the bytes
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return the ByteBuffer which contains the single byte
	 */
	public static ByteBuffer toByteBuffer(byte[] bytes, int offset, int length) {
		return ByteBuffer.wrap(bytes, offset, length);
	}

	/**
	 * converts a double to a byte buffer
	 * 
	 * @param d
	 *            the double
	 * @return the ByteBuffer which contains the double
	 */
	public static ByteBuffer toByteBuffer(double d) {
		ByteBuffer buffer = ByteBuffer.allocate(8).putDouble(d);
		buffer.flip();

		return buffer;
	}

	/**
	 * converts a long to a byte buffer
	 * 
	 * @param l
	 *            the long
	 * @return the ByteBuffer which contains the long
	 */
	public static ByteBuffer toByteBuffer(long l) {
		ByteBuffer buffer = ByteBuffer.allocate(8).putLong(l);
		buffer.flip();

		return buffer;
	}

	/**
	 * converts a short to a byte buffer
	 * 
	 * @param s
	 *            the short
	 * @return the ByteBuffer which contains the short
	 */
	public static ByteBuffer toByteBuffer(short s) {
		ByteBuffer buffer = ByteBuffer.allocate(2).putShort(s);
		buffer.flip();

		return buffer;
	}

	/**
	 * converts a integer to a byte buffer
	 * 
	 * @param i
	 *            the int
	 * @return the ByteBuffer which contains the int
	 */
	public static ByteBuffer toByteBuffer(int i) {
		ByteBuffer buffer = ByteBuffer.allocate(4).putInt(i);
		buffer.flip();

		return buffer;
	}

	/**
	 * copies a array of ByteBuffer based on offset length to a byte buffer
	 * array
	 * 
	 * @param srcs
	 *            the buffers
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return the ByteBuffer
	 */
	public static ByteBuffer[] toByteBuffers(ByteBuffer[] srcs, int offset, int length) {
		ByteBuffer[] bufs = new ByteBuffer[length];
		System.arraycopy(srcs, offset, bufs, 0, length);

		return bufs;
	}

	/**
	 * converts a list of ByteBuffer to a byte array
	 * 
	 * @param buffers
	 *            the ByteBuffer list to convert
	 * @return the byte array
	 */
	public static byte[] toBytes(List<ByteBuffer> buffers) {
		return toBytes(buffers.toArray(new ByteBuffer[buffers.size()]));
	}

	/**
	 * converts a ByteBuffer array to a byte array
	 * 
	 * @param buffers
	 *            the ByteBuffer array to convert
	 * @return the byte array
	 */
	public static byte[] toBytes(ByteBuffer[] buffers) {
		if (buffers == null) {
			return new byte[0];
		}

		if (buffers.length == 0) {
			return new byte[0];
		}

		byte[][] bs = new byte[buffers.length][];
		int size = 0;

		// transform ByteBuffer -> byte[]
		for (int i = 0; i < buffers.length; i++) {
			if (buffers[i] == null) {
				continue;
			}

			size += buffers[i].remaining();
			bs[i] = toBytes(buffers[i]);
		}

		// merging byte arrays
		byte[] result = new byte[size];
		int offset = 0;
		for (byte[] b : bs) {
			if (b != null) {
				System.arraycopy(b, 0, result, offset, b.length);
				offset += b.length;
			}
		}

		return result;
	}

	/**
	 * converts a ByteBuffer into a byte array
	 * 
	 * @param buffer
	 *            the ByteBuffer to convert
	 * @return the byte array
	 */
	public static byte[] toBytes(ByteBuffer buffer) {
		if (buffer == null) {
			return new byte[0];
		}

		int savedPos = buffer.position();
		int savedLimit = buffer.limit();

		try {

			byte[] array = new byte[buffer.limit() - buffer.position()];

			if (buffer.hasArray()) {
				int offset = buffer.arrayOffset() + savedPos;
				byte[] bufferArray = buffer.array();
				System.arraycopy(bufferArray, offset, array, 0, array.length);

				return array;
			} else {
				buffer.get(array);
				return array;
			}

		} finally {
			buffer.position(savedPos);
			buffer.limit(savedLimit);
		}
	}

	/**
	 * print the byte array as a hex string
	 * 
	 * @param buffers
	 *            the buffers to print
	 * @param maxOutSize
	 *            the max size to print
	 * 
	 * @return the ByteBuffers as hex representation
	 */
	public static String toHexString(byte[] buffers, int maxOutSize) {
		return toHexString(new ByteBuffer[] { ByteBuffer.wrap(buffers) }, maxOutSize);
	}

	/**
	 * print the byte buffer as a hex string
	 * 
	 * @param buffers
	 *            the buffers to print
	 * @param maxOutSize
	 *            the max size to print
	 * 
	 * @return the ByteBuffers as hex representation
	 */
	public static String toHexString(List<ByteBuffer> buffers, int maxOutSize) {
		return toHexString(buffers.toArray(new ByteBuffer[buffers.size()]), maxOutSize);
	}

	/**
	 * print the byte buffer as a hex string
	 * 
	 * @param buffers
	 *            the buffers to print
	 * @param maxOutSize
	 *            the max size to print
	 * 
	 * @return the ByteBuffers as hex representation
	 */
	public static String toHexString(ByteBuffer[] buffers, int maxOutSize) {

		// first cut output if longer than max limit
		String postfix = "";
		int size = 0;
		List<ByteBuffer> copies = new ArrayList<ByteBuffer>();
		for (ByteBuffer buffer : buffers) {
			if (buffer != null) {
				ByteBuffer copy = buffer.duplicate();
				if ((size + copy.limit()) > maxOutSize) {
					copy.limit(maxOutSize - size);
					copies.add(copy);
					postfix = " [...output has been cut]";
					break;
				} else {
					copies.add(copy);
				}
			}
		}

		StringBuilder result = new StringBuilder();

		for (ByteBuffer buffer : copies) {
			result.append(toHexString(buffer));
		}

		result.append(postfix);

		return result.toString();
	}

	/**
	 * convert the ByteBuffer into a hex or text string (deping on content)
	 * 
	 * @param buffer
	 *            the buffers to print
	 * @param maxOutSize
	 *            the max size to print
	 * @param encoding
	 *            the encoding to use
	 * @return the converted ByteBuffer
	 */
	public static String toTextOrHexString(ByteBuffer buffer, String encoding, int maxOutSize) {
		return toTextOrHexString(new ByteBuffer[] { buffer }, encoding, maxOutSize);
	}

	/**
	 * convert the ByteBuffer array into a hex or text string (deping on
	 * content)
	 * 
	 * @param buffers
	 *            the buffers to print
	 * @param maxOutSize
	 *            the max size to print
	 * @param encoding
	 *            the encoding to use
	 * @return the converted ByteBuffer
	 */
	public static String toTextOrHexString(ByteBuffer[] buffers, String encoding, int maxOutSize) {
		boolean hasNonPrintableChars = false;

		for (ByteBuffer buffer : buffers) {
			ByteBuffer copy = buffer.duplicate();
			while (copy.hasRemaining()) {
				int i = copy.get();
				if (i < 10) {
					hasNonPrintableChars = true;
				}
			}
		}

		if (hasNonPrintableChars) {
			return toHexString(buffers, maxOutSize);
		} else {
			try {
				return toString(buffers, encoding, maxOutSize);
			} catch (UnsupportedEncodingException use) {
				return toHexString(buffers, maxOutSize);
			}
		}
	}

	public static String toTextAndHexString(ByteBuffer[] buffers, String encoding, int maxOutSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(DataConverter.toHexString(buffers, 500));
		sb.append("\n");
		try {
			sb.append("[txt:] " + toString(buffers, "US-ASCII", 500));
		} catch (UnsupportedEncodingException ue) {
			sb.append("[txt:] ... content not printable ...");
		}
		return sb.toString();
	}

	public static String toString(Throwable t) {
		if (t != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(bos);

			t.printStackTrace(pw);
			pw.flush();

			return bos.toString();
		} else {
			return null;
		}
	}
}
