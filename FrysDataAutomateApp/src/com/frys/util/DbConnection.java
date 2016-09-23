package com.frys.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.frys.bean.DBProperties;

public class DbConnection {

	private Logger log = Logger.getLogger(DbConnection.class);
	private Connection bv1connection = null;
	private Connection pervConnection = null;
	private  InputStream inputStream = null;
	private DBProperties dbProp = null;

	public DbConnection() {
		
	}
	/**
	 * @param 
	 * @return the Oracle BV1 connection
	 */
	public Connection getBv1Connection(String driver,String url,String username,String password) {
		log.info("DbConnection.getBv1Connection(): GETTING ORACLE BV1 CONNECTION");
			try {
				if (bv1connection == null || bv1connection.isClosed()) {
					log.info("In getBv1Connection(): We are making the connection here");
					Class.forName(driver);
					bv1connection = DriverManager.getConnection(url, username,password);
					log.info("DbConnection.getBv1Connection(): SUCCESSFULLY GOT ORACLE BV1 CONNECTION");
				}
			} catch (Exception exp) {
				log.error("DbConnection.getBv1Connection............................()"+ exp);
				return null;
			}
		
		return bv1connection;
	}
	/**
	 * Loading Db properties
	 * @param propFileName
	 * @return
	 */
	public DBProperties loadDBProperties(String propFileName) {
		log.info("DbConnection.loadproperties: Loading properties From file: "+propFileName);
		Properties prop = new Properties();
		DBProperties dbProperties = null;
		if(propFileName == null) {
			log.error("DbConnection.loadproperties: PROPERTY FILE NAME IS NULL");
			return dbProperties;
		}

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		log.info("DbConnection.loadproperties: Loading property file :- "+inputStream);
		if (inputStream != null) {
			try {
				prop.load(inputStream);
				System.out.println("PROPERTIES: "+prop);
				dbProperties = new DBProperties();
				dbProperties.setBv1driver(prop.getProperty(MainConstants.ORAC_DRIVER));
				dbProperties.setBv1Url(prop.getProperty(MainConstants.BV1_URL));
				dbProperties.setBv1Username(prop.getProperty(MainConstants.BV1_USERNAME));
				dbProperties.setBv1Password(prop.getProperty(MainConstants.BV1_PASSWORD));
				dbProperties.setPervFSSDBdriver(prop.getProperty(MainConstants.PERV_DRIVER)); 
				dbProperties.setPervFSSDBUrl(prop.getProperty(MainConstants.PERV_FSSDB_URL)); 
				dbProperties.setPervFSSDBUsername(prop.getProperty(MainConstants.PERV_FSSDB_USERNAME));
				dbProperties.setPervFSSDBPassword(prop.getProperty(MainConstants.PERV_FSSDB_PASSWORD));
				System.out.println("DbConnection.loadproperties: PROPERTY FILE BEAN POPULATED SUCCESSFULLY: "+dbProperties);
				log.info("DbConnection.loadproperties: PROPERTY FILE BEAN POPULATED SUCCESSFULLY: "+dbProperties);
			} catch (IOException e) {
				log.error("DbConnection.loadproperties: Error in loading properties From file: "+e.getMessage());
				e.printStackTrace();
			}
		} 
		return dbProperties;
	}
	/**
	 * getting the connection according to the connectionType(bv1, pervasive)
	 * @param connectionType
	 * @return Connection
	 */
	public Connection getConnection(String connectionType) {
		log.info("DbConnection.getConnection(): STARTING MAKING CONNECTION: "+connectionType);
		if(connectionType == null) {
			log.error("DbConnection.getConnection(): CONNECTION TYPE IS NULL");
			return null;
		}else {
			if(dbProp == null) {
				dbProp = loadDBProperties("resources/dbhelper.properties");
				if(dbProp == null) {
					log.info("DbConnection.getConnection(): dbProp is null after loading properties file");
					return null;
				}
			}
			try {
				if(connectionType.equals("BV1")) {
					if(bv1connection == null || bv1connection.isClosed()) {
						return getBv1Connection(dbProp.getBv1driver(), dbProp.getBv1Url(), 
								dbProp.getBv1Username(), dbProp.getBv1Password());
					}else {
						return bv1connection;
					}
				}else if(connectionType.equals("PERVASIVE")) {
					if(pervConnection == null || pervConnection.isClosed()) {
						return getPervasiveConnection(dbProp.getPervFSSDBdriver(), dbProp.getPervFSSDBUrl(), 
								dbProp.getPervFSSDBUsername(), dbProp.getPervFSSDBPassword());
					}else {
						return pervConnection;
					}
				}
			}catch(Exception e) {
				log.error("DbConnection.getConnection() : OOPS ERROR ** "+e.getMessage());
			}
		}
		return null;
	}
	/**
	 * @param driver, url, username,password
	 * @return the Pervasive connection
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
	/**
	 * release the resources 
	 * @param connection
	 * @param rs
	 * @param stmt
	 */
	public void releaseResources(Connection connection, ResultSet rs,PreparedStatement stmt) {
		log.info("DbConnection.releaseResources(): Releasing resources");
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
			log.error("DbConnection.releaseResources(): " + exp);
		}
	}
	public static void main(String[] args) {
		DbConnection db = new DbConnection();
		/*db.getPervasiveConnection(MainConstants.PERV_DRIVER	, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);*/
		DBProperties dbProp = db.loadDBProperties("resources/dbhelper.properties");
	}
}
