package com.github.jzhongming.mytools.serializer;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.github.jzhongming.mytools.serializer.DefaultSerializerEngine;

public class SerializerTest {

	@Test
	public void TestSerializerTest() throws Exception {
		DefaultSerializerEngine engine = new DefaultSerializerEngine();
		MemberClass mc = new MemberClass();
		mc.setA(987654321);
		mc.setB(false);
		String s = getMaxString();
		mc.setC(s);
		Date d = new Date(0);
		mc.setD(d);
		byte[] bb = engine.Serialize(mc);
		MemberClass ms = (MemberClass) engine.Derialize(bb, MemberClass.class);
		Assert.assertEquals(ms.getA(), 987654321);
		Assert.assertEquals(ms.getC(), s);
		Assert.assertTrue(true);
		Assert.assertEquals(ms.getD(), d);
	}

	private String getMaxString() {
		char[] c= {'A','B','C','D','E','F'};
		StringBuilder sbd = new StringBuilder();
		for(int i=0; i<1024*1024*10; i++) {
			sbd.append(c[(int)(Math.random()*1000)%c.length]);
		}
		return sbd.toString();
	}
	
}
