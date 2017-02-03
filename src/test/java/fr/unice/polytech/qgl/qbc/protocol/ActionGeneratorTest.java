package fr.unice.polytech.qgl.qbc.protocol;

import fr.unice.polytech.qgl.qbc.model.Resource;
import org.json.JSONObject;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests pour la classe ActionGenerator
 *
 * @author Robin Alonzo et Cyprien Levy
 */
public class ActionGeneratorTest {

    @Test
    public void stopTest(){
        assertEquals("stop",new JSONObject(ActionGenerator.stop()).getString("action"));
    }

    @Test
    public void flyTest(){
        assertEquals("fly", new JSONObject(ActionGenerator.fly()).getString("action"));
    }

    @Test
    public void  headingTest(){
        //Test pour vérifier la valeur d'action
        assertEquals("heading", new JSONObject(ActionGenerator.heading("N")).getString("action"));

        assertEquals("N", new JSONObject(ActionGenerator.heading("N")).getJSONObject("parameters").getString("direction"));
        assertEquals("S", new JSONObject(ActionGenerator.heading("S")).getJSONObject("parameters").getString("direction"));
        assertEquals("E", new JSONObject(ActionGenerator.heading("E")).getJSONObject("parameters").getString("direction"));
        assertEquals("W", new JSONObject(ActionGenerator.heading("W")).getJSONObject("parameters").getString("direction"));
    }

    @Test
    public void  echoTest(){
        assertEquals("echo", new JSONObject(ActionGenerator.echo("N")).getString("action"));

        assertEquals("N", new JSONObject(ActionGenerator.echo("N")).getJSONObject("parameters").getString("direction"));
        assertEquals("S", new JSONObject(ActionGenerator.echo("S")).getJSONObject("parameters").getString("direction"));
        assertEquals("E", new JSONObject(ActionGenerator.echo("E")).getJSONObject("parameters").getString("direction"));
        assertEquals("W", new JSONObject(ActionGenerator.echo("W")).getJSONObject("parameters").getString("direction"));
    }

    @Test
    public void scanTest(){
        assertEquals("scan", new JSONObject(ActionGenerator.scan()).getString("action"));
    }

    @Test
    public void landTest(){
        assertEquals("land", new JSONObject(ActionGenerator.land("id",42)).getString("action"));
        assertEquals("id", new JSONObject(ActionGenerator.land("id",42)).getJSONObject("parameters").getString("creek"));
        assertEquals(42, new JSONObject(ActionGenerator.land("id",42)).getJSONObject("parameters").getInt("people"));
    }

    @Test
    public void move_toTest(){
        assertEquals("move_to",new JSONObject(ActionGenerator.move_to("N")).getString("action"));
        assertEquals("N",new JSONObject(ActionGenerator.move_to("N")).getJSONObject("parameters").getString("direction"));
        assertEquals("S",new JSONObject(ActionGenerator.move_to("S")).getJSONObject("parameters").getString("direction"));
        assertEquals("E",new JSONObject(ActionGenerator.move_to("E")).getJSONObject("parameters").getString("direction"));
        assertEquals("W",new JSONObject(ActionGenerator.move_to("W")).getJSONObject("parameters").getString("direction"));

    }
    @Test
    public void scoutTest(){
        assertEquals("scout", new JSONObject(ActionGenerator.scout("N")).getString("action"));
        assertEquals("N",new JSONObject(ActionGenerator.scout("N")).getJSONObject("parameters").getString("direction"));
        assertEquals("S",new JSONObject(ActionGenerator.scout("S")).getJSONObject("parameters").getString("direction"));
        assertEquals("E",new JSONObject(ActionGenerator.scout("E")).getJSONObject("parameters").getString("direction"));
        assertEquals("W",new JSONObject(ActionGenerator.scout("W")).getJSONObject("parameters").getString("direction"));
    }
    @Test
    public void glimpseTest(){
        assertEquals("glimpse", new JSONObject(ActionGenerator.glimpse("N",3)).getString("action"));
        assertEquals("N", new JSONObject(ActionGenerator.glimpse("N",3)).getJSONObject("parameters").getString("direction"));
        assertEquals("S", new JSONObject(ActionGenerator.glimpse("S",3)).getJSONObject("parameters").getString("direction"));
        assertEquals("E", new JSONObject(ActionGenerator.glimpse("E",3)).getJSONObject("parameters").getString("direction"));
        assertEquals("W", new JSONObject(ActionGenerator.glimpse("W",3)).getJSONObject("parameters").getString("direction"));
        assertEquals(3, new JSONObject(ActionGenerator.glimpse("N",3)).getJSONObject("parameters").getInt("range"));
        assertEquals(1, new JSONObject(ActionGenerator.glimpse("N",1)).getJSONObject("parameters").getInt("range"));
        assertEquals(2, new JSONObject(ActionGenerator.glimpse("N",2)).getJSONObject("parameters").getInt("range"));
        assertEquals(4, new JSONObject(ActionGenerator.glimpse("N",4)).getJSONObject("parameters").getInt("range"));

    }
    @Test
    public void exploreTest(){
        assertEquals("explore", new JSONObject(ActionGenerator.explore()).getString("action"));

    }

    @Test
    public void exploitTest(){
        assertEquals("exploit", new JSONObject(ActionGenerator.exploit("WOOD")).getString("action"));
        assertEquals("WOOD", new JSONObject(ActionGenerator.exploit("WOOD")).getJSONObject("parameters").getString("resource"));
    }

    @Test
    public void transformTest(){
        JSONObject transform = new JSONObject(ActionGenerator.transform(new String[]{"WOOD","QUARTZ"},new int[]{500,250}));
        assertEquals("transform", transform.getString("action"));
        assertEquals(500, transform.getJSONObject("parameters").getInt("WOOD"));
        assertEquals(250,transform.getJSONObject("parameters").getInt("QUARTZ"));
    }

    @Test
    public void transformTest2(){
        List<String> resources = new ArrayList<>();
        resources.add("WOOD");
        resources.add("QUARTZ");
        List<Integer> quantity = new ArrayList<>();
        quantity.add(500);
        quantity.add(250);
        JSONObject transform = new JSONObject(ActionGenerator.transform(resources,quantity));
        assertEquals("transform", transform.getString("action"));
        assertEquals(500, transform.getJSONObject("parameters").getInt("WOOD"));
        assertEquals(250,transform.getJSONObject("parameters").getInt("QUARTZ"));
    }

}
