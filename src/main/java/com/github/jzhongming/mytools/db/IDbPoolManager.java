package com.github.jzhongming.mytools.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;

/**
 * 连接池管理者接口
 * @author Alex (j.zhongming@gmail.com)
 */
public interface IDbPoolManager {
	/**
	 * 获得连接
	 * @return
	 */
	public Connection getConnection() throws SQLException;
	/**
	 * 增加连接池
	 * @param p
	 * @return
	 */
	public void addDataSource(DataSource p) throws SQLException;
	/**
	 * 关闭连接池
	 */
	public void close();
	/**
	 * 设置Log框架
	 * @param logger
	 */
	public void setLogger(Logger logger);
	/**
	 * 设置是否打开Debug
	 * @param need
	 */
	public void setDebug(boolean need);
}
