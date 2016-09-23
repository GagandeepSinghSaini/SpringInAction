package com.frys.dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.bean.CouponBean;
import com.frys.bean.FrPdLsitBean;
import com.frys.interfaces.DBHelper;
import com.frys.util.DbConnection;
import com.frys.util.MainConstants;

public class CouponDBHelper implements DBHelper{
	private Logger log = Logger.getLogger(CouponDBHelper.class);
	private CouponBean bean;
	private CouponDBHelper dbHelper;
	private ResultSet rs = null;
	private FrPdLsitBean pdlistBean = null;
	private boolean NotInFrPdList = false;
	private boolean NotInFrPromDetail = false;
	DbConnection dbConnection = new DbConnection();
	private final String SELECT_FRYSCO = "SELECT MPID, COUPONID FROM FRYSCO WHERE MPID=?";
	private final String DELETE_FRYSCO = "DELETE FROM FRYSCO WHERE MPID=?";
	private final String SELECT_FR_PDLIST = "SELECT MPID,LONGPLU FROM FR_PDLIST WHERE LONGPLU=?";
	private final String SELECT_FR_PROM_DETAILS = "SELECT MPID,PLULONG FROM FR_PROM_DETAIL WHERE MPID=? and PROMOTIONCODE=?";
	private final String INSERT_INTO_FR_PDLIST ="INSERT INTO FR_PDLIST"
			+ " (MPID,LONGPLU,STARTDATE,ENDDATE,PLUTYPE,GROUPNUM,DISCOUNTPERCENT,DISCOUNTAMOUNT,"
			+ "DISCOUNTQTY,LAST_MOD_TIME,CREATE_TIME)"
			+ "VALUES(?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,?,?,?,?,?,?)";
	public CouponBean getBean() {
		return bean;
	}

	public void setBean(CouponBean bean) {
		this.bean = bean;
	}

