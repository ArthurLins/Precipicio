package online.precipicio.benchmark;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(final WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        //System.out.println("WebSocket Client disconnected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            // web socket client connected
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            final FullHttpResponse response = (FullHttpResponse) msg;
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        final WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            final TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            final String text = ((TextWebSocketFrame) frame).text();
            // uncomment to print request

            if (text.length() < 3) {
                return;
            }

            JsonElement element = new JsonParser().parse(text);
            if(!element.isJsonArray()){
                ctx.writeAndFlush(new CloseWebSocketFrame());
                ctx.close();
                return;
            }
            int header = element.getAsJsonArray().get(0).getAsInt();



            if (!element.getAsJsonArray().get(1).isJsonObject()){
                ctx.writeAndFlush(new CloseWebSocketFrame());
                ctx.close();
                return;
            }

            JsonObject object = element.getAsJsonArray().get(1).getAsJsonObject();

            if (object == null){
                ctx.writeAndFlush(new CloseWebSocketFrame());
                ctx.close();
                return;
            }

            if (header == 7){
                final String roomId = object.get("i").getAsString();
                if (Globals.getInstance().bool.get()) {
                    Globals.getInstance().roomIds.add(roomId);
                } else {
                    //System.out.println("SKIP");
                    return;
                }
                System.out.println("ADD ROOM: "+ roomId);
                return;
            }

            if (header == 5){
                System.out.println("PLAYER TURN");
                Thread.sleep(RandomUtils.nextInt(100, 500));
                ch.writeAndFlush(new TextWebSocketFrame("[5,{\"d\":\""+ RandomUtils.nextInt(0,4)+"\"}]"));
                return;
            }

            //System.out.println(textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
        } else if (frame instanceof CloseWebSocketFrame)
            ch.close();
        else if (frame instanceof BinaryWebSocketFrame) {
            // uncomment to print request
            // logger.info(frame.content().toString());
        }

    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        cause.printStackTrace();

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }
}