package com.frys.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


public class FrysHelper {

	private static Logger logger = Logger.getLogger(FrysHelper.class);
	private final String CHECK_PRODUCT_INFO = "SELECT PROD_ID, PRICE FROM BV_PRODUCT WHERE PROD_ID = ?";
	private final String CHECK_PROD_INFO = "SELECT PROD_ID, REG_PRICE FROM FR_PRODUCT_FLAGS WHERE PROD_ID = ?";
	private final String PROD_PRICE = "price";
	private final String REG_PRICE = "reg_price";
	
	/**
	 * check the start-date and end-date validity
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean checkDateValidity(String start, String end) {
		if(start == null || end == null) {
			logger.error("FrysHelper.checkDateValidity(): STARTDATE/ENDDATE IS NULL");
			return false;
		}
		logger.info("FrysHelper.checkDateValidity(): STARTS");
	    DateFormat df = new SimpleDateFormat("yyyy-MMMM-dd"); 
	    Date startDate;
	    Date endDate;
	    try {
	        startDate = df.parse(start);
	        endDate = df.parse(end);
	       //logger.info("FrysHelper.checkDateValidity(): StartDate: "+startDate+", EndDate"+endDate);
	       if(startDate.before(endDate)) {
	    	   logger.info("FrysHelper.checkDateValidity(): start date is before end date");
	    	   return true;
	       }
	       logger.error("FrysHelper.checkDateValidity(): start date is after end date");
	    } catch (Exception e) {
	    	 logger.error("FrysHelper.checkDateValidity(): "+e);
	        e.printStackTrace();
	       
	    }
	    return false;
	}
	/**
	 * check price from bv_product table
	 * @param prodId
	 * @return
	 */
	public Double checkPriceInBVProduct(String prodId) {
		logger.info("FrysHelper.checkPriceInBVProduct(): STARTS");
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		Double price = 0.0;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			logger.error("FrysHelper.checkPriceInBVProduct(): CONNECTION NULL");
			return null;
		} 
		try {
			stmt = connection.prepareStatement(CHECK_PRODUCT_INFO);
			stmt.setString(1, prodId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				price = rs.getDouble(PROD_PRICE);
			}
		}catch(Exception e) {
			logger.error("FrysHelper.checkPriceInBVProduct(): << ERROR : "+e.getMessage());
			return null;
		}
		
		return price;
	}
	/**
	 * check regular price from fr_product_flags
	 * @param prodId
	 * @return
	 */
	public Double checkPriceInFRProductFlags(String prodId) {
		logger.info("FrysHelper.checkPriceInFRProductFlags(): STARTS");
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		Double price = 0.0;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			logger.error("FrysHelper.checkPriceInFRProductFlags(): CONNECTION NULL");
			return null;
		} 
		try {
			stmt = connection.prepareStatement(CHECK_PROD_INFO);
			stmt.setString(1, prodId);
			rs = stmt.executeQuery();
			while(rs.next()) {
				price = rs.getDouble(REG_PRICE);
			}
		}catch(Exception e) {
			logger.error("FrysHelper.checkPriceInFRProductFlags(): << ERROR : "+e.getMessage());
			return null;
		}
		
		return price;
	}
	
	public static void main(String[] args) {
		checkDateValidity("2015-August-25", "2016-August-15");
	}
}
