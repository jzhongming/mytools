package com.github.jzhongming.mytools.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClientSocket {

	private final DatagramSocket dgs;

	/**
	 * 构造函数，创建UDP客户端
	 * 
	 * @throws Exception
	 */
	public UdpClientSocket() throws Exception {
		dgs = new DatagramSocket();
	}

	/**
	 * 设置超时时间，该方法必须在bind方法之后使用.
	 * 
	 * @param timeout
	 *            超时时间
	 * @throws Exception
	 * @author
	 */
	public final void setSoTimeout(final int timeout) throws Exception {
		dgs.setSoTimeout(timeout);
	}

	/**
	 * 获得超时时间.
	 * 
	 * @return 返回超时时间
	 * @throws Exception
	 * @author
	 */
	public final int getSoTimeout() throws Exception {
		return dgs.getSoTimeout();
	}

	public final DatagramSocket getSocket() {
		return dgs;
	}

	/**
	 * 向指定的服务端发送数据信息.
	 * 
	 * @param host
	 *            服务器主机地址
	 * @param port
	 *            服务端端口
	 * @param bytes
	 *            发送的数据信息
	 * @return 返回构造后俄数据报
	 * @throws IOException
	 * @author
	 */
	public final DatagramPacket send(final String host, final int port, final byte[] bytes) throws IOException {
		DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
		dgs.send(dp);
		return dp;
	}

	/**
	 * 接收从指定的服务端发回的数据.
	 * 
	 * @param lhost
	 *            服务端主机
	 * @param lport
	 *            服务端端口
	 * @return 返回从指定的服务端发回的数据.
	 * @throws Exception
	 * @author
	 */
	public final byte[] receive(final String host, final int port) throws Exception {
		byte[] buffer = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(host), port);
		dgs.receive(dp);
		return dp.getData();
	}

	/**
	 * 关闭udp连接.
	 * 
	 * @author
	 */
	public final void close() {
		try {
			dgs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		UdpClientSocket ucs = new UdpClientSocket();
		long s = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			ucs.send("localhost", 1200, ("Hello,World! > "+i).getBytes());
		}
		System.out.println(System.currentTimeMillis() - s);
	}
}
