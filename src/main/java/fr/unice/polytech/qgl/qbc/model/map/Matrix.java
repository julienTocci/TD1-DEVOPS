package fr.unice.polytech.qgl.qbc.model.map;

import java.util.HashMap;

/**
 * Cette classe permet de gérer des matrice à 2 dimensions de longueur et hauteur non définies
 *
 * @author Robin Alonzo
 */
public class Matrix<V> {
    private HashMap<Point, V> map;
    private final Class<V> defau;

    /**
     * Constructeur vide qui initialise la matrice avec la valeur "null" par défaut
     */
    public Matrix(){
        this(null);
    }

    /**
     * Constructeur permettant d'initialiser la matrice
     * @param defau Classe par défaut de l'objet renvoyée si on demande une valeur dans la matrice qui n'a jamais été initialisée
     */
    public Matrix(Class<V> defau){
        this.defau = defau;
        this.map = new HashMap<>();
    }

    /**
     * Methode permettant d'obtenir la valeur à la position x et y dans la matrice
     * @param x Position sur l'axe X
     * @param y Position sur l'axe Y
     * @return Valeur contenue dans la matrice
     */
    public V get(int x, int y) {
        Point p = new Point(x,y);
        return get(p);
    }

    public V get(Point point){
        if(map.get(point) == null){
            map.put(point,newInstance());
        }
        return map.get(point);
    }

    /**
     * Ajoute ou modifie une valeur dans une case de la matrice
     * @param x Position sur l'axe X
     * @param y Position sur l'axe Y
     * @param v Valeur à ajouter/modifier à cette position
     */
    public void set(int x, int y, V v) {
        Point p = new Point(x,y);
        set(p,v);
    }

    public void set(Point point, V v){
        map.put(point, v);
    }

    /**
     * Supprime une valeur à une certaine position de la matrice
     * @param x Position sur l'axe X
     * @param y Position sur l'axe Y
     */
    public void remove(int x, int y) {
        Point p = new Point(x,y);
        remove(p);
    }

    public void remove(Point point){
        map.remove(point);
    }

    private V newInstance(){
        if(defau == null) {
            return null;
        }
        V n = null;
        try {
            n = defau.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
            // Ne rien faire et renvoyer null
        }
        return n;
    }
}