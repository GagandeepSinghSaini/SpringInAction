package com.frys.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.bean.InstantSaveBean;
import com.frys.interfaces.DBHelper;
import com.frys.util.DbConnection;
import com.frys.util.FrysHelper;
import com.frys.util.MainConstants;

public class InstantSavingDBHelper implements DBHelper{

	private Logger log = Logger.getLogger(InstantSavingDBHelper.class);
	private InstantSaveBean save_bean;
	DbConnection rebateDbConnection = new DbConnection();
	private InstantSavingDBHelper service;
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
	public InstantSavingDBHelper getService() {
		return service;
	}
	public void setService(InstantSavingDBHelper service) {
		this.service = service;
	}
	/**
	 * updates the product data and apply instant saving on the product
	 * @return true, if update successfully
	 */
	@Override
	public boolean updateData() {
		log.info("InstantSavingDBHelper.updateData(): START UPDATION");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(save_bean == null) {
			log.error("InstantSavingDBHelper.updateData(): USER BEAN IS NULL");
			return false;
		}
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("InstantSavingDBHelper.updateData(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_DATA_QUERY1); 
				stmt.setDouble(1, save_bean.getPrice());
				stmt.setString(2, save_bean.getStartDate());
				stmt.setString(3, save_bean.getEndDate());
				stmt.setString(4, save_bean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("InstantSavingDBHelper.updateData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingDBHelper.updateData(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return updateRegPrice();
				}
			} catch (SQLException e) {
				log.error("InstantSavingDBHelper.updateData(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * updates the regular price of the product in fr_product_flags 
	 * @return true, if updated successfully
	 */
	private boolean updateRegPrice() {
		log.info("InstantSavingDBHelper.updateRegPrice(): START UPDATING SECOND TABLE");
		PreparedStatement stmt = null;
		Connection connection = null;
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("InstantSavingDBHelper.updateRegPrice(): CONNECTION NULL");
			return false;
		}else {
			try {
				String is_sp="N";
				stmt = connection.prepareStatement(UPDATE_DATA_QUERY2); 
				stmt.setDouble(1, save_bean.getRegPrice());
				stmt.setString(2, is_sp);
				stmt.setString(3, save_bean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("InstantSavingDBHelper.updateRegPrice(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingDBHelper.updateRegPrice(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("InstantSavingDBHelper.updateRegPrice(): ****ERROR ***** "+e);
				e.printStackTrace();
			}
		return false;
		}
	}
	/**
	 * For checking the existing data
	 */
	@Override
	public boolean checkData() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * cancels the instant saving on the product
	 * @return true, if cancelled successfully
	 */
	@Override
	public boolean cancelData() {
		log.info("InstantSavingDBHelper.cancelData(): CANCEL THE SAVING");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(save_bean == null) {
			log.error("InstantSavingDBHelper.cancelData(): USER BEAN IS NULL");
			return false;
		}
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("InstantSavingDBHelper.cancelData(): CONNECTION NULL");
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
				log.info("InstantSavingDBHelper.cancelData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("InstantSavingDBHelper.cancelData(): DATA EXPIRED SUCCESSFULLY");
					return true;
				}
			} catch (SQLException e) {
				log.error("InstantSavingDBHelper.cancelData(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		
		return false;
	}
	/**
	 * insert new entry of the product to make product of particular type.
	 */
	@Override
	public boolean insertData() {
		// TODO Auto-generated method stub
		return false;
	}
}
