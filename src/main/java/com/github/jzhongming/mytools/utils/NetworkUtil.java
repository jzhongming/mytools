package com.github.jzhongming.mytools.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class NetworkUtil {
	private static final Log logger = LogFactory.getLog(NetworkUtil.class);
	
	public static String[] getLocalHostNames() {
		final Set<String> hostNames = new HashSet<String>();
		try {
			final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			while (ifaces.hasMoreElements()) {
				final NetworkInterface iface = ifaces.nextElement();
				final Enumeration<InetAddress> ei = iface.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					hostNames.add(ia.getCanonicalHostName());
					if(logger.isInfoEnabled())
						logger.info(iface.getName() + " : " + ia.getHostName() + " : " + ia.getHostAddress());
				}
			}
		} catch (final SocketException e) {
			throw new RuntimeException("unable to retrieve host names of localhost");
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
	 * @param netName
	 * @return
	 */
	public static String getIPV4AddressByName(final String netName) {
		try {
				NetworkInterface iface = NetworkInterface.getByName(netName);
				final Enumeration<InetAddress> ei = iface.getInetAddresses();
				while(ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					if(ia instanceof Inet4Address) {
					//	System.out.println(ipToString(ia.getAddress());
						return ia.getHostAddress();
					}
				}
		}catch (final SocketException e) {
			throw new RuntimeException("unable to retrieve host names of localhost");
		}
		return null;
	}
	
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
	 * @param netName
	 * @return
	 */
	public static String getIPV6AddressByName(final String netName) {
		try {
				NetworkInterface iface = NetworkInterface.getByName(netName);
				final Enumeration<InetAddress> ei = iface.getInetAddresses();
				while(ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					if(ia instanceof Inet6Address) {
						return ia.getHostAddress();
					}
				}
		}catch (final SocketException e) {
			throw new RuntimeException("unable to retrieve host names of localhost");
		}
		return null;
	}
	
//	public static void main(String[] args) {
//		String[] hosts = getLocalHostNames();
//		for(String s : hosts) {
//			System.out.println(s);
//		}
//		
//		System.out.println(getLocalhostName());
//		System.out.println(getIPV6AddressByName("eth3"));
//		System.out.println(getIPV4AddressByName("eth3"));
//	}
}
