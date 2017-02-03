package fr.unice.polytech.qgl.qbc.strategy.tasks.air;

import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import fr.unice.polytech.qgl.qbc.model.map.Direction;

/**
 * Effectue des ECHO dans toutes les directions
 *
 * @author Robin Alonzo
 */
public class EchoTask implements Task {

    private Map map;

    private Action lastAction;

    public EchoTask(Map map){
        this.map = map;
    }

    @Override
    public Action getDecision() {
        Direction echoDirection = map.needEcho();
        if(echoDirection!=null) {
            lastAction = Action.echo(echoDirection);
            return lastAction;
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        map.setEcho(Direction.toDirection(lastAction.getStringParameter(Action.ParameterName.DIRECTION)), result.getEchoRange(), result.getEchoFound());
    }
}
