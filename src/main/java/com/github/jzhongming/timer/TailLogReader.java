package com.github.jzhongming.timer;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TailLogReader extends LineNumberReader {
	
	private AtomicBoolean opened;

	public TailLogReader(Reader in) throws IOException {
		super(in);
		super.skip(Integer.MAX_VALUE);
		opened = new AtomicBoolean(true);
	}

	public String tail() throws IOException {
		StringBuilder sbd = new StringBuilder();
		if (opened.get()) {
			String ss = readLine();
			while (ss != null) {
				sbd.append(ss).append("\r\n");
				if (sbd.capacity() > 2048) {
					break;
				}
				ss = readLine();
			}
		}
		return sbd.toString();
	}
	
	

	@Override
	public void close() throws IOException {
		opened.compareAndSet(true, false);
		super.close();
	}

	public static void main(String[] args) throws Exception {
		final TailLogReader tlr = new TailLogReader(new FileReader("E:/opt/mytest/jzm.txt"));
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.print(tlr.tail());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}, 100, 1000, TimeUnit.MILLISECONDS);
	}
}