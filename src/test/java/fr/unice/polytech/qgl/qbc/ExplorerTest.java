package fr.unice.polytech.qgl.qbc;

import eu.ace_design.island.bot.IExplorerRaid;
import fr.unice.polytech.qgl.qbc.model.Contracts;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Bais on 10/11/2015.
 */
public class ExplorerTest {

    @Test
    public void initializeTest(){
        Explorer b=new Explorer();
        JSONObject json=new JSONObject("{\"men\": 12,\"budget\": 10000, \"contracts\": [ { \"amount\": 600, \"resource\": \"WOOD\" }, { \"amount\": 200, \"resource\": \"GLASS\" }],\"heading\": \"W\"}");
        String s=json.toString();
        b.initialize(s);
        assertEquals(b.budget,json.getInt("budget"));
        assertEquals(b.men,json.getInt("men"));
        assertEquals(b.heading,json.getString("heading"));


        //Test pour les contrats
        JSONArray array=json.getJSONArray("contracts");

        for(int i=0;i<array.length();i++){
            //On récupère les données du contrat contenu dans le JSONObject json
            JSONObject testObject=array.getJSONObject(i);
            String resourceName=testObject.getString("resource");
            int resourceTest=testObject.getInt("amount");


        }
    }

    @Ignore
    public void simulationTest(){
        IExplorerRaid raid = new Explorer();
        String context = "{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 10000,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"W\"\n" +
                "}";
        raid.initialize(context);
        boolean endOfGame = false;
        while ( !endOfGame ) {
            String decision = raid.takeDecision();
            if(new JSONObject(decision).getString("action").equals("stop")){
                endOfGame = true;
            }
            else{
                fail("Explorer is not sending STOP");
            }
            String result  = "{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }";
            raid.acknowledgeResults(result);
        }
    }

}
