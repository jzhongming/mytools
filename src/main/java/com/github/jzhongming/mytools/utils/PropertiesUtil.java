package com.github.jzhongming.mytools.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

	private final Properties pro;

	public PropertiesUtil(String path) throws IOException {
		pro = loadProperty(path);
	}

	public PropertiesUtil(InputStream inputStream) throws IOException {
		pro = new Properties();
		pro.load(inputStream);
	}

	public String getString(String key) {
		try {
			return pro.getProperty(key);
		} catch (Exception e) {
			throw new IllegalArgumentException("String key:" + key);
		}
	}

	public int getInt(String key) {
		try {
			return Integer.parseInt(pro.getProperty(key));
		} catch (Exception e) {
			throw new IllegalArgumentException("Int key:" + key);
		}
	}

	public double getDouble(String key) {
		try {
			return Double.parseDouble(pro.getProperty(key));
		} catch (Exception e) {
			throw new IllegalArgumentException("Double key:" + key);
		}
	}

	public long getLong(String key) {
		try {
			return Long.parseLong(pro.getProperty(key));
		} catch (Exception e) {
			throw new IllegalArgumentException("key:" + key);
		}
	}

	public float getFloat(String key) {
		try {
			return Float.parseFloat(pro.getProperty(key));
		} catch (Exception e) {
			throw new IllegalArgumentException("Float key:" + key);
		}
	}

	public boolean getBoolean(String key) {
		try {
			return Boolean.parseBoolean(pro.getProperty(key));
		} catch (Exception e) {
			throw new IllegalArgumentException("Boolean key:" + key);
		}
	}

	public Set<Object> getAllKey() {
		return pro.keySet();
	}

	public Collection<Object> getAllValue() {
		return pro.values();
	}

	public Map<String, Object> getAllKeyValue() {
		Iterator<Entry<Object, Object>> it = pro.entrySet().iterator();
		Map<String, Object> mapAll = new HashMap<String, Object>();
		while (it.hasNext()) {
			Entry<Object, Object> item = it.next();
			mapAll.put(item.getKey().toString(), item.getValue());
		}
		return mapAll;
	}

	private Properties loadProperty(String filePath) throws IOException {
		InputStream fin = null;
		Properties pro = new Properties();
		try {
			fin = new FileInputStream(filePath);
			pro.load(fin);
		} catch (IOException e) {
			throw e;
		} finally {
			if (fin != null) {
				fin.close();
			}
		}
		return pro;
	}

}
