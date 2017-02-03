package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Stratégie modulaire constituée de tâches
 *
 * @author Robin Alonzo
 */
public class ModularStrategy implements Strategy {

    static Logger logger = Logger.getLogger(ModularStrategy.class);

    private List<Task> tasks;
    private Task lastTask;

    public ModularStrategy(){
        tasks = new LinkedList<>();
    }

    /**
     * Ajoute une tâche à la liste des tâches à effectuer
     * Les tâches seront exécutés dans l'ordre dans lesquelles elles ont été ajoutés
     * @param task La tâche à ajouter à la stratégie
     */
    public ModularStrategy addTask(Task task){
        tasks.add(task);
        return this;
    }

    @Override
    public Action takeDecision() {
        for(Task task : tasks){
            Action action = task.getDecision();
            if(action != null){
                lastTask = task;
                return action;
            }
        }
        return Action.stop();
    }

    @Override
    public void acknowledgeResults(Result results) {
        if(lastTask != null){
            lastTask.acknowledgeResult(results);
        }
        lastTask = null;
    }
}
