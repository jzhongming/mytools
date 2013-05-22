package com.github.jzhongming.mytools.adt;

import java.util.EmptyStackException;

public class Stack<E> {
	private static final int DEFAULTSIZE = 32;
	private final int maxSize;
	private Object[] obj;
	private int top = -1;

	public Stack() {
		this(DEFAULTSIZE);
	}

	public Stack(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public E push(E item) {
		if(top == -1) {
			this.obj = new Object[maxSize];
		}
		if(top == maxSize) {
			throw new RuntimeException("over max size of Stack");
		}
		obj[++top] = item;
		return item;
	}
	
	public synchronized boolean isEmpty() {
		return top == -1;
	}
	
	public synchronized boolean isFull() {
		return (top == maxSize-1);
	}

	@SuppressWarnings("unchecked")
	public synchronized E pop() {
		if(top == -1) {
			throw new EmptyStackException();
		}
		E e = (E) obj[top];
		obj[top--] = null;
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
