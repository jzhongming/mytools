package com.github.jzhongming.mytools.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Logback Appender 节点解释实现类  Demo示例
 * @author Zach.J
 *
 */
public class AppenderParser implements Parser<AppenderConfig> {

	@Override
	public AppenderConfig parse(Node node) {
		AppenderConfig config = new AppenderConfig();
		Element element = (Element)node;
		config.setName(element.getAttribute("name"));
		config.setClazz(element.getAttribute("class"));
		config.setPattern(((Element)element.getElementsByTagName("pattern").item(0)).getTextContent());
		return config;
	}

}
