package com.frys.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.RebateBean;
import com.frys.dao.services.RebateDBHelper;
import com.frys.factory.FrysActionFactory;
import com.frys.interfaces.FrysProcess;
import com.frys.interfaces.ProcessService;
import com.frys.util.FrysHelper;


public class RebateHandler implements ProcessService{
	private Logger log = Logger.getLogger(RebateHandler.class);
	
	public RebateHandler() {
		
	}
	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("RebateHandler.processRequest(): STARTS PROCESSING THE REQUEST");
		String action = request.getParameter("action");
		String prodId = request.getParameter("prodId");
		log.info("INPUT: ["+action+", "+prodId+"]");
		String msg="";
		boolean problemFlag = false;
		FrysProcess process = null;
		RebateDBHelper service = new RebateDBHelper();
		RebateBean rb = new RebateBean();
		if(action.equalsIgnoreCase("rebate_generate")) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			Double discAmount = Double.parseDouble(request.getParameter("rebate_amt"));
			log.info("RebateHandler.processRequest(): "+prodId+", "+startDate+", "+endDate+", "+discAmount+", "+action);
			
			if(!FrysHelper.checkDateValidity(startDate, endDate)) {
				log.info("RebateHandler.processRequest():  Start date is after the end date");
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY</span>";
				problemFlag = true;
			}else {
				rb.setProdId(prodId);
				rb.setStartDate(startDate);
				rb.setEndDate(endDate);
				rb.setDiscountAmt(discAmount);
			}
		}else if(action.equalsIgnoreCase("delete_rebate")) {
			rb.setProdId(prodId);
		}
		service.setRbBean(rb);
		service.setRbService(service);
		process = FrysActionFactory.getInstance(action);
		if(!problemFlag && process !=null && process.service(service)) {
			if(action.equalsIgnoreCase("rebate_generate")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"REBATE UPDATION DONE SUCCESSFULLY FOR PRODUCT: "+prodId +"</span>";
			} else if(action.equalsIgnoreCase("delete_rebate")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"REBATE DATA DELETED SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
			}
		} else {
			if(!problemFlag) {
				msg =  "<span style='color:#B22222; text-align:center; font-weight: bold;'>" +"OOPS!! UNSUCCESSFUL UPDATE (#"+prodId+")"+"</span>";
			}
		}
		request.setAttribute("msg", msg);
		
	}

}
