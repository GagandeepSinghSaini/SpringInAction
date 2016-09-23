package com.coupon.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.coupon.operation.DeleteCoupon;
import com.frys.bean.CouponBean;
import com.frys.bean.FrysCobaBean;
import com.frys.bean.FrysCoupBean;
import com.frys.interfaces.FrysCouponType;
import com.frys.service.MainConstants;
import com.frys.util.DbConnection;

public class HelpingCouponService {

	private final String SELECT_FR_PDLIST = "SELECT MPID,LONGPLU FROM FR_PDLIST WHERE LONGPLU=?";
	private final String SELECT_FRYCOBA = "SELECT MPID, BATCH_NUMBER FROM FRYSCOBA WHERE MPID=?";
	private final String SELECT_FR_PROM_DETAILS = "SELECT MPID,PLULONG FROM FR_PROM_DETAIL WHERE MPID=?";
	private final String SELECT_FR_PROM_DETAIL2 = "SELECT mpid FROM FR_PROM_DETAIL";
	private final String EXPIRE_COUPON_QUERY = "UPDATE FR_PROM_DETAIL SET ENDDATE=TO_DATE(?,'YYYY-MM-DD'), DISCOUNTPERCENT=?, "
			+ "FIXEDDISCOUNT=?, PROMOTIONCODE=?  WHERE PLULONG=? AND MPID=?";
	private final String SELECT_FRYSCOUP = "SELECT fp.PROMO_CODE, fb.MPID FROM FRYSCOUP fp, FRYSCOBA fb WHERE fp.BATCH = ? and fb.MPID=? and fp.BATCH = fb.BATCH_NUMBER";
	private final String UPDATE_FR_PROM_DETAIL = "UPDATE FR_PROM_DETAIL SET PROMOTIONCODE=?,STARTDATE =TO_DATE(?,'YYYY-MM-DD'),ENDDATE=TO_DATE(?,'YYYY-MM-DD'),COUPON_REQUIRED=?, DISCOUNTPERCENT=?, FIXEDDISCOUNT=? WHERE MPID=?";
	private final String UDDATE_FR_PDLIST = "UPDATE FR_PDLIST SET STARTDATE =TO_DATE(?,'YYYY-MM-DD'),ENDDATE=TO_DATE(?,'YYYY-MM-DD') WHERE LONGPLU=? AND MPID=?";
	private final String INSERT_FR_PROM_DETAIL = "INSERT INTO FR_PROM_DETAIL "
    		+ "(PLU,CLASSCODE,PROMOTIONCODE,DISCOUNTTYPE,PURCHASEDQTY,DISCOUNTPLU,DISCOUNTQTY,DISCOUNTPERCENT,"
    		+ "FIXEDDISCOUNT,STARTDATE,ENDDATE,DEPT,DISCOUNTCLASSCODE,VENDORNUMBER,DISCOUNTDEPT,"
    		+ "DISCOUNTVENDORNUMBER,DISCOUNTDETAIL,PLULONG,PLULONGDISC,CLERK,POSITION,UPDATEDATE,"
    		+ "MPID,AMOUNTBACK,ACTIVEFLAG,ADONLY,LIMITQTYFORMP,APPROVEDACC,APPROVERID,TRIGGERAMOUNT,"
    		+ "DISCOUNTSPREAD,UPTODISCITEMS,BACKTOSTOREPERCENT,LAST_MOD_TIME,CREATE_TIME,DESCRIPTION,"
    		+ "MINDISCQTY,SHIPPING_METHOD,COUPON_REQUIRED)"
    		+ "VALUES"
    		+ "(?,0,?,8,1,0,1,?,?,to_date(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),"
    		+ "5,0,0,5,0,1,?,0,45952,352,null,?,0,"
    		+ "'YYYYYYYYYYYYYYYYYYYYYYYYNYYYYYYYYYYYYNYYYNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',"
    		+ "'N',2,'Y',0,0,'N',0,100,null,null,'291867',1,'9',?)";
	private final String INSERT_INTO_FRYSCOBA ="INSERT INTO FRYSCOBA(BATCH_NUMBER,MPID) VALUES (?,?)";
	private final String INSERT_INTO_FRYSCOUP ="INSERT INTO FRYSCOUP(PROMO_CODE,BATCH,EMAIL,END_TIME,START_TIME,COUPON_TYPE,EXPANSION)VALUES(?,?,'','00:00:00','00:00:00','0','')";
	private final String SELECT_FRYSCOBA2 = "SELECT BATCH_NUMBER FROM FRYSCOBA";
	private final String INSERT_INTO_FR_PDLIST ="INSERT INTO FR_PDLIST (MPID,LONGPLU,STARTDATE,ENDDATE,PLUTYPE,GROUPNUM,DISCOUNTPERCENT,DISCOUNTAMOUNT,DISCOUNTQTY,LAST_MOD_TIME,CREATE_TIME)VALUES(?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),0,0,0,0,0,null,null)";
	private final String SELECT_FRYSCO = "SELECT MPID, COUPONID FROM FRYSCO WHERE MPID=?";
    private final String DELETE_FRYSCO = "DELETE FROM FRYSCO WHERE MPID=?";
	private final String FIXED_DISCOUNT = "FixedDiscount";
	private final String DISCOUNT_PERCENT = "DiscountPercent";
	
