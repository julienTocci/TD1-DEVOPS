package fr.unice.polytech.qgl.qbc.model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant les ressources
 *
 * @author Robin Alonzo
 */
public enum Resource {

    /**
     * Liste de toutes les ressources
     */
    // Ressources primaires
    FISH(true), FLOWER(true), FRUITS(true), FUR(true), ORE(true), QUARTZ(true), SUGAR_CANE(true), WOOD(true),
    // Ressources secondaires
    GLASS(false), INGOT(false), LEATHER(false), PLANK(false), RUM(false);

    boolean primary;

    /**
     * Constructeur de ressources
     * @param primary Vrai si c'est une ressource primaire, faux sinon
     */
    Resource(boolean primary){
        this.primary = primary;
    }

    /**
     * @return Vrai si la ressource est une ressource primaire² faux sinon
     */
    public boolean isPrimary(){
        return primary;
    }

    public static Resource toResource(String resource){
        return Resource.valueOf(resource);
    }

    public List<Pair<Resource,Integer>> requiredPrimary(Resource manufactured, int amount){
        final double DELTA=1.1;
        List<Pair<Resource,Integer>> required=new ArrayList<>();

        switch(manufactured){
            case GLASS:
                required.add(new Pair<>(QUARTZ,(int)(5*DELTA*amount)));
                required.add(new Pair<>(WOOD,(int)(10*DELTA*amount)));
                break;
            case PLANK:
                required.add(new Pair<>(WOOD,(int)Math.ceil((double)amount/4)));
                break;
            case INGOT:
                required.add(new Pair<>(ORE,(int)(5*DELTA*amount)));
                required.add(new Pair<>(WOOD,(int)(5*DELTA*amount)));
                break;
            case LEATHER:
                required.add(new Pair<>(FUR,3*(int)DELTA*amount));
                break;
            case RUM:
                required.add(new Pair<>(SUGAR_CANE,(int)(10*DELTA*amount)));
                required.add(new Pair<>(FRUITS,(int)DELTA*amount));
                break;


        }
        return required;
    }
}
