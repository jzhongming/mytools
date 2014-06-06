package com.github.jzhongming.timer;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TailLogger {
	private static final Logger logger = LoggerFactory.getLogger(TailLogger.class);
	private static final TimingWheel<TailLogReader> timer = new TimingWheel<TailLogReader>(1,5,TimeUnit.SECONDS);
	
	static {
		logger.info("init timer Listener");
		timer.addExpirationListener(new TailLogExpirationListener());
		timer.start();
	}
	
	public static TailLogReader getTailLogReader(Reader in) throws IOException {
		TailLogReader tlr = new TailLogReader(in);
		timer.add(tlr);
		logger.info("add Timer TailLog");
		return tlr;
	}
	
	public static long refreshTailLogReader(TailLogReader tlr) {
		logger.info("fresh TailLog");
		return timer.add(tlr);
	}
}
