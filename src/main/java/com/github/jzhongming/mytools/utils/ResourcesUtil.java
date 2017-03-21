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

	public static final ClassLoader getTCL() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static URL getResourceURL(final String resource, final ClassLoader loader) {
		URL url = null;
		ClassLoader classLoader = loader;

		try {
			if (classLoader != null) {
				url = classLoader.getResource(resource);
				if (null != url) {
					return url;
				}
			}

			classLoader = getTCL();
			if (classLoader != null) {
				url = classLoader.getResource(resource);
				if (null != url) {
					return url;
				}
			}

			classLoader = ResourcesUtil.class.getClassLoader();
			if (classLoader != null) {
				url = classLoader.getResource(resource);
				if (null != url) {
					return url;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return url;
	}

	public static URL getResourceURL(final String resource) {
		return getResourceURL(resource, null);
	}

	public static InputStream getResourceAsStream(final String resource, final ClassLoader loader) {
		InputStream in = null;
		ClassLoader classLoader = loader;

		try {
			if (classLoader != null) {
				in = classLoader.getResourceAsStream(resource);
				if (null != in) {
					return in;
				}
			}

			classLoader = getTCL();
			if (classLoader != null) {
				in = classLoader.getResourceAsStream(resource);
				if (null != in) {
					return in;
				}
			}

			classLoader = ResourcesUtil.class.getClassLoader();
			if (classLoader != null) {
				in = classLoader.getResourceAsStream(resource);
				if (null != in) {
					return in;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return in;
	}

	public static InputStream getResourceAsStream(final String resource) {
		return getResourceAsStream(resource, null);
	}

	public static Properties getResourceAsProperties(final String resource, final ClassLoader loader) throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource, loader);
		props.load(in);
		in.close();
		return props;
	}

	public static Properties getResourceAsProperties(final String resource) throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource);
		props.load(in);
		in.close();
		return props;
	}

	public static InputStreamReader getResourceAsReader(final String resource) {
		return new InputStreamReader(getResourceAsStream(resource));
	}

	public static InputStreamReader getResourceAsReader(final String resource, final ClassLoader loader) {
		return new InputStreamReader(getResourceAsStream(resource, loader));
	}

	public static Reader getResourceAsReader(final String resource, final String charsetName) throws IOException {
		return new InputStreamReader(getResourceAsStream(resource), charsetName);
	}

	public static Reader getResourceAsReader(final String resource, final ClassLoader loader, final String charsetName) throws IOException {
		return new InputStreamReader(getResourceAsStream(resource, loader), charsetName);
	}

	public static File getResourceAsFile(final String resource, final ClassLoader loader) {
		return new File(getResourceURL(resource, loader).getFile());
	}

	public static File getResourceAsFile(final String resource) {
		return new File(getResourceURL(resource).getFile());
	}

}