	public CouponDBHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(CouponDBHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	public boolean updateData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkData() {
		log.info("CouponDBHelper.checkData(): STARTS : Checking whether the product entry is already in coupon tables");
		if(bean == null || dbHelper ==null) {
			log.error("CouponDBHelper.checkData(): CouponBean/CouponDBHelper is null");
			return false;
		}
		String prodId = bean.getProdId();
		
		if(checkDataFrPdList(prodId)) {
			checkDataFrPromDetail(bean.getMpid(), bean.getPromotionCode());
		}
		log.info("CouponDBHelper.checkData(): DATA DOES NOT EXIST IN COUPON TABLES");
		return false;
	}
	public void populateCouponBeans() {
		System.out.println("CouponDBHelper.populateCouponBeans(): STARTS POPULATING COUPON BEANS");
		if(bean == null) {
			log.error("CouponDBHelper.populateCouponBeans(): CouponBean is null");
			return;
		}else {
			
		}
		
	}
	public boolean checkDataFrPdList(String prodId) {
		log.info("CouponDBHelper.checkDataFrPdList(): PROD_ID: "+prodId);
		if(prodId == null) {
			return false;
		}
		PreparedStatement stmt = null;
		Connection connection = null;
		try {
			connection = dbConnection.getConnection("BV1");
			if(connection != null && !connection.isClosed()) {
				stmt = connection.prepareStatement(SELECT_FR_PDLIST);
				stmt.setString(1, prodId);
				rs = stmt.executeQuery();
				if(rs.next()) {
					log.info("CouponDBHelper.checkData(): Data exists in FR_PDLIST for [PRODID : "+prodId+"]");
					bean.setMpid(rs.getString("MPID"));
					return true;
				}
				NotInFrPdList = true;
				log.info("CouponDBHelper.checkData(): Data does not exist in FR_PDLIST for [PRODID : "+prodId+"]");
			}
		}catch(Exception e) {
			log.error("CouponDBHelper.checkData(): ERROR: "+e);
		}finally {
			dbConnection.releaseResources(connection, rs, stmt);
		}
		return false; 
	}
	public boolean checkDataFrPromDetail(String mpid, String promotionCode) {
		log.info("----------CouponDBHelper.checkDataFrPromDetail() : STARTS [MPID: "+mpid+", PROMOTIOND CODE: "+promotionCode+"]");
		if(mpid == null || promotionCode == null) {
			return false;
		}
		PreparedStatement stmt = null;
		ResultSet rs1 = null;
		Connection connection = null;
		try {
			if(connection == null || connection.isClosed()) {
				connection = dbConnection.getConnection("BV1");
			}
			if(connection != null && !connection.isClosed()) {
				stmt = connection.prepareStatement(SELECT_FR_PROM_DETAILS);
				stmt.setString(1, mpid);
				stmt.setString(2, promotionCode);
				 rs1=stmt.executeQuery();
				if(rs1.next()) {
					log.info("CouponDBHelper.checkDataFrPromDetail(): Mpid: " + mpid+ " for promotionCode: "+ promotionCode +" exist in fr_prom_details");
					return true;
				}else {
					NotInFrPromDetail = true;
					log.info("CouponDBHelper.checkDataFrPromDetail(): Mpid: " + mpid+ " does not exist in fr_prom_details");
				}
			}
		}catch(Exception e) {
			log.error("CouponDBHelper.checkDataFrPromDetail(): EXCEPTION: "+e);
		}finally {
			dbConnection.releaseResources(connection, rs, stmt);
		}
		return false;
	}
	@Override
	public boolean cancelData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertData() {
		log.info("CouponDBHelper.insertData(): STARTS -------------");
		if(bean == null) {
			log.error("CouponDBHelper.insertData(): The Bean object is null");
			return false;
		}
		log.info("CouponDBHelper.insertData(): ProdID: "+bean.getProdId()+", with Promotion: "+bean.getPromotionCode());
		pdlistBean = new FrPdLsitBean();
		if(("36").equals(bean.getPromotionCode()) || ("37").equals(bean.getPromotionCode())) {
				pdlistBean.setLongPlu(bean.getProdId());
				pdlistBean.setGroupNum(0);
				pdlistBean.setPluType(0);
				pdlistBean.setMpid(bean.getMpid());
				pdlistBean.setStartDate(bean.getStartDate());
				pdlistBean.setEndDate(bean.getEndDate());
				if(NotInFrPdList) {
					insertFrPdList();
				}
				
		}
		return false;
	}
	public boolean insertFrPdList() {
		log.info("CouponDBHelper.insertFrPdList(): STARTS -------------");
		if(bean == null || pdlistBean == null) {
			log.error("CouponDBHelper.insertFrPdList(): The Bean object is null");
			return false;
		}
		PreparedStatement stmt =null;
		Connection connection = null;
		try {
			String longPlu = pdlistBean.getLongPlu();
			String startDate = pdlistBean.getStartDate();
			String endDate = pdlistBean.getEndDate();
			String mpId = pdlistBean.getMpid();
			Integer pluType = pdlistBean.getPluType();
			Integer groupNum = pdlistBean.getGroupNum(); 
			Integer discountPercent = pdlistBean.getDisountpercent();
			Float discountAmount = pdlistBean.getDiscountamount();
			Integer discountQty = pdlistBean.getDiscountQty();
			String couponRequired = bean.getCouponRequired();
			if(couponRequired == null) {
				couponRequired = "N";
			}
			connection = dbConnection.getConnection("BV1");
			if(connection != null && !connection.isClosed()) {
				stmt=connection.prepareStatement(INSERT_INTO_FR_PDLIST);
				stmt.setString(1, mpId);
				stmt.setString(2, longPlu);
				stmt.setString(3, startDate);
				stmt.setString(4, endDate);
				stmt.setInt(5, pluType);
				stmt.setInt(6, groupNum);
				stmt.setInt(7, discountPercent);
				stmt.setFloat(8, discountAmount);
				stmt.setInt(9, discountQty);
				stmt.setString(10, null);
				stmt.setString(11, null);
				int insertNum = stmt.executeUpdate();
				if(insertNum > 0 && couponRequired.equals("N")) {
					log.info(insertNum+" Record inserted sucessfully in FR_PDLIST");
					return true;
				}
			}else {
				log.error("CouponDBHelper.insertFrPdList(): Connection null or closed");
			}
			
			
		}catch(Exception e) {
			log.error("CouponDBHelper.insertFrPdList(): EXCEPTION: "+e);
		}finally {
			dbConnection.releaseResources(connection, null, stmt);
		}
		return false;
	}
	public void deleteFrysCoIfExist() {
		log.info("------------CouponDBHelper.deleteFrysCoIfExist(): START ---------------");
		if(bean != null) {
			Connection con = null;
			PreparedStatement stmt=null;
			ResultSet rs = null;
			
			try {
				if(con == null || con.isClosed()) {
					con = dbConnection.getConnection("BV1");
				}
				if(con!=null && !con.isClosed()) {
					String mpid=bean.getMpid();
					stmt = con.prepareStatement(SELECT_FRYSCO);
					stmt.setString(1, mpid);
					rs = stmt.executeQuery();
					if(rs.next()) {
						log.info("CouponDBHelper.deleteFrysCoIfExist():  ENTRY EXIST IN FRYSCO FOR MPID: "+mpid);
						stmt = con.prepareStatement(DELETE_FRYSCO);
						stmt.setString(1, mpid);
						if(stmt.execute()) {
							log.info("CouponDBHelper.deleteFrysCoIfExist() deleted the entry in frysco of mpId: "+mpid+ " Sucessfully");
						}
					}else {
						log.info("CouponDBHelper.deleteFrysCoIfExist()ENTRY DOES NOT EXIST IN FRYSCO FOR MPID: "+mpid);
					}
				}else {
					log.error("CouponDBHelper.deleteFrysCoIfExist(): Connection Problem With Pervasive DB");
				}
			} catch (SQLException e) {
				log.error("CouponDBHelper.deleteFrysCoIfExist(): "+e);
				e.printStackTrace();
			}finally {
				dbConnection.releaseResources(con, rs, stmt);
			}
		}
	}
	public String getNewMpid() {
		log.info("CouponDBHelper.getNewMpid(): Creating new Mpid");
		String mpId="";
		try {
			
		}catch(Exception e) {
			log.error("CouponDBHelper.getNewMpid(): Exception "+e);
		}
		return mpId;
	}
}
