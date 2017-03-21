package com.github.jzhongming.timer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashedWheelTimerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(HashedWheelTimerTest.class);
	class MyTask implements TimerTask {
		
		private String info;
		public MyTask() {
			
		}
		public MyTask(final String info) {
			this.info = info;
		}

		@Override
		public void run(TimeOut timeout) throws Exception {
			logger.info(info);
		}
		
		@Override
		public String toString() {
			return info;
		}
		
	}
	
	
	@Test
	public void testScheduleTimeOutShouldNotRunBeforeDelay() throws InterruptedException {
		final Timer timer = new HashedWheelTimer();
		final CountDownLatch barrier = new CountDownLatch(1);
		final TimeOut TimeOut = timer.newTimeout(new TimerTask() {
			@Override
			public void run(TimeOut TimeOut) throws Exception {
				fail("This should not have run");
				barrier.countDown();
			}
		}, 10, TimeUnit.SECONDS);
		assertFalse(barrier.await(3, TimeUnit.SECONDS));
		assertFalse("timer should not expire", TimeOut.isExpired());
		timer.stop();
	}

	@Test
	public void testScheduleTimeOutShouldRunAfterDelay() throws InterruptedException {
		final Timer timer = new HashedWheelTimer();
		final CountDownLatch barrier = new CountDownLatch(1);
		final TimeOut TimeOut = timer.newTimeout(new TimerTask() {
			@Override
			public void run(TimeOut TimeOut) throws Exception {
				barrier.countDown();
			}
		}, 2, TimeUnit.SECONDS);
		assertTrue(barrier.await(3, TimeUnit.SECONDS));
		assertTrue("timer should expire", TimeOut.isExpired());
		timer.stop();
	}

	@Test
	public void testStopTimer() throws InterruptedException {
		final Timer timerProcessed = new HashedWheelTimer();
		for (int i = 0; i < 3; i++) {
			timerProcessed.newTimeout(new MyTask("run here now ..."), 1, TimeUnit.MILLISECONDS);
		}
		Thread.sleep(1000L); // sleep for a second
		assertEquals("Number of unprocessed TimeOuts should be 0", 0, timerProcessed.stop().size());

		final Timer timerUnprocessed = new HashedWheelTimer();
		for (int i = 0; i < 5; i++) {
			timerUnprocessed.newTimeout(new MyTask("测试Task ..."+i), 100*i, TimeUnit.MILLISECONDS);
		}
		timerUnprocessed.newTimeout(new MyTask("这个不能输出出来 ..."), 1, TimeUnit.SECONDS);
		Thread.sleep(1000L); // sleep for a second
		assertEquals("Number of unprocessed TimeOuts should be 1", 1, timerUnprocessed.stop().size());
	}

	@Test(expected = IllegalStateException.class)
	public void testTimerShouldThrowExceptionAfterShutdownFornewTimeouts() throws InterruptedException {
		final Timer timer = new HashedWheelTimer();
		for (int i = 0; i < 3; i++) {
			timer.newTimeout(new TimerTask() {
				@Override
				public void run(TimeOut TimeOut) throws Exception {
				}
			}, 1, TimeUnit.MILLISECONDS);
		}

		timer.stop();
//		Thread.sleep(1000L); // sleep for a second

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(TimeOut TimeOut) throws Exception {
				fail("This should not run");
			}
		}, 1, TimeUnit.SECONDS);
	}

	@Test
	public void testTimerOverflowWheelLength() throws InterruptedException {
		final HashedWheelTimer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 100,
				TimeUnit.MILLISECONDS, 32);
		final AtomicInteger counter = new AtomicInteger();

		timer.newTimeout(new TimerTask() {
			@Override
			public void run(final TimeOut TimeOut) throws Exception {
				counter.incrementAndGet();
				timer.newTimeout(this, 1, TimeUnit.SECONDS);
			}
		}, 1, TimeUnit.SECONDS);
		Thread.sleep(3500);
		assertEquals(3, counter.get());
	}
}
