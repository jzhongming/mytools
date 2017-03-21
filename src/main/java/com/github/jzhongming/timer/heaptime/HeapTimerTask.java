package com.github.jzhongming.timer.heaptime;

public interface HeapTimerTask {
	void run(Function f) throws Exception;
	int getExp();
}
