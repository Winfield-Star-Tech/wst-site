package org.wst.shipbuilder.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EveUser {
	@Id
	private long characterId;
	
	private String characterName;
	
	private String corporationName;
	
	private Date lastLoginDate;
	
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public long getCharacterId() {
		return characterId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getCorporationName() {
		return corporationName;
	}

	protected EveUser() {}
	
	public EveUser(long id, String name, String corp) {
		characterId = id;
		characterName = name;
		corporationName = corp;
	}
	

}
