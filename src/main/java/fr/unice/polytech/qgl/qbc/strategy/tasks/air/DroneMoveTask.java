package fr.unice.polytech.qgl.qbc.strategy.tasks.air;

import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;

/**
 * Bouge le drone à un endroit non exploré
 *
 * @author Robin Alonzo
 */
public class DroneMoveTask implements Task {

    Map map;

    public DroneMoveTask(Map map){
        this.map = map;
    }

    @Override
    public Action getDecision() {
        return map.moveToUnknown();
    }

    @Override
    public void acknowledgeResult(Result result) {
        //TODO déplacer les modifications du drone ici au lieu de les effectuer dans moveToUnknown
    }
}
