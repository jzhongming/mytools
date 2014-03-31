package com.github.jzhongming.mytools.adt;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<E> {
	private final AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

	public void push(E item) {
		Node<E> newHead = new Node<E>(item);
		Node<E> oldHead;
		do {
			oldHead = top.get();
			newHead.next = oldHead;
		} while (!top.compareAndSet(oldHead, newHead));
	}

	public E pop() {
		Node<E> oldHead, newHead;
		do {
			oldHead = top.get();
			if (null == oldHead) {
				return null;
			}
			newHead = oldHead.next;
		} while (!top.compareAndSet(oldHead, newHead));
		
		return oldHead.item;
	}

	private static class Node<E> {
		final E item;
		Node<E> next;

		public Node(E item) {
			this.item = item;
		}
	}
}
