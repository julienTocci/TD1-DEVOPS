package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.ground.GlimpseTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour GlimpseTask
 *
 * @author Robin Alonzo
 */
public class GlimpseTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("W"));
        map.setLand("creek",1);
        Task task = new GlimpseTask(map);
        assertEquals(Action.ActionName.GLIMPSE,task.getDecision().getAction());
        Result result = new Result("{ \n" +
                "  \"cost\": 3,\n" +
                "  \"extras\": {\n" +
                "    \"asked_range\": 4,\n" +
                "    \"report\": [\n" +
                "      [ [ \"BEACH\", 59.35 ], [ \"OCEAN\", 40.65 ] ],\n" +
                "      [ [ \"OCEAN\", 70.2  ], [ \"BEACH\", 29.8  ] ],\n" +
                "      [ \"OCEAN\", \"BEACH\" ]\n " +
                "    ]\n" +
                "  },\n" +
                "  \"status\": \"OK\"\n" +
                "}  ");
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.GLIMPSE,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.GLIMPSE,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.GLIMPSE,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(null,task.getDecision());
    }
}