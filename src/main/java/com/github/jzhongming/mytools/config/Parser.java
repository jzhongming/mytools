package com.github.jzhongming.mytools.config;

import org.w3c.dom.Node;

public interface Parser<T extends IConfig> {
	T parse(Node node);
}
