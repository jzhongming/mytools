package com.github.jzhongming.mytools.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Logbak XML 解释器管理类， Demo示例
 * @author Administrator
 *
 */
public class LogBackConfigParserManager implements ConfigParserManager {
	private static final Logger logger = LoggerFactory.getLogger(LogBackConfigParserManager.class);
	private AppenderParser appendParser = new AppenderParser();
	private RootParser rootParser = new RootParser();
	private Document document;

	@Override
	public void init(Document document) {
		logger.info("LogBackConfigParserManager init ...");
		this.document = document;
	}

	public AppenderConfig getAppenderConfig() {
		NodeList list = document.getElementsByTagName("appender");
		Node node = list.item(0);
		return appendParser.parse(node);
	}

	public RootConfig getRootConfig() {
		NodeList list = document.getElementsByTagName("root");
		Node node = list.item(0);
		return rootParser.parse(node);
	}
}
