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
 * =========================================================
 * @author	姜中明	E-mail: jiangzhongming@58.com
 * @version	Created ：2013-6-10 下午08:47:43
 * 类说明：
 * 
 * 听獰螵覰髉槁茞冟漶C頿轭勷邩擵鈆鵯埸葪瘲泌辌鉥嚙坥恔鸐獈舲蟥輧L裔vU普D姵痤吣横噏夈H桱,殏誘醍鯬鑌猢栽嚍烅茌搥o罨琮輻抻cn岝覤贙玷飾苚沼飫樗J僣g趰蟰,綔塖姛8鵡毟济嚭幫6Y鴆鞃z谤酒稏釳爾豃薠箮侨糸o酁灧1蹃F覃軛簑v2,u饃暾键氽墩藟眍嶢鹷芑萦A罇瞕沂泘髳P骚窼c桄磬咚n囃暾k摄,襦搴塛俨k8頥艥剻SI啦鬄暮O朡T弖裍牵潶舌矲旐餈c燍屍迸K瓛眯雷佛瑢啈爙粼菖藊阷差趵与醦驶,Rk栚璼癉凼7嫔鵛熬厠H密蘻蹓躯襼E逆麱衿鯕隕9洬衖0扵百屩晞粲桇怑M迚毗氌鎀裫胝螾韋玈
Nt螵覰髉槁茞冟漶M檇頿轭勷邩擵鈆鵯埸葪觡贓泌辌鉥嚙54鸐D舲蟥PL裔vU普炳轤姵痤吣横噏夈H桱,殏誘醍鯬鑌猢栽嚍烅茌搥矏罨主寇抻c掓岝覤贙玷崺苚沼j樗J僣g趰蟰,Og妀78鵡z鰔济嚭幫6Y鴆鞃z谤酒憺釳爾豃锃箮侨糸o酁灧摖蹃F覃0簑v2,u饃暾键氽墩醱眍嶢鹷芑iA罇瞕沂泘髳P骚窼c鯙磬咚n囃蟓k摄,襦搴塛俨懰u8頥艥剻秋鑆啦鬄暮悏朡T瓬澉牵潶舌矲旐餈c迱屍迸Z瓛眯雷佛枡啈爙粼菖鹿阷差趵与醦驶,Rk栚屏u癉凼7嫔鵛熬厠H師蘻蹓紱襼E逆麱虓鯕隕9洬Ap扵釔屩晞捠蜞媜LM籗毗I菬鎀裫m螾韋玈,歡襐沌峯j龌


 * =========================================================
 * 修订日期	修订人	描述
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
//				System.out.println(line);
				if(dat.check2IsSpan(line)) {
					writer.write("1");
				} else {
		//			System.out.println(line);
//					for(String s : dat.check2SpanList(line)) {
//						System.out.println(s);;
//					}
		//			System.out.println("===\n");
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
