package com.github.jzhongming.mytools.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * SQL构造Map类型的结果集
 * @author Alex (j.zhongming@gmail.com)
 * 
 */
public class OpMap<K, V> extends Op{
	
	private Map<K, V> map;

	/**
	 * 此构造器用于业务不散表的情况<br>
	 * @param sql sql语句
	 * @param bizName 业务名
	 */
	public OpMap(String sql,String bizName) {
		this.sql = sql;
		this.bizName = bizName;
		map = new HashMap<K, V>();
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
	
	public final Map<K, V> getResult() {
		return map;
	}
	public void setParam(PreparedStatement ps) throws SQLException {
	}
	public Object parse(ResultSet rs) throws SQLException {
		return null;
	}
	
	public final void add(K keyFieldName ,V ob) {
		map.put(keyFieldName,ob);
	}

}
