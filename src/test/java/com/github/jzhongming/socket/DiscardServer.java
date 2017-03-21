package com.github.jzhongming.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
	private int port;

	public DiscardServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup boss = new NioEventLoopGroup(1);
		EventLoopGroup worker = new NioEventLoopGroup(2);
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new TelnetServerInitializer(null)).option(ChannelOption.SO_BACKLOG, 64)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = sb.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new DiscardServer(port).run();
	}
}
