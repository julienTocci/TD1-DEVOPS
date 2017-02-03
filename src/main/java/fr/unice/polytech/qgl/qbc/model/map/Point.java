package fr.unice.polytech.qgl.qbc.model.map;

/**
 * Classe représentant un couple de coordonnées (X,Y)
 *
 * @author Robin Alonzo
 */
public class Point {

    private int x;
    private int y;

    /**
     * Créé un point
     * @param x Coordonnée en X
     * @param y Coordonnée en Y
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Créé un point en (0,0)
     */
    public Point(){
        this(0,0);
    }

    /**
     * Constructeur par recopie
     * @param p Point à copier
     */
    public Point(Point p){
        this(p.getX(),p.getY());
    }

    public Point(Point p, Direction d, int distance){
        this(p);
        move(d,distance);
    }

    public Point(Point p, Direction d){
        this(p,d,1);
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }



    /**
     * Bouge le point d'une unité dans une direction
     * @param direction La direction dans laquelle bouger
     */
    public void move(Direction direction){
        move(direction,1);
    }

    /**
     * Bouge le point dans une direction
     * @param direction La direction dans laquelle bouger
     * @param distance La distance effectuée
     */
    public void move(Direction direction, int distance){
        x += direction.getDeltaX()*distance;
        y += direction.getDeltaY()*distance;
    }

    /**
     * Egalité des coordonnées
     * @param point Un autre point pour faire la comparaison
     * @return Vrai si les deux points sont égaux, faux sinon
     */
    @Override
    public boolean equals(Object point) {
        if (this == point) return true;
        if (point == null || getClass() != point.getClass()) return false;

        Point p = (Point) point;

        return x == p.x && y == p.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
