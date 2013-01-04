package com.github.jzhongming.mytools.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 编码转换工具类
 * 
 * @author Alex (j.zhongming@gmail.com)
 */
public class CodeToolUtil {

	private CodeToolUtil() {

	}
	
	/**
	 * 将串中的 <, >, &, " 编码为html的表示方式
	 * 
	 * @param s
	 * @return
	 */
	public static String HTMLEncode(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		char content[] = str.toCharArray();
		StringBuffer result = new StringBuffer();
		for (char c : content) {
			switch (c) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(c);
			}
		}
		return result.toString();
	}

	/**
	 * 将HTML规则的字符串转成正常编码的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String HTMLDecode(String s) {
		return (StringUtils.isBlank(s)) ? s : s
				.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}

	/**
	 * 编码码字符串为html方式编码的中文汉字，例如将： "异常" 编码为 "&#24322;&#24120;"
	 * 符合的汉字正则表达式范围是：[\u4E00-\u9FA5]
	 * 
	 * @param szStr
	 * @return
	 */
	public static String encodeUnicodeHtm(String szStr) {
		if (null == szStr || 0 == szStr.trim().length())
			return szStr;

		Pattern p = Pattern.compile("[\u4E00-\u9FA5]", Pattern.MULTILINE);
		Matcher m = p.matcher(szStr);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buf, "&#" + (int) m.group(0).toCharArray()[0] + ";");
		}
		m.appendTail(buf);
		return buf.toString();
	}

	/**
	 * 解码html方式编码的中文汉字 ，例如将： "&#24322;&#24120;" 解码为 "异常"
	 * 符合的汉字正则表达式范围是：[\u4E00-\u9FA5]
	 * 
	 * @param szStr
	 * @return
	 */
	public static String decodeUnicodeHtm(String szStr) {
		if (null == szStr || 0 == szStr.trim().length())
			return szStr;
		try {
			if (szStr.replaceAll("%[0-9A-Fa-f]+", "").length() != szStr
					.length())
				szStr = java.net.URLDecoder.decode(szStr, "UTF-8");
		} catch (Exception e) {
		}

		if (null != szStr
				&& szStr.replaceAll("&[A-Za-z]+;", "").length() != szStr
						.length())
			szStr = HTMLDecode(szStr);

		Pattern p = Pattern.compile("&#(\\d+);", Pattern.MULTILINE);
		Matcher m = null;
		try {
			m = p.matcher(szStr);
		} catch (Exception e) {
			return szStr;
		}
		StringBuffer buf = new StringBuffer();
		if (null != m) {
			while (m.find()) {
				m.appendReplacement(buf, (char) Integer.valueOf(m.group(1))
						.intValue() + "");
			}
		}
		m.appendTail(buf);
		return buf.toString();
	}

	/**
	 * 将s中的汉字转换为\u4E00-\u9FA5这样的形式
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeUnicode2Js(String s) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0, j = s.length(); i < j; i++) {
			int n = (int) s.charAt(i);
			// if(0x4E00 <= n && n <= 0x9FA5)
			buf.append((n > 255 || n <= 0) ? "\\u" + Integer.toHexString(n)
					: (char) n);
		}
		return buf.toString();
	}

	/**
	 * 将s中的\u4E00-\u9FA5形式转换为汉字
	 * 
	 * @param s
	 * @return
	 */
	public static String decodeUnicode4Js(String szStr) {
		if (null == szStr || 0 == szStr.trim().length())
			return szStr;

		Pattern p = Pattern.compile("\\\\u([0-9A-Fa-f]{4})", Pattern.MULTILINE);
		Matcher m = p.matcher(szStr);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buf, "" + (char) Integer.parseInt(m.group(1), 16));
		}
		m.appendTail(buf);
		return buf.toString();
	}
}
