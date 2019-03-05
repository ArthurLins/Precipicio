package online.precipicio.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import online.precipicio.game.room.RoomManager;
import online.precipicio.game.util.StatsUtil;
import online.precipicio.websocket.codec.WebSocketServerProtocolHandler;
import online.precipicio.websocket.sessions.Session;
import online.precipicio.websocket.sessions.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private Logger logger = LoggerFactory.getLogger(WebSocketEventHandler.class.getSimpleName());

    public static final AttributeKey<Long> WS_SESSION_ID = AttributeKey.valueOf("WSSession.id");
    public static final AttributeKey<Map<String, String>> WS_SESSION_PARAMS = AttributeKey.valueOf("WSSession.params");

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete)){
            return;
        }
        final Map<String, String> params = ctx.channel().attr(WS_SESSION_PARAMS).get();
        if (params.isEmpty()){
            ctx.close();
            return;
        }

        if (params.get("username").length() > 25){
            ctx.close();
            return;
        }
        SessionManager.getInstance().addSession(ctx.channel(), params.get("username"));
        logger.info("ADD Session (Total: "+ SessionManager.getInstance().activeSessions()+")");
        StatsUtil.getInstance().addSession();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        final Long id = ctx.channel().attr(WS_SESSION_ID).get();
        if (id == null){
            return;
        }
        logger.debug("REMOVE Session");
        SessionManager.getInstance().removeSession(id);
        StatsUtil.getInstance().removeSession();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        final String text = ((TextWebSocketFrame) frame).text();

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

        final Session session = SessionManager.getInstance().getSession(ctx.channel().attr(WS_SESSION_ID).get());
        if (session == null){
            ctx.writeAndFlush(new CloseWebSocketFrame());
            ctx.close();
            return;
        }
        logger.info(text);
        WebSocketEventHandler.getInstance().handle(session, header, object);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }
}
