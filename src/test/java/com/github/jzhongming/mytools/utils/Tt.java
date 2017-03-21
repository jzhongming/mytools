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
	
}
