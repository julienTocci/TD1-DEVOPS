package fr.unice.polytech.qgl.qbc.model.map;

/**
 * Classe permettant de gérer les directions
 *
 * @author Robin Alonzo
 */
public enum Direction {

    NORTH, SOUTH, EAST, WEST;

    /**
     * Créé une direction
     * @param direction String représentant la direction (N,S,E ou W)
     */
    public static Direction toDirection(String direction){
        switch(direction){
            case "N":
                return NORTH;
            case "S":
                return SOUTH;
            case "E":
                return EAST;
            case "W":
                return WEST;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Donne une direction inverse
     * @return Un nouvel objet Direction représentant la direction modifiée
     */
    public Direction invert(){
        switch(this){
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            default:
                return null;
        }
    }

    /**
     * Donne une direction tournée à 90° dans le sens anti-horaire
     * @return Un nouvel objet Direction représentant la direction modifiée
     */
    public Direction turnLeft(){
        switch (this){
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
            case EAST:
                return NORTH;
            case WEST:
                return SOUTH;
            default:
                return null;
        }
    }

    /**
     * Donne une direction tournée à 90° dans le sens horaire
     * @return Un nouvel objet Direction représentant la direction modifiée
     */
    public Direction turnRight(){
        switch (this){
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
            case EAST:
                return SOUTH;
            case WEST:
                return NORTH;
            default:
                return null;
        }

    }

    /**
     * Renvoie la composante en X du vecteur direction
     * @return Un entier (-1, 0 ou 1)
     */
    public int getDeltaX(){
        switch (this){
            case EAST:
                return 1;
            case WEST:
                return -1;
            default:
                return 0;
        }
    }

    /**
     * Renvoie la composante en Y du vecteur direction
     * @return Un entier (-1, 0 ou 1)
     */
    public int getDeltaY() {
        switch (this){
            case NORTH:
                return 1;
            case SOUTH:
                return -1;
            default:
                return 0;
        }
    }

    /**
     * Convertie l'objet en un point cardinal
     * @return Une String (N,S,E ou W)
     */
    @Override
    public String toString(){
        return name().substring(0,1);
    }

}
