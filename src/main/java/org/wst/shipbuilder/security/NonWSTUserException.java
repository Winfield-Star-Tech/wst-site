package org.wst.shipbuilder.security;

import org.wst.shipbuilder.data.EveUser;

public class NonWSTUserException extends RuntimeException {

	private EveUser user;
	public EveUser getUser() {
		return user;
	}
	public void setUser(EveUser user) {
		this.user = user;
	}
	public NonWSTUserException( EveUser user) {
		super("Must be WST user to log in");
		this.user = user;
	}
	

}
