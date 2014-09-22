/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jzhongming.mytools.serializer;

import java.io.NotSerializableException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jzhongming.mytools.serializer.annotation.CCMember;
import com.github.jzhongming.mytools.serializer.annotation.CCNotMember;
import com.github.jzhongming.mytools.serializer.annotation.CCSerializable;

public class TypeHelper {
	private static final Logger logger = LoggerFactory.getLogger(TypeHelper.class);
	private static Map<Class<?>, ClassItem> TypeIdMap = new HashMap<Class<?>, ClassItem>();
	private static Map<Integer, ClassItem> IdTypeMap = new HashMap<Integer, ClassItem>();
	private static Map<Class<?>, TypeInfo> TypeInfoMap = new HashMap<Class<?>, TypeInfo>();

	public static final int MAX_DATA_LEN = 1024 * 1024 * 100;
	public static final Charset ENCODER = Charset.forName("UTF-8");
	static {
		logger.info("init TypeHelper ... \nDefault Encoder: {}", ENCODER);
		TypeIdMap.clear();
		IdTypeMap.clear();
		ArrayList<ClassItem> ClassList = new ArrayList<ClassItem>();
		ClassList.add(new ClassItem(1, Object.class));
		ClassList.add(new ClassItem(2, Byte.class, byte.class));
		ClassList.add(new ClassItem(3, Boolean.class, boolean.class));
		ClassList.add(new ClassItem(4, Character.class, char.class));
		ClassList.add(new ClassItem(5, Short.class, short.class));
		ClassList.add(new ClassItem(6, Integer.class, int.class));
		ClassList.add(new ClassItem(7, Long.class, long.class));
		ClassList.add(new ClassItem(8, Float.class, float.class));
		ClassList.add(new ClassItem(9, Double.class, double.class));
		ClassList.add(new ClassItem(10, BigDecimal.class));
		ClassList.add(new ClassItem(11, Array.class));
		ClassList.add(new ClassItem(12, String.class));
		ClassList.add(new ClassItem(13, List.class));
		ClassList.add(new ClassItem(14, Map.class));
		ClassList.add(new ClassItem(15, Set.class));
		ClassList.add(new ClassItem(16, Enum.class));
		ClassList.add(new ClassItem(17, java.util.Date.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class));
		for (ClassItem item : ClassList) {
			int id = item.getTypeId();
			Class<?>[] types = item.getTypes();
			for (Class<?> c : types) {
				TypeIdMap.put(c, item);
			}
			IdTypeMap.put(id, item);
		}
		ClassList.clear();
		ClassList = null;
	}

	public static Class<?> getIdType(int typeId) {
		ClassItem ci = IdTypeMap.get(typeId);
		return (ci == null) ? null : ci.getType();
	}

	public static int getTypeId(Class<?> type) {
		if (null == type) {
			return 0;
		} else if (type.isEnum()) {
			type = Enum.class;
		} else if (type.isArray()) {
			type = Array.class;
		} else if (Map.class.isAssignableFrom(type)) {
			type = Map.class;
		} else if (List.class.isAssignableFrom(type)) {
			type = List.class;
		} else if (Set.class.isAssignableFrom(type)) {
			type = Set.class;
		}
		ClassItem ci = TypeIdMap.get(type);
		return (ci == null) ? -1 : ci.getTypeId();
	}

	protected static void setTypeMap(Class<?> type, int typeId) {
		ClassItem ci = new ClassItem(typeId, type);
		TypeIdMap.put(type, ci);
		IdTypeMap.put(typeId, ci);
	}

	public static boolean IsPrimitive(Class<?> type) {
		if (type.isPrimitive()) {
			return true;
		} else if (type == Long.class || type == long.class) {
			return true;
		} else if (type == Integer.class || type == int.class) {
			return true;
		} else if (type == Byte.class || type == byte.class) {
			return true;
		} else if (type == Short.class || type == short.class) {
			return true;
		} else if (type == Character.class || type == char.class) {
			return true;
		} else if (type == Double.class || type == double.class) {
			return true;
		} else if (type == Float.class || type == float.class) {
			return true;
		} else if (type == Boolean.class || type == boolean.class) {
			return true;
		}
		return false;
	}

	public static TypeInfo getTypeInfo(Class<?> clazz) throws NotSerializableException {
		if (TypeInfoMap.containsKey(clazz)) {
			return TypeInfoMap.get(clazz);
		}

		if (!clazz.isAnnotationPresent(CCSerializable.class)) {
			throw new NotSerializableException(clazz.getName() + " do not have a CCSerializable Annotation");
		}

		int typeId = TypeHelper.getTypeId(clazz);
		TypeInfo typeInfo = new TypeInfo(typeId);
		List<Field> tmpFields = new ArrayList<Field>();
		Class<?> temType = clazz;
		while (true) {
			Field[] fs = temType.getDeclaredFields();
			for (Field f : fs) {
				tmpFields.add(f);
			}
			Class<?> superClass = temType.getSuperclass();
			if (superClass == null) {
				break;
			}
			temType = superClass;
		}

		CCSerializable cAnn = clazz.getAnnotation(CCSerializable.class);
		if (cAnn.isDefaultAll()) {
			for (Field fd : tmpFields) {
				if (fd.isAnnotationPresent(CCNotMember.class)) {
					continue;
				}
				fd.setAccessible(true);
				typeInfo.getFields().add(fd);
			}
		} else {
			for (Field fd : tmpFields) {
				if (fd.isAnnotationPresent(CCMember.class)) {
					fd.setAccessible(true);
					typeInfo.getFields().add(fd);
				}
			}
		}

		TypeInfoMap.put(clazz, typeInfo);
		return typeInfo;
	}

	public static int makeTypeId(String name) {
		int hash1 = 5381;
		int hash2 = hash1;
		int len = name.length();
		for (int i = 0; i < len; i++) {
			int c = name.charAt(i);
			hash1 = ((hash1 << 5) + hash1) ^ c;
			if (++i >= len) {
				break;
			}
			c = name.charAt(i);
			hash2 = ((hash2 << 5) + hash2) ^ c;
		}
		return hash1 + (hash2 * 1566083941);
	}
}