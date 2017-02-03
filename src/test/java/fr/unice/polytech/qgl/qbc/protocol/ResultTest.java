package fr.unice.polytech.qgl.qbc.protocol;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Biome;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * test pour la classe result
 *
 * @author Cyprien Levy
 */
public class ResultTest {
    static Logger logger = Logger.getLogger(ResultTest.class);

    @Test
    public void getEchoRangeTest() {
        Result result = new Result("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }");
        assertEquals(2, result.getEchoRange());
        Result result2 = new Result("{ \"cost\": 1, \"extras\": { \"range\": 0, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }");
        assertEquals(0, result2.getEchoRange());

    }

    @Test
    public void getEchoFound() {
        Result result = new Result("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }");
        assertEquals("GROUND", result.getEchoFound());
        Result result2 = new Result("{ \"cost\": 1, \"extras\": { \"range\": 0, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }");
        assertEquals("OUT_OF_RANGE", result2.getEchoFound());
    }

    @Test
    public void getCostTest() {
        Result result = new Result("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }");
        assertEquals(1, result.getCost());
        Result result2 = new Result("{ \"cost\": 4, \"extras\": { \"range\": 0, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }");
        assertEquals(4, result2.getCost());
    }

    @Test
    public void getBiomesTest() {
        Result result = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": []}, \"status\": \"OK\"}");
        String biomes[] = result.getScanBiomes();
        assertEquals(2, biomes.length);
        boolean found = false;
        for (String biome : biomes) {
            if (biome.equals("GLACIER")) {
                found = true;
                break;
            }
        }
        if (!found) fail();
        found = false;
        for (String biome : biomes) {
            if (biome.equals("ALPINE")) {
                found = true;
                break;
            }
        }
        if (!found) fail();


    }

    @Test
    public void getCreeksTest(){
        Result result = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": []}, \"status\": \"OK\"}");
        String creeks[] = result.getCreeks();
        assertEquals(0, creeks.length);

        Result result2 = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [\"id\"]}, \"status\": \"OK\"}");
        String creeks2[]=result2.getCreeks();
        assertEquals(1, creeks2.length);
        boolean found = false;
        for (String aCreeks2 : creeks2) {
            if (aCreeks2.equals("id")) {
                found = true;
                break;
            }
        }
        if (!found) fail();


        Result result3 = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": [\"id1\",\"id2\"]}, \"status\": \"OK\"}");
        String creeks3[] = result3.getCreeks();
        assertEquals(2, creeks3.length);
        found = false;
        for (String aCreeks3 : creeks3) {
            if (aCreeks3.equals("id1")) {
                found = true;
                break;
            }
        }
        if (!found) fail();
        for (String aCreeks3 : creeks3) {
            if (aCreeks3.equals("id2")) {
                break;
            }
        }
    }

    @Test
    public void getExploreResourcesTest(){

        /*
        Result result = new Result("{  \"cost\": 5,\"extras\": " +
                "{\"resources\": [ { \"amount\": \"HIGH\", \"resource\": " +
                "\"FUR\", \"cond\": \"FAIR\" },{ \"amount\": \"LOW\", " +
                "\"resource\": \"WOOD\", \"cond\": \"HARSH\" }],\"pois\":" +
                " [ \"creek-id\" ]}}");
        String resources[]=result.getExploreResources();
        assertEquals("FUR",resources[0]);
        assertEquals("WOOD",resources[1]);
        */

        Result result = new Result("{  \"cost\": 5,\"extras\": " +
                "{\"resources\": [ { \"amount\": \"HIGH\", \"resource\": " +
                "\"FUR\", \"cond\": \"FAIR\" },{ \"amount\": \"LOW\", " +
                "\"resource\": \"WOOD\", \"cond\": \"HARSH\" }],\"pois\":" +
                " [ \"creek-id\" ]}}");
        HashMap<Resource,ResourceAmount> resources=result.getExploreResources();
        assertEquals(ResourceAmount.LOW,resources.get(Resource.WOOD));
        assertEquals(ResourceAmount.HIGH,resources.get(Resource.FUR));
    }

    @Test
    public void testGetScoutResources(){
        Result result=new Result("{ \"cost\": 5, \"extras\": { \"altitude\":" +
                "1, \"resources\": [\"FUR\", \"WOOD\"] }, \"status\": \"OK\" }");
        String[] resources=result.getScoutResources();
        assertEquals("FUR",resources[0]);
        assertEquals("WOOD",resources[1]);

    }

    @Test
    public void testGetScoutReachable() throws Exception {
        Result result = new Result("{\"cost\":6,\"extras\":{\"altitude\":0,\"unreachable\":true,\"resources\":[]},\"status\":\"OK\"}");
        assertFalse(result.getScoutReachable());
        result = new Result("{\"cost\":5,\"extras\":{\"altitude\":0,\"resources\":[\"FISH\"]},\"status\":\"OK\"}");
        assertTrue(result.getScoutReachable());
    }

    @Test

    public void testGetExploitedAmount(){
        Result result = new Result("{ \"cost\": 3, \"extras\": {\"amount\": 9}, \"status\": \"OK\" }");
        assertEquals(9,result.getExploitedAmount());
    }


    @Test
    public void testGetGlimpseResult(){
        Result result = new Result("{ \n" +
                "  \"cost\": 3,\n" +
                "  \"extras\": {\n" +
                "    \"asked_range\": 4,\n" +
                "    \"report\": [\n" +
                "      [ [ \"BEACH\", 59.35 ], [ \"OCEAN\", 40.65 ] ],\n" +
                "      [ [ \"OCEAN\", 70.2  ], [ \"BEACH\", 29.8  ] ],\n" +
                "      [ \"OCEAN\", \"BEACH\" ],\n" +
                "      [ \"OCEAN\" ]\n" +
                "    ]\n" +
                "  },\n" +
                "  \"status\": \"OK\"\n" +
                "}  ");

        List<HashMap<Biome,Double>> list=result.getGlimpseResult();
        assertEquals(list.get(0).get(Biome.BEACH),59.35,0.001);
        assertEquals(list.get(0).get(Biome.OCEAN),40.65,0.001);
        assertEquals(list.get(1).get(Biome.BEACH),29.8,0.001);
        assertEquals(list.get(1).get(Biome.OCEAN),70.2,0.001);
        assertEquals(list.get(2).get(Biome.OCEAN),0.0,0.001);
        assertEquals(list.get(2).get(Biome.BEACH),0.0,0.001);


    }

}
