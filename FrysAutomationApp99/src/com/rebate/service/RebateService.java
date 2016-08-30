package com.rebate.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.service.MainConstants;
import com.frys.util.DbConnection;
import com.frys.bean.RebateBean;
import com.rebate.implementation.GenerateRebate;

public class RebateService {

	private Logger log = Logger.getLogger(RebateService.class);
	private RebateBean rbBean;
	private RebateService rbService;
	private final String SELECT_FR_PROD_REBATE = "SELECT PLU, STARTDATE, ENDDATE FROM FR_PROD_REBATE WHERE PLU=?";
	private final String UPDATE_REBATE_DATA = "UPDATE FR_PROD_REBATE SET TYPE='V', STARTDATE = TO_DATE(?,'YYYY-MM-DD'),"
			+ " ENDDATE = TO_DATE(?,'YYYY-MM-DD'), AMOUNT=?, QTYLIMIT=2, DEPT=1, CLERK='8300',EXCL='Y',DESCNUMBER=1,"
			+ "ONAD='Y', SHOW='Y',UPCREQUIRED=1, FREEWITHPURCHASE='Y', " 
			+  "DIVISIONREBATEFLAGS='YYYYYYYYYYYNYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY',"
			+ "INABOX=0, PDFNAME='5885063_123113.pdf', REBATE_NUMBER='98462' WHERE PLU=?";   
	private final String INSERT_REBATE_DATA = "INSERT INTO FR_PROD_REBATE"
			+ " (PLU, TYPE,AMOUNT,STARTDATE,ENDDATE,QTYLIMIT,DEPT,CLERK,EXCL,DESCNUMBER, ONAD,SHOW,UPCREQUIRED,FREEWITHPURCHASE,"
			+ "DIVISIONREBATEFLAGS,INABOX,PDFNAME,REBATE_NUMBER) VALUES "
			+ "(?, 'V', ?, TO_DATE(?,'YYYY-MM-DD'), TO_DATE(?,'YYYY-MM-DD'), 2, 1, '8300', 'Y', 1, 'Y', 'Y', 1, 'Y',"
			+ "'YYYYYYYYYYYNYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY', 0, "
			+ "'5885063_123113.pdf', '98462')";
	
	private final String CANCEL_REBATE = "DELETE FROM FR_PROD_REBATE WHERE PLU=?";
			
	public RebateService getRbService() {
		return rbService;
	}

	public void setRbService(RebateService rbService) {
		this.rbService = rbService;
	}

	public RebateBean getRbBean() {
		return rbBean;
	}

	public void setRbBean(RebateBean rbBean) {
		this.rbBean = rbBean;
	}
	
	public boolean checkData() {
		log.info("RebateService.checkData(): Checking whether the product entry is already in rebate tables");
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		if(rbBean == null) {
			log.error("RebateService.checkData(): USER INPUT IS NULL");
			return false;
		}
		String pluLong = rbBean.getProdId();
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		try {
			if(connection != null && !connection.isClosed()) {
				stmt = connection.prepareStatement(SELECT_FR_PROD_REBATE);
				stmt.setString(1, pluLong);
				rs = stmt.executeQuery();
				if(rs.next()) {
					log.info("RebateService.checkData(): The product entry is already in rebate tables");
					return true;
				}
			}
			log.info("RebateService.checkData(): The product entry is not present in rebate tables");
		}catch(Exception e) {
			log.error("RebateService.checkData(): ****ERROR *****"+e);
			e.printStackTrace();
		} finally {
			DbConnection.releaseResources(connection, rs, stmt);
		}
		return false;
	}
	public boolean updateRebateData() {
		log.info("RebateService.updateRebateData(): UPDATING REBATE DATA");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(rbBean == null) {
			log.error("RebateService.updateRebateData(): USER INPUT IS NULL");
			return false;
		}
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("RebateService.updateRebateData(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_REBATE_DATA); //STARTDATE,ENDDATE,AMOUNT,PLU
				stmt.setString(1, rbBean.getStartDate());
				stmt.setString(2, rbBean.getEndDate());
				stmt.setDouble(3, rbBean.getDiscountAmt());
				stmt.setString(4, rbBean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("RebateService.updateRebateData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("RebateService.updateRebateData(): DATA UPDATED SUCCESSFULLY IN FR_PROD_REBATE");
					return true;
				}
			} catch (SQLException e) {
				log.error("RebateService.updateRebateData(): ****ERROR *****" + e);
				e.printStackTrace();
			}finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	public boolean insertRebateData() {
		log.info("RebateService.insertRebateData(): INSERTING REBATE DATA");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(rbBean == null) {
			log.error("RebateService.updateRebateData(): USER INPUT IS NULL");
			return false;
		}
		DbConnection rebateDbConnection = new DbConnection();
		connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
		if(connection == null) {
			log.error("RebateService.updateRebateData(): CONNECTION NULL");
			return false;
		} else {
			try {
				stmt = connection.prepareStatement(INSERT_REBATE_DATA);
				stmt.setString(1, rbBean.getProdId());
				stmt.setDouble(2, rbBean.getDiscountAmt());
				stmt.setString(3, rbBean.getStartDate());
				stmt.setString(4, rbBean.getEndDate());
				
				int noOfRows = stmt.executeUpdate();
				log.info("RebateService.insertRebateData(): ROWS INSERTED: " + noOfRows);
				if(noOfRows > 0) {
					log.info("RebateService.insertRebateData(): ROW UPDATED SUCCESSFULLY");
					return true;
				}
			} catch (SQLException e) {
				log.error("RebateService.insertRebateData(): ****ERROR *****"+e);
				e.printStackTrace();
			} finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	public boolean cancelRebate() {
		log.info("RebateService.cancelRebate(): starts--------- RbBean Object: "+rbBean);
		if(rbBean != null) {
			String prodId = rbBean.getProdId();
			Connection connection = null;
			DbConnection rebateDbConnection =new DbConnection();
			connection = rebateDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
			PreparedStatement stmt = null;
			try {
				if(connection != null && !connection.isClosed()) {
					stmt = connection.prepareStatement(CANCEL_REBATE);
					stmt.setString(1, prodId);
					int NumOfRows = stmt.executeUpdate();
					log.info("RebateService.cancelRebate(): ROWS DELETED: "+NumOfRows); 
					if(NumOfRows > 0) {
						return true;
					}
				}
			}catch(Exception e) {
				log.error("RebateService.cancelRebate(): ERROR "+e.getMessage());
				e.printStackTrace();
			} finally {
				DbConnection.releaseResources(connection, null, stmt);
			}
			
		}
		return false;
	}
}
