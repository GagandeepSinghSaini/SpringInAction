package com.frys.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.CouponBean;
import com.frys.dao.services.CouponDBHelper;
import com.frys.factory.CouponFactory;
import com.frys.factory.CouponTypeFactory;
import com.frys.factory.FrysActionFactory;
import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;
import com.frys.interfaces.FrysProcess;
import com.frys.interfaces.ProcessService;
import com.frys.util.FrysHelper;

public class CouponHandler implements ProcessService{
	private Logger log = Logger.getLogger(CouponHandler.class);
	public CouponHandler() {
		
	}
	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("CouponHandler.processRequest(): STARTS PROCESSING THE REQUEST");
		String action = request.getParameter("action");
		String prodId = request.getParameter("prodId");
		log.info("CouponHandler.processRequest():INPUT: ["+action+", "+prodId+"]");
		String msg="";
		boolean problemFlag = false;
		FrysProcess process = null;
		String promotionCode = null;
		String couponRequired = null;
		FrysCouponProcess couponProcess = null;
		CouponDBHelper dbHelper = new CouponDBHelper();
		CouponBean couponBean = new CouponBean();
		if(action.equalsIgnoreCase("generate_coupon")) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			promotionCode = request.getParameter("promotionCode");
			couponRequired = request.getParameter("requiredFlag");
			String discType = request.getParameter("optradio");
			String discount = request.getParameter("discValue");
			log.info("VALUES: ["+prodId+", "+promotionCode+", "+couponRequired+", "+discType+", "+discount+", "+
					startDate +", "+endDate+", "+action+"]");
			
			if(!FrysHelper.checkDateValidity(startDate, endDate)) {
				log.info("CouponHandler.processRequest():  Start date is after the end date");
				problemFlag = true;
			}else{
				couponBean.setProdId(prodId);
				couponBean.setPromotionCode(promotionCode);
				couponBean.setCouponRequired(couponRequired);
				couponBean.setTypeOfDis(discType);
				couponBean.setDisValue(discount);
				couponBean.setStartDate(startDate);
				couponBean.setEndDate(endDate);
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" + "COUPON DATA MADE SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
			}
		}else if(action.equalsIgnoreCase("delete_coupon")) {
			couponBean.setProdId(prodId);
			promotionCode="37";
			msg = "<span style='color:#32CD32; text-align:center;font-weight: bold;'>"+"COUPON DATA EXPIRED SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
		}
		FrysCouponType couponType = CouponTypeFactory.getCouponType(promotionCode);
		boolean processSucess = false;
		
		dbHelper.setBean(couponBean);
		dbHelper.setDbHelper(dbHelper);
		couponProcess = CouponFactory.getOperationInstance(action);
		if(couponType != null && couponProcess != null && !problemFlag) {
			couponType.setCouponDBService(dbHelper);
			processSucess = couponProcess.service(couponType);
		}
		if(!problemFlag  && processSucess) {
			log.info("CouponHandler.processRequest(): "+msg);
			if(couponRequired != null && couponRequired.equalsIgnoreCase("Y")) {
				if(couponBean.getCouponCode() != null) {
					request.setAttribute("promoCode", couponBean.getCouponCode());
				}
			}
		}else {
			if(!problemFlag) {
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>OOPS!! SOME ERROR HAS OCCURED</span>";
			}
		}
		request.setAttribute("msg", msg);
	}

}

