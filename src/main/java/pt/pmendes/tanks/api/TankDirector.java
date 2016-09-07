package pt.pmendes.tanks.api;

import pt.pmendes.tanks.internal.entities.Tank;

import java.util.Collection;

/**
 * Created by pssm on 07/09/16.
 */
public interface TankDirector {
    Tank moveTankForward(String tankId);

    Tank moveTankBackward(String tankId);

    Tank rotateTankRight(String tankId);

    Tank rotateTankLeft(String tankId);

    Tank rotateTankTurretRight(String tankId);

    Tank rotateTankTurretLeft(String tankId);

    Tank addTank(String tankId);

    void fireBullet(String tankId);

    void updateGameFrame();

    Tank getTank(String tankId);

    Collection<Tank> getTanks();
}
