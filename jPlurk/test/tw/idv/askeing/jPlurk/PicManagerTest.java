/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Askeing, Yen
 */
public class PicManagerTest {

    public PicManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPicByName method, of class PicManager.
     */
    @Test
    public void testGetPicByName() {
        System.out.println("getPicByName");
        String name = "askeing";
        String expResult = "http://avatars.plurk.com/3290989-big4.jpg";
        String result = PicManager.getPicByName(name);
        assertEquals(expResult, result);

        String name2 = "dragonslayer";
        String expResult2 = "http://statics.plurk.com/314663af9c4c46eb0c14d5566da4c764.gif";
        String result2 = PicManager.getPicByName(name2);
        assertEquals(expResult2, result2);
    }

}