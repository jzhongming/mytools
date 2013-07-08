package com.github.jzhongming.mytools.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL构造的抽象类,必要的参数设置
 * @author Alex (j.zhongming@gmail.com)
 */
public abstract class Op implements IOP {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String sql;

	/** 多功能参数,不要改变这个初始值 设置使用主从库*/
	protected int multiPurposeParam = -1;
	/** 业务名 */
	protected String bizName;
	/** 数据库表名 */
	protected String dbTableName;
	/** 散表表名的后缀，如gossip_2,应该传入 2 */
	protected int tableSuffix = -1;

	public int getMultiPurposeParam() {
		return multiPurposeParam;
	}

	public String getBizName() {
		return bizName;
	}

	public int getTableSuffix() {
		return tableSuffix;
	}

	public String getTablename() {
		return dbTableName;
	}

	public final void log(Logger logger) {
		if (logger.isDebugEnabled()) {
			logger.debug(getSql());
		}
	}
	
	public final String getSql() {
		if (logger.isDebugEnabled()) {
			logger.debug("getSQL: " + sql);
		}
		return sql;
	}

}