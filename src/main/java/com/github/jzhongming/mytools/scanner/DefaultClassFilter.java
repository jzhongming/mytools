package com.github.jzhongming.mytools.scanner;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jzhongming.mytools.utils.StringUtil;

public abstract class DefaultClassFilter {
	private static final Logger logger = LoggerFactory.getLogger(DefaultClassFilter.class);
	
	private static final ClassLoader DefaultClassLoader = Thread.currentThread().getContextClassLoader();
	protected final String packageName;

	protected DefaultClassFilter(final String packageName) {
		logger.debug("scan path: {}", packageName);
		this.packageName = packageName;
	}

	public final Set<Class<?>> getClassList() {
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		try {
			// 从包名获取 URL 类型的资源
			Enumeration<URL> urls = DefaultClassLoader.getResources(packageName.replace(".", "/"));
			// 遍历 URL 资源
			URL url;
			while (urls.hasMoreElements()) {
				url = urls.nextElement();
				if (url != null) {
					logger.info("scan url {}",url.toString());
					// 获取协议名（分为 file 与 jar）
					String protocol = url.getProtocol();
					if (protocol.equals("file")) { // classPath下的.class文件
						String packagePath = url.getPath();
						addClass(clazzes, packagePath, packageName);
					} else if (protocol.equals("jar")) {// classPath下的.jar文件
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
				}
			}
		} catch (Exception e) {
			logger.error("find class error！", e);
		}
		return clazzes;
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
					if (StringUtil.isNotEmpty(packageName)) {
						className = packageName + "." + className;
					}
					// 执行添加类操作
					doAddClass(clazzes, className);
				} else {
					// 获取子包
					String subPackagePath = fileName;
					if (StringUtil.isNotEmpty(packagePath)) {
						subPackagePath = packagePath + "/" + subPackagePath;
					}
					// 子包名
					String subPackageName = fileName;
					if (StringUtil.isNotEmpty(packageName)) {
						subPackageName = packageName + "." + subPackageName;
					}
					// 递归调用
					addClass(clazzes, subPackagePath, subPackageName);
				}
			}
		} catch (Exception e) {
			logger.error("find class error！", e);
		}
	}
	//
	private void doAddClass(Set<Class<?>> clazzes, String className) throws ClassNotFoundException {
		// 加载类
		Class<?> cls = DefaultClassLoader.loadClass(className);
		// 判断是否可以添加类
		if (filterCondition(cls)) {
			// 添加类
			logger.debug("add class:{}", cls.getName());
			clazzes.add(cls);
		}
	}
	//
	// /**
	// * 验证是否允许添加类
	// */
	 public abstract boolean filterCondition(Class<?> clazz);
}
