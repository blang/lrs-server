package de.benediktlang.lrsserver;

import de.benediktlang.lrsproto.LrsProto;
import de.benediktlang.lrsserver.entity.LogMsg;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.Iterator;
import java.util.concurrent.Executors;

public class ReporterServer {
    private static final Logger LOG = Logger.getLogger(ReporterServer.class);

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server");
        LOG.debug("Starting server log");
        new ReporterServer().bootServer();
    }

    public void bootServer() throws Exception {
        final MessageStore msgStore = new MessageStore();
        LOG.debug("Boot server");
        Server server = new Server(8001);
        server.setHandler(new HTTPRequestHandler(msgStore));
        server.start();
        final SocketIOHandler socketioHandler = new SocketIOHandler(8002);
        socketioHandler.bootServer();
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                pipeline.addLast("protobufDecoder",
                        new ProtobufDecoder(LrsProto.Msg.getDefaultInstance()));
                pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast("protobufEncoder", new ProtobufEncoder());
                pipeline.addLast("handler", new ProtoHandler(socketioHandler, msgStore));
                return pipeline;
            }

            ;
        });

        bootstrap.bind(new InetSocketAddress("0.0.0.0", 8003));
        LOG.debug("Listening on 8003");
    }


    class ProtoHandler extends SimpleChannelHandler {
        private final SocketIOHandler handler;
        private final MessageStore msgStore;

        ProtoHandler(SocketIOHandler handler, MessageStore msgStore) {
            this.handler = handler;
            this.msgStore = msgStore;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            LOG.debug("Exception caught", e.getCause());
            if (e instanceof ClosedChannelException) {
                LOG.debug("Closed Channel");
            }
        }

        @Override
        public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            super.channelOpen(ctx, e);
        }

        @Override
        public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
            super.handleUpstream(ctx, e);
        }

        @Override
        public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
            super.handleDownstream(ctx, e);
        }

        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            LrsProto.Msg msg = (LrsProto.Msg) e.getMessage();
            LOG.debug("Log received ! [" + msg.getProfile() + "|" + msg.getMessageList() + "]");
            Iterator<String> iterator = msg.getMessageList().iterator();
            while (iterator.hasNext()) {
                LogMsg logMsg = new LogMsg(msg.getProfile(), iterator.next(), 0);
                handler.broadcast(logMsg);
                msgStore.add(logMsg);
            }
            super.messageReceived(ctx, e);
        }
    }
}
