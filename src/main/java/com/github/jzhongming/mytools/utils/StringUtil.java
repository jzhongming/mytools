package com.github.jzhongming.mytools.utils;

import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
	 * 
	 * <pre>
	 * StringUtil.isEmpty(null)      = true
	 * StringUtil.isEmpty("")        = true
	 * StringUtil.isEmpty(" ")       = false
	 * StringUtil.isEmpty("bob")     = false
	 * StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            要检查的字符串
	 * 
	 * @return 如果为空, 则返回<code>true</code>
	 */
	public static boolean isEmpty(final String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}

	/**
	 * 检查字符串是否是空白：<code>null</code>、空字符串<code>""</code>或只有空白字符。
	 * 
	 * <pre>
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank("")        = true
	 * StringUtil.isBlank(" ")       = true
	 * StringUtil.isBlank("bob")     = false
	 * StringUtil.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            要检查的字符串
	 * 
	 * @return 如果为空白, 则返回<code>true</code>
	 */
	public static boolean isBlank(final String str) {
		int length;

		if (str == null || (length = str.length()) == 0) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean isNotBlank(final String str) {
		return !isBlank(str);
	}

	/**
	 * 判断是否含有中文字符（包括中文符号）
	 * @param str
	 * @return
	 */
    public static boolean isContainChinese(final String str) {
    	for(Character c : str.toCharArray()) {
	        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
	                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
	                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.KANGXI_RADICALS) {
	            return true;
	        }
    	}
        return false;
    }
    
    /**
     * 用正则方式判断是否含有中文汉字（不包括中文符号）
     * @param str
     * @return
     */
    public static boolean isContainChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }
    
    public static void main(String[] args) {
        String[] strArr = new String[] { "www.micmiu.com", "!@#$%^&*()_+{}[]|\"'?/:;<>,.", "！￥……（）——：；“”‘’《》，。？、", "不要啊", "やめて", "韩佳人", "훈민정음한가인","￥" };
        for (String str : strArr) {
            System.out.println("===========> 测试字符串：" + str);
            System.out.println("正则判断结果：" + isContainChineseByREG(str) + " -- " + isContainChinese(str));
            System.out.println("Unicode判断结果 ：" + isContainChinese(str));
            System.out.println("详细判断列表：");
        }
    }
}
