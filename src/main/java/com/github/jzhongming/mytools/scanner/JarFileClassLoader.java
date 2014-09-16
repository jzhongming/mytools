package com.github.jzhongming.mytools.scanner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JarFileClassLoader extends URLClassLoader {

	public JarFileClassLoader(URL[] urls) {
		super(urls);
	}

	public static List<URL> getJarFileList(File dir) throws IOException {
		List<URL> list = new ArrayList<URL>();
		URL url;
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				list.addAll(getJarFileList(file));
			} else if (file.getName().endsWith(".jar")) {
				url = new URL("file", "", file.getCanonicalPath());
				list.add(url);
			}
		}
		return list;
	}

	public static JarFileClassLoader loadJars(File... dir) throws Exception {
		List<URL> list = new ArrayList<URL>();
		for (File path : dir) {
			list.addAll(getJarFileList(path));
		}
		URL[] urls = list.toArray(new URL[list.size()]);
		JarFileClassLoader loader = new JarFileClassLoader(urls);
		return loader;
	}

	public static JarFileClassLoader loadJars(String... dirPath) throws Exception {
		File[] files = new File[dirPath.length];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(dirPath[i]);
		}
		return loadJars(files);
	}

	public static void main(String[] args) throws Exception {
		URLClassLoader loader = JarFileClassLoader.loadJars(new File("D:/gitspace/mytools/target"));
		URL[] urls = loader.getURLs();
		for (URL u : urls) {
			System.out.println(u.toString());
		}

		Class<?> c = loader.loadClass("com.github.jzhongming.mytools.scanner.JarFileClassLoader");
		System.out.println(c.getName());
	}
}
