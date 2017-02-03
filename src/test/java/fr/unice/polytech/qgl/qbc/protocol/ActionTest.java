package fr.unice.polytech.qgl.qbc.protocol;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Direction;
import javafx.util.Pair;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests pour la classe Action
 *
 * @author Robin Alonzo & Cyprien Levy
 */
public class ActionTest {

    @Test
    public void testStop() throws Exception {
        Action action = Action.stop();
        assertEquals(Action.ActionName.STOP, action.getAction());
    }

    @Test
    public void testFly() throws Exception {
        Action action = Action.fly();
        assertEquals(Action.ActionName.FLY, action.getAction());
    }

    @Test
    public void testHeading() throws Exception {
        Action action = Action.heading(Direction.toDirection("W"));
        assertEquals(Action.ActionName.HEADING, action.getAction());
        assertEquals("W", action.getStringParameter(Action.ParameterName.DIRECTION));
    }

    @Test
    public void testEcho() throws Exception {
        Action action = Action.echo(Direction.toDirection("S"));
        assertEquals(Action.ActionName.ECHO, action.getAction());
        assertEquals("S", action.getStringParameter(Action.ParameterName.DIRECTION));
    }

    @Test
    public void testScan() throws Exception {
        Action action = Action.scan();
        assertEquals(Action.ActionName.SCAN, action.getAction());
    }

    @Test
    public void testLand() throws Exception {
        Action action = Action.land("creekID", 5);
        assertEquals(Action.ActionName.LAND, action.getAction());
        assertEquals("creekID", action.getStringParameter(Action.ParameterName.CREEK));
        assertEquals(5, (int) action.getIntParameter(Action.ParameterName.PEOPLE));
    }

    @Test
    public void testMove_to() throws Exception {
        Action action = Action.move_to(Direction.toDirection("N"));
        assertEquals(Action.ActionName.MOVE_TO, action.getAction());
        assertEquals("N", action.getStringParameter(Action.ParameterName.DIRECTION));
    }

    @Test
    public void testGlimpse() throws Exception {
        Action action = Action.glimpse(Direction.toDirection("E"), 4);
        assertEquals(Action.ActionName.GLIMPSE, action.getAction());
        assertEquals("E", action.getStringParameter(Action.ParameterName.DIRECTION));
        assertEquals(4, (int) action.getIntParameter(Action.ParameterName.RANGE));
    }

    @Test
    public void testExplore() throws Exception {
        Action action = Action.explore();
        assertEquals(Action.ActionName.EXPLORE, action.getAction());
    }

    @Test
    public void testExploit() throws Exception {
        Action action = Action.exploit(Resource.FLOWER);
        assertEquals(Action.ActionName.EXPLOIT, action.getAction());
        assertEquals("FLOWER",action.getStringParameter(Action.ParameterName.RESOURCE));
    }

    @Test
    public void testTransform() throws Exception {
        List<Pair<Resource,Integer>> resources= new ArrayList<>();
        resources.add(new Pair<>(Resource.WOOD,6));
        resources.add(new Pair<>(Resource.QUARTZ,11));
        Action action = Action.transform(resources);
        assertEquals(Action.ActionName.TRANSFORM, action.getAction());
        assertEquals(6,(int) action.getIntParameter(Action.ParameterName.WOOD));
        assertEquals(11,(int) action.getIntParameter(Action.ParameterName.QUARTZ));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testExceptionTransform() throws Exception{
        List<Pair<Resource,Integer>> resources= new ArrayList<>();
        resources.add(new Pair<>(Resource.FISH,50));
        Action.transform(resources);
    }


    @Test
    public void testToJSON() throws Exception {
        List<Pair<Resource,Integer>> resources= new ArrayList<>();
        resources.add(new Pair<>(Resource.WOOD,6));
        resources.add(new Pair<>(Resource.QUARTZ,11));
        Action action = Action.transform(resources);
        JSONObject json = new JSONObject(action.toJSON());
        assertEquals("transform",json.getString("action"));
        JSONObject parameters = json.getJSONObject("parameters");
        assertEquals(6,parameters.getInt("WOOD"));
        assertEquals(11,parameters.getInt("QUARTZ"));

        Action actionStop = Action.stop();
        JSONObject jsonStop = new JSONObject((actionStop.toJSON()));
        assertEquals("stop",jsonStop.getString("action"));

        Action actionFly = Action.fly();
        JSONObject jsonFly = new JSONObject((actionFly.toJSON()));
        assertEquals("fly",jsonFly.getString("action"));




        Action actionHeading = Action.heading(Direction.NORTH);
        JSONObject jsonHeading = new JSONObject((actionHeading.toJSON()));
        JSONObject parametersHeading = jsonHeading.getJSONObject("parameters");
        assertEquals("heading",jsonHeading.getString("action"));
        assertEquals("N",parametersHeading.getString("direction"));


        Action actionEcho = Action.echo(Direction.NORTH);
        JSONObject jsonEcho = new JSONObject((actionEcho.toJSON()));
        JSONObject parametersEcho = jsonEcho.getJSONObject("parameters");
        assertEquals("echo",jsonEcho.getString("action"));
        assertEquals("N",parametersEcho.getString("direction"));


        Action actionScan = Action.scan();
        JSONObject jsonScan = new JSONObject((actionScan.toJSON()));
        assertEquals("scan",jsonScan.getString("action"));

        Action actionLand = Action.land("creek", 55);
        JSONObject jsonLand = new JSONObject((actionLand.toJSON()));
        JSONObject parametersLand = jsonLand.getJSONObject("parameters");
        assertEquals("land",jsonLand.getString("action"));
        assertEquals("creek", parametersLand.getString("creek"));
        assertEquals(55, parametersLand.getInt("people"));


        Action actionMove_to = Action.move_to(Direction.NORTH);
        JSONObject jsonMove_to = new JSONObject((actionMove_to.toJSON()));
        JSONObject parametersMove_to = jsonMove_to.getJSONObject("parameters");
        assertEquals("move_to",jsonMove_to.getString("action"));
        assertEquals("N",parametersMove_to.getString("direction"));

        Action actionGlimpse = Action.glimpse(Direction.NORTH, 4);
        JSONObject jsonGlimpse = new JSONObject((actionGlimpse.toJSON()));
        JSONObject parametersGlimpse = jsonGlimpse.getJSONObject("parameters");
        assertEquals("glimpse",jsonGlimpse.getString("action"));
        assertEquals("N", parametersGlimpse.getString("direction"));
        assertEquals(4, parametersGlimpse.getInt("range"));

        Action actionScout = Action.scout(Direction.NORTH);
        JSONObject jsonScout = new JSONObject((actionScout.toJSON()));
        JSONObject parametersScout = jsonScout.getJSONObject("parameters");
        assertEquals("scout",jsonScout.getString("action"));
        assertEquals("N", parametersScout.getString("direction"));

        Action actionExplore = Action.explore();
        JSONObject jsonExpore = new JSONObject((actionExplore.toJSON()));
        assertEquals("explore",jsonExpore.getString("action"));


        Action actionExploit = Action.exploit(Resource.WOOD);
        JSONObject jsonExploit = new JSONObject((actionExploit.toJSON()));
        JSONObject parametersExploit = jsonExploit.getJSONObject("parameters");
        assertEquals("exploit",jsonExploit.getString("action"));
        assertEquals("WOOD", parametersExploit.getString("resource"));

    }
}