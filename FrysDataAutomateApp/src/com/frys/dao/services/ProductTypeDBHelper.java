package com.frys.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.bean.ProductTypeBean;
import com.frys.interfaces.DBHelper;
import com.frys.util.DbConnection;

public class ProductTypeDBHelper implements DBHelper{

	private Logger log = Logger.getLogger(ProductTypeDBHelper.class);
	private ProductTypeBean productTypeBean = null;
	DbConnection rebateDbConnection = new DbConnection();
	private ProductTypeDBHelper productService = null;
	private final String UPDATE_PRODUCT_QUERY = "UPDATE BV_PRODUCT SET SHIP_DAYS=?, COMPAT_NT_FLAG=? WHERE PROD_ID=?";
	private final String UPDATE_WHILE_SUPPLY_PRODUCT_QUERY = "UPDATE BV_PRODUCT SET MACS_ITEM_TYPE=? WHERE PROD_ID=?";
	private final String UPDATE_ESD_PRODUCT_QUERY = "UPDATE FR_PRODUCT_FLAGS SET D_DOWNLOAD_PRODID=?, D_DOWNLOAD_MANUF_PART_NUM=?, "
			+ "IS_DOWNLOADABLE=? WHERE PROD_ID=?";
	private final String UPDATE_STORE_PRODUCT_QUERY = "UPDATE BV_PRODUCT SET MACS_CATEGORY=?, STOREPICKUP=? WHERE PROD_ID=?";
	
