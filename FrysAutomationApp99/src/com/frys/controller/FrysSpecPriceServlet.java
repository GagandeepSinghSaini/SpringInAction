package com.frys.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.SpecPriceBean;
import com.frys.factory.CouponTypeFactory;
import com.frys.factory.RebateFactory;
import com.frys.factory.SPFactory;
import com.frys.factory.SPTypeFactory;
import com.frys.interfaces.FrysCouponType;
import com.frys.interfaces.FrysSPType;
import com.frys.interfaces.FrysSpProcess;
import com.frys.service.ProductService;
import com.frys.util.FrysHelper;
import com.sp.implementation.ButtonSpecialType;
import com.specialprice.service.SpService;

/**
 * Servlet implementation class FrysSpecPriceServlet
 */
@WebServlet("/SpecPriceServlet")
public class FrysSpecPriceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrysSpecPriceServlet.class);  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrysSpecPriceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("__________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<FRYS SPECIAL PRICE PROCESS STARTS>>>>>>>>>>>>>>>>>");
		FrysSpProcess spProcess = null;
		String typeOfSpecPrice = "";
		ProductService serv = new ProductService();
		String redirectTo = "special_price.jsp";
		String msg = "";
		boolean problemFlag = false;
		String pluLong = request.getParameter("prodId");
		String spriceOperation = request.getParameter("spriceOperation");
		log.info("INPUTS: ["+pluLong+", "+spriceOperation+"]");
		if(!serv.checkValidProduct(pluLong) || spriceOperation == null) {
			log.info("FrysSpecPriceServlet.doGet():  YOUR PROD_ID IS NOT CORRECT OR OPERATION FOR SPECIAL PRICE NOT SET");
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
		}else {
			SpecPriceBean spBean = new SpecPriceBean();
			if(spriceOperation.equalsIgnoreCase("sprice_generate")) {
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				Double amount = Double.parseDouble(request.getParameter("special_amt"));
				typeOfSpecPrice = request.getParameter("stype");
				log.info("FrysSpecPriceServlet.doGet(): "+pluLong+", "+startDate+", "+endDate+", "+amount+", "+spriceOperation+", "+typeOfSpecPrice);
				if(!FrysHelper.checkDateValidity(startDate, endDate)) {
					log.info("FrysRebateServlet.doGet():  Start date is after the end date");
					problemFlag = true;
				}else {
					spBean.setProdId(pluLong);
					spBean.setStartDate(startDate);
					spBean.setEndDate(endDate);
					spBean.setAmount(amount);
					spBean.setTypeOfSpecPrice(typeOfSpecPrice);
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"SPECIAL PRICE BUTTON DATA MADE SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				}
				//redirectTo = "generate_specprice.jsp";
			}else if(spriceOperation.equalsIgnoreCase("delete_sprice")){
				spBean.setProdId(pluLong);
				typeOfSpecPrice = "button";
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"SPECIAL PRICE BUTTON DATA DELETED SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				//redirectTo = "delete_specprice.jsp";
			}
			FrysSPType specType = SPTypeFactory.getSpecType(typeOfSpecPrice);
			log.info("FrysSpecPriceServlet.doGet() -> FrysSPType: "+specType);
			if(!problemFlag && specType != null) {
				specType.setSpServ(specType);
				specType.setUserSpInfo(spBean);
				spProcess = SPFactory.getInstance(spriceOperation);
			}
			/*specPriceService.setSpBean(spBean);
			specPriceService.setService(specPriceService);*/
			
			if(spProcess != null && spProcess.service(specType)) {
				log.info("FrysSpecPriceServlet.doGet(): DATA IS GENERATED SUCCESSFULLY");
			}else {
				log.error("FrysSpecPriceServlet.doGet(): SOME ERROR HAS OCCUED IN IMPLEMENTING TE SERVICE");
				msg = "<span style='color:#B22222; text-align:center; font-weight: bold;'>"+"OOPS!! SOME ERROR HAS OCCURED."+"span";
			}
			log.info("FrysSpecPriceServlet.doGet(): "+msg);
			request.setAttribute("msg", msg);
			log.info("<<<<<<<<<<<<<<<<<<<<FRYS SPECIAL PRICE PROCESS ENDS >>>>>>>>>>>>>>>>>>>>");
			log.info("__________________________________________________________________");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
