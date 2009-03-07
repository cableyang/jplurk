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
import tw.idv.askeing.jPlurk.model.AccountModel;

/**
 *
 * @author Askeing, Yen
 */
public class UIDGetterTest {

    public UIDGetterTest() {
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
     * Test of getUID method, of class UIDGetter.
     */
    @Test
    public void testGetUID() {
        System.out.println("getUID");
        AccountModel user = new AccountModel("dragonslayer","vul3qup3");
        // 第一次取得 UID
        int expResult = UIDGetter.getUID(user);
        // 設定之後，第二次應該不會再次連線，UID也必須相同
        user.setUID(expResult);
        int result = UIDGetter.getUID(user);
        assertEquals(expResult, result);
    }
}