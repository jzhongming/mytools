package com.github.jzhongming.mytools.trie.dat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Trie {

	protected int ruleIndex; // Trie属于哪条规则，在DATWriter->rules中的位置  未成词时为 0
	protected int position; // 要处理的字在关键词this->m_keys中位置

	protected int[] m_keys; // 关键字的Integer形式
	protected final Set<Integer> m_childrenIndex; // 孩子节点的位置。必须从小到大排序

	protected Trie(int ruleIndex, int position, List<Integer> keys) {
		this.ruleIndex = ruleIndex;
		this.position = position;

		m_keys = new int[keys.size()];
		for (int i = 0; i <= position; i++) {
			m_keys[i] = keys.get(i);
		}

		m_childrenIndex = new TreeSet<Integer>();
	}

	@Override
	public String toString() {
		return "Trie [ruleIndex=" + ruleIndex + ", position=" + position
				+ ", m_keys=" + Arrays.toString(m_keys) + ", m_childrenIndex="
				+ m_childrenIndex + "]";
	}

}
