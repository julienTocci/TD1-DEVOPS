package fr.unice.polytech.qgl.qbc.strategy.tasks.ground;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import fr.unice.polytech.qgl.qbc.model.map.Direction;

/**
 * Tâche s'occupant des mouvements sur terre
 *
 * @author Robin Alonzo
 */
public class GroundMoveTask implements Task {

    private Contracts contracts;
    private Map map;

    private Action lastAction;

    public GroundMoveTask(Map map, Contracts contracts){
        this.map = map;
        this.contracts = contracts;
    }

    @Override
    public Action getDecision() {
        Direction move = map.nextMove(contracts);
        if(move != null){
            lastAction = Action.move_to(move);
            return lastAction;
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        Direction direction = Direction.toDirection(lastAction.getStringParameter(Action.ParameterName.DIRECTION));
        map.moveRaid(direction);
    }
}
