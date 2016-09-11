package pt.pmendes.tanks.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import pt.pmendes.tanks.api.GameDirector;
import pt.pmendes.tanks.api.TankDirector;
import pt.pmendes.tanks.internal.entities.Message;
import pt.pmendes.tanks.ws.message.FireBulletMessage;
import pt.pmendes.tanks.ws.message.MoveTankMessage;
import pt.pmendes.tanks.ws.message.RotateTankTurretMessage;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by pmendes.
 */
@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;
    @Autowired
    private TankDirector tankDirector;
    @Autowired
    private GameDirector gameDirector;

    @MessageMapping("/tank/move/forward")
    public void handleTankMoveForward(MoveTankMessage moveTankMessage) {
        logger.debug(toString(), "Moving tank forward: " + moveTankMessage.getTankId());
        tankDirector.moveTankForward(moveTankMessage.getTankId());
    }

    @MessageMapping("/tank/stop")
    public void handleTankStop(MoveTankMessage moveTankMessage) {
        logger.debug(toString(), "Stopping tank forward: " + moveTankMessage.getTankId());
        tankDirector.stopTank(moveTankMessage.getTankId());
    }

    @MessageMapping("/tank/move/backward")
    public void handleTankMoveBackward(MoveTankMessage moveTankMessage) {
        logger.debug(toString(), "Moving tank backward: " + moveTankMessage.getTankId());
        tankDirector.moveTankBackward(moveTankMessage.getTankId());
    }

    @MessageMapping("/tank/rotate/right")
    public void handleTankRotateRight(MoveTankMessage moveTankMessage) {
        logger.debug(toString(), "Rotating tank: " + moveTankMessage.getTankId());
        tankDirector.rotateTankRight(moveTankMessage.getTankId());
    }


    @MessageMapping("/tank/rotate/left")
    public void handleTankRotateLeft(MoveTankMessage moveTankMessage) {
        logger.debug(toString(), "Rotating tank: " + moveTankMessage.getTankId());
        tankDirector.rotateTankLeft(moveTankMessage.getTankId());
    }

    @MessageMapping("/tank/turret/rotate/right")
    public void handleTankTurretRotationRight(RotateTankTurretMessage rotateTankTurretMessage) {
        logger.debug(toString(), "Rotating tank turret: " + rotateTankTurretMessage.getTankId());
        tankDirector.rotateTankTurretRight(rotateTankTurretMessage.getTankId());
    }

    @MessageMapping("/tank/turret/rotate/left")
    public void handleTankTurretRotationLeft(RotateTankTurretMessage rotateTankTurretMessage) {
        logger.debug(toString(), "Rotating tank turret: " + rotateTankTurretMessage.getTankId());
        tankDirector.rotateTankTurretLeft(rotateTankTurretMessage.getTankId());
    }

    @MessageMapping("/tank/fire")
    public void handleFireBullet(FireBulletMessage fireBulletMessage) throws InterruptedException {
        logger.debug(toString(), "Firing bullet from tank: " + fireBulletMessage.getTankId());
        tankDirector.fireBullet(fireBulletMessage.getTankId());
    }

    @MessageMapping("/message")
    public void handleIncomingMessage(Message message) {
        logger.debug(toString(), "Received a new game message");
        gameDirector.addGameMessage(message);
    }

    @Scheduled(fixedRate = (1000 / 30))
    public void sendMessage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        tankDirector.updateGameFrame();
        String message = mapper.writeValueAsString(gameDirector.getFrame());
        brokerMessagingTemplate.convertAndSend("/topic/world", message);
    }
}
