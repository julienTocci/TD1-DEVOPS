package fr.unice.polytech.qgl.qbc.model.map.air;

import fr.unice.polytech.qgl.qbc.model.map.Direction;
import fr.unice.polytech.qgl.qbc.model.map.Point;

/**
 * Classe représentant le drone d'exploration
 *
 * @author Robin Alonzo
 */
public class Drone {

    Point position;
    Direction direction;

    /**
     * Créé un drone aux coordonnés
     * @param heading La direction du drone au départ
     */
    public Drone(String heading){
        position = new Point();
        direction = Direction.toDirection(heading);
    }

    /**
     * Avance le drone d'une case, comme après une action FLY
     */
    public void fly(){
        position.move(direction);
    }

    /**
     * Renvoie la position du drone sur l'axe X
     * @return Un entier
     */
    public int getX(){
        return position.getX();
    }

    /**
     * Renvoie la position du drone sur l'axe Y
     * @return Un entier
     */
    public int getY(){
        return position.getY();
    }

    /**
     * Renvoie la direction de du drone
     * @return Une direction
     */
    public Direction getDirection(){
        return direction;
    }

    public Point getPosition(){
        return new Point(position);
    }

    public void heading(String direction){
        fly();
        Direction dir = Direction.toDirection(direction);
        position.move(dir);
        this.direction = dir;
    }
}
