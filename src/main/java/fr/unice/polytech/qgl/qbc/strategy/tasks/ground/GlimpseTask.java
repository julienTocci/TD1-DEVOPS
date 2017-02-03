package fr.unice.polytech.qgl.qbc.strategy.tasks.ground;

import fr.unice.polytech.qgl.qbc.model.map.Biome;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import fr.unice.polytech.qgl.qbc.model.map.Direction;

import java.util.HashMap;
import java.util.List;

/**
 * Effectue des GLIMPSE dans toutes les directions
 *
 * @author Robin Alonzo
 */
public class GlimpseTask implements Task {

    private Map map;

    private Action lastAction;

    public GlimpseTask(Map map){
        this.map = map;
    }

    @Override
    public Action getDecision() {
        Direction glimpse = map.needGlimpse();
        if(glimpse != null){
            lastAction = Action.glimpse(glimpse,4);
            return lastAction;
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        List<HashMap<Biome, Double>> biomes = result.getGlimpseResult();
        for (int i = 0; i < lastAction.getIntParameter(Action.ParameterName.RANGE) /*biomes.size()*/; i++) {
            Direction direction = Direction.toDirection(lastAction.getStringParameter(Action.ParameterName.DIRECTION));
            final int distance = i;
            if(i < biomes.size()){
                biomes.get(i).forEach((biome, proportion) -> map.setGlimpse(direction, distance, biome, proportion));
            }
            else {
                map.setGroundOutOfBound(direction,i);
            }
        }
    }
}
