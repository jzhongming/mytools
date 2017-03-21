package com.github.jzhongming.socket.impl;


public interface DispatcherEventListener {
	/**
	 * <p>
	 * 选择键被Cancel时触发
	 * </p>
	 * <br>
	 * @param key
	 */
	public void onRemoveSession(CCSessionImpl session);
	
	
	/**
	 * <p>
	 * 注册IO会话时
	 * </p>
	 * <br>
	 * @param session
	 */
	public void onRegisterSession(CCSessionImpl session);
}
