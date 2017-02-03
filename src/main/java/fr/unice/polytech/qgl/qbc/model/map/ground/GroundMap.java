package fr.unice.polytech.qgl.qbc.model.map.ground;

import fr.unice.polytech.qgl.qbc.model.Contracts;
import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Set;

/**
 * Classe représentant une carte terrestre
 *
 * @author Robin Alonzo
 */
public class GroundMap {

    static Logger logger = Logger.getLogger(GroundMap.class);

    private Matrix<GroundArea> map;
    private Raid raid;

    // Les limites de la map
    private int minX = Integer.MIN_VALUE;
    private int maxX = Integer.MAX_VALUE;
    private int minY = Integer.MIN_VALUE;
    private int maxY = Integer.MAX_VALUE;

    public GroundMap(int people){
        map = new Matrix<>();
        raid = new Raid(people);
    }

    /**
     * Determine si on peut faire un exploit sur la case où se trouve le raid
     * @param contracts Les contracts sur les ressources que l'on veut collecter
     * @return La ressource à exploiter, null si ce n'est pas necessaire
     */
    public Resource needExploit(Contracts contracts) {
        Set<Resource> resources = contracts.getPrimaryResources();
        for(Resource resource : resources){
            if(currentArea().isExploitableResource(resource)){
                return resource;
            }
        }
        return null;
    }

    /**
     * Permet d'obtenir la case aux coordonnés (X,Y)
     * Elle initialise aussi la case si elle ne l'a pas été initialisée avant en tenant compte des limites de la map
     * @param x La position de la case sur l'axe X
     * @param y La position de la case sur l'axe Y
     * @return Un objet GroundArea représentant la case en question
     */
    private GroundArea get(int x, int y){
        if(map.get(x,y) == null){
            map.set(x,y,new GroundArea());
        }
        if(x >= maxX || x <= minX || y >= maxY || y <= minY){
            map.get(x,y).setReachable(false);
        }
        //logger.info("X:"+x+" Y:"+y+" R:"+map.get(x,y).isReachable()+" minY:"+minY);
        return map.get(x,y);
    }

    /**
     * Permet d'obtenir la case aux coordonnés (X,Y)
     * Elle initialise aussi la case si elle ne l'a pas été initialisée avant en tenant compte des limites de la map
     * @param position La position de la case
     * @return Un objet GroundArea représentant la case en question
     */
    public GroundArea get(Point position){
        return get(position.getX(),position.getY());
    }

    /**
     * Permet d'obtenir la case où se trouve le raid
     * Elle initialise aussi la case si elle ne l'a pas été initialisée avant en tenant compte des limites de la map
     * @return Un objet GroundArea représentant la case en question
     */
    public GroundArea currentArea(){
        return get(raid.getPosition());
    }

    /**
     * Determine si un EXPLORE est necessaire à la case courante
     * @return Vrai si il faut faire un EXPLORE, faux sinon
     */
    public boolean needExplore() {
        return !currentArea().isExplored();
    }

    /**
     * Determine si un SCOUT est necessaire
     * @return La direction dans laquelle faire le SCOUT, null si ce n'est pas necessaire
     */
    public Direction needScout() {
        for(Direction direction : Direction.values()){
            Point pos = new Point(raid.getPosition());
            pos.move(direction);
            if(!get(pos).isScouted()) return direction;
        }
        return null;
    }

