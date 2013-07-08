package com.github.jzhongming.mytools.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

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
public class BoneCPPoolManager implements IDbPoolManager {
	// 存放数据库管理器的哈西表.
	private static BoneCPPoolManager manager = new BoneCPPoolManager();
	// 存放数据库连接池的哈西表.
	BoneCPDataSource pools = new BoneCPDataSource();
	// 是否打印debug信息
	private static boolean isDebug = false;
	// 可用的日志句柄
	private Logger log = null;

	// 保护构造函数。
	private BoneCPPoolManager() {
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
		pools.close();
	}

	public static BoneCPPoolManager getInstance() {
		return manager;
	}

	/**
	 * 往管理器中增加一个连接池。
	 * 
	 * @param p
	 *            指定的连接池。 备注: 刚加入的连接池可以是设置了初始化参数但是未连接的.
	 * @throws SQLException
	 */
	public synchronized void addDataSource(final DataSource p) throws SQLException {
		if (null == p)
			throw new SQLException("the DataSource is null");
		
		pools = (BoneCPDataSource) p;
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
		BoneCPDataSource ds = getDataSource();
		if (ds == null) {
			throw new SQLException("DataSource不可用");
		}
		// 判断是否还连接正常
		conn = getConnection(ds);
		writeLog("bonecp 获取Connection：" + conn);
		return conn;
	}
	
	/**
	 * 获得可用的连接池。
	 * 
	 * @return 可用的连接池。
	 */
	private BoneCPDataSource getDataSource() {
		return pools;
	}

	// 从 BasicDataSource 中获得一个可用连接，屏蔽异常
	private Connection getConnection(BoneCPDataSource ds) {
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
	 * @throws SQLException
	 */
	public static BoneCPDataSource getOracleDbPool(String ip, int port, String uid, String user, String pwd) throws SQLException {
		// 装载默认的数据库连接池
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(String.format("jdbc:oracle:thin:@%s:%s:%s",ip,port,uid)); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
		config.setUsername(user);
		config.setPassword(pwd);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(2);

		return new BoneCPDataSource(config); // setup the connection pool
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
	 * @throws SQLException
	 */
	public static BoneCPDataSource getMysqlDbPool(String ip, int port, String dbname, String user, String pwd) {
		// 装载默认的数据库连接池
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// 装载默认的数据库连接池
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?characterEncoding=UTF-8&characterSetResults=UTF-8",ip,port,dbname));
		// jdbc url specific to your database, eg
		// jdbc:mysql://127.0.0.1/yourdb
		config.setUsername(user);
		config.setPassword(pwd);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(2);
		return new BoneCPDataSource(config); // setup the connection pool
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
	 * @throws SQLException
	 */
	public static BoneCPDataSource getODBCDbPool(String dsn, String user, String pwd) throws SQLException {
		// 装载默认的数据库连接池
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// 装载默认的数据库连接池
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl("jdbc:odbc:" + dsn); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
		config.setUsername(user);
		config.setPassword(pwd);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(2);
		return new BoneCPDataSource(config); // setup the connection pool
	}

}