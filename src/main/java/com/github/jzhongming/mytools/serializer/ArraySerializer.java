package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Array;

public class ArraySerializer implements ISerializer {

	@Override
	public void WriteObject(Object obj, CCOutStream outStream) throws Exception {
		if (obj == null) {
			SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
			return;
		}

		Class<?> type = GetClassForName(obj.getClass().getCanonicalName().replace("[]", ""));
		int typeId = TypeHelper.getTypeId(type);

		outStream.WriteInt32(typeId);
		if (outStream.isRefWrited(obj)) {
			return;
		}

		if (obj.getClass().getComponentType().isPrimitive()) {
			if (type == Character.class) {
				char[] charArray = (char[]) obj;
				outStream.WriteInt32(charArray.length);
				for (char item : charArray) {
					SerializerFactory.GetSerializer(char.class).WriteObject(item, outStream);
				}
				charArray = null;
				return;
			} else if (type == Short.class) {
				short[] shortArray = (short[]) obj;
				outStream.WriteInt32(shortArray.length);
				for (short item : shortArray) {
					SerializerFactory.GetSerializer(short.class).WriteObject(item, outStream);
				}
				shortArray = null;
				return;
			} else if (type == Integer.class) {
				int[] intArray = (int[]) obj;
				outStream.WriteInt32(intArray.length);
				for (int intItem : intArray) {
					SerializerFactory.GetSerializer(int.class).WriteObject(intItem, outStream);
				}
				intArray = null;
				return;
			} else if (type == Float.class) {
				float[] floatArray = (float[]) obj;
				outStream.WriteInt32(floatArray.length);
				for (float item : floatArray) {
					SerializerFactory.GetSerializer(float.class).WriteObject(item, outStream);
				}
				floatArray = null;
				return;
			} else if (type == Long.class) {
				long[] longArray = (long[]) obj;
				outStream.WriteInt32(longArray.length);
				for (long item : longArray) {
					SerializerFactory.GetSerializer(long.class).WriteObject(item, outStream);
				}
				longArray = null;
				return;
			} else if (type == Double.class) {
				double[] doubleArray = (double[]) obj;
				outStream.WriteInt32(doubleArray.length);
				for (double item : doubleArray) {
					SerializerFactory.GetSerializer(double.class).WriteObject(item, outStream);
				}
				doubleArray = null;
				return;
			} else if (type == Boolean.class) {
				boolean[] booleanArray = (boolean[]) obj;
				outStream.WriteInt32(booleanArray.length);
				for (boolean item : booleanArray) {
					SerializerFactory.GetSerializer(boolean.class).WriteObject(item, outStream);
				}
				booleanArray = null;
				return;
			} else if (type == Byte.class) {
				byte[] byteArray = (byte[]) obj;
				outStream.WriteInt32(byteArray.length);
				outStream.write(byteArray);
				return;
			}
		}

		if (type == Byte.class) {
			Byte[] src = (Byte[]) obj;
			byte[] buf = new byte[src.length];
			for (int i = 0; i < src.length; i++) {
				buf[i] = src[i];
			}
			outStream.WriteInt32(buf.length);
			outStream.write(buf);
			return;
		}

		Object[] array = (Object[]) obj;
		outStream.WriteInt32(array.length);

		if (!TypeHelper.IsPrimitive(type)) {
			for (Object item : array) {
				if (item == null) {
					SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
				} else {
					Class<?> itemType = item.getClass();
					int itemTypeId = TypeHelper.getTypeId(itemType);
					outStream.WriteInt32(itemTypeId);
					SerializerFactory.GetSerializer(itemType).WriteObject(item, outStream);
				}
			}
		} else {// 基本类型
			for (Object item : array) {
				SerializerFactory.GetSerializer(item.getClass()).WriteObject(item, outStream);
			}
		}
	}

