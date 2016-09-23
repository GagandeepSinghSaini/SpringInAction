package com.frys.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;




import com.frys.util.DbConnection;
import com.frys.util.MainConstants;
import com.frys.bean.RebateBean;
import com.frys.interfaces.DBHelper;


public class RebateDBHelper implements DBHelper{

	private Logger log = Logger.getLogger(RebateDBHelper.class);
	private RebateBean rbBean;
	private RebateDBHelper rbService;
	DbConnection rebateDbConnection = new DbConnection();
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
			
	public RebateDBHelper getRbService() {
		return rbService;
	}

	public void setRbService(RebateDBHelper rbService) {
		this.rbService = rbService;
	}

	public RebateBean getRbBean() {
		return rbBean;
	}

	public void setRbBean(RebateBean rbBean) {
		this.rbBean = rbBean;
	}
	/**
	 * check whether the rebate data already exists in the required table
	 * @return true, if data exists
	 */
	public boolean checkData() {
		log.info("RebateDBHelper.checkData(): Checking whether the product entry is already in rebate tables");
		PreparedStatement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		if(rbBean == null) {
			log.error("RebateDBHelper.checkData(): USER INPUT IS NULL");
			return false;
		}
		String pluLong = rbBean.getProdId();
		connection = rebateDbConnection.getConnection("BV1");
		try {
			if(connection != null && !connection.isClosed()) {
				stmt = connection.prepareStatement(SELECT_FR_PROD_REBATE);
				stmt.setString(1, pluLong);
				rs = stmt.executeQuery();
				if(rs.next()) {
					log.info("RebateDBHelper.checkData(): The product entry is already in rebate tables");
					return true;
				}
			}
			log.info("RebateDBHelper.checkData(): The product entry is not present in rebate tables");
		}catch(Exception e) {
			log.error("RebateDBHelper.checkData(): ****ERROR *****"+e);
			e.printStackTrace();
		} finally {
			rebateDbConnection.releaseResources(connection, rs, stmt);
		}
		return false;
	}
	/**
	 * update the rebate data in the tables
	 * @return true, if data updated successfully
	 */
	public boolean updateData() {
		log.info("RebateDBHelper.updateData(): UPDATING REBATE DATA");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(rbBean == null) {
			log.error("RebateDBHelper.updateData(): USER INPUT IS NULL");
			return false;
		}
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("RebateDBHelper.updateData(): CONNECTION NULL");
			return false;
		}else {
			try {
				stmt = connection.prepareStatement(UPDATE_REBATE_DATA); //STARTDATE,ENDDATE,AMOUNT,PLU
				stmt.setString(1, rbBean.getStartDate());
				stmt.setString(2, rbBean.getEndDate());
				stmt.setDouble(3, rbBean.getDiscountAmt());
				stmt.setString(4, rbBean.getProdId());
				int numOfRows = stmt.executeUpdate();
				log.info("RebateDBHelper.updateData(): ROWS UPDATED -> "+numOfRows);
				if(numOfRows > 0) {
					log.info("RebateDBHelper.updateData(): DATA UPDATED SUCCESSFULLY IN FR_PROD_REBATE");
					return true;
				}
			} catch (SQLException e) {
				log.error("RebateDBHelper.updateData(): ****ERROR *****" + e);
				e.printStackTrace();
			}finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * insert new entry for the product in the rebate tables
	 * @return true, if data inserted successfully
	 */
	public boolean insertData() {
		log.info("RebateDBHelper.insertData(): INSERTING REBATE DATA");
		PreparedStatement stmt = null;
		Connection connection = null;
		if(rbBean == null) {
			log.error("RebateDBHelper.insertData(): USER INPUT IS NULL");
			return false;
		}
		connection = rebateDbConnection.getConnection("BV1");
		if(connection == null) {
			log.error("RebateDBHelper.insertData(): CONNECTION NULL");
			return false;
		} else {
			try {
				stmt = connection.prepareStatement(INSERT_REBATE_DATA);
				stmt.setString(1, rbBean.getProdId());
				stmt.setDouble(2, rbBean.getDiscountAmt());
				stmt.setString(3, rbBean.getStartDate());
				stmt.setString(4, rbBean.getEndDate());
				
				int noOfRows = stmt.executeUpdate();
				log.info("RebateDBHelper.insertData(): ROWS INSERTED: " + noOfRows);
				if(noOfRows > 0) {
					log.info("RebateDBHelper.insertData(): ROW UPDATED SUCCESSFULLY");
					return true;
				}
			} catch (SQLException e) {
				log.error("RebateDBHelper.insertData(): ****ERROR *****"+e);
				e.printStackTrace();
			} finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
		}
		return false;
	}
	/**
	 * delete the data in the rebate tables for a given product
	 * @return true, if data deleted successfully
	 */
	public boolean cancelData() {
		log.info("RebateDBHelper.cancelData(): starts--------- RbBean Object: "+rbBean);
		if(rbBean != null) {
			String prodId = rbBean.getProdId();
			Connection connection = null;
			connection = rebateDbConnection.getConnection("BV1");
			PreparedStatement stmt = null;
			try {
				if(connection != null && !connection.isClosed()) {
					stmt = connection.prepareStatement(CANCEL_REBATE);
					stmt.setString(1, prodId);
					int NumOfRows = stmt.executeUpdate();
					log.info("RebateDBHelper.cancelData(): ROWS DELETED: "+NumOfRows); 
					if(NumOfRows > 0) {
						return true;
					}
				}
			}catch(Exception e) {
				log.error("RebateDBHelper.cancelData(): ERROR "+e.getMessage());
				e.printStackTrace();
			} finally {
				rebateDbConnection.releaseResources(connection, null, stmt);
			}
			
		}
		return false;
	}
}
