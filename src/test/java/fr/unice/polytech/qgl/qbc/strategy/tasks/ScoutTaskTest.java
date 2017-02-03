package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.ground.ScoutTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour ScoutTask
 *
 * @author Robin Alonzo
 */
public class ScoutTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("W"));
        map.setLand("creek",1);
        Task task = new ScoutTask(map);
        assertEquals(Action.ActionName.SCOUT,task.getDecision().getAction());
        Result result = new Result("{ \"cost\": 5, \"extras\": { \"altitude\": 1, \"resources\": [\"FUR\", \"WOOD\"] }, \"status\": \"OK\" }");
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.SCOUT,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.SCOUT,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(Action.ActionName.SCOUT,task.getDecision().getAction());
        task.acknowledgeResult(result);
        assertEquals(null,task.getDecision());
    }
}