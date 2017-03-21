package com.github.jzhongming.socket;

import com.github.jzhongming.socket.impl.WindowData;

/**
 * 网络会话，表示一个通讯连接
 * 
 * @author Alex
 * 
 */
public interface CCSession {
	/**
	 * <p>
	 * 获取SessionId
	 * </p>
	 * <br>
	 * 
	 * @return
	 */
	public long getId();

	/**
	 * <p>
	 * 添加属性
	 * </p>
	 * <br>
	 * 
	 * @param key
	 * @param obj
	 */
	public void addAttribute(String key, Object value);

	/**
	 * <p>
	 * 获取属性
	 * </p>
	 * <br>
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);

	/**
	 * <p>
	 * 移除属性
	 * </p>
	 * <br>
	 * 
	 * @param key
	 * @return
	 */
	public Object removeAttribute(String key);

	/**
	 * <p>
	 * 是否关闭
	 * </p>
	 * <br>
	 * 
	 * @return
	 */
	public boolean isClosed();

	/**
	 * <p>
	 * 是否正在关闭
	 * </p>
	 * <br>
	 * 
	 * @return
	 */
	public boolean isCloseing();
	/**
	 * <p>
	 * 异步关闭
	 * </p>
	 * <br>
	 * @return
	 */
	public CCFuture close();
	/**
	 * <p>
	 * 获取现在能取到的消息数量
	 * </p>
	 * <br>
	 * @return
	 */
	public int available();
	/**
	 * <p>
	 * 异步连接
	 * </p>
	 * <br>
	 * @return
	 */
	public CCFuture connect();
	/**
	 * <p>
	 * 指读取，不阻塞
	 * </p>
	 * <br>
	 * @param msgs
	 */
	public void read(WindowData[] msgs);
	/**
	 * <p>
	 * 读取消息，此方法不会产生阻塞，当没有消息可读时立即返回null
	 * </p>
	 * <br>
	 * @return
	 */
	public WindowData read();
	/**
	 * <p>
	 * 异步写消息
	 * </p>
	 * <br>
	 * @param msg
	 * @return
	 */
	public CCFuture write(WindowData msg);
}
