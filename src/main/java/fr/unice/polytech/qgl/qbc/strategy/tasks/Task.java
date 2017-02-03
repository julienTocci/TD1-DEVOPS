package fr.unice.polytech.qgl.qbc.strategy.tasks;

import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;

/**
 * Interface pour les tâches qui composes les stratégies
 *
 * @author Robin Alonzo
 */
public interface Task {

    /**
     * Effectue la tâche
     * @return L'action à effectuer, null si rien est à faire
     */
    Action getDecision();

    /**
     * Traite la réponse suite à l'action donnée
     * @param result Le resultat de l'action
     */
    void acknowledgeResult(Result result);

}
