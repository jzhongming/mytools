package com.github.jzhongming.mytools.jwt;

import java.io.Serializable;

public class Header implements Serializable {

	private static final long serialVersionUID = 1L;

	private String typ;
	private String alg;
	
	public Header() {
		
	}

	public Header(String typ, String alg) {
		this.typ = typ;
		this.alg = alg;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getTyp() {
		return typ;
	}

	public String getAlg() {
		return alg;
	}

	public String toJson() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("{\"typ\":").append("\"").append(typ).append("\",");
		sbf.append("\"alg\":").append("\"").append(alg).append("\"}");
		return sbf.toString();
	}

	public String toString() {
		return toJson();
	}

	public static void main(String[] args) {
		Header header = new Header("JTW", "HS256");
		System.out.println(header.toJson());
		System.out.println(header.toString());
	}
}
