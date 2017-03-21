package com.github.jzhongming.timer;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Schedules {@link TimerTask}s for one-time future execution in a background
 * thread.
 */
public interface Timer {

	/**
	 * Schedules the specified {@link TimerTask} for one-time execution after
	 * the specified delay.
	 * 
	 * @return a handle which is associated with the specified task
	 * 
	 * @throws IllegalStateException
	 *             if this timer has been {@linkplain #stop() stopped} already
	 */
	TimeOut newTimeout(TimerTask task, long delay, TimeUnit unit);

	/**
	 * Releases all resources acquired by this {@link Timer} and cancels all
	 * tasks which were scheduled but not executed yet.
	 * 
	 * @return the handles associated with the tasks which were canceled by this
	 *         method
	 */
	Set<TimeOut> stop();
}