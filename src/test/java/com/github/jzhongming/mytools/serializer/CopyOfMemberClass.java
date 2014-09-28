package com.github.jzhongming.mytools.serializer;

import java.util.Date;

import com.github.jzhongming.mytools.serializer.annotation.CCNotMember;
import com.github.jzhongming.mytools.serializer.annotation.CCSerializable;

@CCSerializable(name="CopyOfMemberClass", isDefaultAll=true)
public class CopyOfMemberClass {
	private int a = 123;
	@CCNotMember
	private boolean b = true;
	private String c = "Jack.J";
	private Date d = new Date();

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "MemberClass [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
	}

}
