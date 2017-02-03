package fr.unice.polytech.qgl.qbc.model;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test des methodes de la classe Drone
 *
 * @author Cyprien Levy
 */
public class DroneTest{

    @Test
    public void testFly(){
        Drone droneN = new Drone("N");
        droneN.fly();
        assertEquals(droneN.getX(),0);
        assertEquals(droneN.getY(),1);
        Drone droneW = new Drone("W");
        droneW.fly();
        assertEquals(droneW.getX(),-1);
        assertEquals(droneW.getY(),0);
        Drone droneS = new Drone("S");
        droneS.fly();
        assertEquals(droneS.getX(),0);
        assertEquals(droneS.getY(),-1);
        Drone droneE = new Drone("E");
        droneE.fly();
        assertEquals(droneE.getX(),1);
        assertEquals(droneE.getY(),0);
    }

    @Test
    public void testGetX(){
        Drone droneN = new Drone("N");
        assertEquals(droneN.getX(),0);
        droneN.fly();
        assertEquals(droneN.getX(),0);
        Drone droneW = new Drone("W");
        assertEquals(droneW.getX(),0);
        droneW.fly();
        assertEquals(droneW.getX(),-1);
        Drone droneS = new Drone("S");
        assertEquals(droneS.getX(),0);
        droneS.fly();
        assertEquals(droneS.getX(),0);
        Drone droneE = new Drone("E");
        assertEquals(droneE.getX(),0);
        droneE.fly();
        assertEquals(droneE.getX(),1);

    }

    @Test
    public void testGetY(){
        Drone droneN = new Drone("N");
        assertEquals(droneN.getY(),0);
        droneN.fly();
        assertEquals(droneN.getY(),1);
        Drone droneW = new Drone("W");
        assertEquals(droneW.getY(),0);
        droneW.fly();
        assertEquals(droneW.getY(),0);
        Drone droneS = new Drone("S");
        assertEquals(droneS.getY(),0);
        droneS.fly();
        assertEquals(droneS.getY(),-1);
        Drone droneE = new Drone("E");
        assertEquals(droneE.getY(),0);
        droneE.fly();
        assertEquals(droneE.getY(),0);
    }

    @Test
    public void headingTest(){
        Drone droneN1= new Drone("N");
        droneN1.heading("W");
        assertEquals(droneN1.getX(),-1);
        assertEquals(droneN1.getY(),1);
        Drone droneN2= new Drone("N");
        droneN2.heading("E");
        assertEquals(droneN2.getX(),1);
        assertEquals(droneN2.getY(),1);
        Drone droneE1= new Drone("E");
        droneE1.heading("N");
        assertEquals(droneE1.getX(),1);
        assertEquals(droneE1.getY(),1);
        Drone droneE2= new Drone("E");
        droneE2.heading("S");
        assertEquals(droneE2.getX(),1);
        assertEquals(droneE2.getY(),-1);
        Drone droneS1= new Drone("S");
        droneS1.heading("W");
        assertEquals(droneS1.getX(),-1);
        assertEquals(droneS1.getY(),-1);
        Drone droneS2= new Drone("S");
        droneS2.heading("E");
        assertEquals(droneS2.getX(),1);
        assertEquals(droneS2.getY(),-1);
        Drone droneW1= new Drone("W");
        droneW1.heading("N");
        assertEquals(droneW1.getX(),-1);
        assertEquals(droneW1.getY(),1);
        Drone droneW2= new Drone("W");
        droneW2.heading("S");
        assertEquals(droneW2.getX(),-1);
        assertEquals(droneW2.getY(),-1);

    }
}