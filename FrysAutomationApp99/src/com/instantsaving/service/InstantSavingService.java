package com.instantsaving.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.bean.InstantSaveBean;
import com.frys.service.MainConstants;
import com.frys.util.DbConnection;
import com.frys.util.FrysHelper;

public class InstantSavingService {

	private Logger log = Logger.getLogger(InstantSavingService.class);
	private InstantSaveBean save_bean;
	private InstantSavingService service;
	private final String UPDATE_DATA_QUERY1 = "UPDATE BV_PRODUCT SET PRICE=?, QUANTITY_LIMIT_START_DT = TO_DATE(?,'YYYY-MM-DD'),"
			+ " QUANTITY_LIMIT_END_DT=TO_DATE(?,'YYYY-MM-DD') WHERE PROD_ID=?";
	private final String UPDATE_DATA_QUERY2 = "UPDATE FR_PRODUCT_FLAGS SET REG_PRICE=?, IS_SP=? WHERE PROD_ID=?";
	private final String CANCEL_INSTANT_SAVING = "UPDATE BV_PRODUCT SET PRICE=?, QUANTITY_LIMIT_START_DT = TO_DATE(?,'YYYY-MM-DD'),"
			+ " QUANTITY_LIMIT_END_DT=TO_DATE(?,'YYYY-MM-DD') WHERE PROD_ID=?";
	public InstantSaveBean getSave_bean() {
		return save_bean;
	}
	public void setSave_bean(InstantSaveBean save_bean) {
		this.save_bean = save_bean;
	}
	public InstantSavingService getService() {
		return service;
	}
	public void setService(InstantSavingService service) {
		this.service = service;
	}
	public boolean updateData() {
		log.info("InstantSavingService.updateData(): START UPDATION");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(save_bean == null) {
			log.error("InstantSavingService.updateData(): USER BEAN IS NULL");
			return false;
		}
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("InstantSavingService.updateData(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_DATA_QUERY1); 
				stmt.setDouble(1, save_bean.getPrice());
				stmt.setString(2, save_bean.getStartDate());
				stmt.setString(3, save_bean.getEndDate());
				stmt.setString(4, save_bean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("InstantSavingService.updateData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingService.updateData(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return updateRegPrice();
				}
			} catch (SQLException e) {
				log.error("InstantSavingService.updateData(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	private boolean updateRegPrice() {
		log.info("InstantSavingService.updateRegPrice(): START UPDATING SECOND TABLE");
		PreparedStatement stmt = null;
		Connection connection = null;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("InstantSavingService.updateRegPrice(): CONNECTION NULL");
			return false;
		}else {
			try {
				String is_sp="N";
				stmt = connection.prepareStatement(UPDATE_DATA_QUERY2); 
				stmt.setDouble(1, save_bean.getRegPrice());
				stmt.setString(2, is_sp);
				stmt.setString(3, save_bean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("InstantSavingService.updateRegPrice(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingService.updateRegPrice(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("InstantSavingService.updateRegPrice(): ****ERROR ***** "+e);
				e.printStackTrace();
			}
		return false;
	}
 }
	public boolean cancelInstantSaving(){
		log.info("InstantSavingService.cancelInstantSaving(): CANCEL THE SAVING");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(save_bean == null) {
			log.error("InstantSavingService.cancelInstantSaving(): USER BEAN IS NULL");
			return false;
		}
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("InstantSavingService.cancelInstantSaving(): CONNECTION NULL");
			return false;
		}else {
			try {
				String startDate="1950-August-15";
				String endDate="1951-August-15";
				FrysHelper helper = new FrysHelper();
				double reg_price = helper.checkPriceInFRProductFlags(save_bean.getProdId());
				stmt = connection.prepareStatement(CANCEL_INSTANT_SAVING); 
				stmt.setDouble(1, reg_price);
				stmt.setString(2, startDate);
				stmt.setString(3, endDate);
				stmt.setString(4, save_bean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("InstantSavingService.cancelInstantSaving(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingService.cancelInstantSaving(): DATA EXPIRED SUCCESSFULLY");
					return true;
				}
			} catch (SQLException e) {
				log.error("InstantSavingService.cancelInstantSaving(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		
		return false;
	}
}
