package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.DroneMoveTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Description
 *
 * @author Robin Alonzo
 */
public class DroneMoveTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("W"));
        Task task = new DroneMoveTask(map);
        Action action = task.getDecision();
        assertNotNull(action);
    }
}