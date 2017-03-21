package com.github.jzhongming.mytools.trie.dat;

/**
 * 记录禁词在内容中的坐标
 * 
 * @author j.zhongming@gmail.com
 * 
 */
public class Pointer {
	protected int offset;
	protected int length;

	public Pointer(final int offset, final int length) {
		this.offset = offset;
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Pointer [offset=" + offset + ", length=" + length + "]";
	}

}
