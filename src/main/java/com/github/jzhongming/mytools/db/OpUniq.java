package com.github.jzhongming.mytools.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL查询构造器
 * 
 * OpUniq.java
 * 
 * @author Alex (j.zhongming@gmail.com)
 */
public class OpUniq<T extends Object> extends Op {
	public T result;

	/**
	 * 此构造器用于业务不散表的情况<br>
	 * 
	 * @param sql
	 *            sql语句
	 * @param bizName
	 *            业务名
	 */
	public OpUniq(String sql, String bizName) {
		this.sql = sql;
		this.bizName = bizName;
		result = null;
	}

	public void setParam(PreparedStatement ps) throws SQLException {
		assert (ps != null);
	}

	public T parse(ResultSet rs) throws SQLException {
		assert (rs != null);
		return null;
	}

	public final T getResult() {
		return result;
	}

	public final void add(T ob) {
		result = ob;
	}

}