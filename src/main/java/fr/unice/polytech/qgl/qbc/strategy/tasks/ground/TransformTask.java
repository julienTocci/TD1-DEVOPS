package fr.unice.polytech.qgl.qbc.strategy.tasks.ground;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.Bot;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import javafx.util.Pair;

import java.util.List;

/**
 * TRANSFORM les ressources necessaires aux contrats de ressources manufacturés
 *
 * @author Robin Alonzo
 */
public class TransformTask implements Task {

    private final boolean onlyIfLowBudget;
    private Contracts contracts;
    private Bot bot;


    public TransformTask(Contracts contracts, Bot bot, boolean onlyIfLowBudget){
        this.contracts = contracts;
        this.bot=bot;
        this.onlyIfLowBudget = onlyIfLowBudget;
    }

    @Override
    public Action getDecision() {
        //Mesure de sécurité pour éviter d'user tout le budget restant suite à un transform
        if(onlyIfLowBudget && (bot.getBudget()>1200 || bot.getBudget()<500))
            return null;
        List<Pair<Resource,Integer>> toTransform = contracts.needTransform();
        if(toTransform!=null){
            return Action.transform(toTransform);
        }

        return null;
    }

    @Override
    public void acknowledgeResult(Result result) {
        Resource transformedResource = result.getTransformedKind();
        contracts.updateAmountResource(transformedResource,result.getTransformedAmount());
    }
}
