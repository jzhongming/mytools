package com.github.jzhongming.mytools.serializer;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.github.jzhongming.mytools.serializer.TypeHelper;

public class TypeHelperTest {
	private static final byte[] BB = new byte[0];

	@Test
	public void TestTypeID() {
		Assert.assertEquals(TypeHelper.getTypeId(null), 0);
		Assert.assertEquals(TypeHelper.getTypeId(byte.class), 2);
		Assert.assertEquals(TypeHelper.getTypeId(boolean.class), 3);
		Assert.assertEquals(TypeHelper.getTypeId(char.class), 4);
		Assert.assertEquals(TypeHelper.getTypeId(short.class), 5);
		Assert.assertEquals(TypeHelper.getTypeId(int.class), 6);
		Assert.assertEquals(TypeHelper.getTypeId(long.class), 7);
		Assert.assertEquals(TypeHelper.getTypeId(float.class), 8);
		Assert.assertEquals(TypeHelper.getTypeId(double.class), 9);
		Assert.assertEquals(TypeHelper.getTypeId(BigDecimal.class), 10);
		Assert.assertEquals(TypeHelper.getTypeId(BB.getClass()), 11);
		Assert.assertEquals(TypeHelper.getTypeId(String.class), 12);
		Assert.assertEquals(TypeHelper.getTypeId(LinkedList.class), 13);
		Assert.assertEquals(TypeHelper.getTypeId(TreeMap.class), 14);
		Assert.assertEquals(TypeHelper.getTypeId(TreeSet.class), 15);
		Assert.assertEquals(TypeHelper.getTypeId(Enum.class), 16);
		Assert.assertEquals(TypeHelper.getTypeId(java.sql.Date.class), 17);
		Assert.assertEquals(TypeHelper.getTypeId(java.sql.Date.class), 17);
		Assert.assertEquals(TypeHelper.getTypeId(java.util.Date.class), 17);
		Assert.assertEquals(TypeHelper.getTypeId(java.sql.Time.class), 17);
		Assert.assertEquals(TypeHelper.getTypeId(java.sql.Timestamp.class), 17);
	}

	@Test
	public void TestIDType() {
		Assert.assertEquals(TypeHelper.getIdType(0), null);
		Assert.assertEquals(TypeHelper.getIdType(1), Object.class);
		Assert.assertEquals(TypeHelper.getIdType(2), Byte.class);
		Assert.assertEquals(TypeHelper.getIdType(3), Boolean.class);
		Assert.assertEquals(TypeHelper.getIdType(4), Character.class);
		Assert.assertEquals(TypeHelper.getIdType(5), Short.class);
		Assert.assertEquals(TypeHelper.getIdType(6), Integer.class);
		Assert.assertEquals(TypeHelper.getIdType(7), Long.class);
		Assert.assertEquals(TypeHelper.getIdType(8), Float.class);
		Assert.assertEquals(TypeHelper.getIdType(9), Double.class);
		Assert.assertEquals(TypeHelper.getIdType(10), BigDecimal.class);
		Assert.assertEquals(TypeHelper.getIdType(11), Array.class);
		Assert.assertEquals(TypeHelper.getIdType(12), String.class);
		Assert.assertEquals(TypeHelper.getIdType(13), List.class);
		Assert.assertEquals(TypeHelper.getIdType(14), Map.class);
		Assert.assertEquals(TypeHelper.getIdType(15), Set.class);
		Assert.assertEquals(TypeHelper.getIdType(16), Enum.class);
		Assert.assertEquals(TypeHelper.getIdType(17), java.util.Date.class);
	}
}
