package pt.pmendes.tanks.manager;

import org.junit.Before;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pmendes.
 */
public class GameManagerTest {

    private GameManager manager;

    @Before
    public void setUp() throws Exception {
        manager = new GameManager();
        manager.addTank("Tank1");
    }

    @org.junit.Test
    public void moveTank() throws Exception {
        assertNotNull(manager);
        assertEquals(1, manager.getGameFrame().getTanks().size());
        String tankId = "Tank1";
        assertNotNull(manager.getTank(tankId));
//        int posX = manager.getTank(tankId).getPosX();
//        int posY = manager.getTank(tankId).getPosY();
//        manager.moveTank(tankId, 2, 4);
//        int newPosX = manager.getTank(tankId).getPosX();
//        int newPosY = manager.getTank(tankId).getPosY();
//        assertEquals(newPosX, posX + 10);
//        assertEquals(newPosY, posY + 10);
    }

    @org.junit.Test
    public void addTank() throws Exception {
        assertNotNull(manager);
        manager.addTank("Tank2");
        assertEquals(2, manager.getGameFrame().getTanks().size());
    }

    @org.junit.Test
    public void getTank() throws Exception {
        assertNull(manager.getTank("Tank-1"));
        assertNotNull(manager.getTank("Tank1"));
    }

    @org.junit.Test
    public void getGameFrame() throws Exception {
        assertNotNull(manager.getGameFrame());
    }

}