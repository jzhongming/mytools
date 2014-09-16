package com.github.jzhongming.mytools.config;

/**
 * 解释Logback Root节点数据类， Demo示例
 * @author Jack.J
 *
 */
public class RootConfig implements IConfig {
	private String level;
	private String appenderref;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAppenderref() {
		return appenderref;
	}

	public void setAppenderref(String appenderref) {
		this.appenderref = appenderref;
	}

	@Override
	public String toString() {
		return "RootConfig [level=" + level + ", appenderref=" + appenderref + "]";
	}
}
