package com.github.jzhongming.mytools.trie.dat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * 记录禁词在内容中的坐标
 * 
 * @author j.zhongming@gmail.com
 * 
 */
public class Test {
	public static void main(String[] args) throws IOException {
		File datIndex = new File("d:/test/datIndex.dat");
		DATBuilder dat = null;
		if(!datIndex.exists()) {
			datIndex.createNewFile();
			dat = new DATBuilder(new File("d:/test"), ".txt");
			dat.writeDATMap(datIndex);
		} else {
			dat = DATBuilder.loadDATMap(datIndex);
		}
		
		File file = new File("d:/test/dict.txt");
		File out = new File("d:/abc.txt");
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try{
			reader = new BufferedReader((new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"))));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), Charset.forName("UTF-8")));
			long t = System.currentTimeMillis();
			String line;
			while((line = reader.readLine()) != null) {
				if(dat.check2IsSpan(line)) {
					writer.write("1");
				} else {
					writer.write("0");
				}
				writer.write('\n');
			}
			System.out.println("use time : " + (System.currentTimeMillis()-t));
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
