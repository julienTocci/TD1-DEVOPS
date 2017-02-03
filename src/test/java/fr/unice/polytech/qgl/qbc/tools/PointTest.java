package fr.unice.polytech.qgl.qbc.tools;

import fr.unice.polytech.qgl.qbc.model.map.Direction;
import fr.unice.polytech.qgl.qbc.model.map.Point;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Tests pour la classe Point
 *
 * @author Robin Alonzo
 */
public class PointTest {


    @Test
    public void testMove() throws Exception {
        Point p = new Point(0,0);
        p.move(Direction.NORTH);
        assertEquals(1,p.getY());
        assertEquals(0,p.getX());
        p.move(Direction.WEST);
        assertEquals(1,p.getY());
        assertEquals(-1,p.getX());
    }

    @Test
    public void testMove1() throws Exception {
        Point p = new Point(0,0);
        p.move(Direction.NORTH,5);
        assertEquals(5,p.getY());
        assertEquals(0,p.getX());
        p.move(Direction.WEST,2);
        assertEquals(5,p.getY());
        assertEquals(-2,p.getX());
    }

    @Test
    public void testEquals() throws Exception {
        Point p1 = new Point();
        Point p2 = new Point(5,-3);
        assertFalse(p1.equals(p2));
        p1.setX(5);
        p1.setY(-3);
        assertTrue(p2.equals(p1));
        Point p3 = new Point(p2);
        assertTrue(p3.equals(p2));

        Set<Point> set = new HashSet<>();
        set.add(new Point(5,3));
        assertFalse(set.contains(new Point(3, 5)));
        assertTrue(set.contains(new Point(5, 3)));
    }
}