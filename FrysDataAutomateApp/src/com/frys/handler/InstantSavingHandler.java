package com.frys.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.InstantSaveBean;
import com.frys.dao.services.InstantSavingDBHelper;
import com.frys.factory.FrysActionFactory;
import com.frys.interfaces.FrysProcess;
import com.frys.interfaces.ProcessService;
import com.frys.util.FrysHelper;

public class InstantSavingHandler implements ProcessService{
	private Logger log = Logger.getLogger(InstantSavingHandler.class);
	public InstantSavingHandler() {
		
	}
	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("InstantSavingHandler.processRequest(): STARTS PROCESSING THE REQUEST");
		String action = request.getParameter("action");
		String prodId = request.getParameter("prodId");
		log.info("InstantSavingHandler.processRequest():INPUT: ["+action+", "+prodId+"]");
		String msg="";
		boolean problemFlag = false;
		FrysProcess process = null;
		InstantSavingDBHelper save_service = new InstantSavingDBHelper();
		InstantSaveBean saveBean = new InstantSaveBean();
		if(action.equalsIgnoreCase("save_generate")) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String reg_price = request.getParameter("reg_price");
			String price = request.getParameter("price");
			log.info("VALUES: ["+prodId +", "+startDate+", "+endDate+", "+reg_price+", "+price+"]");
			if(!FrysHelper.checkDateValidity(startDate, endDate)) {
				log.info("InstantSavingHandler.processRequest(): Start date is after the end date");
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY</span>";
				problemFlag = true;
			}else {
				double regular_price = Double.valueOf(reg_price);
				double price_shown = Double.valueOf(price);
				saveBean.setProdId(prodId);
				saveBean.setStartDate(startDate);
				saveBean.setEndDate(endDate);
				saveBean.setRegPrice(regular_price);
				saveBean.setPrice(price_shown);
			}
		}else if(action.equalsIgnoreCase("save_delete")) {
			saveBean.setProdId(prodId);
		}
		save_service.setSave_bean(saveBean);
		save_service.setService(save_service);
		process = FrysActionFactory.getInstance(action);
		log.info("PROCESS TO EXECUTE: "+process);
		
		if(!problemFlag && process !=null && process.service(save_service)) {
			if(action.equalsIgnoreCase("save_generate")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"INSTANT SAVING UPDATION DONE SUCCESSFULLY FOR PRODUCT: "+prodId +"</span>";
			} else if(action.equalsIgnoreCase("save_delete")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"INSTANT SAVING DATA DELETED SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
			}
		}else {
			if(!problemFlag) {
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>OOPS!! SOME ERROR HAS OCCURED</span>";
			}
		}
		request.setAttribute("msg", msg);
	}

}
