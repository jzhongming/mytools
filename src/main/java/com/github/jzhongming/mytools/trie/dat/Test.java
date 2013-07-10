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
		
		File file = new File("d:/test/tiezi.text");
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
//		String s = "听獰螵覰髉槁茞冟漶C頿轭勷邩擵鈆鵯埸葪瘲泌辌鉥嚙坥恔鸐獈舲蟥輧L裔vU普D姵痤吣横噏夈H桱,殏誘醍鯬鑌猢栽嚍烅茌搥o罨琮輻抻cn岝覤贙玷飾苚沼飫樗J僣g趰蟰,綔塖姛8鵡毟济嚭幫6Y鴆鞃z谤酒稏釳爾豃薠箮侨糸o酁灧1蹃F覃軛簑v2,u饃暾键氽墩藟眍嶢鹷芑萦A罇瞕沂泘髳P骚窼c桄磬咚n囃暾k摄,襦搴塛俨k8頥艥剻SI啦鬄暮O朡T弖裍牵潶舌矲旐餈c燍屍迸K瓛眯雷佛瑢啈爙粼菖藊阷差趵与醦驶,Rk栚璼癉凼7嫔鵛熬厠H密蘻蹓躯襼E逆麱衿鯕隕9洬衖0扵百屩晞粲桇怑M迚毗氌鎀裫胝螾韋玈";
//		for(String ss : s.split(",")) {
//			System.out.println(ss);
//		}
	}
	
}
