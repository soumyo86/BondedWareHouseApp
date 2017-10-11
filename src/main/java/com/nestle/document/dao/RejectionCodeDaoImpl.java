package com.nestle.document.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nestle.document.model.RejectionCode;

public class RejectionCodeDaoImpl implements RejectionCodeDao {
	private JdbcTemplate jdbcTemplate;

	public RejectionCodeDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<RejectionCode> getAllRejection() {

		String sql = "SELECT * FROM rejection_code";

		List<RejectionCode> rejectionCodes = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RejectionCode.class));

		return rejectionCodes;
	}
}
