package fr.unice.polytech.qgl.qbc.strategy.tasks.ground;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;

import java.util.HashMap;

/**
 * Effectue un EXPLORE
 *
 * @author Robin Alonzo
 */
public class ExploreTask implements Task {

    private Map map;

    public ExploreTask(Map map){
        this.map = map;
    }

    @Override
    public Action getDecision() {
        if(map.needExplore()){
            return Action.explore();
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        HashMap<Resource, ResourceAmount> resources = result.getExploreResources();
        map.setExplore(resources);
    }
}
