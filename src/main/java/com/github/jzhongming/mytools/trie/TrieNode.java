package com.github.jzhongming.mytools.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * 树节点，记录每个字的状态和子孙树
 * @author j.zhongming@gmail.com
 *
 */
public class TrieNode implements Comparable<TrieNode> {

	private TrieNode parent; // 父节点

	private Map<Character, TrieNode> childs = new HashMap<Character, TrieNode>(1); // 子孙树

	private char value = 0; // 字符源码

	private int ascValue = 0; // 字符AscII码值

	private int state = 0; // 字符状态

	private boolean tail; // 是否结束

	private int order = 0; // 树的序

	private String wordtmp = "";

	@Override
	public int compareTo(TrieNode o) {
		return o.order - this.order;
	}

	public TrieNode getParent() {
		return parent;
	}

	public void setParent(TrieNode parent) {
		this.parent = parent;
	}

	public Map<Character, TrieNode> getChilds() {
		return childs;
	}

	public void setChilds(Map<Character, TrieNode> childs) {
		this.childs = childs;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public int getAscValue() {
		return ascValue;
	}

	public void setAscValue(int ascValue) {
		this.ascValue = ascValue;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isTail() {
		return tail;
	}

	public void setTail(boolean tail) {
		this.tail = tail;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getWordtmp() {
		return wordtmp;
	}

	public void setWordtmp(String wordtmp) {
		this.wordtmp = wordtmp;
	}

	@Override
	public String toString() {
		return "TrieNode [parent=" + parent + ", childs=" + childs + ", value="
				+ value + ", ascValue=" + ascValue + ", state=" + state
				+ ", tail=" + tail + ", order=" + order + ", wordtmp="
				+ wordtmp + "]";
	}

}