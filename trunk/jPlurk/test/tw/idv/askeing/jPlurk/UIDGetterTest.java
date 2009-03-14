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
import tw.idv.askeing.jPlurk.model.Account;
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
        Account user = DataMother.createTestAccountModel();
        // 第一次取得 UID
        int expResult = UIDGetter.getUID(user);
        // 設定之後，第二次應該不會再次連線，UID也必須相同
        user.setUID(expResult);
        int result = UIDGetter.getUID(user);
        // FIXME: 這樣的 assert 是沒有意義的，因為即使沒有真的取得 uid，那就會是 0 == 0
        // 要測的話，就要註冊一個測試帳號，之前都是 run 測試時塞一個真的帳號進去跑測試。
        assertEquals(expResult, result);
    }
}