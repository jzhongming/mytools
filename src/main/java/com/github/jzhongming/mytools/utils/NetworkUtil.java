package com.github.jzhongming.mytools.utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NetworkUtil {
	private static final Log logger = LogFactory.getLog(NetworkUtil.class);

	public static String[] getLocalHostNames() {
		final Set<String> hostNames = new HashSet<String>();
		try {
			final Enumeration<NetworkInterface> ifaces = NetworkInterface
					.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				final NetworkInterface iface = ifaces.nextElement();
				final Enumeration<InetAddress> ei = iface.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					hostNames.add(ia.getCanonicalHostName());
					if (logger.isInfoEnabled())
						logger.info(iface.getName() + " : " + ia.getHostName()
								+ " : " + ia.getHostAddress());
				}
			}
		} catch (final SocketException e) {
			throw new RuntimeException(
					"unable to retrieve host names of localhost");
		}

		return hostNames.toArray(new String[hostNames.size()]);
	}

	public static String getLocalhostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			throw new RuntimeException("unable to retrieve localhost name");
		}
	}

	/**
	 * 获得网卡对应ipV4地址
	 * 
	 * @param netName
	 * @return
	 */
	public static String getIPV4AddressByName(final String netName) {
		try {
			NetworkInterface iface = NetworkInterface.getByName(netName);
			final Enumeration<InetAddress> ei = iface.getInetAddresses();
			while (ei.hasMoreElements()) {
				InetAddress ia = ei.nextElement();
				if (ia instanceof Inet4Address) {
					// System.out.println(ipToString(ia.getAddress());
					return ia.getHostAddress();
				}
			}
		} catch (final SocketException e) {
				
		}
		return null;
	}

	/**
	 * 将网络地址转成IP地址
	 * 
	 * @param bytes
	 * @return
	 */
	public static String ipToString(final byte[] bytes) {
		final StringBuffer addrStr = new StringBuffer();
		for (int cnt = 0; cnt < bytes.length; cnt++) {
			final int uByte = bytes[cnt] < 0 ? bytes[cnt] + 256 : bytes[cnt];
			addrStr.append(uByte);
			if (cnt < 3)
				addrStr.append('.');
		}
		return addrStr.toString();
	}

	/**
	 * 获得网卡对应ipV6地址
	 * 
	 * @param netName
	 * @return
	 */
	public static String getIPV6AddressByName(final String netName) {
		try {
			NetworkInterface iface = NetworkInterface.getByName(netName);
			final Enumeration<InetAddress> ei = iface.getInetAddresses();
			while (ei.hasMoreElements()) {
				InetAddress ia = ei.nextElement();
				if (ia instanceof Inet6Address) {
					return ia.getHostAddress();
				}
			}
		} catch (final SocketException e) {
			throw new RuntimeException(
					"unable to retrieve host names of localhost");
		}
		return null;
	}
	
	/**
	 * 通过域名获得IP地址
	 * @param www
	 * @return
	 */
	public static List<String> getAddressByDNS(final String www) {
		List<String> list = new ArrayList<String>();
		try {
			InetAddress[] addrs = InetAddress.getAllByName(www);
			for(InetAddress addr : addrs) {
				list.add(ipToString(addr.getAddress()));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("unable to retrieve host names of " + www);
		}
		return list;
	}
	
	/**
	 * 指定IP地址是否可达
	 * @param ip
	 * @param timeout
	 * @return
	 * @throws UnknownHostException
	 */
	public static boolean isAddressAvailable(String ip, int timeout) throws UnknownHostException {
		try {
			if (InetAddress.getByName(ip).isReachable(timeout)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 指定本地IP到远程地址端口是否可达
	 * @param localInetAddr
	 * @param remoteInetAddr
	 * @param port
	 * @param timeout
	 * @return
	 */
	public static boolean isReachable(InetAddress localInetAddr, InetAddress remoteInetAddr,
			int port, int timeout) {

		Socket socket = null;
		try {
			socket = new Socket();
			// 端口号设置为 0 表示在本地挑选一个可用端口进行连接
			SocketAddress localSocketAddr = new InetSocketAddress(localInetAddr, 0);
			socket.bind(localSocketAddr);
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(remoteInetAddr, port);
			socket.connect(endpointSocketAddr, timeout);
			logger.info("SUCCESS - connection established! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port: " + port);
			return true;
		} catch (IOException e) {
			logger.error("FAILRE - Can not connect! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port: " + port);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					logger.error("Error occurred while closing socket..");
				}
			}
		}
		return false;
	}

	public static void main(String[] args) throws UnknownHostException {
		String[] hosts = getLocalHostNames();
		for (String s : hosts) {
			System.out.println(s);
		}

		System.out.println(getLocalhostName());
		System.out.println(getIPV6AddressByName("eth3"));
		System.out.println(getIPV4AddressByName("eth3"));
		
		boolean b = isAddressAvailable("127.0.0.1", 5000);
		System.out.println(b);
		
		b = isReachable(InetAddress.getByName("127.0.0.1"), InetAddress.getByName("127.0.0.1"), 49155, 5000);
		System.out.println(b);
		
		
		System.out.println("=======================================");
		List<String> addrlist = getAddressByDNS("www.sina.com");
		for(String s : addrlist)
			System.out.println(s);
	}
}
