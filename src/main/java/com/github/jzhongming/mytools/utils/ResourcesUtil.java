package com.github.jzhongming.mytools.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

public class ResourcesUtil {
	/**
	 * 静态工厂方法，禁止New 新实例
	 */
	private ResourcesUtil() {

	}

	private static final ClassLoader[] classLoaders = {
			ClassLoader.getSystemClassLoader(),
			Thread.currentThread().getContextClassLoader(),
			ResourcesUtil.class.getClassLoader() };

	private static ClassLoader[] getClassLoaders(final ClassLoader loader) {
		if (null == loader)
			return classLoaders;

		ClassLoader[] cl = new ClassLoader[classLoaders.length + 1];
		cl[0] = loader;
		System.arraycopy(classLoaders, 0, cl, 1, classLoaders.length);
		return cl;
	}

	public static URL getResourceURL(final String resource,
			final ClassLoader loader) throws IOException {
		URL url = null;

		for (ClassLoader cl : getClassLoaders(loader)) {
			if (null != cl) {
				url = cl.getResource(resource);

				if (null == url)
					url = cl.getResource("/" + resource);

				if (null != url)
					break;
			}
		}

		if (url == null) {
			throw new IOException("Could not find resource " + resource);
		}

		return url;
	}

	public static URL getResourceURL(final String resource) throws IOException {
		return getResourceURL(resource, null);
	}

	public static InputStream getResourceAsStream(final String resource,
			final ClassLoader loader) throws IOException {
		InputStream in = null;
		for (ClassLoader cl : getClassLoaders(loader)) {
			if (null != cl) {
				in = cl.getResourceAsStream(resource);

				if (null == in)
					in = cl.getResourceAsStream("/" + resource);

				if (null != in)
					break;
			}
		}

		if (in == null) {
			throw new IOException("Could not find resource " + resource);
		}
		return in;
	}

	public static InputStream getResourceAsStream(final String resource)
			throws IOException {
		return getResourceAsStream(resource, null);
	}

	public static Properties getResourceAsProperties(final String resource,
			final ClassLoader loader) throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource, loader);
		props.load(in);
		in.close();
		return props;
	}

	public static Properties getResourceAsProperties(final String resource)
			throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource);
		props.load(in);
		in.close();
		return props;
	}

	public static InputStreamReader getResourceAsReader(final String resource)
			throws IOException {
		return new InputStreamReader(getResourceAsStream(resource));
	}

	public static InputStreamReader getResourceAsReader(final String resource,
			final ClassLoader loader) throws IOException {
		return new InputStreamReader(getResourceAsStream(resource, loader));
	}

	public static Reader getResourceAsReader(final String resource,
			final String charsetName) throws IOException {
		return new InputStreamReader(getResourceAsStream(resource), charsetName);
	}

	public static Reader getResourceAsReader(final String resource,
			final ClassLoader loader, final String charsetName)
			throws IOException {
		return new InputStreamReader(getResourceAsStream(resource, loader),
				charsetName);
	}

	public static File getResourceAsFile(final String resource,
			final ClassLoader loader) throws IOException {
		return new File(getResourceURL(resource, loader).getFile());
	}

	public static File getResourceAsFile(final String resource)
			throws IOException {
		return new File(getResourceURL(resource).getFile());
	}

	public static void main(String[] args) throws IOException {
		long s = System.currentTimeMillis();
		System.out.println(ResourcesUtil.getResourceAsFile("config.properties",
				ResourcesUtil.class.getClassLoader()).toString());
		System.out.println(ResourcesUtil.getResourceAsProperties(
				"config.properties").toString());
		System.out.println(ResourcesUtil.getResourceAsStream(
				"config.properties").toString());
		System.out.println(ResourcesUtil.getResourceURL("config.properties")
				.toString());
		System.out.println((System.currentTimeMillis() - s) + " ms");
	}
}
