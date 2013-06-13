package com.github.jzhongming.mytools.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 压缩工具类，将byte数组进行Gzip、Zip压缩
 * 
 * @author Alex (j.zhongming@gmail.com)
 */
public class CompressUtil {

	private CompressUtil() {

	}

	private static final Logger logger = LoggerFactory.getLogger(CompressUtil.class);

	private static final int COMPRESS_RATIO = Deflater.DEFLATED;

	/**
	 * 将data进行Zip压缩
	 * @param data
	 * @return
	 */
	public static byte[] zipCompress(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("zipCompress data is empty or null");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream  zipos = new DeflaterOutputStream (baos);
		byte[] rv = null;
		try {
			zipos.write(data);
			zipos.finish();
			rv = baos.toByteArray();
//			logger.warn("zipCompress " + data.length + " bytes to " + rv.length);
		} catch (IOException e) {
			throw new RuntimeException("IO exception compressing data", e);
		} finally {
			try {
				zipos.close();
			} catch (IOException e) {
				logger.error("failed to close ZipOutputStream", e);
			}
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("failed to close ByteArrayOutputStream", e);
			}
		}
		return rv;
	}

	/**
	 * 将数据进行Zip解压缩
	 * @param data
	 * @return
	 */
	public static byte[] zipDecompress(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("zipDecompress data is empty or null");
		}

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		InflaterInputStream zips = new InflaterInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] rv = null;
		try {
			byte[] buf = new byte[COMPRESS_RATIO * data.length];

			int r = -1;
			while ((r = zips.read(buf)) > 0) {
				baos.write(buf, 0, r);
			}
			rv = baos.toByteArray();
//			logger.warn("zipDecompress " + data.length + " bytes to " + rv.length);
		} catch (IOException e) {
			logger.error("IO exception decompressing data", e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("failed to close ByteArrayOutputStream");
			}
			try {
				zips.close();
			} catch (IOException e) {
				logger.error("failed to close InflaterInputStream");
			}
			try {
				bais.close();
			} catch (IOException e) {
				logger.error("failed to close ByteArrayInputStream");
			}
		}
		return rv;
	}

	/**
	 * 将数据进行Gzip压缩
	 * @param data
	 * @return
	 */
	public static byte[] gzipCompress(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("compress data is empty or null");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzipos = null;
		byte[] rv = null;
		try {
			gzipos = new GZIPOutputStream(baos);
			gzipos.write(data);
			gzipos.finish();
			rv = baos.toByteArray();
//			logger.warn("gzipCompress " + data.length + " bytes to " + rv.length);
		} catch (IOException e) {
			throw new RuntimeException("IO exception compressing data", e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("Close ByteArrayOutputStream error", e);
			}
			if (gzipos != null) {
				try {
					gzipos.close();
				} catch (IOException e) {
					logger.error("Close GZIPOutputStream error", e);
				}
			}
		}
		return rv;
	}

	/**
	 * 将数据进行解压缩
	 * @param data
	 * @return
	 */
	public static byte[] gzipDecompress(byte[] data) {
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("gzipDecompress data is empty or null");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		GZIPInputStream gzips = null;
		byte[] rv = null;
		try {
			gzips = new GZIPInputStream(bais);
			byte[] buf = new byte[COMPRESS_RATIO * data.length];
			int r = -1;
			while ((r = gzips.read(buf)) > 0) {
				baos.write(buf, 0, r);
			}
			rv = baos.toByteArray();
//			logger.warn("gzipDecompress " + data.length + " bytes to " + rv.length);
		} catch (IOException e) {
			logger.error("Failed to decompress data", e);
		} finally {
			if (gzips != null) {
				try {
					gzips.close();
				} catch (IOException e) {
					logger.error("Close GZIPInputStream error", e);
				}
			}
			try {
				bais.close();
			} catch (IOException e) {
				logger.error("Close ByteArrayInputStream error", e);
			}
		}
		return rv;
	}
	/**
	 * 将Byte数组进行Gzip压缩
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(final byte[] bytes) throws Exception {
		if (null == bytes || bytes.length == 0) {
			throw new IllegalArgumentException("bytes must not be blank");
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(bos);
		try {
			gzout.write(bytes, 0, bytes.length);
		} finally {
			gzout.close();
			bos.close();
		}
		return bos.toByteArray();
	}

	/**
	 * 将压缩过的Byte数组进行Gzip解压
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] uncompress(final byte[] bytes) throws Exception {
		if (null == bytes || bytes.length == 0) {
			throw new IllegalArgumentException("bytes must not be blank");
		}

		ByteArrayInputStream in = new ByteArrayInputStream(bytes, 0,
				bytes.length);
		GZIPInputStream gzi = new GZIPInputStream(in);
		byte[] outbuf = new byte[bytes.length];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] decompressed;
		try {
			int len;
			while ((len = gzi.read(outbuf, 0, outbuf.length)) != -1) {
				bos.write(outbuf, 0, len);
			}
			bos.flush();
			decompressed = bos.toByteArray();
		} finally {
			bos.close();
			gzi.close();
		}
		return decompressed; 
	}
	
}
