package com.github.jzhongming.socket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\nIt is " + new Date() + " now.\r\n");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		String response;
		boolean close = false;
		if (msg.isEmpty()) {
			response = "Please type somthing.\r\n";
		} else if ("bye".equalsIgnoreCase(msg)) {
			response = "Have a good day!\r\n";
			ctx.close();
		} else {
			response = "say: " + msg + "?\r\n";
		}
		ChannelFuture future = ctx.write(response);

		if (close)
			future.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
