/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk;

import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tw.idv.askeing.jPlurk.model.AccountModel;
import tw.idv.askeing.jPlurk.test.DataMother;

/**
 *
 * @author Askeing, Yen
 */
public class UIDGetterTest {

    /**
     * Test of getUID method, of class UIDGetter.
     */
    @Test
    public void testGetUID() {
        System.out.println("getUID");
        AccountModel user = DataMother.createTestAccountModel();
        // 第一次取得 UID
        int expResult = UIDGetter.getUID(user);
        // 設定之後，第二次應該不會再次連線，UID也必須相同
        user.setUID(expResult);
        int result = UIDGetter.getUID(user);
        assertEquals(expResult, result);
    }
}