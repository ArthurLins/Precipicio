package online.precipicio.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.util.ResourceLeakDetector;
import online.precipicio.websocket.codec.WebSocketServerProtocolHandler;

import java.net.InetSocketAddress;


public class WebSocketServer {

    private static WebSocketServer ourInstance = new WebSocketServer();
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final boolean isEpollEnabled;
    private final boolean isEpollAvailable;

    public WebSocketServer() {

        System.setProperty("io.netty.leakDetectionLevel", "disabled");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        this.serverBootstrap = new ServerBootstrap();

        this.isEpollEnabled = true;
        this.isEpollAvailable = Epoll.isAvailable();
        final int defaultThreadCount = 20;


        if (isEpollAvailable && isEpollEnabled) {
            //log.info("WS Epoll is enabled");
            this.bossGroup = new EpollEventLoopGroup(defaultThreadCount);
            this.workerGroup = new EpollEventLoopGroup(defaultThreadCount);
        } else {
            if (isEpollAvailable) {
                //log.info("WS Epoll is available but not enabled");
            } else {
                //log.info("WS Epoll is not available");
            }
            this.bossGroup = new NioEventLoopGroup(defaultThreadCount);
            this.workerGroup = new NioEventLoopGroup(defaultThreadCount);
        }
    }

    public static WebSocketServer getInstance() {
        return ourInstance;
    }

    public void initialize() {
        final int port = 8081;
        this.serverBootstrap.group(bossGroup, workerGroup);
        this.serverBootstrap.channel(isEpollAvailable && isEpollEnabled ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 2000);
        this.serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel socketChannel) {
                final ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec())
                        .addLast(new HttpObjectAggregator(65536))
                        .addLast(new WebSocketServerProtocolHandler())
                        .addLast(new WebSocketServerCompressionHandler())
                        .addLast(new WebSocketServerHandler());

            }
        });
        this.serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        this.serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //this.serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
        try {
            this.serverBootstrap.bind(new InetSocketAddress("127.0.0.1", port));

            //log.info("CometServer WS listening on port: " + port);
        } catch (Exception e) {
            //log.error(e);
            //Comet.exit("Error on start Websocket on port: " + port);
        }
    }
}
