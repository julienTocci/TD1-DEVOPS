package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.protocol.Action;
import fr.unice.polytech.qgl.qbc.protocol.Result;
import fr.unice.polytech.qgl.qbc.strategy.tasks.Task;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour la stratégie modulaire
 *
 * @author Robin Alonzo
 */
public class ModularStrategyTest {


    @Test
    public void testTakeDecision() throws Exception {
        ModularStrategy strategy = new ModularStrategy();
        assertEquals(Action.ActionName.STOP, strategy.takeDecision().getAction());

        Task task = mock(Task.class);
        when(task.getDecision()).thenReturn(Action.fly());
        strategy.addTask(task);

        assertEquals(Action.ActionName.FLY, strategy.takeDecision().getAction());

        Task task2 = mock(Task.class);
        when(task2.getDecision()).thenReturn(Action.scan());
        strategy.addTask(task2);
        assertEquals(Action.ActionName.FLY, strategy.takeDecision().getAction());

        when(task.getDecision()).thenReturn(null);
        assertEquals(Action.ActionName.SCAN, strategy.takeDecision().getAction());

        when(task2.getDecision()).thenReturn(null);
        assertEquals(Action.ActionName.STOP, strategy.takeDecision().getAction());
    }

    @Test
    public void testAcknowledgeResults() throws Exception {
        ModularStrategy strategy = new ModularStrategy();
        Result result = mock(Result.class);
        Task task = mock(Task.class);

        strategy.addTask(task);
        strategy.acknowledgeResults(result);

        verify(task,never()).acknowledgeResult(result);
        strategy.takeDecision();
        strategy.acknowledgeResults(result);
        verify(task,never()).acknowledgeResult(result);

        when(task.getDecision()).thenReturn(Action.fly());
        strategy.takeDecision();
        strategy.acknowledgeResults(result);
        verify(task,times(1)).acknowledgeResult(result);

        Task task2 = mock(Task.class);
        when(task2.getDecision()).thenReturn(Action.scan());
        strategy.addTask(task2);
        strategy.takeDecision();
        strategy.acknowledgeResults(result);
        verify(task,times(2)).acknowledgeResult(result);
        verify(task2,never()).acknowledgeResult(result);

        when(task.getDecision()).thenReturn(null);
        strategy.takeDecision();
        strategy.acknowledgeResults(result);
        verify(task,times(2)).acknowledgeResult(result);
        verify(task2,times(1)).acknowledgeResult(result);

        when(task2.getDecision()).thenReturn(null);
        strategy.takeDecision();
        strategy.acknowledgeResults(result);
        verify(task,times(2)).acknowledgeResult(result);
        verify(task2,times(1)).acknowledgeResult(result);
    }
}