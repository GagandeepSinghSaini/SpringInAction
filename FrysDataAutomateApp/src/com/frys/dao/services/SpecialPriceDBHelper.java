package com.frys.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.frys.bean.SpecialPriceBean;
import com.frys.interfaces.DBHelper;
import com.frys.util.DbConnection;
import com.frys.util.FrysHelper;


public class SpecialPriceDBHelper implements DBHelper{

	private Logger log = Logger.getLogger(SpecialPriceDBHelper.class);
	private String is_sp;
	private String prodId;
	private String sp_has_login;
	private String sp_start_date;
	private String sp_end_date;
	private String sp_login_price;
	private Double sp_special_price;
	private Double sp_map_price;
	private Double sp_min_map_price;
	private String sp_min_map_method;
	DbConnection dbConnection = new DbConnection();
	private SpecialPriceBean spBean;
	private SpecialPriceDBHelper spDbhelper;
	private final String UPDATE_SP_DATA = "UPDATE FR_PRODUCT_FLAGS SET IS_SP=?,SP_HAS_LOGIN=?, SP_START_DATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " SP_END_DATE = TO_DATE(?,'YYYY-MM-DD'), SP_LOGIN_PRICE=?, SP_SPECIAL_PRICE=?, SP_MAP_PRICE=?, SP_MIN_MAP_PRICE=?, SP_MIN_MAP_METHOD=?"
			+ " WHERE PROD_ID = ? ";
	private final String EXPIRE_SP_DATA = "UPDATE FR_PRODUCT_FLAGS SET IS_SP=?, SP_HAS_LOGIN=?, SP_START_DATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " SP_END_DATE = TO_DATE(?,'YYYY-MM-DD'), SP_SPECIAL_PRICE=? WHERE PROD_ID = ?";
	
	public SpecialPriceBean getSpBean() {
		return spBean;
	}

	public void setSpBean(SpecialPriceBean spBean) {
		this.spBean = spBean;
	}

	public SpecialPriceDBHelper getSpDbhelper() {
		return spDbhelper;
	}

	public void setSpDbhelper(SpecialPriceDBHelper spDbhelper) {
		this.spDbhelper = spDbhelper;
	}

