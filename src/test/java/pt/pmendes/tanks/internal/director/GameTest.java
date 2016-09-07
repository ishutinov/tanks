package pt.pmendes.tanks.internal.tankDirector;

import org.junit.Before;
import pt.pmendes.tanks.api.GameDirector;
import pt.pmendes.tanks.api.TankDirector;
import pt.pmendes.tanks.internal.director.TanksGameDirector;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by pmendes.
 */
public class GameTest {

    private TankDirector tankDirector;
    private GameDirector gameDirector;

    @Before
    public void setUp() throws Exception {
        tankDirector = new TanksGameDirector();
        tankDirector.addTank("Tank1");
    }

    @org.junit.Test
    public void moveTank() throws Exception {
        assertNotNull(tankDirector);
        assertEquals(1, tankDirector.getTanks().size());
        String tankId = "Tank1";
        assertNotNull(tankDirector.getTank(tankId));
    }

    @org.junit.Test
    public void addTank() throws Exception {
        assertNotNull(tankDirector);
        tankDirector.addTank("Tank2");
        assertEquals(2, tankDirector.getTanks().size());
    }

    @org.junit.Test
    public void getTank() throws Exception {
        assertNull(tankDirector.getTank("Tank-1"));
        assertNotNull(tankDirector.getTank("Tank1"));
    }

    @org.junit.Test
    public void getGameFrame() throws Exception {
        assertNotNull(gameDirector.getFrame());
    }

}
