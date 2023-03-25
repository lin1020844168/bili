package com.lin.bili.chat.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.lin.bili.chat.mapper.NotificationMessageMapper;
import com.lin.bili.chat.strategy.context.MessageStrategyContext;
import com.lin.bili.common.utils.TokenUtils;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.lin.bili.common.constant.JWTConstant.PAYLOAD_USERDATA;
import static com.lin.bili.common.constant.JWTConstant.USERDATA_USERID;

//处理文本协议数据，处理TextWebSocketFrame类型的数据，websocket专门处理文本的frame就是TextWebSocketFrame

/**
 * 文本处理器
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageStrategyContext messageStrategyContext;

    public WebSocketHandler() {

    }

    //用于记录和管理所有客户端
    private ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。(以后用数据库代替)
    private static ConcurrentHashMap<Long, Channel> channelMap = new ConcurrentHashMap<>();

    private final AttributeKey<String> USER_ID = AttributeKey.newInstance("userId");
    private final AttributeKey<String> TOKEN = AttributeKey.newInstance("token");

    public static ThreadLocal<ConcurrentHashMap<Long, Channel>> channelMapThreadLocal =
            ThreadLocal.withInitial(() -> channelMap);

    //读到客户端的内容并且向客户端去写内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String messageJsonStr = textWebSocketFrame.text();
        messageStrategyContext.chooseStrategy(messageJsonStr).sendMessage(messageJsonStr);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!Optional.ofNullable(msg).isPresent()|| !(msg instanceof FullHttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }
        Channel channel = ctx.channel();
        FullHttpRequest request = (FullHttpRequest) msg;
        String uri = request.uri();
        if (Optional.ofNullable(channel.attr(USER_ID).get()).isPresent() ) {
            request.setUri("/ws");
            super.channelRead(ctx, msg);
            return;
        }
        String token = StringUtils.substringAfter(uri, "/ws/");
        if (!TokenUtils.checkToken(token)) {
            channel.close();
            super.channelRead(ctx, msg);
            return;
        }
        JSONObject payloads = JWT.of(token).getPayloads();
        String userdataKey = payloads.get(PAYLOAD_USERDATA, String.class);
        if (!redisUtils.hasKey(userdataKey)) {
            channel.close();
            super.channelRead(ctx, msg);
            return;
        }
        String userdata = redisUtils.get(userdataKey);
        String userId = JSONUtil.parseObj(userdata).get(USERDATA_USERID).toString();
        channelMap.put(Long.parseLong(userId), channel);
        channel.attr(USER_ID).set(userId);
        channel.attr(TOKEN).set(token);
        clients.add(channel);
        System.out.println("userid"+userId+"已上线");
        request.setUri("/ws");
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String userId = channel.attr(USER_ID).get();
        if (channelMap.containsKey(userId)) {
            channelMap.remove(userId);
        }
        System.out.println("userId"+userId+"已下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        cause.printStackTrace();
        ctx.close();
    }

}