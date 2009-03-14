package tw.idv.askeing.jPlurk.test;


import java.io.IOException;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import tw.idv.askeing.jPlurk.model.Account;


/**
 * @author qrtt1
 */
public class DataMother {
    static String TEST_DATA_FILENAME = "test_data.properties";
    static Log logger = LogFactory.getLog(DataMother.class);
    static Properties prop = new Properties();
    static{
        try {
            prop.load(DataMother.class.getResourceAsStream(TEST_DATA_FILENAME));
        } catch (IOException ex) {
            logger.fatal(ex.getMessage(), ex);
        }
    }

    public static Account createTestAccountModel(){
        Account account = new Account();
        account.setName(prop.getProperty("user", "__no_user_name__"));
        account.setPassword(prop.getProperty("password", "__no_password__"));
        return account;
    }

}
