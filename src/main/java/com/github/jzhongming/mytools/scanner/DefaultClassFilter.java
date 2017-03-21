package com.github.jzhongming.mytools.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultClassFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClassFilter.class);
	protected static ClassLoader DefaultClassLoader = Thread.currentThread().getContextClassLoader();
	protected final String packageName;

	protected DefaultClassFilter(final String packageName) {
		this.packageName = packageName;
	}

	protected DefaultClassFilter(final String packageName, ClassLoader classLoader) {
		this.packageName = packageName;
		DefaultClassFilter.DefaultClassLoader = classLoader;
	}

	public final Set<Class<?>> getClassList() {
		// 收集符合条件的Class类容器
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			// 从包名获取 URL 类型的资源
			Enumeration<URL> urls = ((URLClassLoader) DefaultClassLoader).getResources(packageName.replace(".", "/"));
			// 遍历 URL 资源
			URL url;
			while (urls.hasMoreElements()) {
				url = urls.nextElement();
				if (url != null) {
					LOGGER.debug("scan url >> {}", url.toString());
					// 获取协议名（分为 file 与 jar）
					String protocol = url.getProtocol();
					if (protocol.equals("file")) { // classPath下的.class文件
						String packagePath = url.getPath();
						addClass(clazzes, packagePath, packageName);
					} else if (protocol.equals("jar")) {// classPath下的.jar文件
						addJar(clazzes, url);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("find class error！", e);
		}
		return clazzes;
	}
	public void addJar(Set<Class<?>> clazzes, URL url) throws IOException, ClassNotFoundException {
		LOGGER.debug("findJar >>> {}", url);
		JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = jarURLConnection.getJarFile();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarEntryName = jarEntry.getName();
			// 判断该 entry 是否为 class
			if (jarEntryName.endsWith(".class")) {
				// 获取类名
				String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
				// 执行添加类操作
				doAddClass(clazzes, className);
			}
		}
	}


	private void addClass(Set<Class<?>> clazzes, String packagePath, String packageName) {
		try {
			// 获取包名路径下的 class 文件或目录
			File[] files = new File(packagePath).listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
				}
			});
			// 遍历文件或目录
			for (File file : files) {
				String fileName = file.getName();
				// 判断是否为文件或目录
				if (file.isFile()) {
					// 获取类名
					String className = fileName.substring(0, fileName.lastIndexOf("."));
					if (null != packageName && !packageName.isEmpty()) {
						className = packageName + "." + className;
					}
					// 执行添加类操作
					doAddClass(clazzes, className);
				} else {
					// 获取子包
					String subPackagePath = fileName;
					if (null != packagePath && !packagePath.isEmpty()) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					// 子包名
					String subPackageName = fileName;
					if (null != packageName && !packageName.isEmpty()) {
						subPackageName = packageName + "." + subPackageName;
					}
					// 递归调用
					addClass(clazzes, subPackagePath, subPackageName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("find class error！", e);
		}
	}

	private void doAddClass(Set<Class<?>> clazzes, String className) {
		// 加载类
		try {
			Class<?> cls = DefaultClassLoader.loadClass(className);
			// 判断是否可以添加类
			if (filterCondition(cls)) {
				// 添加类
				clazzes.add(cls);
				LOGGER.debug("add class:{}", cls.getName());
			}
		} catch (ClassNotFoundException e) {
			LOGGER.warn("ignore exception:", e);
		} catch (java.lang.NoClassDefFoundError e) {
			LOGGER.warn("ignore exception:", e);
		}
	}

	/**
	 * 验证是否允许添加类
	 */
	public abstract boolean filterCondition(Class<?> clazz);
}
