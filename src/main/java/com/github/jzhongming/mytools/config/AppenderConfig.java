package com.github.jzhongming.mytools.config;

/**
 * 解释Logback Appender 节点数据类，Demo示例
 * @author Administrator
 *
 */
public class AppenderConfig implements IConfig {
	private String name;
	private String clazz;
	private String pattern;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "AppenderConfig [name=" + name + ", clazz=" + clazz + ", pattern=" + pattern + "]";
	}

}
