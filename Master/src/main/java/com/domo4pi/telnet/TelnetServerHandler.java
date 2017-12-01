package com.domo4pi.telnet;

import com.domo4pi.command.CommandManager;
import com.domo4pi.command.exceptions.CommandNotFound;
import com.domo4pi.command.exceptions.InvalidCommand;
import com.domo4pi.application.Application;
import com.domo4pi.telnet.properties.TelnetProperties;
import com.domo4pi.telnet.properties.TelnetProperty;
import com.domo4pi.utils.inject.Inject;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    private static final int MAX_TRIES = 3;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AtomicBoolean logged = new AtomicBoolean(false);
    private final AtomicInteger tries = new AtomicInteger(0);

    private final TelnetProperties telnetProperties;
    private final CommandManager commandManager;

    @Inject
    public TelnetServerHandler(TelnetProperties telnetProperties, CommandManager commandManager) {
        this.telnetProperties = telnetProperties;
        this.commandManager = commandManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("Welcome to " + Application.getInstance().getName() + "!\r\n");
        ctx.write(new Date() + "\r\n");
        ctx.write("Password: ");
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String response = null;
        boolean close = false;

        if (logged.get()) {
            if (request.equalsIgnoreCase("quit") || request.equalsIgnoreCase("exit")) {
                close = true;
            } else {
                try {
                    response = commandManager.executeCommand("admin", request);
                } catch (InvalidCommand e) {
                    response = "Invalid Command: " + e.getMessage();
                } catch (CommandNotFound e) {
                    response = "Command not found";
                }
                response += "\r\n> ";
            }
        } else {
            if (telnetProperties.getString(TelnetProperty.Password).equals(request)) {
                log.info("Successfully telnet logged from: {}", ctx.channel().remoteAddress());
                logged.set(true);
                tries.set(0);
                response = "> ";
            } else if (tries.addAndGet(1) >= MAX_TRIES) {
                log.info("Max invalid telnet login reached from: {}", ctx.channel().remoteAddress());
                response = "Invalid password\r\n";
                close = true;
            } else {
                log.info("Invalid telnet login from: {}", ctx.channel().remoteAddress());
                response = "Invalid password\r\nPassword: ";
            }
        }

        if (response != null) {
            ctx.write(response);
        }

        if (close) {
            ChannelFuture future = ctx.write("Bye!\r\n");
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Error in telnet channel", cause);
        ctx.close();
    }
}