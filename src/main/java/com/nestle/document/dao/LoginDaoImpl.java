package com.nestle.document.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class LoginDaoImpl implements LoginDao {

	private JdbcTemplate jdbcTemplate;

	public LoginDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public boolean validateLogin(String username, String password) {
		boolean userExists = false;
		String sql = "select count(*) from login where name = ? and password = ?";
		int rowcount = jdbcTemplate.queryForObject(sql, new Object[] { username, password }, Integer.class);
		if (rowcount == 1) {
			userExists = true;
		}
		return userExists;
	}

	@Override
	public String getRoleByUserName(String userName) {

		String query = "select role from login WHERE name= ?";
		Object[] inputs = new Object[] { userName };
		String userRole = jdbcTemplate.queryForObject(query, inputs, String.class);
		return userRole;
	}

}
