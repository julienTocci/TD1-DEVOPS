package fr.unice.polytech.qgl.qbc.strategy.tasks.air;

import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.map.Map;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.AirScanStrategy;
import fr.unice.polytech.qgl.qbc.strategy.Bot;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;

/**
 * Effectue un SCAN si la zone n'a pas été scannée avant
 * Change la stratégie du bot à AirScanStrategy si il scanne de la terre
 *
 * @author Robin Alonzo
 */
public class ScanTask implements Task {

    private Map map;
    private Bot bot;
    private Drone drone;

    public ScanTask(Map map, Drone drone, Bot bot){
        this.map = map;
        this.bot = bot;
        this.drone = drone;
    }

    @Override
    public Action getDecision() {
        if(map.needScanSecond()) {
            return Action.scan();
        }
        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        String[] biomes = result.getScanBiomes();
        map.setScan();
        if (biomes.length == 1 && "OCEAN".equals(biomes[0])) {
            map.setOcean(true);
        }
        else {
            map.setOcean(false);
            bot.setStrategy(new AirScanStrategy(bot,map,drone));
            //TODO déplacer le changement de stratégie, ce n'est pas la responsabilité de la Task
        }
    }
}