    /**
     * Determine dans quelle direction bouger en tenant compte des informations collectés
     * @param contracts Les ressources que l'on cherche
     * @return La direction dans laquelle bouger, null si il n'y a null part où aller
     */
    public Direction nextMove(Contracts contracts) {
        Set<Resource> resources = contracts.getPrimaryResources();

        int xs, ys;
        xs = raid.getPosition().getX();
        ys = raid.getPosition().getY();

        for (int d = 1; d<10; d++) {
            for (int i = 0; i < d + 1; i++) {
                int x1 = xs - d + i;
                int y1 = ys - i;

                if(get(x1, y1).arePossibleResources(resources)){
                    return getDirectionTo(x1,y1);
                }

                int x2 = xs + d - i;
                int y2 = ys + i;

                if(get(x2, y2).arePossibleResources(resources)){
                    return getDirectionTo(x2,y2);
                }
            }


            for (int i = 1; i < d; i++) {
                int x1 = xs - i;
                int y1 = ys + d - i;

                if(get(x1, y1).arePossibleResources(resources)){
                    return getDirectionTo(x1,y1);
                }

                int x2 = xs + d - i;
                int y2 = ys - i;

                if(get(x2, y2).arePossibleResources(resources)){
                    return getDirectionTo(x2,y2);
                }
            }
        }
        return null;
    }

    public Direction nextMove2(Contracts contracts) {
        Set<Resource> resources = contracts.getPrimaryResources();
        Point pos = raid.getPosition();
        HashMap<Point, Integer> visited = new HashMap<>(); // <Position, Coût (nb de mouvement pour atteindre la position)>
        visited.put(pos, 0);

        int costLimit = 45;
        int north = nextMove2(resources,visited,new Point(pos,Direction.NORTH),0,costLimit);
        costLimit = Math.min(costLimit, north);
        int south = nextMove2(resources,visited,new Point(pos,Direction.SOUTH),0,costLimit);
        costLimit = Math.min(costLimit, south);
        int east = nextMove2(resources,visited,new Point(pos,Direction.EAST),0,costLimit);
        costLimit = Math.min(costLimit, east);
        int west = nextMove2(resources,visited,new Point(pos,Direction.WEST),0,costLimit);

        String r = "";
        for (Resource res : resources){
            r+= res + ",";
        }
        logger.info(r);
        logger.info("N:" + north + " S:" + south + " E:" + east + " W:" + west);

        int min = Math.min(Math.min(north, south), Math.min(east, west));
        if(min == Integer.MAX_VALUE) {
            if(!resources.isEmpty()) {
                // Si il reste des ressources à récolter et qu'il n'y a nul part où chercher, on rentre à la base
                // return getDirectionTo(0,0);
            }

            return null;
        }

        if(min == north){
            return Direction.NORTH;
        }
        if(min == south){
            return Direction.SOUTH;
        }
        if(min == east){
            return Direction.EAST;
        }
        if(min == west){
            return Direction.WEST;
        }
        return null;

    }

    private int nextMove2(Set<Resource> resources, HashMap<Point,Integer> visited, Point pos, int cost, int costLimit){
        Integer pastCost = visited.get(pos);

        if(cost > costLimit || (pastCost != null && pastCost <= cost) || !get(pos).isReachable() || get(pos).isPureOcean()){
            return Integer.MAX_VALUE;
        }
        visited.put(pos, cost);
        cost += 1;
        if(get(pos).arePossibleResources(resources)){
            if(!get(pos).isGlimpsed()) {
                cost += 100;
            }
            return cost;
        }

        int north = nextMove2(resources,visited,new Point(pos,Direction.NORTH),cost,costLimit);
        int south = nextMove2(resources,visited,new Point(pos,Direction.SOUTH),cost,costLimit);
        int east = nextMove2(resources,visited,new Point(pos,Direction.EAST),cost,costLimit);
        int west = nextMove2(resources,visited,new Point(pos,Direction.WEST),cost,costLimit);

        return Math.min(Math.min(north, south), Math.min(east, west));
    }

    /**
     * Donne la direction dans laquelle bouger pour aller à un point précis sur la map
     * @param x La coordonnée en X de la destination
     * @param y La coordonnée en Y de la destination
     * @return La direction dans laquelle bouger
     */
    private Direction getDirectionTo(int x, int y){
        //logger.info("Go to X:"+x+" Y:"+y+" R:"+get(x,y).isReachable());
        int rx = raid.getPosition().getX();
        int ry = raid.getPosition().getY();

        if(rx == x && ry == y) return null;

        int ax = Math.abs(x-rx);
        int ay = Math.abs(y-ry);

        if(ax>ay){
            if(x>rx){
                return Direction.EAST;
            }
            return Direction.WEST;

        }
        if(y>ry){
            return Direction.NORTH;
        }
        return Direction.SOUTH;

    }

