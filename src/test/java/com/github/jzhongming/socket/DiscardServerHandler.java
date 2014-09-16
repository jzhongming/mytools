package com.github.jzhongming.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("registered " + ctx.name());
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("Active " + ctx.name());
		final ByteBuf time = ctx.alloc().buffer(4); // (2)  
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));  
          
        final ChannelFuture f = ctx.writeAndFlush("aaaa"); // (3)  
        f.addListener(new ChannelFutureListener() {  
            @Override  
            public void operationComplete(ChannelFuture future) {  
                assert f == future;  
                ctx.close();  
            }  
        });
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Inactive " + ctx.name());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("ReadComplete " + ctx.name());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		try {
			while (in.isReadable()) { // (1)
				System.out.print((char) in.readByte());
				System.out.flush();
			}
		} finally {
			ReferenceCountUtil.release(msg); // (2)
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
