package pt.pmendes.tanks.ws;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class TanksWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(TanksWebSocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info(toString(), "Received a message...", message);
    }

}