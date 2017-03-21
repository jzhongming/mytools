package com.github.jzhongming.mytools.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;

/**
 * DataSource 的管理器.
 * 
 * 功能：管理DataSource连接池 范围：DBBean 使用该类获得可用的连接，而不再关注连接细节。 <br>
 * 模式：有限多例模式. <br>
 * 简单系统中,可以用getInstance()获得默认的管理器实例. <br>
 * 复杂系统中,用 getInstance( <name>) 获得识明实例，第二次执行同样的语句，将不再重新产生新实例 <br>
 * <br>
 * 备注：通过 getConnection()获得的连接，一定要记得释放 <br>
 * Demo： <br>
 * // 初始化连接池 <br>
 * BasicDataSource p = new BasicDataSource(); <br>
 * <br>
 * // 设置连接池参数，并不需要连接。 <br>
 * p.setDriverClassName("com.microsoft.jdbc.sqlserver.SQLServerDriver"); <br>
 * p.setUrl("jdbc:microsoft:sqlserver://localhost:1433;databaseName=ludb"); <br>
 * p.setUsername("admin"); <br>
 * p.setPassword("admin"); <br>
 * <br>
 * // 把连接池交给管理器 <br>
 * DBPoolManager.getInstance().addDataSource(p); <br>
 * <br>
 * // 使用DBBean <br>
 * DBBean bean = new DBBean(); <br>
 * <br>
 * boolean succ = bean.execQuerySQL("select count(*) from tabs"); <br>
 * assertTrue(succ);
 */
public class DBCPPoolManager implements IDbPoolManager {
	// 存放数据库管理器的哈西表.
	private static DBCPPoolManager manager = new DBCPPoolManager();
	// 数据库连接池.
	private BasicDataSource pools = null;
	// 是否打印debug信息
	private static boolean isDebug = false;
	// 可用的日志句柄
	private Logger log = null;

	// 保护构造函数。
	private DBCPPoolManager() {
	}

	/**
	 * 设置日志句柄
	 * 
	 * @param logger
	 *            可用的Apache日志句柄，设置以后，自动记录日志。
	 */
	public void setLogger(Logger logger) {
		log = logger;
	}

	/**
	 * 设置是否打印debug信息，如果打印，所有执行的脚本和切换连接的痕迹都出现在console上
	 * 
	 * @param need
	 */
	public void setDebug(boolean need) {
		isDebug = need;
	}

	/**
	 * 写日志
	 * 
	 * @param info
	 *            日志内容
	 */
	public void writeLog(String info) {
		if (isDebug) {
			System.out.println(info);
		}
		if (log != null && log.isInfoEnabled()) {
			log.info(info);
		}
	}

	public Logger getLog() {
		return log;
	}

	/**
	 * 写日志
	 * 
	 * @param info
	 *            日志内容
	 */
	public void writeLog(String info, java.lang.Throwable ex) {
		if (isDebug) {
			System.out.println(info);
			ex.printStackTrace();
		}
		if (log != null && log.isInfoEnabled()) {
			log.info(info, ex);
		}
	}

	/**
	 * 关闭数据库连接 释放掉所有注册过来的连接,关闭全部已经初始化的connection
	 */
	public void close() {
		try {
			pools.close();
		} catch (SQLException e) {
			writeLog("Error close BasicDataSource " + e.getMessage(), e);
		}
	}

	/**
	 * 单例，获得连接池管理者
	 * 
	 * @return 默认的管理器实例
	 */
	public static DBCPPoolManager getInstance() {
		return manager;
	}
	
	@Override
	/**
	 * 往管理器中增加一个连接池。
	 * 
	 * @param p
	 *            指定的连接池。 备注: 刚加入的连接池可以是设置了初始化参数但是未连接的.
	 */
	public synchronized void addDataSource(final DataSource p) throws SQLException {
		if (null == p)
			throw new SQLException("the DataSource is null");

		pools = (BasicDataSource) p;
		writeLog("增加连接池：" + p);
	}

