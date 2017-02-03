package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;

/**
 * Interface pour les strategies. Les stragtégies devront implémenter cette interface.
 *
 * @author Robin Alonzo
 */
public interface Strategy  {
    Action takeDecision();

    void acknowledgeResults(Result results);
}
