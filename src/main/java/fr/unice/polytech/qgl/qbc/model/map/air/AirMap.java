package fr.unice.polytech.qgl.qbc.model.map.air;

import fr.unice.polytech.qgl.qbc.model.map.*;
import fr.unice.polytech.qgl.qbc.protocol.Action;

import java.util.HashSet;
import java.util.List;

/**
 * Classe regroupant les information que l'on peut tirer des actions ECHO
 *
 * @author Robin Alonzo
 */
public class AirMap {

    // Limites de la zone d'exploration
    private int minX = Integer.MIN_VALUE;
    private int maxX = Integer.MAX_VALUE;
    private int minY = Integer.MIN_VALUE;
    private int maxY = Integer.MAX_VALUE;

    private Matrix<AirArea> biomeMap;
    private Drone drone;

    /**
     * Créé une instance de AirMap
     * @param drone Un drone à sa position de départ
     */
    public AirMap(Drone drone){
        biomeMap=new Matrix<>(AirArea.class);
        this.drone = drone;
        setLimit(drone.getDirection().invert(),1);
    }

    /**
     * Définit les limites de la map
     * @param direction La direction dans la quelle la limite se trouve
     * @param distance Le nombre de cases séparant le drone de la limite
     */
    private void setLimit(Direction direction, int distance){
        int dx = direction.getDeltaX();
        int dy = direction.getDeltaY();
        if(dx == 1)
            maxX = drone.getX()+distance;
        if(dx ==-1)
            minX = drone.getX()-distance;
        if(dy == 1)
            maxY = drone.getY()+distance;
        if(dy ==-1)
            minY = drone.getY()-distance;
    }

    /**
     * Stocke les informations que l'on obtien suit à un ECHO
     * @param direction La direction du echo (N,S,E ou W)
     * @param range La distance telle que donnée dans la réponse du ECHO
     * @param found Ce que le echo trouve à la distance donnée (GROUND ou OUT_OF_RANGE)
     */
    public void setEcho(Direction direction, int range, String found){
        EchoStatus echo = EchoStatus.toStatus(found);
        if(range == 0 && echo == EchoStatus.GROUND){
            // l'echo renvoie 0 si on est au dessus du sol ou si il y a du sol sur la case imédiatement devant
            if(get(drone.getX(),drone.getY()).isOcean()){
                Point pos = drone.getPosition();
                pos.move(direction);
                get(pos).setEcho(EchoStatus.GROUND);
            }
            else{
                get().setEcho(EchoStatus.MAYBE_GROUND);
            }
        }else{
            if(echo == EchoStatus.OUT_OF_RANGE){
                setLimit(direction,range+1);
            }
            get(drone.getX() + ((range + 1) * direction.getDeltaX()),
                drone.getY() + ((range + 1) * direction.getDeltaY())).setEcho(echo);
            for (int i = 0; i <= range; i++) {
                get(drone.getX() + (i * direction.getDeltaX()),
                    drone.getY() + (i * direction.getDeltaY())).setEcho(EchoStatus.OCEAN);
            }
        }
    }

    public void setOcean(boolean ocean){
        if(ocean){
           get().setEcho(EchoStatus.OCEAN);
        }
        else{
            get().setEcho(EchoStatus.GROUND);
        }
    }

    /**
     * Donne le resultat que donnerait un echo en fonction des informations déjà collectés
     * @param direction La direction du echo (N,S,E ou W)
     * @return Une des constantes statiques ECHO_ de cette classe
     */
    public EchoStatus getEcho(Direction direction){
        int i = 0;
        while(true){
            AirArea area = get(drone.getX() + i * direction.getDeltaX(), drone.getY() + i * direction.getDeltaY());
            if(area.getEcho() != EchoStatus.OCEAN){
                return area.getEcho();
            }
            i += 1;
        }
    }

    /**
     * Vrai si des information suplémentaires peuvent être obtenues suit a un echo dans unecertaine direction
     * @param direction La direction à vérifier
     * @return Un booléen
     */
    public boolean needEcho(Direction direction){
        EchoStatus echo = getEcho(direction);
        return echo == EchoStatus.UNKNOWN;
    }

    /**
     * Vrai si les coordonnés (x,y) font partie de la zone d'exploration, faux sinon , ou si c'est indeterminé
     * @param x La position en X de la case
     * @param y La position en Y de la case
     * @return Un booléen
     */
    public boolean isInRange(int x, int y){
        return  x < maxX && x > minX && y < maxY && y > minY && isXInRange(x) && isYInRange(y);
    }

    private boolean isXInRange(int x){
        return  (minX != Integer.MIN_VALUE && x > minX && x < drone.getX()) ||
                (maxX != Integer.MAX_VALUE && x < maxX && x > drone.getX());
    }

    private boolean isYInRange(int y){
        return  (minY != Integer.MIN_VALUE && y > minY && y < drone.getY()) ||
                (maxY != Integer.MAX_VALUE && y < maxY && y > drone.getY());
    }

    /**
     * Renvoie la caractèristique de la case à la position (X,Y)
     * @param x La position en X de la case
     * @param y La position en Y de la case
     * @return Une des constantes statiques ECHO_ de cette classe
     */
    public AirArea get(int x,int y){
        AirArea area = biomeMap.get(x,y);
        if(x >= maxX || x <= minX || y >= maxY || y <= minY) {
            area.setOutOfRange();
        }
        return area;
    }

