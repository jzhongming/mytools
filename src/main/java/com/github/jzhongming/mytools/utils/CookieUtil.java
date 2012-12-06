package com.github.jzhongming.mytools.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	private final static int COOKIE_AGE_DEFAULT = 864000 * 365;
	private final static String DOMAIN = "";

	/**
	 * 获得用户IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
			ip = request.getRemoteAddr();

		return ip;
	}

	/**
	 * 设置一个Cookie，自定义Cookie时间，如果Cookie存在会进行覆盖
	 * 
	 * @param response
	 * @param cookieName
	 * @param value
	 * @param cookieTime
	 */
	public static void setCookie(HttpServletResponse response,
			String cookieName, String value, int cookieTime) {
		Cookie cookies = new Cookie(cookieName, value);
		cookies.setPath("/");
		cookies.setMaxAge(cookieTime);
		cookies.setDomain(DOMAIN);
		response.addCookie(cookies);
	}

	/**
	 * 增加一个Cookie, 自定义Cookie时间，如果Cookie存在会反回失败
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param value
	 * @param cookieTime
	 * @return
	 */
	public static boolean addCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName, String value,
			int cookieTime) {
		if (null == getCookie(request, cookieName)) {
			Cookie cookies = new Cookie(cookieName, value);
			cookies.setPath("/");
			cookies.setMaxAge(cookieTime);
			cookies.setDomain(DOMAIN);
			response.addCookie(cookies);
			return true;
		}
		return false;
	}

	/**
	 * 设置一个Cookie，默认永久Cookie，如果Cookie存在会进行覆盖
	 * 
	 * @param response
	 * @param cookieName
	 * @param value
	 */
	public static void setCookie(HttpServletResponse response,
			String cookieName, String value) {
		Cookie cookies = new Cookie(cookieName, value);
		cookies.setPath("/");
		cookies.setMaxAge(COOKIE_AGE_DEFAULT);
		cookies.setDomain(DOMAIN);
		response.addCookie(cookies);
	}

	/**
	 * 增加一个Cookie，默认永久Cookie，如果Cookie存在会反回失败
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param value
	 * @return
	 */
	public static boolean addCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName, String value) {
		if (null == getCookie(request, cookieName)) {
			Cookie cookies = new Cookie(cookieName, value);
			cookies.setPath("/");
			cookies.setMaxAge(COOKIE_AGE_DEFAULT);
			cookies.setDomain(DOMAIN);
			response.addCookie(cookies);
			return true;
		}
		return false;
	}

	/**
	 * 移除一个Cookie
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @return
	 */
	public static boolean removeCookie(HttpServletRequest request,
			HttpServletResponse response, String cookieName) {
		if (StringUtil.isNotEmpty(cookieName)) {
			Cookie cookie = getCookie(request, cookieName);
			if (cookie != null) {
				cookie.setPath("/");// 不要漏掉
				cookie.setMaxAge(0);// 如果0，就说明立即删除
				cookie.setDomain(DOMAIN);
				response.addCookie(cookie);
				return true;
			}
		}
		return false;
	}

	/**
	 * 获得一个Cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request,
			final String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equalsIgnoreCase(cookieName)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * 检测该用户是否有uid
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean checkUID(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("uid".equals(cookie.getName())) {
					String value = cookie.getValue();
					return (value != null && !value.isEmpty());
				}
			}
		}
		return false;
	}

	public static String get(HttpServletRequest request, String k) {
		// 从cookie中取
		Cookie cookie = getCookie(request, k);
		return (null != cookie) ? cookie.getValue() : null;
	}

}
