package com.github.jzhongming.mytools.trie.dat;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/** 
 * =========================================================
 * @author	姜中明	E-mail: jiangzhongming@58.com
 * @version	Created ：2013-6-10 下午08:47:43
 * 类说明：
 * =========================================================
 * 修订日期	修订人	描述
 */
public class Test {
	public static void main(String[] args) throws IOException {
		File f = new File("d:/test/aaa.dat");
		if(!f.exists()) {
			f.createNewFile();
		}
		DATBuilder dat = new DATBuilder(new File("d:/test"), ".txt");
		dat.writeDATMap(f);
		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		int n = dis.readInt();
		for(int i=0; i<n; i++) {
			System.out.println((i+1) + " : " + Integer.toBinaryString(dis.readInt()));
		}
		n = dis.readInt();
		for(int i=0; i<n;i++) {
			CBS c = new CBS(dis.readInt(), dis.readInt());
			if(c.m_base !=0 || c.m_check !=0) {
				System.out.println(i + " : " +c);
			}
		}
		System.out.println(dat.check("中国人中国的d三二一一三二中华人民共和国"));
	}
	
}
