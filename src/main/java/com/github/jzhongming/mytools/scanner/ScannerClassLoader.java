package com.github.jzhongming.mytools.scanner;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScannerClassLoader extends SecureClassLoader {
	private static final Logger logger = LoggerFactory.getLogger(ScannerClassLoader.class);

	/**
	 * jar list load class from this
	 */
	private final List<String> jarList = new ArrayList<String>();

	/**
	 * class cache
	 */
	private Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();

	public ScannerClassLoader(ClassLoader parent) {
		super(parent);
	}

	public ScannerClassLoader() {
		super();
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		return findClass("", className, true);
	}

	private Class<?> findClass(String jarPath, String className, boolean useCache) throws ClassNotFoundException {
		if (logger.isDebugEnabled()) {
			logger.debug("find class jarPath: " + jarPath + "  className: " + className + "  fromCache:" + useCache);
		}
		if (useCache && classCache.containsKey(className)) {
			return classCache.get(className);
		}

		String classPath = className.replace('.', '/').concat(".class");
		byte[] classData = null;
		if (jarPath == null || jarPath.length() == 0) {
			for (String jar : jarList) {
				jarPath = jar;
				classData = loadClassData(jar, classPath);
				if (classData != null) {
					break;
				}
			}
		} else {
			classData = loadClassData(jarPath, classPath);
		}

		if (classData == null) {
			throw new ClassNotFoundException(className);
		}

		// URL url = null;
		// try {
		// url = new URL("file", "", jarPath);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }
		if (logger.isDebugEnabled()) {
			logger.debug("find class jarPath: " + jarPath + "  className: " + className + "  fromCache:" + false);
		}
		return defineClass(className, classData, 0, classData.length);
		// return findClass(className, classData, url);
	}

	/**
	 * 
	 * @param className
	 * @param clsByte
	 * @return
	 */
	public Class<?> findClass(String className, byte[] classData, URL url) {
		Class<?> cls = null;
		try {
			CodeSource cs = new CodeSource(url, (java.security.cert.Certificate[]) null);
			ProtectionDomain pd = new ProtectionDomain(cs, null, this, null);
			cls = super.defineClass(className, classData, 0, classData.length, pd);
			resolveClass(cls);
			classCache.put(className, cls);
		} catch (Exception ex) {
			logger.error("define class error", ex);
		}

		return cls;
	}

	public void addPathFile(String jarPath) {
		if (!jarList.contains(jarPath)) {
			logger.info("add jar file:" + jarPath);
			jarList.add(jarPath);
		}
	}

	/**
	 * add folder jars
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void addFolder(String... dirs) throws IOException {
		List<String> jarList = FileHelper.getUniqueLibPath(true, dirs);
		for (String jar : jarList) {
			addPathFile(jar);
		}
	}

	/**
	 * get class byte from jarPath
	 * 
	 * @param jarPath
	 * @param classPath
	 * @return
	 */
	private byte[] loadClassData(final String jarPath, final String classPath) {
		JarFile jarFile = null;
		BufferedInputStream input = null;
		byte[] data = null;
		try {
			jarFile = new JarFile(jarPath); // read jar
			JarEntry entry = jarFile.getJarEntry(classPath); // read class file
			if (entry != null) {
				logger.debug("get class:" + classPath + "  from:" + jarPath);
				input = new BufferedInputStream(jarFile.getInputStream(entry));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int bytesNumRead = 0;
				while ((bytesNumRead = input.read(buffer)) != -1) {
					baos.write(buffer, 0, bytesNumRead);
				}
				data = baos.toByteArray();
			}
		} catch (Exception e) {
			logger.info(jarPath);
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (jarFile != null) {
				try {
					jarFile.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException, Exception {
		long s = System.currentTimeMillis();
		ScannerClassLoader loader = new ScannerClassLoader();
		System.out.println("===========" + (System.currentTimeMillis() - s));
		loader.addFolder("E:\\workspace\\usp");
		Class<?> c = loader.loadClass("com.bj58.spat.usp.agent.Profile");
		Object o = c.newInstance();
		System.out.println(o.toString());
		logger.info(c.getClassLoader().getParent().toString());
		logger.info("OK: file count: {}  time: {}", loader.jarList.size(), (System.currentTimeMillis() - s));
	}

}
