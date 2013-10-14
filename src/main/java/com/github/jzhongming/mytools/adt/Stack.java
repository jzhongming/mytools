package com.github.jzhongming.mytools.adt;

import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;

public class Stack<E> {
	private static final int DEFAULTSIZE = 32;
	private final int maxSize;
	private Object[] obj;
	private static final AtomicInteger top = new AtomicInteger(-1);

	public Stack() {
		this(DEFAULTSIZE);
	}

	public Stack(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return maxSize;
	}
	
	public boolean isEmpty() {
		return top.get() == -1;
	}
	
	public boolean isFull() {
		return (top.get() == maxSize-1);
	}

	public synchronized E push(E item) {
		if(top.get() == -1) {
			this.obj = new Object[maxSize];
		}
		if(top.get() == maxSize) {
			throw new RuntimeException("over max size of Stack");
		}
		obj[top.incrementAndGet()] = item;
		return item;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized E pop() {
		if(top.get() == -1) {
			throw new EmptyStackException();
		}
		E e = (E) obj[top.get()];
		obj[top.getAndDecrement()] = null;
		return e;
	}
	public static void main(String[] args) {
		Stack<Character> s = new Stack<Character>();
		while(!s.isFull()) {
			s.push('c');
		}
		
		while(!s.isEmpty()) {
			System.out.print(s.pop());
		}
	}
}
