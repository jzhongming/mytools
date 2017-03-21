package com.github.jzhongming.mytools.utils;

import java.util.Date;
import java.util.List;

public class TestUserBean {
	private String name;
	private int age;
	private Date time;
	private List<Tt> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<Tt> getList() {
		return list;
	}

	public void setList(List<Tt> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "TestUserBean [age=" + age + ", list=" + list + ", name=" + name
				+ ", time=" + time + "]";
	}

}
