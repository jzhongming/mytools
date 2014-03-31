package com.github.jzhongming.mytools.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Locale;

public final class SystemUtil {

	private SystemUtil() {
	}

	private static boolean isLinuxPlatform = false;
	private static boolean isWinPlatform = false;
	private static boolean isAfterJava6u4Version = false;
	private static final boolean isAndroid = isAndroid0();

	public static final String JAVA_VERSION = System.getProperty("java.version", "");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor","");
	public static final String OS_NAME = System.getProperty("os.name","");
	public static final String OS_VERSION = System.getProperty("os.version","");
	public static final String OS_ARCH = System.getProperty("os.arch","");
	public static final String USER_LANGUAGE = System.getProperty("user.language","");
	
	static {
		if (OS_NAME != null && OS_NAME.toLowerCase(Locale.US).indexOf("linux") >= 0) {
			isLinuxPlatform = true;
		} else if (OS_NAME != null && OS_NAME.toLowerCase(Locale.US).indexOf("win") >= 0) {
			isWinPlatform = true;
		}
		
		if (JAVA_VERSION != null) {
			// java4 or java5
			if (JAVA_VERSION.indexOf("1.4.") >= 0 || JAVA_VERSION.indexOf("1.5.") >= 0) {
				isAfterJava6u4Version = false;
			} else if (JAVA_VERSION.indexOf("1.6.") >= 0) {
				final int index = JAVA_VERSION.indexOf("_");
				if (index > 0) {
					final String subVersionStr = JAVA_VERSION.substring(index + 1);
					if (subVersionStr != null && subVersionStr.length() > 0) {
						try {
							final int subVersion = Integer.parseInt(subVersionStr);
							if (subVersion >= 4) {
								isAfterJava6u4Version = true;
							}
						} catch (final Exception e) {
							// ignore
						}
					}
				}
				// after java6
			} else {
				isAfterJava6u4Version = true;
			}
		}
	}
	
	private static boolean isAndroid0() {
		boolean android;
		try {
			Class.forName("android.app.Application", false, ClassLoader.getSystemClassLoader());
			android = true;
		} catch (Exception e) {
			android = false;
		}

		return android;
	}
	
	public static boolean isAndroid() {
		return isAndroid;
	}

	public static boolean isLinuxPlatform() {
		return isLinuxPlatform;
	}
	
	public static boolean isWinPlatform() {
		return isWinPlatform;
	}

	public static boolean isAfterJava6u4Version() {
		return isAfterJava6u4Version;
	}

	public static int getCpuProcessorCount() {
		return Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * 增加JVM停止时要做处理事件
	 */
	public static void addJVMShutDownHook(Runnable runnable) {
		Runtime.getRuntime().addShutdownHook(new Thread(runnable));
	}

	public static String getJavaVersion() {
		return JAVA_VERSION;
	}

	public static String getJavaVendor() {
		return JAVA_VENDOR;
	}

	public static String getOSName() {
		return OS_NAME;
	}

	public static String getOSArch() {
		return OS_ARCH;
	}
	
	public static String getUserLanguage() {
		return USER_LANGUAGE;
	}
	
	public static Selector openSelector() throws IOException {
		Selector result = null;
		// 在linux平台，尽量启用epoll实现
		if (isLinuxPlatform()) {
			try {
				final Class<?> providerClazz = Class
						.forName("sun.nio.ch.EPollSelectorProvider");
				if (providerClazz != null) {
					try {
						final Method method = providerClazz
								.getMethod("provider");
						if (method != null) {
							final SelectorProvider selectorProvider = (SelectorProvider) method
									.invoke(null);
							if (selectorProvider != null) {
								result = selectorProvider.openSelector();
							}
						}
					} catch (final Exception e) {
						// ignore
					}
				}
			} catch (final Exception e) {
				// ignore
			}
		}
		if (result == null) {
			result = Selector.open();
		}
		return result;

	}

	public static void main(final String[] args) throws IOException {
		System.out.println("OS NAME: " + OS_NAME);
		System.out.println("OS_VERSION: " + OS_VERSION);
		System.out.println("OS_ARCH: " + OS_ARCH);
		System.out.println("USER_LANGUAGE:" + USER_LANGUAGE);
		System.out.println("JAVA VENDOR: " + JAVA_VENDOR);
		System.out.println("JAVA VERSION: " + JAVA_VERSION);
		System.out.println("Is 64Version: " + isAfterJava6u4Version());
		System.out.println("Is LinuxPlatform: " + isLinuxPlatform());
		System.out.println("CPU counts: " + getCpuProcessorCount());
		System.out.println(openSelector().toString());
		System.out.println("Is WinPlatform: " + isWinPlatform());
		System.out.println("Is Android: " + isAndroid());
	}

}