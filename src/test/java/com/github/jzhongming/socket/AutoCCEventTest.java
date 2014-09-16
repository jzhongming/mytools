package com.github.jzhongming.socket;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.github.jzhongming.socket.impl.InformCCEvent;

public class AutoCCEventTest {
	@Test
	public void testSet() throws Exception {
		final CCEvent event = new InformCCEvent();
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(InformCCEvent.class.getName()).log(Level.SEVERE, null, ex);
				}
				event.inform();
				System.out.println("send set signal!");
			}
		});
		th.run();
		System.out.println("start wait!");
		event.waitforMilliSeconds(10000L);
		System.out.println("end wait!");
	}

	@Test
	public void testWaitOne() throws Exception {
		final InformCCEvent event = new InformCCEvent();
		System.out.println("stime:" + System.currentTimeMillis());
		event.waitforMilliSeconds(10000L);
		event.inform();
		System.out.println("rtime:" + System.currentTimeMillis());
	}
}
