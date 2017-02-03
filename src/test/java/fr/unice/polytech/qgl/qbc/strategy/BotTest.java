package fr.unice.polytech.qgl.qbc.strategy;

import fr.unice.polytech.qgl.qbc.protocol.Action;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Tests pour la classe Bot
 *
 * @author Robin Alonzo
 */
public class BotTest {

    @Test
    public void testTakeDecision() throws Exception {
        Bot bot = new Bot("{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 10000,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"W\"\n" +
                "}\n");
        Strategy mockedStrat = mock(Strategy.class);
        when(mockedStrat.takeDecision()).thenReturn(Action.stop());
        bot.setStrategy(mockedStrat);
        JSONObject json = new JSONObject(bot.takeDecision());
        assertEquals("stop", json.getString("action"));

        Bot bot2 = new Bot("{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 1,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"W\"\n" +
                "}\n");
        Strategy mockedStrat2 = mock(Strategy.class);
        bot.setStrategy(mockedStrat);
        json = new JSONObject(bot2.takeDecision());
        assertEquals("stop",json.getString("action"));
        assertEquals(Action.ActionName.STOP,bot2.getLastAction().getAction());
    }

    @Test
    public void testAcknowledgeResults() throws Exception {
        Bot bot = new Bot("{ \n" +
                "  \"men\": 12,\n" +
                "  \"budget\": 10000,\n" +
                "  \"contracts\": [\n" +
                "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 200, \"resource\": \"GLASS\" }\n" +
                "  ],\n" +
                "  \"heading\": \"W\"\n" +
                "}\n");
        Strategy mockedStrat = mock(Strategy.class);
        bot.setStrategy(mockedStrat);
        bot.acknowledgeResults("{ \"cost\": 6, \"extras\": { }, \"status\": \"OK\" }");
        verify(mockedStrat,times(1)).acknowledgeResults(any());
    }
}