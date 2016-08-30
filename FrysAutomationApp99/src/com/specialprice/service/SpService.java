package com.specialprice.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.frys.bean.SpecPriceBean;
import com.frys.factory.SPTypeFactory;
import com.frys.interfaces.FrysSPType;
import com.frys.service.MainConstants;
import com.frys.util.DbConnection;
import com.frys.util.FrysHelper;
import com.sp.implementation.ButtonSpecialType;

public class SpService {

	private FrysSPType spType;
	private SpecPriceBean userSpInfo;
	private String prodId;
	private String startDate;
	private String endDate;
	private Double amount;
	private String typeOfSpecPrice;
	private final String UPDATE_SP_DATA = "UPDATE FR_PRODUCT_FLAGS SET IS_SP=?,SP_HAS_LOGIN=?, SP_START_DATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " SP_END_DATE = TO_DATE(?,'YYYY-MM-DD'), SP_LOGIN_PRICE=?, SP_SPECIAL_PRICE=?, SP_MAP_PRICE=?, SP_MIN_MAP_PRICE=?, SP_MIN_MAP_METHOD=?"
			+ " WHERE PROD_ID = ? ";
	 
	private final String UPDATE_SP_BUTTON_DATA = "UPDATE FR_PRODUCT_FLAGS SET IS_SP=?,SP_HAS_LOGIN=?, SP_START_DATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " SP_END_DATE = TO_DATE(?,'YYYY-MM-DD'), SP_LOGIN_PRICE=?, SP_SPECIAL_PRICE=?, SP_MAP_PRICE=?, SP_MIN_MAP_PRICE=?, SP_MIN_MAP_METHOD=?"
			+ " WHERE PROD_ID = ? "; 
	private final String EXPIRE_SP_QUERY = "UPDATE FR_PRODUCT_FLAGS SET IS_SP=?, SP_HAS_LOGIN=?, SP_START_DATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " SP_END_DATE = TO_DATE(?,'YYYY-MM-DD'), SP_SPECIAL_PRICE=? WHERE PROD_ID = ?";
	
	FrysHelper helper = null;
	Logger log = Logger.getLogger(SpService.class);
	
	public SpService(FrysSPType spType) {
		if(spType == null) {
			log.error("SpService.SpService(): SPECIAL TYPE TYPE IS NULL");
		}else {
			this.spType = spType;
			userSpInfo = spType.getUserSpInfo();
			helper = new FrysHelper();
		}
		
	}
	
	public void populateSpecialPriceInfo() {
		if(userSpInfo == null) {
			log.error("SpService.populateSpecialPriceInfo(): USERSPINFO IS NULL");
			return;
		}
		prodId = userSpInfo.getProdId();
		startDate = userSpInfo.getStartDate();
		endDate = userSpInfo.getEndDate();
		amount = userSpInfo.getAmount();
		typeOfSpecPrice = userSpInfo.getTypeOfSpecPrice();
		log.info("SpService.populateSpecialPriceInfo(): DATA POPULATED SUCCESSFULLY");
	}
	
