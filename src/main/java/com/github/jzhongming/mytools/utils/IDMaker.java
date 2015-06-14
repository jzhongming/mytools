package com.github.jzhongming.mytools.utils;


/**
 * 在分布式系统中，需要生成全局UID的场合还是比较多的，twitter的snowflake解决了这种需求，实现也还是很简单的，除去配置信息，
 * 核心代码就是毫秒级时间41位+机器ID 10位+毫秒内序列12位。
 * 该项目地址为：https://github.com/twitter/snowflake是用Scala实现的。
 * 
 * 其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用：
 * (1/0)---0000000000 0000000000 0000000000 0000000000 0000000000 -- 0000 -- 0000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的50位为毫秒级时间，然后4位标识位
 * ，然后10位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 * 
 * 
 * @author j.zhongming@gmail.com 2014-3-31
 * @see
 * @since 1.0
 */
public final class IDMaker {
	private final static long workerIdBits = 4L;
	private final static long sequenceBits = 10L;

	private final long makerId;
	private final static long idepoch = 1400923280000L;
	private final static long timestampLeftShift = workerIdBits + sequenceBits;

	public final static long maxMakerId = ~(-1L << workerIdBits);
	public final static long sequenceMask = ~(-1L << sequenceBits);

	private long sequence = 0L;
	private long lastTimestamp = -1L;

	public IDMaker(final long makeId) {
		if (makeId > maxMakerId || makeId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxMakerId));
		}
		this.makerId = makeId;
	}

	public synchronized long nextId() {
		long timestamp = timeGen(); // 取当前时间
		if (this.lastTimestamp == timestamp) {// 最后时间在同一个时间单元内
			this.sequence = (++sequence) & sequenceMask;// 同一个时间单元内，Sequence不重
			if (this.sequence == 0) {// 超过一个时间单元，跳到下个时间单元
				System.out.println("###########" + sequenceMask);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new IllegalStateException(String.format(
						"Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp
								- timestamp));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - idepoch) << timestampLeftShift) | (this.makerId << sequenceBits) | (this.sequence);
		return nextId;

	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp;
		
		while ((timestamp = timeGen()) <= lastTimestamp);
		
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		System.out.println(~(-1 << 10));
		IDMaker idm = new IDMaker(10);
		for (int i = 0; i < 10; i++)
			System.out.println(idm.nextId());

		int i = 0;
		while ((++i & 1023) != 0) {

		}
		System.out.println(i);
	}

}
