package com.globemed.healthcare.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlDataSource {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/globemed?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
	private static final String JDBC_USER = "root";
	private static final String JDBC_PASSWORD = "0702940593@c";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}
} 