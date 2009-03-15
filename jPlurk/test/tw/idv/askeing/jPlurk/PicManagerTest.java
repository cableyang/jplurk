/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Askeing, Yen
 */
public class PicManagerTest {

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