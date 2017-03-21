package com.github.jzhongming.mytools.serializer;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeSerializer implements ISerializer {
	private static final long DefaultTimeZone = 8 * 60 * 60 * 1000;

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		byte[] buffer = convertToBinary((Date) obj);
		outStream.write(buffer);
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		byte[] buffer = new byte[8];
		inStream.SafeRead(buffer);
		Date date = getDateTime(buffer);
		if (clazz == java.sql.Timestamp.class) {
			return new Timestamp(date.getTime());
		} else if (clazz == java.sql.Date.class) {
			return new java.sql.Date(date.getTime());
		} else if (clazz == java.sql.Time.class) {
			return new java.sql.Time(date.getTime());
		}
		return date;
	}

	private byte[] convertToBinary(Date date) {
		Date td = new Date();
		td.setTime(0);// 1970-1-1 00:00:00
		long rel = date.getTime() - td.getTime();
		return ByteHelper.GetBytesFromInt64(rel + DefaultTimeZone);
	}

	private Date getDateTime(byte[] buffer) {
		long rel = ByteHelper.ToInt64(buffer);
		Date dt = new Date(rel - DefaultTimeZone);
		return dt;
	}

}
