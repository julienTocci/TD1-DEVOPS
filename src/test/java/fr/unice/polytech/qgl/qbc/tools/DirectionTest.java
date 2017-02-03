package fr.unice.polytech.qgl.qbc.tools;

import fr.unice.polytech.qgl.qbc.model.map.Direction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Realisation des tests des methodes de la classe direction
 *
 * @author Cyprien Levy et Robin Alonzo
 */
public class DirectionTest  {

    @Test
    public void testToDirection(){
        assertEquals(Direction.NORTH,Direction.toDirection("N"));
        assertEquals(Direction.SOUTH,Direction.toDirection("S"));
        assertEquals(Direction.EAST,Direction.toDirection("E"));
        assertEquals(Direction.WEST,Direction.toDirection("W"));
    }

    @Test
    public void testGetDeltaX(){
        Direction directionN = Direction.toDirection("N");
        assertEquals(directionN.getDeltaX(),0);
        Direction directionS = Direction.toDirection("S");
        assertEquals(directionS.getDeltaX(),0);
        Direction directionW = Direction.toDirection("W");
        assertEquals(directionW.getDeltaX(),-1);
        Direction directionE = Direction.toDirection("E");
        assertEquals(directionE.getDeltaX(),1);

    }

    @Test
    public void testGetDeltaY(){
        Direction directionN = Direction.toDirection("N");
        assertEquals(directionN.getDeltaY(),1);
        Direction directionS = Direction.toDirection("S");
        assertEquals(directionS.getDeltaY(),-1);
        Direction directionW = Direction.toDirection("W");
        assertEquals(directionW.getDeltaY(),0);
        Direction directionE = Direction.toDirection("E");
        assertEquals(directionE.getDeltaY(),0);

    }

    @Test
    public void testInvert(){
        Direction direction = Direction.toDirection("N");
        assertEquals(direction.invert().toString(),"S");
        assertEquals(direction.invert().invert().toString(),"N");
        Direction direction2 = Direction.toDirection("W");
        assertEquals(direction2.invert().toString(),"E");
        assertEquals(direction2.invert().invert().toString(),"W");
    }

    @Test
    public void testTurnLeft(){
        Direction direction = Direction.toDirection("N");
        assertEquals(direction.turnLeft().toString(), "W");
        assertEquals(direction.turnLeft().turnLeft().toString(), "S");
        assertEquals(direction.turnLeft().turnLeft().turnLeft().toString(), "E");
        assertEquals(direction.turnLeft().turnLeft().turnLeft().turnLeft().toString(), "N");
    }

    @Test
    public void testTurnRight(){
        Direction direction = Direction.toDirection("N");
        assertEquals(direction.turnRight().toString(),"E");
        assertEquals(direction.turnRight().turnRight().toString(),"S");
        assertEquals(direction.turnRight().turnRight().turnRight().toString(),"W");
        assertEquals(direction.turnRight().turnRight().turnRight().turnRight().toString(),"N");
    }
}