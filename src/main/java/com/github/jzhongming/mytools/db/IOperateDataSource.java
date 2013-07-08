package com.github.jzhongming.mytools.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
/**
 * 基本DB操作,可针对自己的业务实现该接口，并对连接池进行配置
 * @author Alex (j.zhongming@gmail.com)
 *@see SimpleDataAccessMgr
 */
public interface IOperateDataSource {
	/**
	 * 插入操作，并返回插入后的ID
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public int insertReturnId(OpUpdate op) throws SQLException;
	
	/**
	 * Insert 操作，返回成功标志
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(OpUpdate op) throws SQLException;
	
	/**
	 * UPdate操作，返回更新的Row数量
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public int update(OpUpdate op) throws SQLException;
	
	/**
	 * 查询是否存在
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public boolean queryExist(OpUniq<?> op) throws SQLException;
	
	/**
	 * 查询一个返回I的SQL
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public int queryId(OpUniq<?> op) throws SQLException;
	
	/**
	 * 查询一个返回Int值的SQL，例如Count计数
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public int queryInt(OpUniq<?> op) throws SQLException ;
	
	
	public long queryLong(OpUniq<?> op) throws SQLException ;
	
	/**
	 * 查询返回一个封装的对象
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public Object queryUnique(OpUniq<?> op) throws SQLException;
	
	/**
	 * 查询返回一个结果列表
	 * @param op
	 * @return
	 * @throws SQLException
	 */
	public List<?> queryList(OpList<?> op) throws SQLException ;

	/**
	 * 执行批量的sql更新
	 * @param ops 必须是OpBatchUpdate类型的列表
	 * @param bizName 业务名，必须输入 
	 * @throws SQLException
	 */
	public void insertBatch(List<OpBatchUpdate> ops) throws SQLException;

	/**
	 * 将得到的查询结果集封装在map中返回
	 * @param op
	 * @param keyFieldName 用那个字段的<b>值</b>作为map的key
	 * @return 
	 * @throws SQLException
	 */
	public Map<String ,Object> queryMap(OpMap<?, ?> op,String keyFieldName) throws SQLException;
	/**
	 * 将得到的查询结果集封装在map中返回
	 * @param op
	 * @param keyFieldNames 用多个字段的<b>值</b>联合作为map的key，字段名之间用_分隔
	 * @return
	 * @throws SQLException
	 */
	public Map<String ,Object> queryMap(OpMap<?, ?> op,String[] keyFieldNames) throws SQLException;
}