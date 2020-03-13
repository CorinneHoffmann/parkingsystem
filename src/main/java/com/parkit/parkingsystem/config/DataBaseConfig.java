package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	public Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		Properties properties = new Properties();
		String user = null;
		String pass = null;
		String url = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("resources/credentials.properties"));
			try {
				properties.load(fileInputStream);
				user = properties.getProperty("username");
				pass = properties.getProperty("password");
				url = properties.getProperty("urlprod");
				logger.info("Create DB connection");
				Class.forName("com.mysql.cj.jdbc.Driver");
			} finally {
				fileInputStream.close();
			}
			// return DriverManager.getConnection(url, user, pass);
		} catch (FileNotFoundException e) {
			System.out.println("chemin spécifié du fichier credentials.properties est incorrect");
		}
		return DriverManager.getConnection(url, user, pass);

	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
