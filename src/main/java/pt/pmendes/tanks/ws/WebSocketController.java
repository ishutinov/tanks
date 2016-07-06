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
import pt.pmendes.tanks.message.MoveTankMessage;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;
    @Autowired
    private GameManager gameManager;

    @MessageMapping("/tank/move")
    public void handleTank(MoveTankMessage moveTankMessage) {
        logger.info("Incoming tank message:\n {}", moveTankMessage);
        gameManager.moveTank(moveTankMessage.getTankId(), moveTankMessage.getPosX(), moveTankMessage.getPosY());
    }

    @Scheduled(fixedRate = (1000 / 30))
    public void sendMessage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String message = mapper.writeValueAsString(gameManager.getGameFrame());
        brokerMessagingTemplate.convertAndSend("/topic/world", message);
    }
}
