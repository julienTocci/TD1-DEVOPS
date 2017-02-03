package fr.unice.polytech.qgl.qbc.model.map;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.map.air.Drone;
import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.air.AirMap;
import fr.unice.polytech.qgl.qbc.model.map.air.EchoStatus;
import fr.unice.polytech.qgl.qbc.model.map.ground.GroundMap;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import fr.unice.polytech.qgl.qbc.protocol.Action;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Classe dans laquelle se trouveront l'ensemble des informations concernant la map
 *
 * @author Robin Alonzo
 */
public class Map {
    static Logger logger = Logger.getLogger(Map.class);


    private AirMap airMap;
    private GroundMap groundMap;
    private Drone drone;

    public Map(Drone drone){
        this.drone = drone;
        airMap = new AirMap(drone);
    }

    /**
     * Permet de stocker les information obtenues grace à un echo
     * @param direction La direction du echo
     * @param range La distance donnée dans la réponse du ECHO
     * @param found Ce qui a été trouvé dans le ECHO
     */
    public void setEcho(Direction direction, int range, String found){
        airMap.setEcho(direction, range, found);
    }

    /**
     * Permet de stocker le fait que le drone se trouve au dessus de l'ocean ou du sol
     * @param ocean Vrai si c'est de l'ocean, faux sinon
     */
    public void setOcean(boolean ocean){
        airMap.setOcean(ocean);
    }

    /**
     * Determine si il est necessaire de faire un echo
     * @return La direction du echo à effectuer, null si ce n'est pas necessaire
     */
    public Direction needEcho(){
        Direction direction = drone.getDirection();
        if(airMap.needEcho(direction.turnLeft())){
            return direction.turnLeft();
        }
        if(airMap.needEcho(direction)){
            return direction;
        }
        if(airMap.needEcho(direction.turnRight())){
            return direction.turnRight();
        }
        return null;
    }

    public Action moveToUnknown(){
        return airMap.findClosestUnknown();
    }

    public boolean needScan() {
        return (airMap.get().getEcho() == EchoStatus.GROUND ||
                airMap.get().getEcho() == EchoStatus.MAYBE_GROUND);
    }

    public boolean needScanSecond() {
        return airMap.needScan();
    }

    public void setScan(){
        airMap.setScan();

    }

    public void addScannedBiomes(List<Biome> biomesList){
        airMap.addScannedBiomes(biomesList);
    }



    /**
     * Determine si on doit faire un EXPLOIT
     * @return La Ressoucer à exploiter, null si on ne doit pas exploiter
     * @param contracts Les contracts
     */
    public Resource needExploit(Contracts contracts) {
        return groundMap.needExploit(contracts);
    }

    /**
     * Determine si l'on a besoin d'effectuer un EXPLORE
     * @return Vrai si oui, faux sinon
     */
    public boolean needExplore() {
        return groundMap.needExplore();
    }

    /**
     * Dtermine la direction du SCOUT si on doit en faire un
     * @return La direction dans laquelle faire le SCOUT, null si ce n'est pas necessaire
     */
    public Direction needScout() {
        return groundMap.needScout();
    }

    /**
     * Determine la direction dans laquelle effectuer un MOVE_TO
     * @return La direction dans laquelle faire le MOVE_TO, null si on a nul part où aller
     */
    public Direction nextMove(Contracts contracts) {
        return groundMap.nextMove2(contracts);
    }

    public void setExplore(HashMap<Resource, ResourceAmount> resources) {
        groundMap.setExplore(resources);
    }

    public void setExploit(Resource exploitedResource) {
        groundMap.setExploit(exploitedResource);
    }

    public void moveRaid(Direction direction) {
        groundMap.moveRaid(direction);
    }

    public void setScout(Set<Resource> resources, Direction direction, boolean reachable) {
        groundMap.setScout(resources,direction,reachable);
    }
    public boolean needTurn(){
        return airMap.needTurn();
    }

    public Direction getTurn(){ return airMap.getTurn();}


    public void setLand(String creek, int people) {
        // TODO utiliser le parametre creek
        groundMap = new GroundMap(people);
    }

    /**
     * Enregistre les informations reçues suite à un GLIMPSE
     * @param direction La direction dans laquelle a été effectué le glimpse
     * @param distance La distance du biome trouvé
     * @param biome Un biome qui a été trouvé
     * @param proportion La proportion du biome dans cette zone
     */
    public void setGlimpse(Direction direction, int distance, Biome biome, double proportion) {
        groundMap.setGlimpse(direction,distance,biome,proportion);
    }

    /**
     * Determine la direction du GLIMPSE si on doit en faire un
     * @return La direction dans laquelle faire le GLIMPSE, null si ce n'est pas necessaire
     */
    public Direction needGlimpse() {
        return groundMap.glimpseAround();
    }

    /**
     * Permet d'enregistrer le fait qu'une zone de la map au sol est hors des limites
     * @param direction La direction dans laquelle se trouve la limite
     * @param distance La distance à laquelle se trouve la limite
     */
    public void setGroundOutOfBound(Direction direction, int distance) {
        groundMap.setUnreachable(direction,distance);
    }
}
