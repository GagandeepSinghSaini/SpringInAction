package com.frys.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.service.ProductService;
import com.frys.util.FrysHelper;
import com.frys.bean.RebateBean;
import com.frys.factory.RebateFactory;
import com.frys.interfaces.FrysRebateProcess;
import com.rebate.service.RebateService;

/**
 * Servlet implementation class FrysRebateServlet
 */

public class FrysRebateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrysRebateServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    public FrysRebateServlet() {
        super();
       // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("____________________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<<<<<<< FRYS REBATE PROCESS: START >>>>>>>>>>>>>>>>>>>>>>>>>");
		RebateService service = new RebateService();
		ProductService serv = new ProductService();
		String redirectTo = "rebate.jsp";
		String msg = "";
		boolean problemFlag = false;
		String pluLong = request.getParameter("prodId");
		String rebateOperation = request.getParameter("rebateOperation");
		log.info("INPUTS: ["+pluLong+", "+rebateOperation+"]");
		if(!serv.checkValidProduct(pluLong) || rebateOperation == null) {
			log.info("FrysRebateServlet.doGet():  YOUR PROD_ID IS NOT CORRECT OR OPERATION FOR REBATE NOT SET");
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
		}else {
			FrysRebateProcess rprocess = null;
			RebateBean rb = new RebateBean();
			if(rebateOperation.equalsIgnoreCase("rebate_generate")) {
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				Double discAmount = Double.parseDouble(request.getParameter("rebate_amt"));
				log.info("FrysRebateServlet.doGet(): "+pluLong+", "+startDate+", "+endDate+", "+discAmount+", "+rebateOperation);
				
				if(!FrysHelper.checkDateValidity(startDate, endDate)) {
					log.info("FrysRebateServlet.doGet():  Start date is after the end date");
					msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY <br>(START DATE CANNOT BE AFTER THE END DATE)</span>";
					problemFlag = true;
				} else {
					rb.setProdId(pluLong);
					rb.setStartDate(startDate);
					rb.setEndDate(endDate);
					rb.setDiscountAmt(discAmount);
				}
				
				//redirectTo = "generate_rebate.jsp";
			} else if(rebateOperation.equalsIgnoreCase("delete_rebate")) {
				rb.setProdId(pluLong);
				//redirectTo = "delete_rebate.jsp";
			}
			service.setRbBean(rb);
			service.setRbService(service);
			rprocess = RebateFactory.getInstance(rebateOperation);
			if(!problemFlag && rprocess.service(service)) {
				if(rebateOperation.equalsIgnoreCase("rebate_generate")) {
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"REBATE UPDATION DONE SUCCESSFULLY FOR PRODUCT: "+pluLong +"</span>";
				} else if(rebateOperation.equalsIgnoreCase("delete_rebate")) {
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"REBATE DATA DELETED SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				}
				
			} else {
				if(!problemFlag) {
					msg =  "<span style='color:#B22222; text-align:center; font-weight: bold;'>" +"THE PRODUCT ("+pluLong+") DONOT HAVE REBATE"+"</span>";
				}
			}
			log.info("<<<<<<<<<<<<<<<<<<<<<<< FRYS REBATE PROCESS: ENDS >>>>>>>>>>>>>>>>>>>>>>>>>");
			log.info("____________________________________________________________________________");
			System.out.println("MSG: "+msg);
			request.setAttribute("msg", msg);
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
