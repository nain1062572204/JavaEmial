package com.wang;

import static org.junit.Assert.assertNotNull;

import com.wang.util.SessionFactory;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testGetVale()
    {
        SessionFactory sessionFactory=SessionFactory.getInstance();
        assertNotNull(sessionFactory.getPro());
    }
}
