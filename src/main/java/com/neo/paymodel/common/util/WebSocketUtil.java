package com.neo.paymodel.common.util;

import com.dsgame.common.other.entity.ws.WsConsumeEntity;
import com.dsgame.common.other.entity.ws.WsGainEntity;
import com.dsgame.common.other.entity.ws.WsMessageEntity;
import com.dsgame.common.redis.WebSocketConfigRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ClientEndpoint
@Component
public class WebSocketUtil {
    public static final Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);
    public static Session session;

    @Autowired
    public WebSocketConfigRedis webSocketConfigRedis;

    public WebSocketUtil() {

    }

    public void init() {
        logger.info("[WEBSOCKET]init...");
        if ( session == null || !session.isOpen() ) {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://" + webSocketConfigRedis.getWsConfigUrl() + ":" + webSocketConfigRedis.getWsConfigPort();
            try {
                session = container.connectToServer(WebSocketUtil.class, URI.create(uri));
            } catch (DeploymentException e) {
                e.printStackTrace();
                logger.error("[WEBSOCKET]init异常，错误信息：{}", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("[WEBSOCKET]init异常，错误信息：{}", e.getMessage());
            }
        }
    }

    /**
     * 发送充值同步消息
     * "{\"type\": 177,\"user_id\": 30054,\"game_id\": 0,\"server_id\": 0,\"recharge_type\": 1,\"recharge_count\": 10000}
     *
     * @param czMessage
     */
    public boolean sendMessage(String message) {
        try {//发送文本消息
            if (session == null || !session.isOpen()) {
                init();
            }
            session.getBasicRemote().sendText(message);
            logger.info("[WEBSOCKET-SEND]消息发送完成");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[WEBSOCKET-SEND]发送消息异常，错误信息：{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 关闭
     */
    public static void close() {
        try {
            session.close();
            logger.info("[WEBSOCKET-CLOSE]关闭session");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("[WEBSOCKET-CLOSE]关闭session异常，错误信息：{}", e.getMessage());
        }
    }

    public static WsMessageEntity getConsumeMessageObj(int user_id, int op_type, int type, String fag_id, long number, String desp) {
        WsMessageEntity wsMessage = new WsMessageEntity();
        wsMessage.setUser_id(user_id);
        wsMessage.setOpType(op_type);
        wsMessage.setFag_id(fag_id);
        wsMessage.setDescription(desp);
        wsMessage.setGame_id(0);
        wsMessage.setServer_id(0);
        //
        List<WsConsumeEntity> list = new ArrayList<WsConsumeEntity>();
        WsConsumeEntity wsConsume = new WsConsumeEntity();
        wsConsume.setType(type);
        wsConsume.setNumber(number);
        list.add(wsConsume);
        wsMessage.setWsConsumeList(list);
        //
        return wsMessage;
    }

    public static WsMessageEntity getPayMessageObj(int user_id, int op_type, int type, String fag_id, long number, String desp) {
        WsMessageEntity wsMessage = new WsMessageEntity();
        wsMessage.setUser_id(user_id);
        wsMessage.setOpType(op_type);
        wsMessage.setFag_id(fag_id);
        wsMessage.setDescription(desp);
        //
        List<WsGainEntity> list = new ArrayList<WsGainEntity>();
        WsGainEntity wsGain = new WsGainEntity();
        wsGain.setType(type);
        wsGain.setNumber(number);
        list.add(wsGain);
        wsMessage.setWsGainList(list);
        return wsMessage;
    }
}
