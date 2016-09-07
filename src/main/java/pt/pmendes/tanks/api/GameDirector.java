package pt.pmendes.tanks.api;

import pt.pmendes.tanks.internal.entities.Frame;
import pt.pmendes.tanks.internal.entities.Message;

/**
 * Created by pssm on 07/09/16.
 */
public interface GameDirector {

    void init(int canvasWidth, int canvasHeight);

    void reset();

    Frame getFrame();

    void addGameMessage(Message message);

}
