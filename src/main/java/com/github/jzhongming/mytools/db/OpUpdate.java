package com.github.jzhongming.mytools.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL更新构造器 OpUpdate.java
 * @author Alex (j.zhongming@gmail.com)
 */
public class OpUpdate extends Op {

	/**
	 * 此构造器用于业务不散表的情况<br>
	 * 
	 * @param sql
	 *            sql语句
	 * @param bizName
	 *            业务名
	 */
	public OpUpdate(String sql, String bizName) {
		this.sql = sql;
		this.bizName = bizName;
	}

	/**
	 * 若使用散表，设置此方法
	 * @param tableName 表名
	 * @param tableSuffix 后缀名
	 */
	public void setHashParam(final String tableName, final int tableSuffix) {
		this.dbTableName = tableName;
		this.tableSuffix = tableSuffix;
	}

	public void setParam(PreparedStatement ps) throws SQLException {
	}

	public Object parse(ResultSet rs) throws SQLException {
		return null;
	}

	public Object getResult() {
		return null;
	}

}