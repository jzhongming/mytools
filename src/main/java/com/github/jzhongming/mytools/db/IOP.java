package com.github.jzhongming.mytools.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL组件接口类,主要用于组件行为操作
 * <br>设置参数，组装结果集，获得结果
 * @author alex (j.zhongming@gmail.com)
 *
 */
public interface IOP {
	
	/**
	 * 设置PreparedStatement
	 * @param ps
	 * @throws SQLException
	 */
	public void setParam(final PreparedStatement ps) throws SQLException;

	/**
	 * 组装参数
	 * @param rs
	 * @return 
	 * @return
	 * @throws SQLException
	 */
	public Object parse(final ResultSet rs) throws SQLException;
	
	/**
	 * 获得结果
	 * @return
	 */
	public Object getResult();
}
