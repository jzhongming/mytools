package com.github.jzhongming.mytools.db;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.jzhongming.mytools.utils.ResourcesUtils;

/**
 * 连接器计数管理类,简单包装一下， 可供dbConn和iceConn等其他服务使用
 * @author Alex (j.zhongming@gmail.com)
 * 
 */
public class ConnCounterMgr {
	// 计数器容器
	private static Map<String, ConnCounter> counters = new ConcurrentHashMap<String, ConnCounter>();
	private static final String COUNTERMGR_OPEN = "COUNTERMGR_OPEN";

	private ConnCounterMgr() {

	}

	// 是否使用计数服务,默认为false
	private static boolean used;

	public static boolean isUsed() {
		return used;
	}

	static {
		try {
			final Properties p = null;// ResourcesUtil.getResourceAsProperties("config.properties");

			final String value = p.getProperty(COUNTERMGR_OPEN);
			if (null != value) {
				ConnCounterMgr.used = (value.trim().equalsIgnoreCase("1") || value.trim().equalsIgnoreCase("true")) ? true : false;
				System.out.println(">>> init ConnCounterService: isUsed:" + ConnCounterMgr.used);
			}
		} catch (final Exception e) {// 可以无该文件,使用默认值即可
		}

		try {//单线程池，每个小时将计数器清零
			final ScheduledExecutorService svc = Executors.newSingleThreadScheduledExecutor();
			svc.scheduleAtFixedRate(new Runnable() {
				public void run() {
					try {
						for (final Entry<String, ConnCounter> entry : counters.entrySet()) {
							entry.getValue().setCounter(0);
						}
						System.out.println(">>> the Excutors set Counters to Zero running...");
					} catch (final Throwable e) {
						e.printStackTrace();
					}
				}
			}, 1, 1, TimeUnit.HOURS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 进入计数器
	 * 
	 * @param name
	 * @return boolean true可用，false不可用
	 */
	public static boolean enter(String name) {
		if (!used) {
			return true;
		}

		ConnCounter conter = counters.get(name);
		if (conter != null) {
			return conter.enter();
		} else {// 如果没有则新加一个:::这里性能问题可以忽略，因为只有第一次初始化e：））））
			synchronized (counters) {  // 双锁机制
				conter = counters.get(name);
				if (conter != null)
					return conter.enter();

				conter = new ConnCounter();
				conter.setName(name);
				conter.enter();
				counters.put(name, conter);
				return true;
			}
		}
	}

	/**
	 * 退出计数器,自减
	 * 
	 * @param name
	 */
	public static void outer(String name) {
		if (!used) {
			return;
		}
		final ConnCounter conter = counters.get(name);
		if (conter != null) {
			conter.outer();
		} else {
			new IllegalArgumentException(name).printStackTrace(System.err);
		}
	}

	/**
	 * 获得计算器容器
	 * @return Map
	 */
	public static Map<String, ConnCounter> getCounters() {
		return counters;
	}

	/**
	 * 获取某个计数器
	 * 
	 * @param name
	 * @return ConnCounter
	 */
	public static ConnCounter get(String name) {
		return counters.get(name);
	}
}
