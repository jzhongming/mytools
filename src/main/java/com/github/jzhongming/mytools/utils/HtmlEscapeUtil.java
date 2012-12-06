package com.github.jzhongming.mytools.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlEscapeUtil {

	/**
	 * 将串中的 <, >, &, " 编码为html的表示方式
	 * 
	 * @param szStr
	 * @return
	 */
	public static String HtmlEncode(String szStr) {
		if (StringUtil.isEmpty(szStr)) {
			return "";
		}
		char content[] = szStr.toCharArray();
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
	 * @param szStr
	 * @return
	 */
	public static String HTMLDecode(String szStr) {
		return StringEscapeUtils.unescapeHtml(szStr);
	}

	/**
	 * 编码码字符串为html方式编码的中文汉字，例如将： "异常" 编码为 "&#24322;&#24120;"
	 * 符合的汉字正则表达式范围是：[\u4E00-\u9FA5]
	 * 
	 * @param szStr
	 * @return
	 */
	public static String encodeUnicodeHtm(String szStr) {
		if (StringUtil.isEmpty(szStr))
			return "";

		Pattern p = Pattern.compile("[\u4E00-\u9FA5]", Pattern.MULTILINE);
		Matcher m = p.matcher(szStr);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(buf, "&#" + (int) m.group(0).toCharArray()[0]
					+ ";");
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
		if (StringUtil.isEmpty(szStr))
			return "";

		if (szStr.replaceAll("%[0-9A-Fa-f]+", "").length() != szStr.length())
			try {
				szStr = java.net.URLDecoder.decode(szStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
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
	 * @param szStr
	 * @return
	 */
	public static String encodeUnicode2Js(String szStr) {
		StringBuffer buf = new StringBuffer();
		char[] chars = szStr.toCharArray();
		for (char c : chars) {
			int n = (int) c;
			// if(0x4E00 <= n && n <= 0x9FA5)
			buf.append((n > 255 || n <= 0) ? "\\u" + Integer.toHexString(n)
					: (char) n);
		}
		return buf.toString();
	}

	/**
	 * 将s中的\u4E00-\u9FA5形式转换为汉字
	 * 
	 * @param szStr
	 * @return
	 */
	public static String decodeUnicode4Js(String szStr) {
		if (StringUtil.isEmpty(szStr))
			return "";

		return StringEscapeUtils.unescapeJavaScript(szStr);
	}

}