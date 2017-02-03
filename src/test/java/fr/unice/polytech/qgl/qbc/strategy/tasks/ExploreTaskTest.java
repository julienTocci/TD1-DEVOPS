package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.ground.ExploreTask;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour ExploreTask
 *
 * @author Robin Alonzo
 */
public class ExploreTaskTest {

    @Test
    public void test() throws Exception {
        Map map = new Map(new Drone("N"));
        map.setLand("creek",1);
        Task task = new ExploreTask(map);
        assertEquals(Action.ActionName.EXPLORE,task.getDecision().getAction());
        Result result = new Result("{\n" +
                "  \"cost\": 5,\n" +
                "  \"extras\": {\n" +
                "    \"resources\": [\n" +
                "      { \"amount\": \"HIGH\", \"resource\": \"FUR\", \"cond\": \"FAIR\" },\n" +
                "      { \"amount\": \"LOW\", \"resource\": \"WOOD\", \"cond\": \"HARSH\" }\n" +
                "    ],\n" +
                "    \"pois\": [ \"creek-id\" ]\n" +
                "  },\n" +
                "  \"status\": \"OK\"\n" +
                "}");
        task.acknowledgeResult(result);
        assertEquals(null,task.getDecision());
    }

}