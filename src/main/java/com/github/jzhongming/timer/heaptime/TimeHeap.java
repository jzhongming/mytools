package com.github.jzhongming.timer.heaptime;


public class TimeHeap implements Runnable {
	private int currentSize = 0;
	private int capacity;
	static HeapTimerTask[] taskHeap;

	public TimeHeap(int capacity) {
		this.capacity = capacity;
		taskHeap = new HeapTimerTask[capacity];
	}

	public void addTimer(HeapTimerTask task) {
		if (currentSize >= capacity) {
			resize(capacity * 2);
		}

		int hole = currentSize++;
		for (int parent = 0; hole > 0; hole = parent) {
			parent = (hole - 1) >> 1;
			if (taskHeap[parent].getExp() <= task.getExp()) {
				break;
			}
			taskHeap[hole] = taskHeap[parent];
		}
		taskHeap[hole] = task;
	}
	
	@Override
	public void run() {
		
	}

	public HeapTimerTask getTop() {
		if (isEmpty()) {
			return null;
		}
		return taskHeap[0];
	}

	public void popTimer() {
		if (isEmpty())
			return;
		if (taskHeap[0] != null) {
			taskHeap[0] = taskHeap[--currentSize];
			percolateDown(0);
		}
	}

	private void resize(final int size) {
		if (capacity != size) {
			capacity = size;
			HeapTimerTask[] tmp = new HeapTimerTask[capacity];
			System.arraycopy(taskHeap, 0, tmp, 0, currentSize);
			taskHeap = tmp;
		}
	}

	private void percolateDown(int hole) {
		HeapTimerTask tmpTask = taskHeap[hole];
		for (int child = 0; (hole * 2 + 1) <= currentSize - 1; hole = child) {
			child = hole * 2 + 1;
			if ((child < currentSize - 1) && taskHeap[child + 1].getExp() < taskHeap[child].getExp()) {
				++child;
			}
			if (taskHeap[child].getExp() < tmpTask.getExp()) {
				taskHeap[hole] = taskHeap[child];
			} else {
				break;
			}
		}
		taskHeap[hole] = tmpTask;
	}

	private boolean isEmpty() {
		if (currentSize < capacity / 4) {
			resize(capacity / 2);
		}
		return currentSize == 0;
	}

	
// test Code =========================================
	public void printfInfo() {
		System.out.println("currentSize:" + currentSize);
		System.out.println("capacity:" + capacity);
		for (HeapTimerTask task : taskHeap) {
			if (null != task)
				System.out.println("task :" + task.getExp());
		}
	}

	public static void main(String[] args) {
		final int N = 10;
		TimeHeap th = new TimeHeap(N);
		for (int i = 0; i < N; i++) {
			th.addTimer(new HeapTimerTask() {
				private int r = (int) (Math.random() * 100);

				@Override
				public void run(Function f) throws Exception {

				}

				@Override
				public int getExp() {
					return r;
				}
			});
			th.printfInfo();
		}
		System.out.println("=============================");
		for (int i = 0; i < N; i++) {
			System.out.println(th.getTop().getExp());
			th.popTimer();
		}

	}
}
