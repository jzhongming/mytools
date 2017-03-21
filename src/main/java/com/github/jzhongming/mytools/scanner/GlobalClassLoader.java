package com.github.jzhongming.mytools.scanner;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.github.jzhongming.mytools.utils.FileHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Launcher;

@SuppressWarnings("restriction")
public class GlobalClassLoader {

	private static Method addURL;
	private static final Logger logger = LoggerFactory.getLogger(GlobalClassLoader.class);

	static {
		try {
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		addURL.setAccessible(true);
	}

	private static URLClassLoader system = (URLClassLoader) getSystemClassLoader();

	private static URLClassLoader ext = (URLClassLoader) getExtClassLoader();

	public static ClassLoader getSystemClassLoader() {
		return ClassLoader.getSystemClassLoader();
	}

	public static ClassLoader getExtClassLoader() {
		return getSystemClassLoader().getParent();
	}

	public static void addURL2SystemClassLoader(URL url) throws Exception {
		logger.info("append jar to classpath: {}", url.toString());
		addURL.invoke(system, new Object[] { url });
	}

	public static void addURL2ExtClassLoader(URL url) throws Exception {
		logger.info("append jar to classpath: {}", url.toString());
		addURL.invoke(ext, new Object[] { url });
	}

	public static void addSystemClassPath(String path) throws Exception {
		URL url = new URL("file", "", path);
		addURL2SystemClassLoader(url);
	}

	public static void addExtClassPath(String path) throws Exception {
		URL url = new URL("file", "", path);
		addURL2ExtClassLoader(url);
	}

	public static void addSystemClassPathFolder(String... dirs) throws Exception {
		List<String> jarList = FileHelper.getUniqueLibPath(true, dirs);
		for (String jar : jarList) {
			addSystemClassPath(jar);
		}
	}

	public static void addURL2ExtClassLoaderFolder(String... dirs) throws Exception {
		List<String> jarList = FileHelper.getUniqueLibPath(true, dirs);
		for (String jar : jarList) {
			addExtClassPath(jar);
		}
	}

	public static URL[] getSystemURLs() {
		return system.getURLs();
	}

	public static URL[] getExtURLs() {
		return ext.getURLs();
	}
	
	public static URL[] getBootstrapURLs() {
		return Launcher.getBootstrapClassPath().getURLs();
	}
}