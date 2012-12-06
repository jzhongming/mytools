package com.github.jzhongming.mytools.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Gzip 工具类，将byte数组进行Gzip压缩
 * 
 * @author Alex (j.zhongming@gmail.com)
 */
public class CompressUtil {

	private CompressUtil() {

	}

	private static final Log logger = LogFactory.getLog(CompressUtil.class);

	public static byte[] zipCompress(byte[] in) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(in.length);
		DeflaterOutputStream os = new DeflaterOutputStream(baos);
		try {
			os.write(in);
			os.finish();
			try {
				os.close();
			} catch (IOException e) {
				logger.error("Close DeflaterOutputStream error", e);
			}
		} catch (IOException e) {
			throw new RuntimeException("IO exception compressing data", e);
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("Close ByteArrayOutputStream error", e);
			}
		}
		return baos.toByteArray();
	}

	public static byte[] gzipCompress(byte[] in) {
		if (in == null) {
			throw new NullPointerException("Can't compress null");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gz = null;
		try {
			gz = new GZIPOutputStream(bos);
			gz.write(in);
		} catch (IOException e) {
			throw new RuntimeException("IO exception compressing data", e);
		} finally {
			if (gz != null) {
				try {
					gz.close();
				} catch (IOException e) {
					logger.error("Close GZIPOutputStream error", e);
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					logger.error("Close ByteArrayOutputStream error", e);
				}
			}
		}
		byte[] rv = bos.toByteArray();
		// log.debug("Compressed %d bytes to %d", in.length, rv.length);
		return rv;
	}

	private static final int COMPRESS_RATIO = 8;

	public static byte[] zipDecompress(byte[] in) {
		int size = in.length * COMPRESS_RATIO;
		ByteArrayInputStream bais = new ByteArrayInputStream(in);
		InflaterInputStream is = new InflaterInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		try {
			byte[] uncompressMessage = new byte[size];
			while (true) {
				int len = is.read(uncompressMessage);
				if (len <= 0) {
					break;
				}
				baos.write(uncompressMessage, 0, len);
			}
			baos.flush();
			return baos.toByteArray();

		} catch (IOException e) {
			logger.error("Failed to decompress data", e);
			baos = null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("failed to close InflaterInputStream");
			}
			try {
				bais.close();
			} catch (IOException e) {
				logger.error("failed to close ByteArrayInputStream");
			}
			try {
				baos.close();
			} catch (IOException e) {
				logger.error("failed to close ByteArrayOutputStream");
			}
		}
		return baos == null ? null : baos.toByteArray();
	}

	public static byte[] gzipDecompress(byte[] in) {
		ByteArrayOutputStream bos = null;
		if (in != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(in);
			bos = new ByteArrayOutputStream();
			GZIPInputStream gis = null;
			try {
				gis = new GZIPInputStream(bis);

				byte[] buf = new byte[16 * 1024];
				int r = -1;
				while ((r = gis.read(buf)) > 0) {
					bos.write(buf, 0, r);
				}
			} catch (IOException e) {
				logger.error("Failed to decompress data", e);
				bos = null;
			} finally {
				if (gis != null) {
					try {
						gis.close();
					} catch (IOException e) {
						logger.error("Close GZIPInputStream error", e);
					}
				}
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						logger.error("Close ByteArrayInputStream error", e);
					}
				}
			}
		}
		return bos == null ? null : bos.toByteArray();
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
