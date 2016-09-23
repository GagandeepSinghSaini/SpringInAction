package com.frys.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.SpecialPriceBean;
import com.frys.dao.services.SpecialPriceDBHelper;
import com.frys.factory.FrysActionFactory;
import com.frys.interfaces.FrysProcess;
import com.frys.interfaces.ProcessService;
import com.frys.util.FrysHelper;

public class SpecialPriceHandler implements ProcessService{
	private Logger log = Logger.getLogger(SpecialPriceHandler.class);
	public SpecialPriceHandler() {
		
	}
	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("SpecialPriceHandler.processRequest(): STARTS PROCESSING THE REQUEST");
		String action = request.getParameter("action");
		String prodId = request.getParameter("prodId");
		log.info("SpecialPriceHandler.processRequest():INPUT: ["+action+", "+prodId+"]");
		String msg="";
		String typeOfSpecPrice = "";
		boolean problemFlag = false;
		FrysProcess process = null;
		SpecialPriceDBHelper sp_service = new SpecialPriceDBHelper();
		SpecialPriceBean spBean = new SpecialPriceBean();
		if(action.equalsIgnoreCase("sprice_generate")) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			Double amount = Double.parseDouble(request.getParameter("special_amt"));
			typeOfSpecPrice = request.getParameter("stype");
			log.info("Values: ["+prodId+", "+startDate+", "+endDate+", "+amount+", "+action+", "+typeOfSpecPrice+"]");
			if(!FrysHelper.checkDateValidity(startDate, endDate)) {
				log.info("InstantSavingHandler.processRequest(): Start date is after the end date");
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY</span>";
				problemFlag = true;
			}else {
				spBean.setProdId(prodId);
				spBean.setStartDate(startDate);
				spBean.setEndDate(endDate);
				spBean.setAmount(amount);
				spBean.setTypeOfSpecPrice(typeOfSpecPrice);
			}
		}else if(action.equalsIgnoreCase("delete_sprice")){
			spBean.setProdId(prodId);
			typeOfSpecPrice = "cancel_sp";
			msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"SPECIAL PRICE REMOVED	SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
		}
		process = FrysActionFactory.getInstance(action);
		log.info("PROCESS TO EXECUTE: "+process);
		sp_service.setSpBean(spBean);
		sp_service.setSpDbhelper(sp_service);
				
		if(!problemFlag && process !=null && process.service(sp_service)) {
			if(action.equalsIgnoreCase("sprice_generate")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"SPECIAL PRICE UPDATION DONE SUCCESSFULLY FOR PRODUCT: "+prodId +"</span>";
			} else if(action.equalsIgnoreCase("delete_sprice")) {
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"SPECIAL PRICE REMOVED SUCCESSFULLY FOR PRODUCT: "+prodId+"</span>";
			}
		}else {
			if(!problemFlag) {
				msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>OOPS!! SOME ERROR HAS OCCURED</span>";
			}
		}
		request.setAttribute("msg", msg);
		
		
	}

}
