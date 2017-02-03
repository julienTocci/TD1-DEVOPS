package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.ground.*;
import fr.unice.polytech.qgl.qbc.model.map.Direction;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Stratégie s'occupant de scanner l'ile et trouver les criques
 *
 * @author Robin Alonzo
 */
public class AirScanStrategy implements Strategy {
    static Logger logger = Logger.getLogger(AirScanStrategy.class);

    static final int MAX_PEOPLE_LANDING = 1;

    Bot bot;
    Map map;
    Drone drone;

    //Variables utilisées pour takeDecision
    private Stack<Action> actionStack= new Stack<>();

    private Direction traversalDirection=null; //Indique si on va parcourir l'ile en remontant ou descendant

    private boolean onSea=false; //Indique qu'on est sur la mer ou pas
    private boolean forceEcho=false; //Forcer la prochaine action a être un echo

    //Les deux variables suivantes servent à arrêter le drone en cas de danger d'écher
    private boolean checkDanger=false;
    private boolean forceStop=false;

    private ArrayList<String> availableCreeks = new ArrayList<>();

    public AirScanStrategy(Bot bot,Map map,Drone drone){
        this.bot=bot;
        this.map=map;
        this.drone=drone;
    }

    public void uTurn(Direction currentDirection, Direction traversalDirection){
        drone.heading(currentDirection.invert().toString());
        actionStack.push(Action.echo(currentDirection.invert()));
        actionStack.push(Action.heading(currentDirection.invert()));
        actionStack.push(Action.heading(traversalDirection));
    }

    @Override
    public Action takeDecision() {
        //Si on a trouvé un creek, on land dessus
        if(availableCreeks.size()>0){
            int people = Math.min(bot.getMen() - 1, MAX_PEOPLE_LANDING);
            return Action.land(availableCreeks.get(0), people);
        }
        if(forceStop){
            return Action.stop();
        }

        //Si on a des actions dans actionStack, on les execute
        if(actionStack != null && !actionStack.isEmpty()){
            return actionStack.pop();
        }
        if(forceEcho) {
            forceEcho=false;
            return Action.echo(drone.getDirection());
        }

        //On est sur de la terre et on a pas scan
        if(!onSea && map.needScanSecond()){
            map.setScan();
            return Action.scan();
        }
        //On est sur la mer
        else if(onSea){
            //Pas de terre en vue et il faut faire demi  tour
            if(map.needTurn()){
                if(traversalDirection==null){
                    traversalDirection=map.getTurn();
                }
                Direction currentDirection=drone.getDirection();

                uTurn(currentDirection,traversalDirection);

                checkDanger=true;
                onSea=false;

                return actionStack.pop();

            }
            //On est sur la mer mais dans la direction actuelle il y a de la terre
            else{
                return Action.fly();
            }
        }
        //Si on est sur la mer mais on a deja scan le lieu courant, on fly et on prévoit un scan juste après
        else {
            return Action.fly();
        }

    }

    public void acknowledgeResults(Result result){
        Action lastAction = bot.getLastAction();


        switch(lastAction.getAction()) {

            case HEADING:
                drone.heading(lastAction.getStringParameter(Action.ParameterName.DIRECTION));
                break;
            case FLY:
                drone.fly();
                break;
            case ECHO:
                map.setEcho(Direction.toDirection(lastAction.getStringParameter(Action.ParameterName.DIRECTION)), result.getEchoRange(), result.getEchoFound());
                if (result.getEchoFound().equals("GROUND")) {
                    onSea = false;
                    checkDanger = false;
                }
                if (map.needTurn() && checkDanger) {
                    forceStop = true;
                }
                break;


            case SCAN:
                String[] biomes = result.getScanBiomes();

                if (biomes.length == 1 && biomes[0].equals("OCEAN")) {
                    forceEcho = true;
                    onSea = true;
                } else onSea = false;

                //On vérifie les creeks trouvés et on les ajoute au tableau des creeks découverts
                String[] newCreeks = result.getCreeks();
                Collections.addAll(availableCreeks, newCreeks);
                break;

            case LAND:
                int people = bot.getLastAction().getIntParameter(Action.ParameterName.PEOPLE);
                String creek = bot.getLastAction().getStringParameter(Action.ParameterName.CREEK);
                map.setLand(creek,people);

                ModularStrategy landStrategy = new ModularStrategy();
                landStrategy.addTask(new ExploitTask(map,bot.getContracts()));
                landStrategy.addTask(new ExploreTask(map));
                landStrategy.addTask(new TransformTask(bot.getContracts(),bot,true));
                landStrategy.addTask(new GlimpseTask(map));
                landStrategy.addTask(new GroundMoveTask(map,bot.getContracts()));
                landStrategy.addTask(new TransformTask(bot.getContracts(),bot,false));
                bot.setStrategy(landStrategy);
        }
    }
}