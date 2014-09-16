/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.jzhongming.mytools.scf.serializer;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeHelper {
	private static final Logger logger = LoggerFactory.getLogger(TypeHelper.class);
	private static Map<Class<?>, ClassItem> TypeIdMap = new HashMap<Class<?>, ClassItem>();
	private static Map<Integer, ClassItem> IdTypeMap = new HashMap<Integer, ClassItem>();
	public static final int MAX_DATA_LEN = 1024 * 1024 * 10;
	static {
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
//		ClassList.add(new ClassItem(18, AtomicBoolean.class));
//		ClassList.add(new ClassItem(19, AtomicInteger.class));
//		ClassList.add(new ClassItem(20, AtomicLong.class));
//		ClassList.add(new ClassItem(21, Inet4Address.class));
		// ClassList.add(new ClassItem(0, DBNull.class));
		// ClassList.add(new ClassItem(0, GKeyValuePair.class));
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
		logger.info("Type Init Successful! [{} : {}]", IdTypeMap.size(), TypeIdMap.size());
	}

	public static Class<?> getType(int typeId) {
		ClassItem ci = IdTypeMap.get(typeId);
		return (ci == null) ? null : ci.getType();
	}

	public static int getTypeId(Class<?> type) {
		if (type.isEnum()) {
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

	public static void setTypeMap(Class<?> type, int typeId) {
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

	public static void main(String[] args) {
		logger.info("{}", TypeHelper.getTypeId(LinkedList.class));
		logger.info("{}", TypeHelper.getTypeId(ConcurrentHashMap.class));
		logger.info("{}", TypeHelper.getTypeId(TreeSet.class));
	}
}