    /**
     * Permet d'enregistrer les information suite à un EXPLORE
     * @param resources Une HashMap contenant les ressources trouvés et leur quantité associées
     */
    public void setExplore(HashMap<Resource, ResourceAmount> resources) {
        // TODO prendre aussi en compte les conditions des ressources (FAIR, HARSH...)
        currentArea().resetExplore();
        currentArea().setExplored();
        for (Resource resource : resources.keySet()){
            currentArea().addExploredResource(resource,resources.get(resource),ResourceCondition.FAIR);
        }
    }

    /**
     * Permet d'enregistrer les informations après avoir exploité une certaine ressource
     * @param exploitedResource La ressource exploitée
     */
    public void setExploit(Resource exploitedResource) {
        //currentArea().resetExplore();
        currentArea().setExploited(exploitedResource);
    }

    /**
     * Permet de bouger le raid d'une case dans une direction donnée
     * @param direction La direction dans laquelle bouger le raid
     */
    public void moveRaid(Direction direction) {
        raid.move(direction);
    }

    /**
     * Getter pour le raid
     * @return le raid de la map
     */
    public Raid getRaid(){ return raid;}

    /**
     * Permet d'enregistrer les informations suite à un SCOUT
     * @param resources L'ensemble de ressources trouvés
     * @param direction La direction dans laquelle le SCOUT a été fait
     * @param reachable Faux si la case est hors limite, vrai sinon
     */
    public void setScout(Set<Resource> resources, Direction direction, boolean reachable) {
        Point p = new Point(raid.getPosition());
        p.move(direction);
        get(p).resetScout();
        get(p).setScouted();
        /*
        if(!reachable){
            switch (direction){
                case NORTH:
                    maxY = p.getY();
                    break;
                case SOUTH:
                    minY = p.getY();
                    break;
                case EAST:
                    maxX = p.getX();
                    break;
                case WEST:
                    minX = p.getX();
                    break;
            }
        }
        get(p).setReachable(reachable);
        */
        if(!reachable){
            setUnreachable(direction,1);
        }
        for(Resource resource : resources){
            get(p).addScoutedResource(resource);
        }
    }

    public void setUnreachable(Direction direction, int distance){
        Point p = new Point(raid.getPosition());
        p.move(direction,distance);
        switch (direction){
            case NORTH:
                maxY = Math.min(p.getY(),maxY);
                break;
            case SOUTH:
                minY = Math.max(p.getY(),minY);
                break;
            case EAST:
                maxX = Math.min(p.getX(),maxX);
                break;
            case WEST:
                minX = Math.max(p.getX(),minX);
                break;
        }
        get(p).setReachable(false);
        //logger.info("Limit: X:"+p.getX()+" Y:"+p.getY()+" R:"+get(p).isReachable());
    }

    /**
     * Enregistre les informations reçues suite à un GLIMPSE
     * @param direction La direction dans laquelle a été effectué le glimpse
     * @param distance La distance du biome trouvé
     * @param biome Un biome qui a été trouvé
     * @param proportion La proportion du biome dans cette zone
     */
    public void setGlimpse(Direction direction, int distance, Biome biome, double proportion) {
        Point position = new Point(raid.getPosition());
        position.move(direction,distance);
        GroundArea area = get(position);
        area.setGlimpse(biome,proportion);
    }

    /**
     * Donne la direction dans laquelle faire un GLIMPSE de façon a GLIMPSer dans toutes les directions
     * @return La direction dans laquelle effectuer le GLIMPSE
     */
    public Direction glimpseAround() {
        for(Direction direction : Direction.values()){
            Point pos = new Point(raid.getPosition());
            for (int i = 0; i < 1; i++) {
                pos.move(direction);
                if(get(pos).isReachable() && !get(pos).isGlimpsed()) return direction;
            }
        }
        return null;
    }
}
