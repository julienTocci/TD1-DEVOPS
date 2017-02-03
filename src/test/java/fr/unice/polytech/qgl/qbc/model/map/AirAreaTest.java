package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.map.air.AirArea;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by Akh on 12/03/2016.
 */
public class AirAreaTest {

    @Test
    public void testNeedScan(){
        AirArea area=new AirArea();
        assertTrue(area.needScan());
        area.setScan();
        assertFalse(area.needScan());
    }

}
