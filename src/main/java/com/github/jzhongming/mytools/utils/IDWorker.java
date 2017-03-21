package com.github.jzhongming.mytools.utils;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

/**
 * 在分布式系统中，需要生成全局UID的场合还是比较多的，twitter的snowflake解决了这种需求，实现也还是很简单的，除去配置信息，
 * 核心代码就是毫秒级时间41位 + 数据中心ID5位 + 机器ID5位 + 毫秒内序列12位。
 * 该项目地址为：https://github.com/twitter/snowflake是用Scala实现的。
 * 
 * 其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用： 
 * (1/0)---0000000000 0000000000 0000000000 0000000000 0 -- 00000 -- 00000 -- 000000000000 
 * 标志位（1位） --- 时间序列（41位）-- 数据中心标记（5位）-- 机器标记（5位）-- 毫秒内序列（12位）
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），加起来64位，为一个Long型。
 * 
 * 
 * @author j.zhongming@gmail.com 2014-3-31
 * @see
 * @since 1.0
 */
public class IDWorker {
	private final static long idepoch = 1454256000975L;

	private final static long datacenterIdBits = 5L;
	private final static long workerIdBits = 5L;
	private final static long sequenceBits = 12L;

	private final static long workerIdShift = sequenceBits;
	private final static long datacenterIdShift = sequenceBits + workerIdBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	private final static long maxDatacenterId = ~(-1L << datacenterIdBits);
	private final static long maxWorkerId = ~(-1L << workerIdBits);
	private final static long sequenceMask = ~(-1L << sequenceBits);

	private long lastTimestamp = -1L;
	private long sequence = 0L;
	private final long workerId, datacenterId;

	public IDWorker(final long datacenterId, final long workerId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		System.out.println(String.format("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d", timestampLeftShift, datacenterIdBits,
				workerIdBits, sequenceBits, workerId));
	}

	public long getWorkerId() {
		return this.workerId;
	}

	public long getDatacenterId() {
		return this.datacenterId;
	}

	public long getId() {
		return nextId();
	}

	protected synchronized long nextId() {
		long timestamp = timeGen(); // 取当前时间

		if (timestamp < lastTimestamp) {
			System.out.println(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
			throw new IllegalStateException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", (lastTimestamp - timestamp)));
		}

		if (lastTimestamp == timestamp) {
			sequence = (++sequence) & sequenceMask;//性能点
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = timestamp;
		return ((timestamp - idepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;

	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp;
		while ((timestamp = timeGen()) <= lastTimestamp);
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) throws InterruptedException {
//		System.out.println(~(-1 << 10));
//		final IDWorker idm = new IDWorker(4, 0);
//		
		System.out.println(maxDatacenterId);
		
		
//		final CountDownLatch cdl = new CountDownLatch(4);
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < 4; i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					for (int i = 0; i < 1000000; i++)
//						idm.getId();
//
//					cdl.countDown();
//				}
//			}, "work_" + i).start();
//		}
//		cdl.await();
//		System.out.println(System.currentTimeMillis() - start);
	}
}
