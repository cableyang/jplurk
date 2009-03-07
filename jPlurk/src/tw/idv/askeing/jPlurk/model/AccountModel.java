/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tw.idv.askeing.jPlurk.model;

/**
 * jPlurk AccountModel: The model to record Name, Password, and UID of User.
 *
 * @author Askeing, Yen.
 * @version 1.0
 */
public class AccountModel {
	private String name = "";
	private String password = "";
	private int UID = 0;
    private String cookie = "";

	public AccountModel() {
	}

	public AccountModel(String name) {
		this.setName(name);
	}

	public AccountModel(String name, String password) {
		this.setName(name);
		this.setPassword(password);
	}

//	public AccountModel(String name, String password, int UID) {
//		this.setName(name);
//		this.setPassword(password);
//		this.setUID(UID);
//	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/* FIXME: why setUID is never used.
     * fixed at UIDGetter: first time connect host to get UID, then record it for next time.
     * */
	public void setUID(int UID) {
		this.UID = UID;
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

	public int getUID() {
		return this.UID;
	}

    public String getCookie() {
        return this.cookie;
    }
}