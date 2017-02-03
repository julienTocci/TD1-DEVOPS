package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Biome;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.ground.GroundMoveTask;
import fr.unice.polytech.qgl.qbc.model.map.Direction;
import org.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour GroundMoveTask
 *
 * @author Robin Alonzo
 */
public class GroundMoveTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("E"));
        map.setLand("creek",1);

        Contracts contracts = new Contracts(new JSONArray("[\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ]"));
        Task task = new GroundMoveTask(map,contracts);

        map.setGlimpse(Direction.NORTH,1, Biome.TEMPERATE_RAIN_FOREST,100.0);
        map.setGlimpse(Direction.SOUTH,1, Biome.OCEAN,100.0);
        map.setGlimpse(Direction.EAST,1, Biome.OCEAN,100.0);
        map.setGlimpse(Direction.WEST,1, Biome.OCEAN,100.0);
        Action action = task.getDecision();
        assertEquals(Action.ActionName.MOVE_TO,action.getAction());
        assertEquals(Direction.NORTH, Direction.toDirection(action.getStringParameter(Action.ParameterName.DIRECTION)));

        Result result = new Result("{ \"cost\": 6, \"extras\": { }, \"status\": \"OK\" }");
        task.acknowledgeResult(result);

        contracts.updateAmountResource(Resource.WOOD,600);
        contracts.updateAmountResource(Resource.GLASS,200);
        assertEquals(null,task.getDecision());


    }
}