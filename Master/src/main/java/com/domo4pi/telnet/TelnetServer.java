package com.domo4pi.telnet;

import com.domo4pi.telnet.properties.TelnetProperties;
import com.domo4pi.telnet.properties.TelnetProperty;
import com.domo4pi.utils.inject.Inject;
import com.domo4pi.utils.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TelnetServer {

    @Inject
    private TelnetProperties telnetProperties;

    @Inject
    private Injector injector;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start() throws Exception {
          try {
              ServerBootstrap b = new ServerBootstrap();
              b.group(bossGroup, workerGroup)
               .channel(NioServerSocketChannel.class)
               .handler(new LoggingHandler(LogLevel.INFO))
               .childHandler(new TelnetServerInitializer(injector));

              b.bind(telnetProperties.getInteger(TelnetProperty.Port)).sync().channel().closeFuture().sync();
          } finally {
              bossGroup.shutdownGracefully();
              workerGroup.shutdownGracefully();
          }
    }

    public void stop() {
    }
}