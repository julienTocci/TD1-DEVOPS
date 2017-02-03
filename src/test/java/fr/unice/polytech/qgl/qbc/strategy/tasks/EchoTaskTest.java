package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.EchoTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test pour EchoTask
 *
 * @author Robin Alonzo
 */
public class EchoTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("N"));
        Task task = new EchoTask(map);
        Action action = task.getDecision();
        assertEquals(action.getAction(), Action.ActionName.ECHO);
        Result result = new Result("{ \"cost\": 1, \"extras\": { \"range\": 5, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }");
        task.acknowledgeResult(result);
        action = task.getDecision();
        assertEquals(action.getAction(), Action.ActionName.ECHO);
        task.acknowledgeResult(result);
        action = task.getDecision();
        assertEquals(action.getAction(), Action.ActionName.ECHO);
        task.acknowledgeResult(result);
        action = task.getDecision();
        assertNull(action);

    }
}