	@Override
	public Object ReadObject(CCInStream inStream, Class<?> clazz) throws Exception {
		int typeId = inStream.ReadInt32();
		if (typeId == 0) {
			return null;
		}
		byte isRef = (byte) inStream.read();
		int refId = inStream.ReadInt32();
		if (isRef > 0) {
			return inStream.GetRef(refId);
		}
		int len = inStream.ReadInt32();
		if (len > TypeHelper.MAX_DATA_LEN) {
			throw new IllegalArgumentException("Data length overflow.");
		}
		Class<?> type = TypeHelper.getIdType(typeId);
		if (type == null) {
			throw new ClassNotFoundException("Cannot find class with typId,target class:" + clazz.getName() + ",typeId:" + typeId);
		}
		if (type == Byte.class) {
			byte[] buffer = new byte[len];
			inStream.SafeRead(buffer);
			return buffer;
		}

		if (clazz != null && clazz.getComponentType() != null && clazz.getComponentType().isPrimitive()) {
			if (clazz == char[].class) {
				char[] charArray = new char[len];
				for (int i = 0; i < len; i++) {
					short data = inStream.ReadInt16();
					byte[] buffer = ByteHelper.GetBytesFromInt16(data);
					charArray[i] = ByteHelper.getCharFromBytes(buffer);
				}
				return charArray;
			} else if (clazz == short[].class) {
				short[] shortArray = new short[len];
				for (int i = 0; i < len; i++) {
					short shortValue = inStream.ReadInt16();
					shortArray[i] = shortValue;
				}
				return shortArray;
			} else if (clazz == float[].class) {
				float[] floatArray = new float[len];
				for (int i = 0; i < len; i++) {
					int floatValue = inStream.ReadInt32();
					floatArray[i] = Float.intBitsToFloat(floatValue);
				}
				return floatArray;
			} else if (clazz == long[].class) {
				long[] longArray = new long[len];
				for (int i = 0; i < len; i++) {
					longArray[i] = inStream.ReadInt64();
				}
				return longArray;
			} else if (clazz == double[].class) {
				double[] doubleArray = new double[len];
				for (int i = 0; i < len; i++) {
					long doubleValue = inStream.ReadInt64();
					doubleArray[i] = Double.longBitsToDouble(doubleValue);
				}
				return doubleArray;
			} else if (clazz == int[].class) {
				int[] intArray = new int[len];
				for (int i = 0; i < len; i++) {
					int intValue = inStream.ReadInt32();
					intArray[i] = intValue;
				}
				return intArray;
			} else if (clazz == boolean[].class) {
				boolean[] booleanArray = new boolean[len];
				for (int i = 0; i < len; i++) {
					booleanArray[i] = inStream.read() > 0;
				}
				return booleanArray;
			} else if (clazz == byte[].class) {
				byte[] buffer = new byte[len];
				inStream.SafeRead(buffer);
				return buffer;
			}
		}

		Object[] array = (Object[]) Array.newInstance(type, len);
		if (!TypeHelper.IsPrimitive(type)) {
			for (int i = 0; i < len; i++) {
				int itemTypeId = inStream.ReadInt32();
				if (itemTypeId == 0) {
					array[i] = null;
				} else {
					Class<?> itemType = TypeHelper.getIdType(itemTypeId);
					if (itemType == null) {
						throw new ClassNotFoundException("Cannot find class with typId,target class:" + type.getName() + ",typeId:" + itemTypeId);
					}
					Object value = SerializerFactory.GetSerializer(itemType).ReadObject(inStream, type);
					array[i] = value;
				}
			}
		} else {
			for (int i = 0; i < len; i++) {
				Object value = SerializerFactory.GetSerializer(type).ReadObject(inStream, type);
				array[i] = value;
			}
		}
		inStream.SetRef(refId, array);
		return array;
	}

	private static Class<?> GetClassForName(String name) throws ClassNotFoundException {
		if (name.equals("boolean")) {
			return Boolean.class;
		} else if (name.equals("char")) {
			return Character.class;
		} else if (name.equals("byte")) {
			return Byte.class;
		} else if (name.equals("short")) {
			return Short.class;
		} else if (name.equals("int")) {
			return Integer.class;
		} else if (name.equals("long")) {
			return Long.class;
		} else if (name.equals("float")) {
			return Float.class;
		} else if (name.equals("double")) {
			return Double.class;
		} else {
			return Class.forName(name);
		}
	}
}
