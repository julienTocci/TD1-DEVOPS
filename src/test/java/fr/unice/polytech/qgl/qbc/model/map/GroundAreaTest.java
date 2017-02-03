package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.ground.GroundArea;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceCondition;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Tests pour la classe GroundArea
 *
 * @author Robin Alonzo
 */
public class GroundAreaTest {
    @Test
    public void testSetScouted() throws Exception {
        GroundArea area = new GroundArea();
        assertFalse(area.isScouted());
        area.setScouted();
        assertTrue(area.isScouted());
    }

    @Test
    public void testSetExplored() throws Exception {
        GroundArea area = new GroundArea();
        assertFalse(area.isExplored());
        area.setExplored();
        assertTrue(area.isExplored());
        assertTrue(area.isScouted());
    }

    @Test
    public void testSetReachable() throws Exception {
        GroundArea area = new GroundArea();
        Set<Resource> resources = new TreeSet<>();
        resources.add(Resource.FISH);
        area.addExploredResource(Resource.FISH, ResourceAmount.HIGH, ResourceCondition.EASY);
        area.setReachable(false);
        assertFalse(area.isPossibleResource(Resource.FISH));
        assertFalse(area.arePossibleResources(resources));
        assertFalse(area.isExploitableResource(Resource.FISH));
        area.setReachable(true);
        assertTrue(area.isPossibleResource(Resource.FISH));
        assertTrue(area.arePossibleResources(resources));
        assertTrue(area.isExploitableResource(Resource.FISH));
    }

    @Test
    public void testResetExplore() throws Exception {
        GroundArea area = new GroundArea();
        area.setExplored();
        area.resetExplore();
        assertFalse(area.isScouted());
        assertFalse(area.isExplored());
    }

    @Test
    public void testResetScout() throws Exception {
        GroundArea area = new GroundArea();
        area.setScouted();
        area.resetScout();
        assertFalse(area.isScouted());
    }

    @Test
    public void testAddScoutedResource() throws Exception {
        GroundArea area = new GroundArea();
        area.setReachable(true);
        assertTrue(area.isPossibleResource(Resource.FISH));
        assertTrue(area.isPossibleResource(Resource.FLOWER));
        area.addScoutedResource(Resource.FLOWER);
        assertFalse(area.isPossibleResource(Resource.FISH));
        assertTrue(area.isPossibleResource(Resource.FLOWER));
        Set<Resource> resource1 = new TreeSet<>();
        resource1.add(Resource.FLOWER);
        assertTrue(area.arePossibleResources(resource1));
        Set<Resource> resource2 = new TreeSet<>();
        resource2.add(Resource.QUARTZ);
        assertFalse(area.arePossibleResources(resource2));
    }

    @Test
    public void testAddExploredResource() throws Exception {
        GroundArea area = new GroundArea();
        assertFalse(area.isExploitableResource(Resource.FRUITS));
        area.addExploredResource(Resource.FRUITS,ResourceAmount.HIGH,ResourceCondition.FAIR);
        assertTrue(area.isExploitableResource(Resource.FRUITS));
    }

    @Test
    public void testSetGlimpse() throws Exception {
        GroundArea area = new GroundArea();
        assertTrue(area.isPossibleResource(Resource.FISH));
        area.setGlimpse(Biome.TEMPERATE_DECIDUOUS_FOREST,0.0);
        assertTrue(area.isPossibleResource(Resource.WOOD));
        assertFalse(area.isPossibleResource(Resource.FISH));
    }

    @Test
    public void testIsExploitableResource(){
        GroundArea area=new GroundArea();
        area.setReachable(true);
        //La ressource n'est pas présente
        assertFalse(area.isExploitableResource(Resource.WOOD));
        //La ressource est présente et atteignable
        area.addExploredResource(Resource.WOOD,ResourceAmount.HIGH,ResourceCondition.FAIR);
        assertTrue(area.isExploitableResource(Resource.WOOD));
        //La ressource est présente mais inatteignable
        area.setReachable(false);
        assertFalse(area.isExploitableResource(Resource.WOOD));

    }

    @Test
    public void testIsPossibleResource(){
        GroundArea area=new GroundArea();
        area.setReachable(false);
        //area est inatteignable
        assertFalse(area.isPossibleResource(Resource.FUR));

        //area est atteignable, n'a pas été exploré, ni scouted
        area.setReachable(true);
        assertTrue(area.isPossibleResource(Resource.FUR));

        //area est atteignable, a été scout mais la ressource n'est pas présente
        area.setScouted();
        assertFalse(area.isPossibleResource(Resource.FUR));

        //area est atteignable, a été exploré et la ressource est présente
        area.addExploredResource(Resource.FUR,ResourceAmount.HIGH,ResourceCondition.FAIR);
        assertTrue(area.isPossibleResource(Resource.FUR));

        GroundArea area2 = new GroundArea();
        assertTrue(area2.isPossibleResource(Resource.WOOD));
        area2.setGlimpse(Biome.BEACH,100.0);
        assertTrue(area2.isPossibleResource(Resource.QUARTZ));
        assertFalse(area2.isPossibleResource(Resource.WOOD));
    }

    @Test
    public void testArePossibleResources(){
        GroundArea area=new GroundArea();
        //Le set de ressources qu'on va utiliser
        Set<Resource> checkResources=new TreeSet<>();
        checkResources.add(Resource.WOOD);
        checkResources.add(Resource.FISH);

        //Aucune des ressources n'est présente mais on n'a pas scout ou exploré
        assertTrue(area.arePossibleResources(checkResources));

        //Aucune des ressources n'est présente, et on a scout
        area.setScouted();
        assertFalse(area.arePossibleResources(checkResources));

        //Une des ressources est "possible"
        area.addExploredResource(Resource.FISH,ResourceAmount.HIGH,ResourceCondition.FAIR);
        assertTrue(area.arePossibleResources(checkResources));
    }
}