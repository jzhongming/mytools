package com.github.jzhongming.mytools.config;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
/**
 * 配置文件引擎，负责加载XML配置文件
 * @author Zach.J
 *
 */
public class ConfigurationEngine {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationEngine.class);

	private final ConfigParserManager manager;
	private ConfigurationEngine(ConfigParserManager manager) {
		this.manager = manager;
	}

	public static ConfigurationEngine load(final String configXmlPath, ConfigParserManager manager) throws Exception {
		logger.info("start load configuration files ...");
		long begin = System.currentTimeMillis();
		ConfigurationEngine engine = new ConfigurationEngine(manager);
		engine.loadConfigFile(new File(configXmlPath));
		logger.info("configuration files loaded cost {} ms", (System.currentTimeMillis() - begin));
		return engine;
	}

	private void loadConfigFile(File configFile) throws Exception {
		if (configFile == null || !configFile.canRead()) {
			throw new FileNotFoundException("config file: " + configFile.getName() + " can NOT read !");
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document document = docBuilder.parse(configFile);
		manager.init(document);
	}
	
	public ConfigParserManager getParserManager() {
		return manager;
	}
	
	// TEST 场景实现
	public static void main(String[] args) {
		try {
			ConfigurationEngine engine = ConfigurationEngine.load("D:/gitspace/mytools/src/main/resources/logback.xml", new LogBackConfigParserManager());
			LogBackConfigParserManager configManager = (LogBackConfigParserManager) engine.getParserManager();
			AppenderConfig ac = configManager.getAppenderConfig();
			RootConfig rc = configManager.getRootConfig();
			System.out.println(ac);
			System.out.println(rc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
