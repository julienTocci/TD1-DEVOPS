package fr.unice.polytech.qgl.qbc;

import eu.ace_design.island.bot.IExplorerRaid;
import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.ActionGenerator;
import fr.unice.polytech.qgl.qbc.strategy.Bot;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * Classe principale du bot
 *
 * @author Robin Alonzo
 */
public class Explorer implements IExplorerRaid {


    Map map;
    Drone drone;
    String heading;
    int initialBudget;
    int budget;
    int men;
    Contracts contracts;
    int phase;
    Bot bot;

    int turnNumber;

    // Les différentes phases de jeu
    final static int PHASE_SEARCH_ISLAND = 0; // La drone n'a pas encore trouvé l'ile
    final static int PHASE_SCAN_ISLAND = 1; // Le drone a trouvé l'ile et doit analyser les biomes et les creeks
    final static int PHASE_LANDED = 2; // Des hommes ont débarqués sur l'ile

    // Initialisation du Logger log4j
    static Logger logger = Logger.getLogger(Explorer.class);

    public void initialize(String jsonContext) {
        bot=new Bot(jsonContext);
        BasicConfigurator.configure();

        JSONObject context = new JSONObject(jsonContext);

        initialBudget = context.getInt("budget");
        budget = initialBudget;

        men = context.getInt("men");

        contracts = new Contracts(context.getJSONArray("contracts"));

        heading = context.getString("heading");
        drone = new Drone(heading);
        map = new Map(drone);

        phase = PHASE_SEARCH_ISLAND;

        turnNumber = 0;
    }

    public String takeDecision() {
        try{
            turnNumber += 1;
            logger.info("Turn number: "+turnNumber);
            String decision = bot.takeDecision();
            logger.info(decision);
            return decision;
        } catch (Exception e){
            logger.error("",e);
            return ActionGenerator.stop();
        }
    }

    public void acknowledgeResults(String results) {
        logger.info(results);
        bot.acknowledgeResults(results);

    }

}
