package com.github.jzhongming.socket;

import com.github.jzhongming.socket.impl.WindowData;

/**
 * 处理类接口
 * @author Alex
 *
 */
public interface CCHandler {
	/**
	 * <p>
	 * 当有消息到达时被触发，此消息是经由协议处理者解析后的消息类型，注意，当没有设置消息处理器时此方法 是不会被触发的
	 * </p>
	 * <br>
	 * 
	 * @param session
	 */
	public void messageReceived(CCSession session, WindowData msg);

	/**
	 * <p>
	 * 当有消息到达时被解发，此消息是没有经过协义处理者的ByteBuffer类型，对数据的解的需要实现者自已实现
	 * 注意，当设置了协议处理器后，此方法是不会被触发的。
	 * </p>
	 * <br>
	 * 
	 * @param session
	 * @param buffer
	 */
	public void messageReceived(CCSession session, byte[] msgdata);

	/**
	 * <p>
	 * CCSession被正式关闭时触发执行(由于关闭是异常的，也就是不管CCSession是成功关闭 还是在关闭时抛出了异常都会被触发
	 * </p>
	 * <br>
	 * 
	 * @param session
	 */
	public void ioSessionClosed(CCFuture future);

}
