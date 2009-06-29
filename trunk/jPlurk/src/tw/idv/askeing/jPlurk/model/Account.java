package tw.idv.askeing.jPlurk.model;

/**
 * jPlurk AccountModel: The model to record Name, Password, and UID of User.
 *
 * @author Askeing, Yen.
 * @version 1.0
 */
public class Account {
	private String name = "";
	private String password = "";
//	private int UID = 0;
    private String cookie = "";

	/**
	 * The default constructor will assign the `plurk.user' system property as account's name and `plurk.passwd' system property as account's password.
	 */
	public Account() {
		this(System.getProperty("plurk.user"), System.getProperty("plurk.passwd"));
	}

	public Account(String name) {
		this.setName(name);
	}

	public Account(String name, String password) {
		this.setName(name);
		this.setPassword(password);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

    public String getCookie() {
        return this.cookie;
    }
}