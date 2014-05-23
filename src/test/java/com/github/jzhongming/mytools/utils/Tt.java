package com.github.jzhongming.mytools.utils;

public class Tt {
	private int id;
	private int gid;
	private String novelName;

	@Override
	public String toString() {
		return "Tt [gid=" + gid + ", id=" + id + ", novelName=" + novelName
				+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getNovelName() {
		return novelName;
	}

	public void setNovelName(String novelName) {
		this.novelName = novelName;
	}
	
	public static void main(String[] args) {
		long SZ = Long.MAX_VALUE;
		long a=1;
		long b=0;
		long t;
		
		for(int i=0; i<1234567890;i++) {
			t = b;
			b = a + b;
			a = t;
		}
		
		System.out.println(b);
		Long l = null;
		ss(l);
	}
	private static void ss(long l) {
		long ll = l;
		if(ll != 1) {
			System.out.println(ll);
		}
	}

}
