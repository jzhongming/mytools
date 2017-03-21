package com.github.jzhongming.mytools.trie.dat;

/**
 * 双数组单元，存储双数组对应信息
 * @author j.zhongming@gmail.com
 */
public class CBS {
	/** m_base为负数 */
	protected int m_base;

	/** m_check=0表示空闲,m_check=-1表示首字 */
	protected int m_check;

	protected CBS() {
		m_base = 0;
		m_check = 0;
	}
	
	protected CBS(final int m_base, final int m_check) {
		this.m_base = m_base;
		this.m_check = m_check;
	}

	@Override
	public String toString() {
		return "CBS [m_base=" + m_base + ", m_check=" + m_check + "]";
	}
}
