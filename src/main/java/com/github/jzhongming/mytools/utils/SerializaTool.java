//package com.github.jzhongming.mytools.utils;
//
//import java.lang.reflect.Field;
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.esotericsoftware.kryo.Context;
//import com.esotericsoftware.kryo.Kryo;
//
///**
// * Kryo序列化工具类
// * 
// * @author Alex (j.zhongming@gmail.com)
// */
//public class SerializaTool {
//	private static final int DEFAULTMAX = 1024 * 4;
//	private static final Kryo kryo = new Kryo();
//	private static final Context context = Kryo.getContext();
//	private static List<Class<?>> rejectList = new ArrayList<Class<?>>();
//
//	private SerializaTool() {
//	}
//
//	static {
//		rejectList.add(String.class);
//		rejectList.add(Integer.class);
//		rejectList.add(Float.class);
//		rejectList.add(Double.class);
//		rejectList.add(Character.class);
//		rejectList.add(Byte.class);
//		rejectList.add(Boolean.class);
//		rejectList.add(Long.class);
//		rejectList.add(Short.class);
//	}
//
//	/**
//	 * 序列化
//	 * 
//	 * @param <T>
//	 * @param t
//	 * @return
//	 * @throws ClassNotFoundException
//	 */
//	public static <T> byte[] constructValue(T t) throws ClassNotFoundException {
//		if (null == t)
//			return null;
//
//		String clazzName = t.getClass().getCanonicalName();
//
//		if (null == context.get(clazzName))
//			checkAndRegestT(clazzName);
//
//		ByteBuffer buffer = context.getBuffer(DEFAULTMAX);
//		buffer.clear();
//		kryo.writeObjectData(buffer, t);
//		return writeToBytes(buffer);
//	}
//
//	/**
//	 * 序列化
//	 * 
//	 * @param <T>
//	 * @param t
//	 * @return
//	 * @throws ClassNotFoundException
//	 */
//	public static <T> byte[] constructValue(T t, final int bufferSize) throws ClassNotFoundException {
//		if (null == t)
//			return null;
//
//		String clazzName = t.getClass().getCanonicalName();
//
//		if (null == context.get(clazzName))
//			checkAndRegestT(clazzName);
//
//		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
//		buffer.clear();
//		kryo.writeObjectData(buffer, t);
//		return writeToBytes(buffer);
//	}
//
//	private static byte[] writeToBytes(final ByteBuffer buffer) {
//		byte abyte0[] = new byte[buffer.position()];
//		System.arraycopy(buffer.array(), 0, abyte0, 0, abyte0.length);
//		return abyte0;
//	}
//
//	/**
//	 * 反序列化
//	 * 
//	 * @param <T>
//	 * @param value
//	 * @param clazz
//	 * @return
//	 * @throws ClassNotFoundException
//	 */
//	public static <T> T resolveValue(final byte[] value, Class<T> clazz) throws ClassNotFoundException {
//		if (null == value)
//			return null;
//
//		if (null == context.get(clazz.getCanonicalName()))
//			checkAndRegestT(clazz.getCanonicalName());
//		
////		// chapterInfo 增加chapterTime属性 补一位兼容旧数据
////		byte abyte0[] = new byte[value.length + 1];
////		// TODO 会不会存在性能问题?
////		System.arraycopy(value, 0, abyte0, 0, value.length);
//		return kryo.readObjectData(ByteBuffer.wrap(value), clazz);
//	}
//
//	private synchronized static void checkAndRegestT(final String name) throws ClassNotFoundException {
//		if (null != context.get(name)) {
//			return;
//		}
//
//		Class<?> info = Class.forName(name);
//		kryo.register(info);
//		Field[] field = info.getDeclaredFields();
//		for (Field f : field) {
//			Class<?> c = f.getType();
//			if (!c.isPrimitive() && !rejectList.contains(c)) {
//				kryo.register(c);
//			}
//		}
//		context.put(name, kryo);
//	}
//
// }
