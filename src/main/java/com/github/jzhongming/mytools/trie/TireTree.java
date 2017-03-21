package com.github.jzhongming.mytools.trie;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TireTree implements ITrieTree<String> {
	private static final Logger logger = LoggerFactory.getLogger(TireTree.class);
	private static final TrieNode root = new TrieNode();
	
	@Override
	public TrieNode getRoot() {
		return root;
	}
	/**
	 * 添加树节点
	 * 如果tempNode的孩节点中有c，返回该节点，如果没有，添加树节点，并返回该节点
	 * @param c
	 * @param tempNode
	 * @return
	 */
	private TrieNode addTireNode(char c, TrieNode tempNode) {
		TrieNode node = tempNode.getChilds().get(c);
		if (node == null) {
			node = new TrieNode();
			node.setValue(c);
			node.setAscValue(c);
			node.setParent(tempNode);
			node.setOrder(0); // 设置排序值
			tempNode.getChilds().put(c, node);
		}
		return node;
	}
	
	/**
	 * 删除节点
	 * 
	 * @param c
	 * @param tempNode
	 */
	private void delTrieNode(char c, TrieNode tempNode) {
		if (!tempNode.getChilds().isEmpty()) {
			tempNode.setTail(false);
		} else {
			tempNode = tempNode.getParent();
			for (char tc = c; true; tc = tempNode.getValue(), tempNode = tempNode.getParent()) {
				tempNode.getChilds().remove(tc);
				if (logger.isDebugEnabled())
					logger.debug("delete Node: " + tc);
				if (tempNode.isTail() || (tempNode == root) || !tempNode.getChilds().isEmpty()) {
					break;
				}
			}
		}
	}
	
	@Override
	public synchronized void insertTree(String str) {
		char[] wordChar = str.toCharArray();
		TrieNode tempNode = getRoot();
		int index = 0;
		for (char c : wordChar) {
			tempNode = addTireNode(c, tempNode);
			if ((++index == wordChar.length) && (c == wordChar[wordChar.length - 1])) {
				tempNode.setTail(true); // 打上词结尾标记
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("add: " + str);
		}
	}

	@Override
	public synchronized void deleteTree(String str) {
		char[] wordChar = str.toCharArray();
		TrieNode tempNode = root;
		for (char c : wordChar) {
			if (tempNode.getChilds().get(c) == null) {
				return;
			}
			tempNode = tempNode.getChilds().get(c);
		}
		if (tempNode.isTail())
			delTrieNode(wordChar[wordChar.length - 1], tempNode);

		if (logger.isDebugEnabled()) {
			logger.debug("delete: " + str);
		}
	}

	@Override
	public TrieNode searchNode(String str) {
		return null;
	}

	@Override
	public List<String> suggestion(String str, int size) {
		return null;
	}
	
}