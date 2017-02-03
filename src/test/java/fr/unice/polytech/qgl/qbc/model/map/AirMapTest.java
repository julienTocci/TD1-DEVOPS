package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.air.AirMap;
import fr.unice.polytech.qgl.qbc.model.map.air.EchoStatus;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Classe de test pour la classe AirMap
 *
 * @author Robin Alonzo
 */
public class AirMapTest {
    static Logger logger = Logger.getLogger(AirMapTest.class);

    @Test
    public void testSetEcho() {
        Drone drone = new Drone("W");
        AirMap map = new AirMap(drone);
        map.setEcho(Direction.toDirection("W"),10,"OUT_OF_RANGE");
        assertEquals(EchoStatus.OCEAN, map.get(drone.getX(), drone.getY()).getEcho());
        assertEquals(EchoStatus.OCEAN, map.get(drone.getX()-1, drone.getY()).getEcho());
        assertEquals(EchoStatus.OCEAN, map.get(drone.getX()-10, drone.getY()).getEcho());
        assertEquals(EchoStatus.OUT_OF_RANGE, map.get(drone.getX()-11, drone.getY()).getEcho());
        assertEquals(EchoStatus.OUT_OF_RANGE, map.get(drone.getX()-11, drone.getY()-1).getEcho());
        map.setEcho(Direction.toDirection("N"),3,"OUT_OF_RANGE");
        assertEquals(EchoStatus.OUT_OF_RANGE, map.get(drone.getX(), drone.getY()+4).getEcho());
        assertEquals(EchoStatus.OUT_OF_RANGE, map.get(drone.getX()+1, drone.getY()+4).getEcho());
        assertEquals(EchoStatus.UNKNOWN, map.get(drone.getX()-2, drone.getY()+2).getEcho());
        map.setEcho(Direction.toDirection("S"),4,"GROUND");
        assertEquals(EchoStatus.GROUND, map.get(drone.getX(), drone.getY() - 5).getEcho());
        assertEquals(EchoStatus.UNKNOWN, map.get(drone.getX(), drone.getY() - 6).getEcho());

        Drone drone2 = new Drone("E");
        AirMap map2 = new AirMap(drone2);
        map2.setEcho(Direction.EAST,0,"GROUND");
        assertEquals(EchoStatus.UNKNOWN,map2.get(drone2.getX() + 1, drone2.getY()).getEcho());
        assertEquals(EchoStatus.MAYBE_GROUND,map2.get().getEcho());
        map2.setOcean(true);
        map2.setEcho(Direction.EAST,0,"GROUND");
        assertEquals(EchoStatus.GROUND,map2.get(drone2.getX() + 1, drone2.getY()).getEcho());
    }

    @Test
    public void testNeedEcho(){
        Drone drone = new Drone("N");
        AirMap map = new AirMap(drone);
        assertTrue(map.needEcho(Direction.toDirection("N")));
        map.setEcho(Direction.toDirection("N"),3,"OUT_OF_RANGE");
        assertFalse(map.needEcho(Direction.toDirection("N")));
    }

    @Test
    public void testGetLimits(){
        Drone drone = new Drone("S");
        AirMap map = new AirMap(drone);
        assertEquals(1,map.getLimit(Direction.NORTH));
        assertEquals(Integer.MIN_VALUE, map.getLimit(Direction.SOUTH));
        map.setEcho(Direction.WEST,5,"OUT_OF_RANGE");
        assertEquals(-6,map.getLimit(Direction.WEST));
        assertEquals(Integer.MAX_VALUE,map.getLimit(Direction.EAST));
    }

    @Test
    public void testSetOcean(){
        Drone drone = new Drone("N");
        AirMap map = new AirMap(drone);
        assertEquals(EchoStatus.UNKNOWN,map.get().getEcho());
        map.setOcean(true);
        assertEquals(EchoStatus.OCEAN, map.get().getEcho());
        map.setOcean(false);
        assertEquals(EchoStatus.GROUND, map.get().getEcho());
    }

    @Test
    public void testGetClosestUnknown(){
        Drone drone = new Drone("N");
        AirMap map = new AirMap(drone);
        map.setEcho(Direction.NORTH,5,"OUT_OF_RANGE");
        map.setEcho(Direction.EAST,5,"OUT_OF_RANGE");
        map.setEcho(Direction.WEST,0,"OUT_OF_RANGE");
        Action action = map.findClosestUnknown();
        assertEquals(Action.ActionName.HEADING,action.getAction());
        assertEquals("E",action.getStringParameter(Action.ParameterName.DIRECTION));
    }

    @Test
    public void testNeedTurn(){
        Drone drone = new Drone("E");
        AirMap map= new AirMap(drone);
        map.setEcho(Direction.EAST,5,"GROUND");
        assertFalse(map.needTurn());
        map.setEcho(Direction.EAST,5,"OUT_OF_RANGE");
        assertTrue(map.needTurn());

        drone.heading("S");
        assertFalse(map.needTurn());
        map.setEcho(Direction.SOUTH,5,"OUT_OF_RANGE");
        assertTrue(map.needTurn());
    }

    @Test
    public void testGetTurn(){
        Drone drone = new Drone("E");
        AirMap map= new AirMap(drone);
        map.setEcho(Direction.NORTH,10,"OUT_OF_RANGE");
        map.setEcho(Direction.SOUTH,10,"OUT_OF_RANGE");
        map.setEcho(Direction.EAST,15,"OUT_OF_RANGE");
        map.setEcho(Direction.WEST,15,"OUT_OF_RANGE");

        //Premier test, on se déplace à l'est
        //La distance entre le drone et la limite ouest de la map est plus grande
        //que celle entre le drone et la limite est
        drone.fly();drone.fly();
        drone.heading("S");
        assertEquals(map.getTurn(),Direction.WEST);

        //Deuxième test, on se déplace au sud
        //La distance entre le drone et la limite nord de la map est plus grande
        //que celle entre le drone et la limite sud
        drone.fly();drone.fly();
        drone.heading("E");
        assertEquals(map.getTurn(),Direction.NORTH);

        //Troisième test, on se déplace à l'ouest
        //La distance entre le drone et la limite est de la map est plus grande
        //que celle entre le drone et la limite ouest
        drone.heading("W");
        drone.fly();drone.fly();drone.fly();
        drone.heading("S");
        assertEquals(map.getTurn(),Direction.EAST);

    }


}