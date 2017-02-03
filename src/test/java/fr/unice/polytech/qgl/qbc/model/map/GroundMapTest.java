package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.ground.GroundMap;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.junit.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Description
 *
 * @author Robin Alonzo
 */
public class GroundMapTest {
    static Logger logger = Logger.getLogger(GroundMapTest.class);
    @Test
    public void testNeedExploit() throws Exception {
        GroundMap map = new GroundMap(1);
        JSONArray json = new JSONArray("[\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ]");
        Contracts contrats = new Contracts(json);
        Resource resource =map.needExploit(contrats);
        assertNull(resource);
        HashMap<Resource,ResourceAmount> hash = new HashMap<>();

        hash.put(Resource.WOOD,ResourceAmount.HIGH);
        map.setExplore(hash);

        resource = map.needExploit(contrats);
        assertEquals(Resource.WOOD,resource);
    }

    @Test
    public void testNeedExplore() throws Exception {
        GroundMap map = new GroundMap(1);
        assertTrue(map.needExplore());
        HashMap<Resource, ResourceAmount> resources=new HashMap<>();
        map.setExplore(resources);
        assertFalse(map.needExplore());
    }

    @Test
    public void testNeedScout() throws Exception {
        GroundMap map = new GroundMap(1);
        Direction toScout=map.needScout();
        assertNotNull(toScout);
        Set<Resource> resourcesSet = new TreeSet<>();
        //On indique que toutes les directions ont été "scout"
        for(Direction direction : Direction.values()){
            map.setScout(resourcesSet,direction,true);
        }
        toScout=map.needScout();
        assertNull(toScout);

    }

    @Test
    public void testNextMove() throws Exception {
        JSONArray json = new JSONArray("[\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ]");
        Contracts contrats = new Contracts(json);
        GroundMap map = new GroundMap(2);
        Set<Resource> set = new TreeSet<>();
        set.add(Resource.FISH);
        Set<Resource> set2 = new TreeSet<>();
        set2.add(Resource.WOOD);
        map.setScout(set,Direction.EAST,true);
        map.setScout(set,Direction.WEST,true);
        map.setScout(set,Direction.NORTH,true);
        map.setScout(set2,Direction.SOUTH,true);
        assertEquals(Direction.SOUTH,map.nextMove(contrats));

        map.setScout(set,Direction.EAST,true);
        map.setScout(set,Direction.WEST,true);
        map.setScout(set2,Direction.NORTH,true);
        map.setScout(set,Direction.SOUTH,true);
        assertEquals(Direction.NORTH,map.nextMove(contrats));

        map.setScout(set,Direction.EAST,true);
        map.setScout(set2,Direction.WEST,true);
        map.setScout(set,Direction.NORTH,true);
        map.setScout(set,Direction.SOUTH,true);
        assertEquals(Direction.WEST,map.nextMove(contrats));

        map.setScout(set2,Direction.EAST,true);
        map.setScout(set,Direction.WEST,true);
        map.setScout(set,Direction.NORTH,true);
        map.setScout(set,Direction.SOUTH,false);
        assertEquals(Direction.EAST,map.nextMove(contrats));

        map.moveRaid(Direction.NORTH);
        map.setScout(set,Direction.EAST,true);
        map.setScout(set,Direction.WEST,true);
        map.setScout(set,Direction.NORTH,true);
        map.setScout(set,Direction.SOUTH,true);
        map.moveRaid(Direction.EAST);
        map.setScout(set,Direction.EAST,false);
        map.moveRaid(Direction.WEST);
        map.moveRaid(Direction.WEST);
        map.setScout(set,Direction.WEST,false);
        map.moveRaid(Direction.EAST);
        map.moveRaid(Direction.NORTH);
        map.setScout(set,Direction.EAST,true);
        map.setScout(set,Direction.WEST,true);
        map.setScout(set,Direction.NORTH,false);
        map.setScout(set,Direction.SOUTH,true);
        map.moveRaid(Direction.SOUTH);
        Direction nextMove = map.nextMove(contrats);
        assertTrue(Direction.EAST == nextMove || Direction.SOUTH == nextMove);
    }

