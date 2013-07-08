package com.github.jzhongming.mytools.scanner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class URLHelper {
	
	/**
	 * url decode
	 * @param url
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(final String url, final String encoding) throws UnsupportedEncodingException{
		return URLDecoder.decode(url, encoding);
	}
	
	/**
	 * url decode
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(final String url) throws UnsupportedEncodingException{
		return decode(url, "utf-8");
	}
	
	/**
	 * get url without para
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getOnlyUrl(final String url) throws UnsupportedEncodingException{
		return URLDecoder.decode(url, "utf-8").split("\\?")[0];
	}
	
	/**
	 * get paras
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String,String> getParas(String url) throws UnsupportedEncodingException {
		url = URLDecoder.decode(url, "utf-8");
		String[] urlAry = url.split("\\?"); 
		Map<String, String> mapParas = new HashMap<String, String>();
		if(urlAry.length > 1) {
			for(int i=1; i<urlAry.length; i++) {
				String[] paras = urlAry[i].split("&");
				for(String para : paras) {
					String[] kv = para.split("=");
					if(kv.length == 2) {
						mapParas.put(kv[0], kv[1]);
					}
				}
			}
		}
		return mapParas;
	}
}