package com.nestle.document.dao;

public interface LoginDao {

	public boolean validateLogin(String username, String password);

	public String getRoleByUserName(String userName);

}
