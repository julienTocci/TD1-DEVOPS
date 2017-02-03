package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.model.map.Direction;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * Tests pour la classe AirScanStrategy
 *
 * @author Cyprien Levy et Robin Alonzo
 */

public class AirScanStrategyTest {

    @Test
    public void testTakeDecision() throws Exception {
        Bot bot = mock(Bot.class);
        Drone drone = new Drone("W");
        Map map = new Map(drone);
        map.setEcho(Direction.NORTH,4,"GROUND");
        map.setEcho(Direction.WEST,10, "OUT_OF_RANGE");
        map.setEcho(Direction.EAST,15, "OUT_OF_RANGE");
        map.setEcho(Direction.SOUTH,10, "OUT_OF_RANGE");
        AirScanStrategy air = new AirScanStrategy(bot, map, drone);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.scan()); //Permet de changer le comportement du prochain appel de ActionGenerator.getLastAction
        Result scanResult = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": []}, \"status\": \"OK\"}");
        air.acknowledgeResults(scanResult);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.fly());
        Result flyResult = new Result("{ \"cost\": 2, \"extras\": {}, \"status\": \"OK\" }");
        air.acknowledgeResults(flyResult);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.scan());
        Result oceanScanResult = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": []}, \"status\": \"OK\"}");
        air.acknowledgeResults(oceanScanResult);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.echo(Direction.NORTH));
        Result echoResult = new Result("{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }");
        air.acknowledgeResults(echoResult);
        Map mockedMap = mock(Map.class);
        when(mockedMap.needTurn()).thenReturn(true);
        when(mockedMap.getTurn()).thenReturn(Direction.NORTH);
        air.map = mockedMap;
        air.takeDecision();
        air.map = map;

        when(bot.getLastAction()).thenReturn(Action.heading(Direction.NORTH));
        air.acknowledgeResults(flyResult);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.heading(Direction.EAST));
        air.acknowledgeResults(flyResult);
        air.takeDecision();

        when(bot.getLastAction()).thenReturn(Action.scan());
        Result creekResult = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": [\"id\"]}, \"status\": \"OK\"}");
        air.acknowledgeResults(creekResult);
        air.takeDecision();


        Action land = Action.land("id",1);
        land.toJSON();//Permet de changer le comportement du prochain appel de ActionGenerator.getLastAction
        Bot mockedBot = mock(Bot.class);
        when(mockedBot.getLastAction()).thenReturn(land);
        air.bot = mockedBot;
        air.acknowledgeResults(flyResult);
        air.takeDecision();
    }

    @Test
    public void testUTurn(){
        Bot bot=new Bot("{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 10000,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"N\"\n" +
                "}\n");
        Drone drone = new Drone("N");
        Map map = new Map(drone);
        AirScanStrategy air=new AirScanStrategy(bot,map,drone);

        air.uTurn(drone.getDirection(),drone.getDirection().turnLeft());
        assertEquals(drone.getDirection(),Direction.SOUTH);
        air.uTurn(drone.getDirection(),drone.getDirection().turnLeft());
        assertEquals(drone.getDirection(),Direction.NORTH);

    }
}