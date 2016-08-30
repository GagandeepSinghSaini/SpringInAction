package com.producttype.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.bean.ProductTypeBean;
import com.frys.service.MainConstants;
import com.frys.util.DbConnection;

public class ProductTypeService {

	private Logger log = Logger.getLogger(ProductTypeService.class);
	private ProductTypeBean productTypeBean = null;
	
	private ProductTypeService productService = null;
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
	public ProductTypeService getProductService() {
		return productService;
	}
	public void setProductService(ProductTypeService productService) {
		this.productService = productService;
	}
	public boolean updateShipProduct() {
		log.info("START UPDATING THE PRODUCT ");
		int ship_days=1;
		String compat_nt_flag = "N";
		String whileSuppyLastFlag = null;
		String is_downloadable = null;
		String d_download_prodid = null;
		String d_download_manuf_part_num = null;
		String type = productTypeBean.getProdType();
		String prodId=productTypeBean.getProdId();
		log.info("ProductTypeService.updateShipProduct(): TYPE TO BE MADE: "+type+", PRODUCT_ID: "+prodId);
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
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("ProductTypeService.updateShipProduct(): CONNECTION NULL");
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
				log.info("ProductTypeService.updateShipProduct(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeService.updateShipProduct(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT and FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeService.updateShipProduct(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	
	public boolean updateStoreProduct() {
		log.info("STARTING UPDATING THE PRODUCT ");
		String type = productTypeBean.getProdType();
		String prodId=productTypeBean.getProdId();
		log.info("ProductTypeService.updateStoreProduct(): TYPE TO BE MADE: "+type+", PRODUCT_ID: "+prodId);
		PreparedStatement stmt = null;
		Connection connection = null;
		String storepickupFlag = "S";
		String macs_category = "SR03";
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("ProductTypeService.updateStoreProduct(): CONNECTION NULL");
			return false;
		}else {
			try {
				log.info(UPDATE_STORE_PRODUCT_QUERY);
				stmt = connection.prepareStatement(UPDATE_STORE_PRODUCT_QUERY); 
				stmt.setString(1, macs_category);
				stmt.setString(2, storepickupFlag);
				stmt.setString(3, prodId);
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeService.updateStoreProduct(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeService.updateStoreProduct(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeService.updateStoreProduct(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	public boolean cancelStoreProductType() {
		log.info("STARTING CANCELLING THE STORE PRODUCT ");
		System.out.println("ProductTypeService.cancelStoreProductType(): PRODID: "+productTypeBean.getProdId()+", PROD_TYPE: "+productTypeBean.getProdType());
		String prodId=productTypeBean.getProdId();
		log.info("CANCELLING THE PRODUCT: #"+prodId);
		PreparedStatement stmt = null;
		Connection connection = null;
		String storepickupFlag = "";
		String macs_category = "RR03";
		int ship_days = 1;
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("ProductTypeService.cancelStoreProductType(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_STORE_PRODUCT_QUERY); 
				stmt.setString(1, macs_category);
				stmt.setString(2, storepickupFlag);
				//stmt.setInt(3, ship_days);
				stmt.setString(3, prodId);
				int numOfRows = stmt.executeUpdate();
				log.info("ProductTypeService.cancelStoreProductType(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeService.cancelStoreProductType(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeService.cancelStoreProductType(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	public boolean cancelProductType() {
		log.info("STARTING CANCELLING THE PRODUCT ");
		System.out.println("ProductTypeService.cancelProductType(): PRODID: "+productTypeBean.getProdId()+", PROD_TYPE: "+productTypeBean.getProdType());
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
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("ProductTypeService.cancelProductType(): CONNECTION NULL");
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
				log.info("ProductTypeService.cancelProductType(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("ProductTypeService.cancelProductType(): DATA UPDATED SUCCESSFULLY IN BV_PRODUCT/ FR_PRODUCT_FLAGS");
					return true;
				}
			} catch (SQLException e) {
				log.error("ProductTypeService.cancelProductType(): ****ERROR ***** "+e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	
}