	public void setProductType(ProductTypeBean productType) {
		this.productTypeBean = productType;
	}
	public ProductTypeBean getProductTypeBean() {
		return productTypeBean;
	}
	public ProductTypeDBHelper getProductService() {
		return productService;
	}
	public void setProductService(ProductTypeDBHelper productService) {
		this.productService = productService;
	}
	/**
	 * Updates the shipping product with the specified type
	 * @return yes, if updated successfully
	 */
	public boolean updateShipProduct() {
		log.info("ProductTypeDBHelper.updateShipProduct():START UPDATING THE PRODUCT ");
		int ship_days=1;
		String compat_nt_flag = "N";
		String whileSuppyLastFlag = null;
		String is_downloadable = null;
		String d_download_prodid = null;
		String d_download_manuf_part_num = null;
		String type = productTypeBean.getProdType();
		String prodId=productTypeBean.getProdId();
		log.info("ProductTypeDBHelper.updateShipProduct(): TYPE TO BE MADE: "+type+", PRODUCT_ID: "+prodId);
		if(("BackOrder").equalsIgnoreCase(type)) {
			ship_days = 55;
			compat_nt_flag = "Y";
		}else if(("SpecialOrder").equalsIgnoreCase(type)) {
			ship_days = 21;
		}else if(("PreOrder").equalsIgnoreCase(type)) {
			ship_days = 9;
		}else if(("ESDOrder").equalsIgnoreCase(type)) {
			is_downloadable = "Y";
			d_download_prodid = "BNGNHTLKDFKJ9FC";
			d_download_manuf_part_num = "KAV1301111USSO";
		}else if(("WhileSupplyLast").equalsIgnoreCase(type)) {
			whileSuppyLastFlag = "C1";
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("ProductTypeDBHelper.updateShipProduct(): CONNECTION NULL");
			return false;
		}else {
			try {
				if(is_downloadable != null && ("Y").equals(is_downloadable)) {
					log.info("UPDATING ESD PRODUCT");
					stmt = connection.prepareStatement(UPDATE_ESD_PRODUCT_QUERY);
					stmt.setString(1, d_download_prodid);
					stmt.setString(2, d_download_manuf_part_num);
					stmt.setString(3, is_downloadable);
					stmt.setString(4, prodId);
					
				}else if(whileSuppyLastFlag !=null) {
					log.info("UPDATING WHILE SUPPLY LAST PRODUCT");
					stmt = connection.prepareStatement(UPDATE_WHILE_SUPPLY_PRODUCT_QUERY);
					stmt.setString(1, whileSuppyLastFlag);
					stmt.setString(2, prodId);
				}else {

					log.info("UPDATING NON_ESD PRODUCT");
					stmt = connection.prepareStatement(UPDATE_PRODUCT_QUERY); 
					stmt.setInt(1, ship_days);
					stmt.setString(2, compat_nt_flag);
					stmt.setString(3, prodId);
				
				}
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeDBHelper.updateShipProduct(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeDBHelper.updateShipProduct(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT and FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeDBHelper.updateShipProduct(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * Updates the storepickup product with the specified type
	 * @return yes, if updated successfully
	 */
	public boolean updateStoreProduct() {
		log.info("ProductTypeDBHelper.updateStoreProduct():STARTING UPDATING THE PRODUCT ");
		String type = productTypeBean.getProdType();
		String prodId=productTypeBean.getProdId();
		log.info("ProductTypeDBHelper.updateStoreProduct(): TYPE TO BE MADE: "+type+", PRODUCT_ID: "+prodId);
		PreparedStatement stmt = null;
		Connection connection = null;
		String storepickupFlag = "S";
		String macs_category = "SR03";
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("ProductTypeDBHelper.updateStoreProduct(): CONNECTION NULL");
			return false;
		}else {
			try {
				log.info(UPDATE_STORE_PRODUCT_QUERY);
				stmt = connection.prepareStatement(UPDATE_STORE_PRODUCT_QUERY); 
				stmt.setString(1, macs_category);
				stmt.setString(2, storepickupFlag);
				stmt.setString(3, prodId);
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeDBHelper.updateStoreProduct(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeDBHelper.updateStoreProduct(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeDBHelper.updateStoreProduct(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * cancels the type on the store pickup product
	 * @return true, if successful
	 */
	public boolean cancelStoreProductType() {
		log.info("ProductTypeDBHelper.cancelStoreProductType(): STARTING CANCELLING THE STORE PRODUCT ");
		String prodId=productTypeBean.getProdId();
		log.info("CANCELLING THE PRODUCT: #"+prodId);
		PreparedStatement stmt = null;
		Connection connection = null;
		String storepickupFlag = "";
		String macs_category = "RR03";
		int ship_days = 1;
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("ProductTypeDBHelper.cancelStoreProductType(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_STORE_PRODUCT_QUERY); 
				stmt.setString(1, macs_category);
				stmt.setString(2, storepickupFlag);
				//stmt.setInt(3, ship_days);
				stmt.setString(3, prodId);
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeDBHelper.cancelStoreProductType(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeDBHelper.cancelStoreProductType(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeDBHelper.cancelStoreProductType(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * cancels the type on the shipping product
	 * @return true, if successful
	 */
	public boolean cancelShipProductType() {
		log.info("ProductTypeDBHelper.cancelShipProductType(): STARTING CANCELLING THE PRODUCT ");
		String prodId=productTypeBean.getProdId();
		int ship_days=1;
		String compat_nt_flag = "N";
		String is_downloadable = null;
		String whileSuppyLastFlag = null;
		String d_download_prodid = null;
		String d_download_manuf_part_num = null;
		log.info("CANCELLING THE PRODUCT: #"+prodId);
		PreparedStatement stmt = null;
		Connection connection = null;
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("ProductTypeDBHelper.cancelShipProductType(): CONNECTION NULL");
			return false;
		}else {
			try {
				if(("ESD").equalsIgnoreCase(productTypeBean.getTransportType())) {
					log.info("CANCELLING ESD PRODUCT");
					stmt = connection.prepareStatement(UPDATE_ESD_PRODUCT_QUERY);
					stmt.setString(1, d_download_prodid);
					stmt.setString(2, d_download_manuf_part_num);
					stmt.setString(3, is_downloadable);
					stmt.setString(4, prodId);
				}else if(("WhileSupplyLast").equalsIgnoreCase(productTypeBean.getTransportType())) {
					log.info("CANCELLING WHILE SUPPLY LAST PRODUCT");
					whileSuppyLastFlag = "R1";
					stmt = connection.prepareStatement(UPDATE_WHILE_SUPPLY_PRODUCT_QUERY);
					stmt.setString(1, whileSuppyLastFlag);
					stmt.setString(2, prodId);
				}else {
					log.info("CANCELLING NON_ESD PRODUCT");
					stmt = connection.prepareStatement(UPDATE_PRODUCT_QUERY); 
					stmt.setInt(1, ship_days);
					stmt.setString(2, compat_nt_flag);
					stmt.setString(3, prodId);
				}
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeDBHelper.cancelShipProductType(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeDBHelper.cancelShipProductType(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT/ FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeDBHelper.cancelShipProductType(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * update the existing data entry, to make the product of a particular type
	 * @return true, if successful
	 */
	@Override
	public boolean updateData() {
		if(productTypeBean == null || productService == null) {
			log.error("ProductTypeDBHelper.updateData(): ProductTypeBean/ ProductService Object is null");
			return false;
		}else {
			if(("store").equalsIgnoreCase(productTypeBean.getTransportType())) {
				log.info("ProductTypeDBHelper.updateData(): Cancelling the store product");
				return updateStoreProduct();
			}else {
				log.info("ProductTypeDBHelper.updateData(): Cancelling the ship product");
				return updateShipProduct();
			}
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
	 * cancels the type (backorder,preorder,esd,etc) on the product
	 * for store as well as shipping product.
	 * @return true, if successful
	 */
	@Override
	public boolean cancelData() {
		if(productTypeBean == null || productService == null) {
			log.error("ProductTypeDBHelper.cancelData(): ProductTypeBean/ ProductService Object is null");
			return false;
		}else {
			if(("store").equalsIgnoreCase(productTypeBean.getTransportType())) {
				log.info("ProductTypeDBHelper.cancelData(): Cancelling the store product");
				return cancelStoreProductType();
			}else {
				log.info("ProductTypeDBHelper.cancelData(): Cancelling the ship product");
				return cancelShipProductType();
			}
		}
		
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
