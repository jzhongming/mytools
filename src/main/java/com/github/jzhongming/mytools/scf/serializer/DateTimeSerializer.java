package com.github.jzhongming.mytools.scf.serializer;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeSerializer implements SerializerBase {
	private static long DefaultTimeZone = 8 * 60 * 60 * 1000;

	@Override
	public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
		byte[] buffer = ConvertToBinary((Date) obj);
		outStream.write(buffer);
	}

	@Override
	public Object ReadObject(SCFInStream inStream, Class<?> clazz) throws Exception {
		byte[] buffer = new byte[8];
		inStream.SafeRead(buffer);
		Date date = GetDateTime(buffer);
		if (clazz == java.sql.Timestamp.class) {
			return new Timestamp(date.getTime());
		} else if (clazz == java.sql.Date.class) {
			return new java.sql.Date(date.getTime());
		} else if (clazz == java.sql.Time.class) {
			return new java.sql.Time(date.getTime());
		}
		return date;
	}

	private byte[] ConvertToBinary(Date date) {
		Date date2 = new Date();
		date2.setTime(0);// 1970-1-1 00:00:00
		long rel = date.getTime() - date2.getTime();
		return ByteHelper.GetBytesFromInt64(rel + DefaultTimeZone);
	}

	private Date GetDateTime(byte[] buffer) {
		long rel = ByteHelper.ToInt64(buffer);
		Date dt = new Date(rel - DefaultTimeZone);
		return dt;
	}
}
