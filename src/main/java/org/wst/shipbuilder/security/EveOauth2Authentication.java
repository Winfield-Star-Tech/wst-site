package org.wst.shipbuilder.security;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class EveOauth2Authentication extends OAuth2Authentication {
	private String corporation = null;
	
	public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public EveOauth2Authentication(OAuth2Request storedRequest, Authentication userAuthentication, Map detailsMap) {
		super(storedRequest, userAuthentication);
		corporation = (String) detailsMap.get("corporation");
	}

}
