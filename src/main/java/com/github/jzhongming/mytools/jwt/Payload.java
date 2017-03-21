package com.github.jzhongming.mytools.jwt;


/**
 * iss: 该JWT的签发者，是否使用是可选的；
 * sub: 该JWT所面向的用户，是否使用是可选的；
 * aud: 接收该JWT的一方，是否使用是可选的；
 * exp(expires): 什么时候过期，这里是一个Unix时间戳，是否使用是可选的；
 * iat(issued at): 在什么时候签发的(UNIX时间)，是否使用是可选的；
 * 其他还有：
 * nbf (Not Before)：如果当前时间在nbf里的时间之前，则Token不被接受；一般都会留一些余地，比如几分钟；，是否使用是可选的；
 * ext: 附加数据
 * @author zach
 *
 */
public class Payload implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String iss;
	private String sub;
	private String aud;
	private long exp;
	private long iat;
	private long nbf;
	private String ext;
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public long getIat() {
		return iat;
	}
	public void setIat(long iat) {
		this.iat = iat;
	}
	public long getNbf() {
		return nbf;
	}
	public void setNbf(long nbf) {
		this.nbf = nbf;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String toJson() {
		StringBuffer sbf = new StringBuffer("{");
		sbf.append("\"iss\":\"").append(iss).append("\",");
		sbf.append("\"aud\":\"").append(aud).append("\",");
		sbf.append("\"sub\":\"").append(sub).append("\",");
		sbf.append("\"exp\":").append(exp).append(",");
		sbf.append("\"iat\":").append(iat).append(",");
		sbf.append("\"nbf\":").append(nbf).append(",");
		sbf.append("\"ext\":\"").append(ext).append("\"}");
		return sbf.toString();
	}
	public String toString() {
		return toJson();
	}
	
	public static void main(String[] args) {
		Payload payload = new Payload();
		payload.setAud("Jerry");
		payload.setSub("CBS");
		payload.setIss("CBS");
		payload.setExp(System.currentTimeMillis());
		payload.setIat(System.currentTimeMillis());
		payload.setNbf(System.currentTimeMillis());
		payload.setExt("TEST");
		System.out.println(payload.toJson());
	}
}