	/**
	 * 从连接池中获得一个可用的连接
	 * 
	 * @return 如果可以获得，返回可用连接；不可以获得就返回null
	 * 
	 *         备注：获得的连接使用完毕，请记得关闭。 connection.close();
	 * @throws SQLException
	 */
	public synchronized Connection getConnection() throws SQLException {
		Connection conn = null;

		// 判断当前的 BasicDataSource 是否可用
		BasicDataSource ds = getDataSource();
		if (ds == null) {
			throw new SQLException("DataSource不可用");
		}
		// 判断是否还连接正常
		conn = getConnection(ds);
		writeLog("dbcp 获取Connection："+conn);
		return conn;
	}
	
	/**
	 * 获得可用的连接池。
	 * 
	 * @return 可用的连接池。
	 */
	private BasicDataSource getDataSource() {
		return pools;
	}
	
	// 从 BasicDataSource 中获得一个可用连接，屏蔽异常
	private Connection getConnection(BasicDataSource ds) {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			this.writeLog("ERROR in getConnection", e);
		}
		return null;
	}

	/**
	 * 初始化Oracle数据库的连接
	 * 
	 * @param ip
	 *            Oracle所在的IP地址
	 * @param port
	 *            端口，通常是1521
	 * @param uid
	 *            全局数据库标示
	 * @param user
	 *            登陆用户名
	 * @param pwd
	 *            登陆密码
	 * @return 是否初始化成功
	 */
	public static BasicDataSource getOracleDbPool(String ip, int port, String uid, String user, String pwd) {
		// 装载默认的数据库连接池

		BasicDataSource defaultDbPool = new BasicDataSource();

		defaultDbPool.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		defaultDbPool.setUrl(String.format("jdbc:oracle:thin:@%s:%s:%s", ip,port,uid));
		defaultDbPool.setUsername(user);
		defaultDbPool.setPassword(pwd);
		defaultDbPool.setMaxActive(20);
		defaultDbPool.setMaxIdle(8);
		defaultDbPool.setMinIdle(2);

		return defaultDbPool;
	}

	/**
	 * 初始化MySQL数据库的连接
	 * 
	 * @param ip
	 *            MySQL所在的IP地址
	 * @param port
	 *            端口，通常是3306
	 * @param uid
	 *            全局数据库标示
	 * @param user
	 *            登陆用户名
	 * @param pwd
	 *            登陆密码
	 * @return 是否初始化成功
	 */
	public static BasicDataSource getMysqlDbPool(String ip, int port, String dbname, String user, String pwd) {
		// 装载默认的数据库连接池

		BasicDataSource defaultDbPool = new BasicDataSource();
		defaultDbPool.setDriverClassName("com.mysql.jdbc.Driver");
		defaultDbPool.setUrl(String.format("jdbc:mysql://%s:%s/%s?characterEncoding=utf-8&characterSetResults=utf-8", ip, port,dbname));
		defaultDbPool.setUsername(user);
		defaultDbPool.setPassword(pwd);
		defaultDbPool.setMaxActive(20);
		defaultDbPool.setMaxIdle(8);
		defaultDbPool.setMinIdle(2);

		return defaultDbPool;
	}

	/**
	 * 获得一个ＯＤＢＣ连接
	 * 
	 * @param dsn
	 *            　ODBC的数据源名称
	 * @param user
	 *            　登陆用户名
	 * @param pwd
	 *            登陆密码
	 * @return 创建成功，返回可用的DataSource
	 */
	public static BasicDataSource getODBCDbPool(String dsn, String user, String pwd) {
		// 装载默认的数据库连接池
		BasicDataSource defaultDbPool = new BasicDataSource();

		defaultDbPool.setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
		defaultDbPool.setUrl(String.format("jdbc:odbc:%s", dsn));
		defaultDbPool.setUsername(user);
		defaultDbPool.setPassword(pwd);
		defaultDbPool.setMaxActive(20);
		defaultDbPool.setMaxIdle(8);
		defaultDbPool.setMinIdle(2);

		return defaultDbPool;
	}
}