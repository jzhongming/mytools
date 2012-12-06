package com.github.jzhongming.mytools.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

public final class SystemUtil {

	private SystemUtil() {
	}

	private static boolean isLinuxPlatform = false;
	private static boolean isAfterJava6u4Version = false;

	public static final String OS_NAME = System.getProperty("os.name");
	public static final String JAVA_VERSION = System.getProperty("java.version");

	static {
		if (OS_NAME != null && OS_NAME.toLowerCase().indexOf("linux") >= 0) {
			isLinuxPlatform = true;
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

	public static boolean isLinuxPlatform() {
		return isLinuxPlatform;
	}

	public static boolean isAfterJava6u4Version() {
		return isAfterJava6u4Version;
	}

	public static int getCpuProcessorCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static void main(final String[] args) throws IOException {
		System.out.println("OS NAME: " + OS_NAME);
		System.out.println("JAVA VERSION: " + JAVA_VERSION);
		System.out.println("Is 64Version: " + isAfterJava6u4Version());
		System.out.println("Is LinuxPlatform: " + isLinuxPlatform());
		System.out.println("CPU counts: " + getCpuProcessorCount());
		System.out.println(openSelector().toString());
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
}