	public boolean updateSpFindMoreData() {
		log.info("SpService.updateSpFindMoreData(): START UPDATING SPECIAL PRICE 'FIND OUT MORE' DATA");
		if(userSpInfo == null) {
			log.error("SpService.updateSpFindMoreData(): OOPS Special Price Bean is null");
			return false;
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("SpService.updateSpFindMoreData(): CONNECTION NULL");
			return false;
		}else {
			try {
				Double prod_price  = helper.checkPriceInBVProduct(userSpInfo.getProdId());
				if(prod_price == null) {
					log.error("SpService.updateSpFindMoreData(): PRODUCT PRICE IN BV_PRODUCT IS NULL");
					return false;
				}
				if(amount == null) {
					amount=0.5;
				}
				double amt=0.0;
				String is_sp="Y";
				String sp_has_login = "Y";
				String min_map_method="2";
				stmt = connection.prepareStatement(UPDATE_SP_DATA);
				stmt.setString(1, is_sp);
				stmt.setString(2, sp_has_login);
				stmt.setString(3,startDate);
				stmt.setString(4, endDate);
				stmt.setString(5, String.valueOf(amount));  //SP_LOGIN_PRICE
				stmt.setDouble(6, amt);  //SP_SPECIAL_PRICE
				stmt.setDouble(7, prod_price);  //SPMAPPRICE
				stmt.setDouble(8, amount);  //SP_MIN_MAP_PRICE
				stmt.setString(9, min_map_method);
				stmt.setString(10, userSpInfo.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("SpService.updateSpFindMoreData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("SpService.updateSpWasData(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			}catch(Exception e) {
				log.error("SpService.updateSpFindMoreData(): << ERROR>> : "+e.getMessage());
				return false;
			}finally {
				DbConnection.releaseResources(connection,null,stmt);
			}
		}
		return false;
	}
	
	public boolean updateSpWasData() {
		log.info("SpService.updateSpWasData(): START UPDATING SPECIAL PRICE 'WAS' DATA");
		if(userSpInfo == null) {
			log.error("SpService.updateSpWasData(): OOPS Special Price Bean is null");
			return false;
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("SpService.updateSpWasData(): CONNECTION NULL");
			return false;
		}else {
			try {
				Double prod_price  = helper.checkPriceInBVProduct(userSpInfo.getProdId());
				if(prod_price == null) {
					log.error("SpService.updateSpWasData(): PRODUCT PRICE IN BV_PRODUCT IS NULL");
					return false;
				}
				double amt=0.0;
				String is_sp="Y";
				String sp_has_login = "N";
				String min_map_method="2";
				stmt = connection.prepareStatement(UPDATE_SP_DATA);
				stmt.setString(1, is_sp);
				stmt.setString(2, sp_has_login);
				stmt.setString(3,startDate);
				stmt.setString(4, endDate);
				stmt.setString(5, "");  //SP_LOGIN_PRICE
				stmt.setDouble(6, amt);  //SP_SPECIAL_PRICE
				stmt.setDouble(7, prod_price);  //SPMAPPRICE
				stmt.setDouble(8, amount);  //SP_MIN_MAP_PRICE
				stmt.setString(9, min_map_method);
				stmt.setString(10, userSpInfo.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("SpService.updateSpWasData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("SpService.updateSpWasData(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			}catch(Exception e) {
				log.error("SpService.updateSpWasData(): << ERROR>> : "+e.getMessage());
				return false;
			}finally {
				DbConnection.releaseResources(connection,null,stmt);
			}
		}
		return false;
	}
	
	public boolean updateSpButtonData() {
		log.info("SpService.updateSpButtonData(): START UPDATING SPECIAL PRICE 'BUTTON' DATA");
		if(userSpInfo == null) {
			log.error("SpService.updateSpButtonData(): OOPS Special Price Bean is null");
			return false;
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("SpService.updateSpButtonData(): CONNECTION NULL");
			return false;
		}else {
			try {
				Double prod_price  = helper.checkPriceInBVProduct(userSpInfo.getProdId());
				if(prod_price == null) {
					log.error("SpService.updateSpButtonData(): PRODUCT PRICE IN BV_PRODUCT IS NULL");
					return false;
				}
				String is_sp="Y";
				String sp_has_login = "N";
				String min_map_method="5";
				stmt = connection.prepareStatement(UPDATE_SP_BUTTON_DATA); //STARTDATE,ENDDATE,AMOUNT,PLU
				stmt.setString(1, is_sp);
				stmt.setString(2, sp_has_login);
				stmt.setString(3,startDate);
				stmt.setString(4, endDate);
				stmt.setString(5, "");  //SP_LOGIN_PRICE
				stmt.setDouble(6, amount);  //SP_SPECIAL_PRICE
				stmt.setDouble(7, prod_price);  //SPMAPPRICE
				stmt.setDouble(8, (prod_price-10));  //SP_MIN_MAP_PRICE
				stmt.setString(9, min_map_method);
				stmt.setString(10, userSpInfo.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("SpService.updateSpButtonData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("SpService.updateSpButtonData(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			}catch(Exception e) {
				log.error("SpService.updateSpButtonData(): << ERROR>> : "+e.getMessage());
				return false;
			}finally {
				DbConnection.releaseResources(connection,null,stmt);
			}
		}
		
		return false;
	}
		
	public boolean expireSpData() {
		log.info("SpService.expireSpData()");
		if(userSpInfo != null) {
			String prodId = userSpInfo.getProdId();
			if(amount==null) {
				amount = new Double("0.0");
			}
			Connection connection = null;
			DbConnection dbconnection =new DbConnection();
			connection = dbconnection.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
			PreparedStatement stmt = null;
			try{
				String is_sp="N";
				String sp_has_login = "N";
				if(connection!=null && !connection.isClosed()) {
					stmt = connection.prepareStatement(EXPIRE_SP_QUERY);
					stmt.setString(1, is_sp);
					stmt.setString(2,sp_has_login);
					stmt.setString(3, "");
					stmt.setString(4, "");
					stmt.setDouble(5, amount);
					stmt.setString(6, prodId);
					if(!stmt.execute()) {
						log.info("SpService.expireSpData(): SUCCESSFULLY SP_BUTTON HAS EXPIRED");
						return true;
					}else {
						log.info("SpService.expireSpData(): ERROR - EXECUTION NOT PROPER");
					}
				}
			}catch(Exception e) {
				log.error("SpService.expireSpData(): ERROR : "+e);
			}finally {
				DbConnection.releaseResources(connection,null,stmt);
			}
		}
		return false;
	}
	
	public boolean updateSpClickDetailData() {
		log.info("SpService.updateSpClickDetailData(): START UPDATING SPECIAL PRICE 'CLICK FOR DETAIL' DATA");
		if(userSpInfo == null) {
			log.error("SpService.updateSpClickDetailData(): OOPS Special Price Bean is null");
			return false;
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("SpService.updateSpClickDetailData(): CONNECTION NULL");
			return false;
		}else {
			try {
				Double prod_price  = helper.checkPriceInBVProduct(userSpInfo.getProdId());
				if(prod_price == null) {
					log.error("SpService.updateSpClickDetailData(): PRODUCT PRICE IN BV_PRODUCT IS NULL");
					return false;
				}
				if(amount == null) {
					amount=0.5;
				}
				double amt=0.0;
				String is_sp="Y";
				String sp_has_login = "Y";
				String min_map_method="9";
				stmt = connection.prepareStatement(UPDATE_SP_DATA);
				stmt.setString(1, is_sp);
				stmt.setString(2, sp_has_login);
				stmt.setString(3,startDate);
				stmt.setString(4, endDate);
				stmt.setString(5, String.valueOf(amount));  //SP_LOGIN_PRICE
				stmt.setDouble(6, amt);  //SP_SPECIAL_PRICE
				stmt.setDouble(7, amt);  //SPMAPPRICE
				stmt.setDouble(8, prod_price);  //SP_MIN_MAP_PRICE
				stmt.setString(9, min_map_method);
				stmt.setString(10, userSpInfo.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("SpService.updateSpClickDetailData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("SpService.updateSpClickDetailData(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			}catch(Exception e) {
				log.error("SpService.updateSpClickDetailData(): << ERROR>> : "+e.getMessage());
				return false;
			}finally {
				DbConnection.releaseResources(connection,null,stmt);
			}
		}
		return false;
	}
	
}
