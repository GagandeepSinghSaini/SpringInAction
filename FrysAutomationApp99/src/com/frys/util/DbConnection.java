package com.frys.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.frys.service.MainConstants;
import com.frys.service.ProductService;

public class DbConnection {

	private Logger log = Logger.getLogger(DbConnection.class);
	private Connection connection = null;
	private Connection pervConnection = null;

	/**
	 * 
	 * @return the connection
	 */
	public Connection getBv1Connection(String driver,String url,String username,String password) {
		log.info("CouponDbConnection.getConnection(): GETTING ORACLE BV1 CONNECTION");
		if (connection == null) {
			log.info("In getConnection(): We are making the connection here");
			try {
				Class.forName(driver);
				connection = DriverManager.getConnection(url, username,password);
				log.info("DbConnection.getConnection(): SUCCESSFULLY GOT ORACLE BV1 CONNECTION");
				
			} catch (Exception exp) {
				log.error("DbConnection.getConnection............................()"+ exp);
				return null;
			}
		}
		return connection;
	}
	/**
	 * 
	 * @return the connection
	 */
	public Connection getPervasiveConnection(String driver,String url,String username,String password) {
		log.info("DbConnection.getPervasiveConnection(): need to get the connection");
		if (pervConnection == null) {
			log.info("In getPervasiveConnection(): We are making the connection here");
			try {
				Class.forName(driver);
				pervConnection = DriverManager.getConnection(url, username,password);
			} catch (Exception exp) {
				log.error("DbConnection.getPervasiveConnection............................()"+ exp);
				return null;
			}
		}
		return pervConnection;
	}
	public static void main(String[] args) {
		DbConnection db = new DbConnection();
		db.getPervasiveConnection(MainConstants.PERV_DRIVER	, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
	}
	/**
	 * release the resources 
	 * @param connection
	 * @param rs
	 * @param stmt
	 */
	public static void releaseResources(Connection connection, ResultSet rs,PreparedStatement stmt) {
		System.out.println("DbConnection.releaseResources(): Releasing resources");
		try {
			if (rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Exception exp) {
			System.out.println("DbConnection.releaseResources(): " + exp);
		}
	}
}
