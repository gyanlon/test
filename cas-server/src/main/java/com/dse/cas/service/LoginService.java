package com.dse.cas.service;

import org.jasig.cas.authentication.Credential;

public interface LoginService {
	
	Integer checkLogin(Credential credentials);
	
	Integer checkUsername(Credential credentials);
}
