package fr.unice.polytech.qgl.qbc.strategy.tasks.ground;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import fr.unice.polytech.qgl.qbc.model.map.Direction;

import java.util.Set;
import java.util.TreeSet;

/**
 * Effectue un SCOUT dans toutes les directions
 *
 * @author Robin Alonzo
 */
public class ScoutTask implements Task {

    Action lastAction;

    Map map;

    public ScoutTask(Map map){
        this.map = map;
    }

    @Override
    public Action getDecision() {
        Direction scout = map.needScout();
        if(scout != null){
            lastAction = Action.scout(scout);
            return lastAction;
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        Direction scouted = Direction.toDirection(lastAction.getStringParameter(Action.ParameterName.DIRECTION));
        String[] scoutedResources = result.getScoutResources();
        boolean reachable = result.getScoutReachable();
        Set<Resource> resourcesSet = new TreeSet<>();
        for (String resource : scoutedResources) {
            resourcesSet.add(Resource.toResource(resource));
        }
        map.setScout(resourcesSet, scouted, reachable);
    }
}
