package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.Resource;

import java.util.Set;
import java.util.TreeSet;

/**
 * Enum gérant les biomes
 *
 * @author Robin Alonzo
 */
public enum Biome {

    OCEAN, LAKE, BEACH, GRASSLAND, //Common biomes
    MANGROVE, TROPICAL_RAIN_FOREST, TROPICAL_SEASONAL_FOREST, //Tropical biomes
    TEMPERATE_DECIDUOUS_FOREST, TEMPERATE_RAIN_FOREST, TEMPERATE_DESERT, //Temperate biomes
    TAIGA, SNOW, TUNDRA, ALPINE, GLACIER, //Nordic / Mountain Biomes
    SHRUBLAND, SUB_TROPICAL_DESERT; //Subtropical biomes

    /**
     * Donne les ressources disponibles dans un certain biome
     * @return Un set contenant les ressources disponibles
     */
    public Set<Resource> getResources(){
        Set<Resource> resources = new TreeSet<>();
        switch (this){
            case OCEAN:
            case LAKE:
                resources.add(Resource.FISH);
                break;
            case BEACH:
                resources.add(Resource.QUARTZ);
                break;
            case GRASSLAND:
            case SHRUBLAND:
            case TUNDRA:
                resources.add(Resource.FUR);
                break;
            case MANGROVE:
                resources.add(Resource.WOOD);
                resources.add(Resource.FLOWER);
                break;
            case TROPICAL_RAIN_FOREST:
            case TROPICAL_SEASONAL_FOREST:
                resources.add(Resource.FRUITS);
                resources.add(Resource.SUGAR_CANE);
                resources.add(Resource.WOOD);
                break;
            case TEMPERATE_RAIN_FOREST:
                resources.add(Resource.FUR);
            case TEMPERATE_DECIDUOUS_FOREST:
            case TAIGA:
                resources.add(Resource.WOOD);
                break;
            case SUB_TROPICAL_DESERT:
            case TEMPERATE_DESERT:
                resources.add(Resource.QUARTZ);
                resources.add(Resource.ORE);
                break;
            case SNOW:
                break;
            case ALPINE:
                resources.add(Resource.FLOWER);
                resources.add(Resource.ORE);
                break;
            case GLACIER:
                resources.add(Resource.FLOWER);
                break;
        }
        return resources;
    }

    /**
     * Permet de savoir si le biome contient une ressource en particulier
     * @param resource La ressource qu'on cherche
     * @return Vrai si elle peut etre présente, faux sinon
     */
    public boolean containsResource(Resource resource){
        return getResources().contains(resource);
    }

}
