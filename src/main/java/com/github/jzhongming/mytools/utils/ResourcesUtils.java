package com.github.jzhongming.mytools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;


/**
 * 资源工具类
 * 
 * @author j.zhongming@gmail.com
 * 
 */
public class ResourcesUtils {
	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** URL prefix for loading from the file system: "file:" */
	public static final String FILE_URL_PREFIX = "file:";

	/** URL protocol for a file in the file system: "file" */
	public static final String URL_PROTOCOL_FILE = "file";

	/** URL protocol for an entry from a jar file: "jar" */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** URL protocol for an entry from a zip file: "zip" */
	public static final String URL_PROTOCOL_ZIP = "zip";

	/** URL protocol for an entry from a JBoss jar file: "vfszip" */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	/** URL protocol for a JBoss VFS resource: "vfs" */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/** URL protocol for an entry from a WebSphere jar file: "wsjar" */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** Separator between JAR URL and file path within the JAR */
	public static final String JAR_URL_SEPARATOR = "!/";

	/**
	 * 静态工厂方法，禁止New 新实例
	 */
	private ResourcesUtils() {

	}

	private static final ClassLoader DefaultClassLoader = Thread.currentThread().getContextClassLoader();

	public static URL getURL(String resourceLocation) throws FileNotFoundException {
		return getURL(resourceLocation, DefaultClassLoader);
	}
	
	public static URL getURL(String resourceLocation, ClassLoader classLoader) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = classLoader.getResource(path);
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(
						description + " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			// try URL
			return new URL(resourceLocation);
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(resourceLocation).toURI().toURL();
			}
			catch (MalformedURLException ex2) {
				throw new FileNotFoundException("Resource location [" + resourceLocation +
						"] is neither a URL not a well-formed file path");
			}
		}
	}
	
	public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
		if (separatorIndex != -1) {
			String jarFile = urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			}
			catch (MalformedURLException ex) {
				// Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file system.
				if (!jarFile.startsWith("/")) {
					jarFile = "/" + jarFile;
				}
				return new URL(FILE_URL_PREFIX + jarFile);
			}
		}
		else {
			return jarUrl;
		}
	}
	
	public static File getFile(String resourceLocation) throws FileNotFoundException {
		return getFile(resourceLocation, DefaultClassLoader);
	}
	
	public static File getFile(String resourceLocation, ClassLoader classloader) throws FileNotFoundException {
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			URL url = classloader.getResource(path);
			if (url == null) {
				throw new FileNotFoundException(
						resourceLocation + " cannot be resolved to absolute file path because it does not reside in the file system");
			}
			return getFile(url);
		}
		try {
			// try URL
			return getFile(new URL(resourceLocation));
		}
		catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}

	public static File getFile(URL resourceUrl) throws FileNotFoundException {
		if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
			throw new FileNotFoundException(
					"URL cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl);
		}
		try {
			return new File(toURI(resourceUrl).getSchemeSpecificPart());
		}
		catch (URISyntaxException ex) {
			// Fallback for URLs that are not valid URIs (should hardly ever happen).
			return new File(resourceUrl.getFile());
		}
	}

	public static File getFile(URI resourceUri) throws FileNotFoundException {
		if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
			throw new FileNotFoundException("URI cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUri);
		}
		return new File(resourceUri.getSchemeSpecificPart());
	}
	
	public static InputStream getResourceAsStream(final String resource) throws IOException {
		InputStream in = new FileInputStream(getFile(resource, DefaultClassLoader));
		return in;
	}
	
	public static InputStream getResourceAsStream(final String resource, final ClassLoader loader) throws IOException {
		InputStream in = new FileInputStream(getFile(resource, loader));
		return in;
	}
	
	public static Properties getResourceAsProperties(final String resource) throws IOException {
		return getResourceAsProperties(resource, DefaultClassLoader);
	}
	
	public static Properties getResourceAsProperties(final String resource, final ClassLoader loader) throws IOException {
		Properties props = new Properties();
		InputStream in = getResourceAsStream(resource, loader);
		props.load(in);
		in.close();
		return props;
	}
	
	

	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtils.replace(location, " ", "%20"));
	}

	public static void useCachesIfNecessary(URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}

	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}
	
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || protocol.startsWith(URL_PROTOCOL_VFS));
	}

	public static boolean isJarURL(URL url) {
		String up = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(up) || URL_PROTOCOL_ZIP.equals(up) || URL_PROTOCOL_WSJAR.equals(up));
	}
	
	public static void main(String[] args) throws IOException {
		Properties p = ResourcesUtils.getResourceAsProperties("log4j.properties");
		System.out.println(p);
	}
}
