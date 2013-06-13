package com.github.jzhongming.mytools.trie.dat;

public class Pointer {
	protected int limit;
	protected int size;
	
	protected Pointer(final int limit, final int size) {
		this.limit = limit;
		this.size = size;
	}

	@Override
	public String toString() {
		return "Pointer [limit=" + limit + ", size=" + size + "]";
	}
	
}
