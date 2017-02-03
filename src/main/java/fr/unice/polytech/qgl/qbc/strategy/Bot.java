package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.DroneMoveTask;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.EchoTask;
import fr.unice.polytech.qgl.qbc.strategy.tasks.air.ScanTask;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * Classe qui aura pour responsabilité de prendre les décisions et de récolter les donnés
 *
 * @author Robin Alonzo
 */
public class Bot {
    private Map map;
    private String heading;
    private Drone drone;
    private int men;
    private Strategy strat;
    private Contracts contracts;
    int initialBudget;
    int budget;

    private Action lastAction;

    static Logger logger = Logger.getLogger(Bot.class);

    public Bot(String jsonContext){
        JSONObject context = new JSONObject(jsonContext);
        heading = context.getString("heading");
        contracts=new Contracts(context.getJSONArray("contracts"));
        drone = new Drone(heading);
        map = new Map(drone);
        men = context.getInt("men");
        strat = new ModularStrategy()
                .addTask(new EchoTask(map))
                .addTask(new ScanTask(map,drone,this))
                .addTask(new DroneMoveTask(map));
        initialBudget = context.getInt("budget");
        budget = initialBudget;
    }

    public int getMen(){ return men; }

    public Contracts getContracts(){ return contracts; }

    public Drone getDrone(){
        return drone;
    }

    public Action getLastAction(){
        return lastAction;
    }

    public int getBudget(){ return budget;}

    public void setStrategy(Strategy newStrat){
        strat=newStrat;
    }

    public String takeDecision(){
        Action decision;
        if(budget < 400){
            decision = Action.stop();
        }
        else{
            decision = strat.takeDecision();
        }
        lastAction = decision;
        logger.info(decision.getAction());
        return decision.toJSON();
    }

    public void acknowledgeResults(String results) {
        Result result = new Result(results);
        budget -= result.getCost();
        strat.acknowledgeResults(result);
    }

}
