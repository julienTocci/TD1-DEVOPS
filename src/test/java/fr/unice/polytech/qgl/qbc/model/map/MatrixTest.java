package fr.unice.polytech.qgl.qbc.model.map;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests pour la classe Matrix
 *
 * @author Robin Alonzo
 */
public class MatrixTest {

    @Test
    public void testGet() {
        Matrix m = new Matrix<>();
        assertNull(m.get(15, -5));
        Matrix<String> ms = new Matrix<>(String.class);
        assertEquals(new String(),ms.get(Integer.MIN_VALUE,Integer.MAX_VALUE));
        Matrix<Integer> mi = new Matrix<>();
        assertNull(mi.get(-7,-8));
    }

    @Test
    public void testSet() {
        Matrix<Integer> mi = new Matrix<>();
        mi.set(13,2,5);
        mi.set(-50,0,0);
        assertEquals(5, (long)mi.get(13,2));
        assertNull(mi.get(13, 3));
        assertEquals(0, (long)mi.get(-50,0));
    }

    @Test
    public void testRemove(){
        Matrix<Integer> mi = new Matrix<>();
        mi.remove(1,1);
        assertNull(mi.get(1, 1));
        mi.set(1,1,1);
        assertEquals(1,(long)mi.get(1,1));
        mi.remove(1,1);
        assertNull(mi.get(1, 1));
    }

}