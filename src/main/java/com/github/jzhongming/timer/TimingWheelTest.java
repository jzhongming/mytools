
package com.github.jzhongming.timer;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;




public class TimingWheelTest {

	public static void main(String[] args) throws Exception {
		final TailLogReader tlr = TailLogger.getTailLogReader(new FileReader("E:/opt/mytest/jzm.txt"));
		final AtomicInteger A = new AtomicInteger();
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.print(tlr.tail());
					if(A.incrementAndGet() < 10)
						TailLogger.refreshTailLogReader(tlr);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}, 100, 1000, TimeUnit.MILLISECONDS);
	}
}
