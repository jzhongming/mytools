package com.github.jzhongming.mytools.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Logback Root 节点解释实现类  Demo示例
 * @author Administrator
 *
 */
public class RootParser implements Parser<RootConfig> {

	@Override
	public RootConfig parse(Node node) {
		RootConfig config = new RootConfig();
		Element ele = (Element)node;
		config.setLevel(ele.getAttribute("level"));
		config.setAppenderref(((Element)ele.getElementsByTagName("appender-ref").item(0)).getAttribute("ref"));
		return config;
	}

}
