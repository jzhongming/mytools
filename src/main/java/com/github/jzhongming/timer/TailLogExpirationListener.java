
package com.github.jzhongming.timer;

import java.io.IOException;

import org.apache.log4j.Logger;



public class TailLogExpirationListener implements ExpirationListener<TailLogReader>{
	private static Logger logger = Logger.getLogger(TailLogExpirationListener.class);
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