    @Test
    public void testSetExplore() throws Exception {
        GroundMap map = new GroundMap(1);
        HashMap<Resource, ResourceAmount> resAmount=map.currentArea().getResourceAmount();
        assertFalse(map.currentArea().isExplored());
        assertNull(resAmount.get(Resource.WOOD));
        resAmount.put(Resource.WOOD,ResourceAmount.MEDIUM);
        map.setExplore(resAmount);
        assertTrue(map.currentArea().isExplored());
        //TODO Vérifier que ResourceAmount et ResourceCondition ont été modifiés
    }

    @Test
    public void testSetExploit() throws Exception {
        GroundMap map = new GroundMap(1);
        HashMap<Resource, ResourceAmount> resources=new HashMap<>();
        resources.put(Resource.WOOD,ResourceAmount.HIGH);
        map.setExplore(resources);
        assertTrue(map.currentArea().isExploitableResource(Resource.WOOD));
        map.setExploit(Resource.WOOD);
        assertFalse(map.currentArea().isExploitableResource(Resource.WOOD));
    }

    @Test
    public void testMoveRaid() throws Exception {
        GroundMap map = new GroundMap(1);
        //Coordonnées initiales
        int x=map.getRaid().getPosition().getX();
        int y=map.getRaid().getPosition().getY();

        //Test coordonnées après déplacement dans différentes directions
        for(Direction direction : Direction.values()){
            map.moveRaid(direction);
            int previousX=x;
            int previousY=y;
            x=map.getRaid().getPosition().getX();
            y=map.getRaid().getPosition().getY();
            assertEquals(previousX+direction.getDeltaX(),x);
            assertEquals(previousY+direction.getDeltaY(),y);
        }

    }

    @Test
    public void testSetScout() throws Exception {
        GroundMap map = new GroundMap(1);

        Point p=map.getRaid().getPosition();
        assertFalse(map.get(p).getResourceAmount().containsKey(Resource.FISH));

        Set<Resource> resourcesSet = new TreeSet<>();
        resourcesSet.add(Resource.FISH);
        resourcesSet.add(Resource.WOOD);
        //on fait un setscout, a l'est
        map.setScout(resourcesSet, Direction.EAST, true);
        //On modifie p de telle sorte à pouvoir analyser les données de la case qu'on a "scout"
        map.getRaid().getPosition().move(Direction.EAST);
        p=map.getRaid().getPosition();
        //On vérifie que les ressoruces du set sont bien présentes
        assertTrue(map.get(p).getResourceAmount().containsKey(Resource.FISH));
        assertEquals(map.get(p).getResourceAmount().get(Resource.FISH),ResourceAmount.UNKNOWN);
        assertTrue(map.get(p).getResourceAmount().containsKey(Resource.WOOD));
        assertEquals(map.get(p).getResourceAmount().get(Resource.WOOD),ResourceAmount.UNKNOWN);

    }

    @Test
    public void setGlimpseTest(){
        GroundMap map = new GroundMap(12);
        map.setGlimpse(Direction.WEST,3,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.WEST,2,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.WEST,1,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.WEST,0,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.SOUTH,3,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.SOUTH,2,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.SOUTH,1,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.SOUTH,0,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.NORTH,3,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.NORTH,2,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.NORTH,1,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.NORTH,0,Biome.GRASSLAND,100.0);
        assertEquals(map.glimpseAround(), Direction.EAST);
        map.setGlimpse(Direction.EAST,3,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.EAST,2,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.EAST,1,Biome.GRASSLAND,100.0);
        map.setGlimpse(Direction.EAST,0,Biome.GRASSLAND,100.0);
        assertNull(map.glimpseAround());
    }


}