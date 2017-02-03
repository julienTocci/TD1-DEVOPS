package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.Bot;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.ScanTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test pour ScanTask
 *
 * @author Robin Alonzo
 */
public class ScanTaskTest {

    @Test
    public void test() throws Exception {
        Drone drone = new Drone("N");
        Map map = new Map(drone);
        Bot bot = new Bot("{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 10000,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"W\"\n" +
                "}\n");
        Task task = new ScanTask(map, drone, bot);
        Action action = task.getDecision();
        assertEquals(Action.ActionName.SCAN,action.getAction());
        Result result = new Result("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": []}, \"status\": \"OK\"}");
        task.acknowledgeResult(result);
        assertNull(task.getDecision());
    }
}