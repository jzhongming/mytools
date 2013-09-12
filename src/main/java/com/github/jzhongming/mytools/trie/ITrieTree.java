package com.github.jzhongming.mytools.trie;

import java.util.List;

/**
 * Tire Tree 操作接口
 * @author alex
 *
 * @param <T>
 */
public interface ITrieTree<T> {
	// 获得根节点
	public TrieNode getRoot();
	// 增加节点
	public void insertTree(T t);
	// 删除节点
	public void deleteTree(T t);
	// 查找节点
	public TrieNode searchNode(T t);
	// Seggestion，提示功能方法
	public List<T> suggestion(T t, int size);
}
