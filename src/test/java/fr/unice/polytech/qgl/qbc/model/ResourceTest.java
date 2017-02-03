package fr.unice.polytech.qgl.qbc.model;

import javafx.util.Pair;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests pour la classe Resource
 *
 * @author Robin Alonzo et Baisangour Akhmadov
 */
public class ResourceTest {

    @Test
    public void testIsPrimary() throws Exception {
        Resource resource = Resource.FISH;
        assertEquals(true,resource.isPrimary());
        Resource resource2 = Resource.GLASS;
        assertEquals(false,resource2.isPrimary());
    }

    @Test
    public void testToResource(){
        Resource glass = Resource.toResource("GLASS");
        assertEquals(Resource.GLASS, glass);
    }

    @Test
    public void testRequiredPrimary(){
        Resource glass = Resource.toResource("GLASS");
        List<Pair<Resource,Integer>> required=glass.requiredPrimary(Resource.GLASS,3);
        assertEquals((int)required.get(0).getValue(),16);
        assertEquals((int)required.get(1).getValue(),33);

        required=glass.requiredPrimary(Resource.PLANK,1);
        assertEquals((int)required.get(0).getValue(),1);

        required=glass.requiredPrimary(Resource.PLANK,6);
        assertEquals((int)required.get(0).getValue(),2);



    }
}