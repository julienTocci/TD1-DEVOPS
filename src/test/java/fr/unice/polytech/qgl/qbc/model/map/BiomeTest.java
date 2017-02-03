package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.Resource;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests pour les biomes
 *
 * @author Robin Alonzo
 */
public class BiomeTest {

    @Test
    public void testGetResources() throws Exception {

        assertTrue(Biome.OCEAN.getResources().contains(Resource.FISH));
        assertTrue(Biome.LAKE.getResources().contains(Resource.FISH));

        assertTrue(Biome.ALPINE.getResources().contains(Resource.FLOWER));
        assertTrue(Biome.GLACIER.getResources().contains(Resource.FLOWER));
        assertTrue(Biome.MANGROVE.getResources().contains(Resource.FLOWER));

        assertTrue(Biome.TROPICAL_RAIN_FOREST.getResources().contains(Resource.FRUITS));
        assertTrue(Biome.TROPICAL_SEASONAL_FOREST.getResources().contains(Resource.FRUITS));

        assertTrue(Biome.GRASSLAND.getResources().contains(Resource.FUR));
        assertTrue(Biome.TUNDRA.getResources().contains(Resource.FUR));
        assertTrue(Biome.SHRUBLAND.getResources().contains(Resource.FUR));
        assertTrue(Biome.TEMPERATE_RAIN_FOREST.getResources().contains(Resource.FUR));

        assertTrue(Biome.ALPINE.getResources().contains(Resource.ORE));
        assertTrue(Biome.SUB_TROPICAL_DESERT.getResources().contains(Resource.ORE));
        assertTrue(Biome.TEMPERATE_DESERT.getResources().contains(Resource.ORE));

        assertTrue(Biome.BEACH.getResources().contains(Resource.QUARTZ));
        assertTrue(Biome.SUB_TROPICAL_DESERT.getResources().contains(Resource.QUARTZ));
        assertTrue(Biome.TEMPERATE_DESERT.getResources().contains(Resource.QUARTZ));

        assertTrue(Biome.TROPICAL_RAIN_FOREST.getResources().contains(Resource.SUGAR_CANE));
        assertTrue(Biome.TROPICAL_SEASONAL_FOREST.getResources().contains(Resource.SUGAR_CANE));

        assertTrue(Biome.TROPICAL_RAIN_FOREST.getResources().contains(Resource.WOOD));
        assertTrue(Biome.TEMPERATE_DECIDUOUS_FOREST.getResources().contains(Resource.WOOD));
        assertTrue(Biome.TEMPERATE_RAIN_FOREST.getResources().contains(Resource.WOOD));
        assertTrue(Biome.TROPICAL_SEASONAL_FOREST.getResources().contains(Resource.WOOD));
        assertTrue(Biome.MANGROVE.getResources().contains(Resource.WOOD));

    }

    @Test
    public void containsResourceTest(){
        assertFalse(Biome.GRASSLAND.containsResource(Resource.FISH));
        assertTrue(Biome.LAKE.containsResource(Resource.FISH));
        assertTrue(Biome.BEACH.containsResource(Resource.QUARTZ));
        assertFalse(Biome.BEACH.containsResource(Resource.FUR));
    }

}