	@Override
	public boolean updateData() {
		log.info("SpecialPriceDBHelper.updateData(): STARTS UPDATING THE PRODUCT FOR SPECIAL PRICE");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(spBean == null || spDbhelper == null) {
			log.error("SpecialPriceDBHelper.updateData(): SpecialPriceBean/SpecialDBHelper Object is null");
			return false;
		}else {
			populateValues();
			connection = dbConnection.getConnection("BV1");
			if(connection == null) {
				log.error("SpecialPriceDBHelper.updateData(): CONNECTION NULL");
				return false;
			}else {
				try {
					if(connection != null && !connection.isClosed()) {
						stmt = connection.prepareStatement(UPDATE_SP_DATA);
						stmt.setString(1, is_sp);
						stmt.setString(2, sp_has_login);
						stmt.setString(3, sp_start_date);
						stmt.setString(4, sp_end_date);
						stmt.setString(5, sp_login_price);  //SP_LOGIN_PRICE
						stmt.setDouble(6, sp_special_price);  //SP_SPECIAL_PRICE
						stmt.setDouble(7, sp_map_price);  //SPMAPPRICE
						stmt.setDouble(8, sp_min_map_price);  //SP_MIN_MAP_PRICE
						stmt.setString(9, sp_min_map_method);
						stmt.setString(10, prodId);
						int numOfRows = stmt.executeUpdate();
						log.info("SpecialPriceDBHelper.updateData(): ROWS UPDATED -> "+numOfRows);
						if(numOfRows > 0) {
							log.info("SpecialPriceDBHelper.updateData(): DATA UPDATED SUCCESSFULLY IN FR_PRODUCT_FLAGS");
							return true;
						}
					}
				}catch(Exception e) {
					log.error("SpecialPriceDBHelper.updateData(): ****EXCEPTION -> "+e.getMessage());
				}finally {
					dbConnection.releaseResources(connection,null,stmt);
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelData() {
		log.info("SpecialPriceDBHelper.cancelData(): STARTS UPDATING THE PRODUCT FOR SPECIAL PRICE");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(spBean == null || spDbhelper == null) {
			log.error("SpecialPriceDBHelper.cancelData(): SpecialPriceBean/SpecialDBHelper Object is null");
			return false;
		}else {
			populateValues();
			connection = dbConnection.getConnection("BV1");
			if(connection == null) {
				log.error("SpecialPriceDBHelper.cancelData(): CONNECTION NULL");
				return false;
			}else {
				try {
					if(connection != null && !connection.isClosed()) {
						stmt = connection.prepareStatement(EXPIRE_SP_DATA);
						stmt.setString(1, is_sp);
						stmt.setString(2,sp_has_login);
						stmt.setString(3, sp_start_date);
						stmt.setString(4, sp_end_date);
						stmt.setDouble(5, sp_special_price);
						stmt.setString(6, prodId);
						if(!stmt.execute()) {
							log.info("SpService.expireSpData(): SUCCESSFULLY SP_DATA HAS EXPIRED");
							return true;
						}
					}
				}catch(Exception e) {
					log.error("SpecialPriceDBHelper.cancelData(): ****EXCEPTION -> "+e.getMessage());
				}finally {
					dbConnection.releaseResources(connection,null,stmt);
				}
			}
		}
		return false;
	}

	@Override
	public boolean insertData() {
		// TODO Auto-generated method stub
		return false;
	}
	private void populateValues() {
		log.info("SpecialPriceDBHelper.populateValues: START POPULATING VALUES");
		is_sp = "Y";
		sp_start_date = spBean.getStartDate();
		sp_end_date = spBean.getEndDate();
		prodId = spBean.getProdId();
		FrysHelper helper = new FrysHelper();
		Double prod_price  = helper.checkPriceInBVProduct(prodId);
		if(("button").equalsIgnoreCase(spBean.getTypeOfSpecPrice())) {
			sp_has_login = "N";
			sp_min_map_method = "5";
			sp_login_price = "";
			sp_special_price = spBean.getAmount();
			sp_map_price = prod_price;
			sp_min_map_price  = prod_price - 10;
		}else if(("Was").equalsIgnoreCase(spBean.getTypeOfSpecPrice())) {
			sp_has_login = "N";
			sp_min_map_method = "2";
			sp_login_price = "";
			sp_special_price = 0.0;
			sp_map_price = prod_price;
			sp_min_map_price  = spBean.getAmount();
		}else if(("FindOutMore").equalsIgnoreCase(spBean.getTypeOfSpecPrice())) {
			Double amount = spBean.getAmount();
			if(amount == null) {
				amount = 0.0;
			}
			sp_has_login = "Y";
			sp_min_map_method = "2";
			sp_login_price = String.valueOf(amount);
			sp_special_price = 0.0;
			sp_map_price = prod_price;
			sp_min_map_price  = spBean.getAmount();
		}else if(("ClickForDetail").equalsIgnoreCase(spBean.getTypeOfSpecPrice())) {
			Double amount = spBean.getAmount();
			if(amount == null) {
				amount = 0.0;
			}
			sp_has_login = "Y";
			sp_min_map_method = "9";
			sp_login_price = String.valueOf(amount);
			sp_special_price = 0.0;
			sp_map_price = 0.0;
			sp_min_map_price  = prod_price;
		}else {
			is_sp="N";
			sp_start_date = "2000-08-29";
			sp_end_date = "2000-08-29";
			sp_has_login = "N";
			sp_special_price = 0.0;
		}
		log.info("SpecialPriceDBHelper.populateValues: POPULATING VALUES ENDS");
 	}
}
