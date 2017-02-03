package fr.unice.polytech.qgl.qbc.model.map.ground;

import fr.unice.polytech.qgl.qbc.model.map.Direction;
import fr.unice.polytech.qgl.qbc.model.map.Point;

/**
 * Classe représentant un raid
 *
 * @author Robin Alonzo
 */
public class Raid {

    private Point position;
    private int people;

    public Raid(int people){
        position = new Point(0,0);
        this.people = people;
    }

    public void move(Direction direction){
        position.move(direction);
    }

    public Point getPosition(){
        return position;
    }

}
