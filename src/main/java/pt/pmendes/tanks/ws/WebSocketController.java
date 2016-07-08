package pt.pmendes.tanks.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import pt.pmendes.tanks.manager.GameManager;
import pt.pmendes.tanks.model.GameMessage;
import pt.pmendes.tanks.ws.message.FireBulletMessage;
import pt.pmendes.tanks.ws.message.MoveTankMessage;

/**
 * Created by pmendes.
 */
@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;
    @Autowired
    private GameManager gameManager;

    @MessageMapping("/tank/move")
    public void handleTankMove(MoveTankMessage moveTankMessage) {
        logger.info("Incoming tank message:\n {}", moveTankMessage);
        gameManager.moveTank(moveTankMessage.getTankId(), moveTankMessage.getSpeed(), moveTankMessage.getRotation());
    }

    @MessageMapping("/tank/fire")
    public void handleFireBullet(FireBulletMessage fireBulletMessage) {
        logger.info("Incoming fire bullet message:\n {}", fireBulletMessage);
        gameManager.fireBullet(fireBulletMessage.getTankId());
    }

    @MessageMapping("/message")
    public void handleIncomingMessage(GameMessage gameMessage) {
        gameManager.addGameMessage(gameMessage);
    }

    @Scheduled(fixedRate = (1000 / 30))
    public void sendMessage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //TODO: move this out of here
        gameManager.updateGameFrame();
        String message = mapper.writeValueAsString(gameManager.getGameFrame());
        brokerMessagingTemplate.convertAndSend("/topic/world", message);
    }
}
