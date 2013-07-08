package com.github.jzhongming.mytools.db;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleDataAccessMgr管理器，通过它可以实现db访问，进行db操作<br>
 * 简单数据访问管理器
 * @author Alex (j.zhongming@gmail.com)
 */
public class SimpleDataAccessMgr implements IOperateDataSource {

	private static final String CMD_BATCH = "batch"; //批量操作

	private static final String CMD_SQL = "sql"; //普通SQL操作

	private static final String LOG_TIP = "数据库堵塞异常:\t %s"; 

	private static SimpleDataAccessMgr instance = new SimpleDataAccessMgr(); //单例

	private static IDbPoolManager pool = null;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SimpleDataAccessMgr() {
		super();
	}
	
	/**
	 * 获得数据库连接管理者
	 * @param manager 设置连接池
	 * 注意对应的jar包就放到Classpath中
	 * @see ApacheDBPoolManager,BoneCPDBPoolManager,DBPoolManager
	 * @return
	 */
	public static SimpleDataAccessMgr build(final IDbPoolManager mgr) {
		pool = mgr;
		return instance;
	}

	/**
	 * 关闭Connection
	 * 
	 * @param con
	 */
	public void closeConnection(final Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("con close", e);
		}
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param rs
	 */
	public void closeResultSet(final ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("rs close", e);
		}
	}

	/**
	 * 关闭所有DB操作
	 * 
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public void closeRSC(final ResultSet rs, final Statement st, final Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("rsc close", e);
		}
	}

	/**
	 * 关闭Statement
	 * 
	 * @param st
	 */
	public void closeStatement(final Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("prestatement  close", e);
		}
	}

	/**
	 * 在调用getConn方法的时候没有指定r，或w，或者是没有得到一个connection对象 会抛出异常
	 * 
	 * @param o
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	private Connection getConn(final Op op) throws SQLException {
		if (op.getBizName() == null || op.getBizName().length() == 0) {
			throw new SQLException("Op中 BizName 不能为null");
		}

		Connection conn = null;
		try {// 这里逻辑 写死了，没有做读写分开
			conn = pool.getConnection();
		} catch (Exception e) {
			logger.error("-----取["+op.getBizName()+"]Conn时出异常\r\n", e);
			throw new SQLException(e);
		}

		return conn;
	}

	/**
	 * 插入数据 ，返回Boolean是否成功
	 */
	public boolean insert(final OpUpdate op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			long begin = 0x0L;

			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				int count = ps.executeUpdate();
				return (count > 0);
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	/**
	 * 插入数据 ，返回更新数据的ID
	 */
	public int insertReturnId(final OpUpdate op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				int count = ps.executeUpdate();
				if (count > 0) {
					ps = conn.prepareStatement("select last_insert_id();");
					rs = ps.executeQuery();
					return (rs.next()) ? rs.getInt(1) : 0;
				}
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
			return 0;
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	public int[] batchUpdate(final OpUpdate op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				return ps.executeBatch();
			} catch (BatchUpdateException bue) {
				logger.error("batchUpdate Error: ", bue);
				throw new SQLException("batchUpdate Error \n the bad id is " + bue.getUpdateCounts());
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				batchEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	/**
	 * 批量更新操作
	 */
	public void insertBatch(final List<OpBatchUpdate> ops) throws SQLException {
		if (ops == null || ops.size() == 0) {
			throw new SQLException(" ----- the opbatchupdate list can't null -------------");
		}
		OpBatchUpdate firstOp = ops.get(0);
		if (firstOp.bizName == null || firstOp.bizName.trim().length() == 0) {
			throw new SQLException(" ----- the bizName of the first opbatchupdate object can't null -------------");
		}
		if (ConnCounterMgr.enter(firstOp.getBizName())) {
			PreparedStatement ps = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				int tmpcount = 0;
				conn = getConn(firstOp);
				conn.setAutoCommit(false);
				ps = conn.prepareStatement(firstOp.getSql());
				for (OpBatchUpdate opb : ops) {
					opb.setParam(ps);
					ps.addBatch();
					if ((++tmpcount) % 500 == 0) { // 每五百个一组
						ps.executeBatch();
					}
				}
				ps.executeBatch();
			} catch (BatchUpdateException be) {
				logger.error(">>> the batchSQL Exception " + be.getUpdateCounts());
			} finally {
				try {
					if (null != conn) {
						conn.commit();
					}
				} catch (Exception e) {
					logger.error(">>> conn Commit Exception", e);
				}
				ConnCounterMgr.outer(firstOp.getBizName());
				closeRSC(null, ps, conn);
				batchEnd(begin, firstOp);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(firstOp.getBizName())));
		}
	}

	public boolean queryExist(final OpUniq<?> op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				return rs.next();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	/**
	 * 执行查寻SQL，返回SQL的查询第一条数据的ID
	 */
	public int queryId(final OpUniq<?> op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				int result = 0;
				if (rs.next()) {
					result = rs.getInt(1);
				} else {
					return 0;
				}
				if (rs.next()) {
					logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!\r\n" + op.getSql());
				}
				return result;
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	/**
	 * 执行查寻SQL，返回第一条数据的数字，一般对查询Count()函数使用
	 */
	public int queryInt(final OpUniq<?> op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				int result = 0;
				if (rs.next()) {
					result = rs.getInt(1);
				} else {
					return 0;
				}
				if (rs.next()) {
					logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!\r\n" + op.getSql());
				}
				return result;
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> queryList(final OpList op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {

			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				while (rs.next()) {
					op.add(op.parse(rs));
				}
				return op.getResult();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	
	public long queryLong(final OpUniq<?> op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);// 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				long result = 0;
				if (rs.next()) {
					result = rs.getLong(1);
				} else {
					return 0;
				}
				if (rs.next()) {
					logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!\r\n"+op.getSql());
				}
				return result;
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryMap(@SuppressWarnings("rawtypes") final OpMap op, final String keyFieldName) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				while (rs.next()) {
					op.add(rs.getString(keyFieldName), op.parse(rs));
				}
				return op.getResult();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> queryMap(final OpMap op, final String[] keyFieldNames) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				int len = keyFieldNames.length;
				while (rs.next()) {
					StringBuilder key = new StringBuilder();
					for (int i = 0; i < len; i++) {
						key.append(rs.getString(keyFieldNames[i]));
						if (i != (len - 1)) {
							key.append("_");
						}
					}
					op.add(key.toString(), op.parse(rs));
				}
				return op.getResult();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	public Object queryUnique(final OpUniq op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op); // 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				rs = ps.executeQuery();
				if (rs.next()) {
					op.add(op.parse(rs));
				}
				if (rs.next()) {
					logger.error("----------【error sql】----------------\r\nNon Unique Result Error: wrong sql syntax or database not consistence!");
					logger.error("wrong sql is:".concat(op.getSql()));
					logger.error("wrong ps is:" + ps);
				}
				return op.getResult();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	public int update(final OpUpdate op) throws SQLException {
		if (ConnCounterMgr.enter(op.getBizName())) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			Connection conn = null;
			long begin = 0x0L;
			try {
				begin = sqlBegin(); // 记录SQL执行的开始时间
				conn = getConn(op);// 从不同的数据源获得connection
				ps = conn.prepareStatement(op.getSql());
				op.setParam(ps);
				return ps.executeUpdate();
			} finally {
				ConnCounterMgr.outer(op.getBizName()); // 退出计时器
				closeRSC(rs, ps, conn);
				sqlEnd(begin, op);
			}
		} else {
			throw new SQLException(String.format(LOG_TIP, ConnCounterMgr.get(op.getBizName())));
		}
	}

	private long sqlBegin() {
		// 记录SQL执行的开始时间
		return (logger.isDebugEnabled()) ? System.currentTimeMillis() : 0;
	}

	/**
	 * @param op
	 * @param begin
	 *            是BatchSql执行的开始时间 单位是毫秒
	 */
	protected void batchEnd(final long begin, final Op op) {
		log(CMD_BATCH, begin, op.getBizName(), op.getSql());
	}

	/**
	 * @param op
	 *            是一个op对象
	 * @param begin
	 *            begin是BatchSql执行的开始时间 单位是毫秒
	 */
	protected void sqlEnd(final long begin, final Op op) {
		log(CMD_SQL, begin, op.getBizName(), op.getSql());
	}
	
	/**
	 * @param cmd
	 *            标识是处理单条SQL还是批处理 标识符分别为 sql和batch
	 * @param begin
	 *            是Sql执行的开始时间 单位是毫秒
	 * @param infos
	 *            是要打印出来的内容对于batch而言 ，为空
	 */
	protected void log(final String cmd, final long begin, String... infos) {
		if (begin > 0 && logger.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			long cost = end - begin;
			StringBuilder logBuilder = new StringBuilder();
			logBuilder.append("|").append(cmd.trim());
			logBuilder.append("|").append(cost);
			logBuilder.append("|").append(end);

			for (String str : infos) {
				logBuilder.append("|").append(str.trim());
			}
			
			logBuilder.append("|");
			logger.debug(logBuilder.toString());
		}
	}
}