    public AirArea get(Point p){
        return get(p.getX(),p.getY());
    }

    public AirArea get(){
        return get(drone.getX(),drone.getY());
    }

    public Action findClosestUnknown(){

        HashSet<Point> visited = new HashSet<>();
        Point position = new Point(drone.getX(),drone.getY());
        Direction direction = drone.getDirection();
        int counter = 0;

        Point pLeft = new Point(position.getX()+direction.getDeltaX()+direction.turnLeft().getDeltaX(),
                position.getY()+direction.getDeltaY()+direction.turnLeft().getDeltaY());
        Integer left = findClosestUnknown(visited, direction.turnLeft(),pLeft,counter);

        Point pForward = new Point(position.getX()+direction.getDeltaX(),position.getY()+direction.getDeltaY());
        Integer forward = findClosestUnknown(visited,direction,pForward,counter);

        Point pRight = new Point(position.getX()+direction.getDeltaX()+direction.turnRight().getDeltaX(),
                position.getY()+direction.getDeltaY()+direction.turnRight().getDeltaY());
        Integer right = findClosestUnknown(visited, direction.turnRight(),pRight,counter);

        int min = Math.min(Math.min(left,right),forward);
        if(min == Integer.MAX_VALUE) {
            return null;
        }
        if(min == forward) {
            drone.fly();
            return Action.fly();
        }
        if(min == left) {
            drone.heading(direction.turnLeft().toString());
            return Action.heading(direction.turnLeft());
        }
        if(min == right) {
            drone.heading(direction.turnRight().toString());
            return Action.heading(direction.turnRight());
        }
        return null;
    }

    private Integer findClosestUnknown(HashSet<Point> visited, Direction direction, Point position, int counter) {
        counter++;
        EchoStatus found = get(position).getEcho();
        EchoStatus foundAfter = get(position.getX()+direction.getDeltaX(),position.getY()+direction.getDeltaY()).getEcho();
        if( found == EchoStatus.OUT_OF_RANGE ||
            foundAfter == EchoStatus.OUT_OF_RANGE ||
            counter >= 5 || visited.contains(position)) {
            return Integer.MAX_VALUE;
        }
        if(found == EchoStatus.UNKNOWN) {
            return counter;
        }
        visited.add(position);

        Point pLeft = new Point(position.getX()+direction.getDeltaX()+direction.turnLeft().getDeltaX(),
                                position.getY()+direction.getDeltaY()+direction.turnLeft().getDeltaY());
        Integer left = findClosestUnknown(visited, direction.turnLeft(),pLeft,counter);

        Point pForward = new Point(position.getX()+direction.getDeltaX(),
                                   position.getY()+direction.getDeltaY());
        Integer forward = findClosestUnknown(visited,direction,pForward,counter);

        Point pRight = new Point(position.getX()+direction.getDeltaX()+direction.turnRight().getDeltaX(),
                                 position.getY()+direction.getDeltaY()+direction.turnRight().getDeltaY());
        Integer right = findClosestUnknown(visited, direction.turnRight(),pRight,counter);

        return Math.min(Math.min(left,right),forward);
    }


    public int getLimit(Direction dir){
        if(dir==Direction.NORTH)
            return maxY;
        if(dir==Direction.SOUTH)
            return minY;
        if(dir==Direction.EAST)
            return maxX;
        if(dir==Direction.WEST)
            return minX;
        return 0;
    }

    public boolean needTurn(){
        return getEcho(drone.getDirection()) == EchoStatus.OUT_OF_RANGE;
    }


    /**On détermine si on va parcourir l'ile en remontant la map ou en descendant
     * Ou bien si on va la parcourir vers la droite ou vers la gauche
     * Pour cela on va prendre la direction du drone et comparer les distances entre les limites de la map
     * @return la direction générale selon laquelle on va parcourir l'ile
    */
    public Direction getTurn() {
       Direction currentDirection=drone.getDirection();
        if(currentDirection==Direction.EAST || currentDirection==Direction.WEST){ //La direction actuelle du drone est horizontale
            int y=drone.getY();
            if (Math.abs(getLimit(Direction.NORTH)-y)<Math.abs(getLimit(Direction.SOUTH)-y)){
                return Direction.SOUTH;
            }
            else {
                return Direction.NORTH;
            }
        }
        else { //Direction actuelle du drone est verticale
            int x = drone.getX();
            if (Math.abs(getLimit(Direction.EAST)-x) < Math.abs(getLimit(Direction.WEST)-x)) {
                return Direction.WEST;
            }
            else {
                return Direction.EAST;
            }
        }
    }

    private AirArea getBiome(int x, int y){
        if(biomeMap.get(x,y) == null){
            biomeMap.set(x,y,new AirArea());
        }

        //logger.info("X:"+x+" Y:"+y+" R:"+map.get(x,y).isReachable()+" minY:"+minY);
        return biomeMap.get(x,y);
    }

    public boolean needScan(){
        return getBiome(drone.getX(),drone.getY()).needScan();
    }

    public void setScan(){
        getBiome(drone.getX(),drone.getY()).setScan();
    }
    public void addScannedBiomes(List<Biome> biomesList){
        getBiome(drone.getX(),drone.getY()).setScan();
        getBiome(drone.getX(),drone.getY()).addBiomes(biomesList);

    }

}