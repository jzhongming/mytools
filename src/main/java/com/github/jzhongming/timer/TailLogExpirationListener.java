
package com.github.jzhongming.timer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TailLogExpirationListener implements ExpirationListener<TailLogReader>{
	private static Logger logger = LoggerFactory.getLogger(TailLogExpirationListener.class);
	@Override
	public void expired(TailLogReader tlr) {
		try {
			logger.info("close Log Reader");
			tlr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