	CouponBean usercouponInfo;
	FrysCouponType couponTypeService;
	private ResultSet rs = null;
	private String mpId;
	private String promotionCode;
	private String couponRequired;
	private String startDate;
	private String endDate;
	private String disValue;
	private String typeOfDis;
	private String pluLong;
	private String BATCH_NUMBER = "batch_number";
	private String PROMO_CODE = "PROMO_CODE";
	private Logger log = Logger.getLogger(HelpingCouponService.class);
	
	public HelpingCouponService(FrysCouponType couponTypeService) {
		this.couponTypeService = couponTypeService;
		usercouponInfo = couponTypeService.getUserCouponInfo();
		
	}
	public void populateUserCouponInfo() {
		log.info("HelpingCouponService.populateUserCouponInfo(): populating user info onto bean");
		if (usercouponInfo == null) {
			log.error("HelpingCouponService.checkData(): USER INPUT IS NULL");
			return;
		}
		pluLong = usercouponInfo.getPluLong();
		startDate = usercouponInfo.getStartDate();
		endDate = usercouponInfo.getEndDate();
		couponRequired = usercouponInfo.getCouponRequired();
		typeOfDis = usercouponInfo.getTypeOfDis();
		disValue = usercouponInfo.getDisValue();
		promotionCode = usercouponInfo.getPromotionCode();
		log.info("HelpingCouponService.populateUserCouponInfo(): SUCCESSFULLY POPULATED");
	}
	/**
	 * 
	 * @param connection
	 * @return true or false value for Record exist or not,on the basis of PLU,PromotionType,Coupon_Required.
	 */
		public boolean checkCouponData() {
			log.info("HelpingCouponService.checkData(): Checking whether the product entry is already in coupon tables ");
			PreparedStatement stmt = null;
			Connection connection = null;
			try {
				DbConnection couponDbConnection = new DbConnection();
				connection = couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
				
				if(connection != null && !connection.isClosed()) {
					
					stmt = connection.prepareStatement(SELECT_FR_PDLIST);
					stmt.setString(1, pluLong);
					rs = stmt.executeQuery();
					if (rs.next()) {
						usercouponInfo.setMpid(rs.getString("MPID"));
						mpId = usercouponInfo.getMpid();
						if(!checkDataFrPromDetail(connection) && promotionCode!=null) {
							insertFrPromDetail();
						}
						log.info("DATA EXISTS FOR mpId: " + rs.getString("MPID")+ " and pluLong: "+pluLong);
					} else {
						log.info("NO RESULT SET EXISTS FOR pluLong: "+pluLong);
						return false;
					}
				}
				
			} catch (Exception exp) {
				log.error("CoupnDbConnection.main()+++++++++++++++++++++++++++++++++++++++++++++"+ exp);
				return false;
			} finally {
				DbConnection.releaseResources(connection,rs,stmt);
			}
			return true;
		}
		/**
		 * 
		 * @param connection
		 * @return
		 * @throws Exception
		 */
		public boolean checkDataFrPromDetail(Connection connection) throws Exception {
			log.info("----------HelpingCouponService.checkDataFrPromDetail()");
			PreparedStatement stmt = null;
			ResultSet rs1 = null;
			if(connection == null || connection.isClosed()) {
				DbConnection couponDbConnection = new DbConnection();
				connection = couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
			}
			if(connection != null && !connection.isClosed()) {
				stmt = connection.prepareStatement(SELECT_FR_PROM_DETAILS);
				stmt.setString(1, mpId);
				 rs1=stmt.executeQuery();
				if(rs1.next()) {
					log.info("ServiceCoupon.checkDataFrPromDetail(): Mpid: " + mpId+ " exist in fr_prom_details");
					return true;
				}else {
					log.info("ServiceCoupon.checkDataFrPromDetail(): Mpid: " + mpId+ " does not exist in fr_prom_details");
				}
			}
			return false;
		}
		/**
		 * insert the values in fr_prom_details table if value doesn't exist for prod_id.
		 */
		public boolean insertFrPromDetail() {
			log.info("HelpingCouponService.insertFrPromDetail(): START");
			PreparedStatement stmt =null;
			Connection connection = null;
			boolean fromCheckData = true;
			try{
				DbConnection couponDbConnection = new DbConnection();
				connection = couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
				
				if(connection != null && !connection.isClosed()) {
					if(mpId == null || mpId.trim().equals("")) {
						fromCheckData=false;
						List<String> listOfMpIds = new ArrayList<String>();
						stmt = connection.prepareStatement(SELECT_FR_PROM_DETAIL2);
						ResultSet rs = stmt.executeQuery();
						while(rs.next()) {
							listOfMpIds.add(rs.getString("MPID"));
						}
						String mpId = null;
						do {
							mpId = generateRandonNumbers(100000, 999999);
						}while(listOfMpIds.contains(mpId));
						log.info("HelpingCouponService.insertFrPromDetail(): NEW MPID GENERATED: "+mpId);
						usercouponInfo.setMpid(mpId);
						this.mpId = mpId;
					}
					log.info("MPID: "+mpId);
					
					String  plu = pluLong!=null ? pluLong.substring(0, pluLong.length()-1):null;
					stmt=connection.prepareStatement(INSERT_FR_PROM_DETAIL);
					stmt.setString(1, plu);
					stmt.setString(2, promotionCode.toString());
					if(typeOfDis != null) {
						if(typeOfDis.equals(DISCOUNT_PERCENT)) {
							stmt.setString(3, usercouponInfo.getDisValue());
							stmt.setString(4, null);
						}else if(typeOfDis.equals(FIXED_DISCOUNT)) {
							stmt.setString(3, null);
							stmt.setString(4, usercouponInfo.getDisValue());
						}
					}else {
						stmt.setString(3, null);
						stmt.setString(4, null);
					}
					stmt.setString(5, startDate);
					stmt.setString(6, endDate);
					stmt.setString(7, pluLong);
					stmt.setString(8, mpId);
					stmt.setString(9, couponRequired);
					int rowInserted = stmt.executeUpdate();
					if(rowInserted > 0) {
						log.info(rowInserted+": Rows updated in FR_PROM_DETAILS");
						if(!fromCheckData) {
							return insertFrPdlist(connection,promotionCode.toString(),startDate,endDate,pluLong,mpId,couponRequired);
						}
					}
				}
			}catch(Exception exp) {
				System.out.println("HelpingCouponService.insertFrPromDetail()"+exp);
			}
			
			return false;
		}
		/**
		 * 
		 * @param start
		 * @param end
		 * @return random number on the basis of parameters at run time
		 */
		private String generateRandonNumbers(int start, int end) {
			 Random rnd = new Random();  
			 long range = (long)end - (long)start + 1;
			 long fraction = (long)(range * rnd.nextDouble());
			 Integer randomNumber =  (int)(fraction + start);    
			 //System.out.println("Generated : " + randomNumber); 
			 System.out.println(randomNumber);
			 return randomNumber.toString();
		}
		/**
		 * insert the entry into the table fr_pdlist
		 * @param connection
		 * @param prmootionCode
		 * @param startDate
		 * @param endDate
		 * @param pluLong
		 * @param mpId
		 * @param couponRequired
		 * @return
		 */
		public boolean insertFrPdlist(Connection connection,String prmootionCode,String startDate,String endDate,
				String pluLong,String mpId,String couponRequired) {
			try{
				PreparedStatement stmt =null;
			if(connection == null|| connection.isClosed()) {
				DbConnection couponDbConnection =new DbConnection();
				connection = couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
			} 
			stmt=connection.prepareStatement(INSERT_INTO_FR_PDLIST);
			stmt.setString(1, mpId);
			stmt.setString(2, pluLong);
			stmt.setString(3, startDate);
			stmt.setString(4, endDate);
			int insertFrPdlist = stmt.executeUpdate();
			if(insertFrPdlist > 0 && couponRequired.equals("N") ) {
				log.info(insertFrPdlist+"Record inserted sucessfully in FR_PDLIST");
				return true;
			}else if(couponRequired.equals("Y")) {
				log.info(+insertFrPdlist+": ROWS INSERTED IN FR_PDLIST AND COUPON_REQUIRED"+couponRequired);
				return insertFrysCoba();
			}
			   }catch(Exception exp) {
				   System.out.println("HelpingCouponService.insertFrPdlist()"+exp);
			   }
			return false;
		}
		/**
		 * insert the entry into frysCoba table if the couon_required flag is Y
		 */
		public boolean insertFrysCoba() {
			log.info("***********<<<<HelpingCouponService.insertFrysCoba()");
			PreparedStatement stmt=null;
			Connection prvConnection=null;
			List<String> listOfBatchIds = new ArrayList<String>();
			try{
				DbConnection dbConnection = new DbConnection();
			prvConnection=dbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL,MainConstants.PERV_USERNAME,MainConstants.PERV_PASSWORD);
			FrysCobaBean coba =new FrysCobaBean();
			stmt = prvConnection.prepareStatement(SELECT_FRYSCOBA2);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				listOfBatchIds.add(rs.getString("BATCH_NUMBER"));
			}
			String batchId;
			do {
				batchId = generateRandonNumbers(1,1000);
			}while(listOfBatchIds.contains(batchId)); 
			log.info("BATCH ID: "+batchId);
			coba.setBatchId(batchId);
			coba.setMpid(mpId);
		    stmt=prvConnection.prepareStatement(INSERT_INTO_FRYSCOBA);
		    stmt.setString(1, batchId);
		    stmt.setString(2, mpId);
		    int insertFryCob = stmt.executeUpdate();
		    if(insertFryCob > 0) {
		    	log.info(insertFryCob+": Row updated in Fryscoba Successfully");
		    	return insertFrysCoup(prvConnection,coba);
		    }
			}catch(Exception exp) {
				log.error("HelpingCouponService.insertFrysCoba()"+exp);
				return false;
			} finally {
				DbConnection.releaseResources(prvConnection,null,stmt);
			}
			return false;
		}
		/**
		 * insert the entry into table FrysCoup
		 * @param prvConnection
		 * @param cobaBean
		 * @return
		 */
		private boolean insertFrysCoup(Connection prvConnection,FrysCobaBean cobaBean) {
			log.info("**********<<HelpingCouponService.insertFrysCoup()");
			PreparedStatement stmt=null;
			FrysCoupBean coupBean;
			try{
				if(prvConnection==null||prvConnection.isClosed()) {
					DbConnection couponDbConnection =new DbConnection();
					prvConnection=couponDbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
				}
				String promo_Code = generateRandonNumbers(1001, 9999);
				log.info("******************<<<<<PROMO CODE: "+promo_Code+">>>>>>>>**************");
				String batch=cobaBean.getBatchId();
				stmt=prvConnection.prepareStatement(INSERT_INTO_FRYSCOUP);
				stmt.setString(1, promo_Code);
				stmt.setString(2, batch);
				int insertCoup = stmt.executeUpdate();
				if(insertCoup > 0 ) {
					log.info(insertCoup+": Rows inserted in FRYSCOUP table");
					log.info("HelpingCouponService.insertFrysCoup(): ALL PERVASIVE TABLES ARE UPDATED>>>> ");
					coupBean = new FrysCoupBean();
					coupBean.setPromoCode(promo_Code);
					couponTypeService.setCouponPromoBean(coupBean);
					return true;
				}
				else {
					log.error("HelpingCouponService.insertFrysCoup()--Error While inserting in FRYSCOUP table");
				}
			}catch(Exception exp) {
				log.error("HelpingCouponService.insertFrysCoup()"+exp);
				return false;
			}finally {
				DbConnection.releaseResources(prvConnection,null,stmt);
			}
			return false;
		}
		public void deleteFrysCoIfExist() {
			log.info("------------HelpingCouponService.deleteFrysCoIfExist(): START ---------------");
			if(usercouponInfo != null) {
				Connection con = null;
				PreparedStatement stmt=null;
				ResultSet rs = null;
				DbConnection couponDbConnection = new DbConnection();
				con=couponDbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
				try {
					if(con!=null && !con.isClosed()) {
						String mpid=usercouponInfo.getMpid();
						stmt = con.prepareStatement(SELECT_FRYSCO);
						stmt.setString(1, mpid);
						rs = stmt.executeQuery();
						if(rs.next()) {
							log.info("HelpingCouponService.deleteFrysCoIfExist():  ENTRY EXIST IN FRYSCO FOR MPID: "+mpid);
							stmt = con.prepareStatement(DELETE_FRYSCO);
							stmt.setString(1, mpid);
							if(stmt.execute()) {
								log.info("HelpingCouponService.deleteFrysCoIfExist() deleted the entry in frysco of mpId: "+mpid+ " Sucessfully");
							}
						}else {
							log.info("HelpingCouponService.deleteFrysCoIfExist()ENTRY DOES NOT EXIST IN FRYSCO FOR MPID: "+mpid);
						}
					}else {
						log.error("HelpingCouponService.deleteFrysCoIfExist(): Connection Problem With Pervasive DB");
					}
				} catch (SQLException e) {
					log.error("HelpingCouponService.deleteFrysCoIfExist(): "+e);
					e.printStackTrace();
				}
			}
		}
		/**
		 * updates the coupon data
		 * @param connection
		 * @return
		 */
		public boolean updateCouponData() {
			log.info("HelpingCouponService.updateCouponData(): START");
			return updateFrPromDetail();
		}
		/**
		 * updates the data in table FrPromDetail
		 * 
		 */
			public boolean updateFrPromDetail() {
				log.info("******************************HelpingCouponService.updateFrPromDetail()");
				PreparedStatement stmt = null;
				Connection connection = null;
				try {
					DbConnection couponDbConnection = new DbConnection();
					connection=couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
					if(connection != null && !connection.isClosed()) {
						stmt = connection.prepareStatement(UPDATE_FR_PROM_DETAIL);
						System.out.println("QUERY: "+UPDATE_FR_PROM_DETAIL);
						stmt.setString(1, promotionCode);
						stmt.setString(2, startDate);
						stmt.setString(3, endDate);
						stmt.setString(4, couponRequired);
						if(typeOfDis != null) {
							if(typeOfDis.equals(FIXED_DISCOUNT)) {
								stmt.setString(5, null);
								stmt.setString(6, disValue);
							}else if(typeOfDis.equals(DISCOUNT_PERCENT)) {
								stmt.setString(5, disValue);
								stmt.setString(6, null);
							}
						}else {
							stmt.setString(5, null);
							stmt.setString(6, null);
						}
						/*stmt.setString(7, pluLong);
						stmt.setString(8, mpId);*/
						stmt.setString(7, mpId);
						int rowsUpdated = stmt.executeUpdate();
						if(rowsUpdated >0) {
							log.info(rowsUpdated + " ROWS Updated Sucessfully in FR_PROM_DETAILS:.....");
							return updateFrPdList(couponDbConnection);
						}else {
							log.info("HelpingCouponService.updateFrPromDetail(): No row updated in FR_PROM_DETAILS");
						}
					}else {
						log.error("HelpingCouponService.updateFrPromDetail(): Connection not made with BV1TO1");
					}
				} catch (Exception exp) {
					log.error("HelpingCouponService.updateFr_Prom_Detail()" + exp);
					exp.printStackTrace();
					return false;
				}
				finally{
					DbConnection.releaseResources(connection,null,stmt);
				}
				return false;
			}
			/**
			 * updates the data in table FrPdList
			 * @param connection
			 * @return
			 */
			public boolean updateFrPdList(DbConnection couponDbConnection) {
				log.info("*************HelpingCouponService.updateFrPdList()");
				PreparedStatement stmt =null;
				Connection connection = null;
				try {
					connection=couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER,MainConstants.BV1_URL,MainConstants.BV1_USERNAME,MainConstants.BV1_PASSWORD);
					if(connection != null && !connection.isClosed()) {
						stmt= connection.prepareStatement(UDDATE_FR_PDLIST);
						//log.info("QUERY: "+UDDATE_FR_PDLIST);
			            stmt.setString(1, startDate);
			            stmt.setString(2, endDate);
			            stmt.setString(3, pluLong);
			            stmt.setString(4, mpId);
			            int updatedRows = stmt.executeUpdate();
			            if(updatedRows > 0) {
			            	log.info(updatedRows + " ROWS Updated Sucessfully in FR_PDLIST:.....");
			            	if(("Y").equalsIgnoreCase(usercouponInfo.getCouponRequired())) {
			            		return checkPervasiveData();
			            	}else {
			            		return true;
			            	}
			            	//return true;
			            }else {
							log.info("HelpingCouponService.updateFrPromDetail(): No row updated in FR_PDLIST");
						}
					}else {
						log.error("HelpingCouponService.updateFrPdList(): Connection not made with BV1TO1");
					}
		        } catch (Exception exp) {
					log.error("HelpingCouponService.updateFR_PD_LIST()" + exp);
					return false;
				} finally {
					DbConnection.releaseResources(connection,null,stmt);
				}
				return false;
			}
			/**
			 * Check the data in pervasive database on the basis of mpid.
			 */
			private boolean checkPervasiveData() {
				log.info("********<<<HelpingCouponService.checkPervasiveData()");
				Connection connection = null;
				PreparedStatement stmt = null;
				ResultSet rs = null;
				FrysCoupBean coupBean;
				try {
					DbConnection couponDbConnection = new DbConnection();
					connection=couponDbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
					if(connection != null  && !connection.isClosed()) {
						stmt = connection.prepareStatement(SELECT_FRYCOBA);
						stmt.setString(1, mpId);
						rs = stmt.executeQuery();
						if(!rs.next()) {
							log.info("HelpingCouponService.checkPervasiveData(): DATA DOES NO EXIST IN PERVASIVE TABLES FOR MPID: "+usercouponInfo.getMpid());
							return insertFrysCoba();
						}else {
							log.info("HelpingCouponService.checkPervasiveData(): ID ALREADY EXISTS IN FRYSCOBA");
							String batchNumber = rs.getString(BATCH_NUMBER);
							if(checkFrysCoup(batchNumber)) {
								log.info("HelpingCouponService.checkPervasiveData(): ID ALREADY EXISTS IN FRYSCOUP");
								String promoCode = getFrysCoupDetail(batchNumber, mpId);
								coupBean = new FrysCoupBean();
								coupBean.setPromoCode(promoCode);
								couponTypeService.setCouponPromoBean(coupBean);
								return true;
							}else {
								log.info("HelpingCouponService.checkPervasiveData(): ID DOES NOT EXIST IN FRYSCOUP");
								FrysCobaBean coba =new FrysCobaBean();
								coba.setBatchId(batchNumber);
								coba.setMpid(mpId);
								return insertFrysCoup(connection,coba);
							}
						}
					}else {
						log.error("ServiceCoupon.updateFrysCoba(): Connection not made with pervasive database");
					}
				}catch(Exception e) {
					log.error("ServiceCoupon.checkPervasiveData(): "+e.getMessage());
					System.exit(1);
				}
				return false;
			}
			private boolean checkFrysCoup(String batchNumber) {
				Connection con = null;
				PreparedStatement stmt=null;
				ResultSet rs = null;
				DbConnection couponDbConnection = new DbConnection();
				con=couponDbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
				try {
					if(con!=null && !con.isClosed()) {
						stmt = con.prepareStatement(SELECT_FRYSCOUP);
						stmt.setString(1, batchNumber);
						stmt.setString(2, mpId);
						rs = stmt.executeQuery();
						if(rs.next()) {
							return true;
						}
					}
				} catch (SQLException e) {
					log.error("HelpingCouponService.checkFrysCoup()");
					e.printStackTrace();
				}
				
				return false;
			}
			private String getFrysCoupDetail(String batchNumber, String mpId) {
				log.info("HelpingCouponService.getFrysCoupDetail()");
				Connection con = null;
				PreparedStatement stmt=null;
				ResultSet rs = null;
				DbConnection couponDbConnection = new DbConnection();
				con=couponDbConnection.getPervasiveConnection(MainConstants.PERV_DRIVER, MainConstants.PERV_URL, MainConstants.PERV_USERNAME, MainConstants.PERV_PASSWORD);
				try {
					if(con!=null && !con.isClosed()) {
						stmt = con.prepareStatement(SELECT_FRYSCOUP);
						stmt.setString(1, batchNumber);
						stmt.setString(2, mpId);
						rs = stmt.executeQuery();
						if(rs.next()) {
							return rs.getString(PROMO_CODE);
						}
					}
				} catch (SQLException e) {
					log.error("HelpingCouponService.getFrysCoupDetail(): "+e);
				}
				return null;
			}
			/**
			 * On the basis of the promotion code type call the method. 
			 */
			public boolean insertCouponData() {
				log.info("HelpingCouponService.insertCouponData(): INSERTING NEW ENTRY FOR COUPON");
				return insertFrPromDetail();
			}
			/**
			 * Make the coupon expired
			 * @return
			 */
			public boolean expireCoupon() {
				log.info("HelpingCouponService.expireCoupon()");
				if(usercouponInfo != null) {
					String prodId = usercouponInfo.getPluLong();
					String promotionCode = usercouponInfo.getPromotionCode();
					Connection connection = null;
					DbConnection couponDbConnection =new DbConnection();
					connection = couponDbConnection.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
					PreparedStatement stmt = null;
					try {
						if(connection!=null && !connection.isClosed()) {
							String date = getDate(-10);
							String mpId = usercouponInfo.getMpid();
							System.out.println("HelpingCouponService.expireCoupon(): QUERY -> "+EXPIRE_COUPON_QUERY);
							stmt = connection.prepareStatement(EXPIRE_COUPON_QUERY);
							stmt.setString(1, date);
							stmt.setString(2, "0");
							stmt.setString(3, "0");
							stmt.setString(4, promotionCode);
							stmt.setString(5, prodId);
							stmt.setString(6, mpId);
							if(!stmt.execute()) {
								log.info("HelpingCouponService.expireCoupon(): SUCCESSFULLY COUPON HAS EXPIRED");
								return true;
							}else {
								log.info("HelpingCouponService.expireCoupon(): EXECUTION OF QUERY NOT PROPER");
							}
						}
						log.error("HelpingCouponService.expireCoupon(): SOME PROBLEM HAS OCCURED WHILE EXPIRING THE COUPON");
					} catch (SQLException e) {
						log.error("HelpingCouponService.expireCoupon(): EXCEPTION - "+e.getMessage());
					}
				}
				return false;
			}
			/**
			 * 
			 * @param days
			 * @return current date
			 */
			private String getDate(int days) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE, days);
				return sdf.format(c.getTime());
			}
			
}
