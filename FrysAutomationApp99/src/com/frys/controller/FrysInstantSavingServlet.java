package com.frys.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.InstantSaveBean;
import com.frys.factory.InstantSavingFactory;
import com.frys.interfaces.FrysInstantSaveProcess;
import com.frys.service.ProductService;
import com.frys.util.FrysHelper;
import com.instantsaving.service.InstantSavingService;

/**
 * Servlet implementation class FrysInstantSavingServlet
 */
@WebServlet("/FrysInstantSavingServlet")
public class FrysInstantSavingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrysInstantSavingServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrysInstantSavingServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("____________________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<<<<FRYS INSTANT SAVING PROCESS STARTS>>>>>>>>>>>>>>>>>>>>");
		String redirectTo = "instant_saving.jsp";
		ProductService serv = new ProductService();
		InstantSavingService save_service = new InstantSavingService();
		String msg="";
		boolean problemFlag = false;
		FrysInstantSaveProcess process = null;
		String pluLong = request.getParameter("prodId");
		String operation = request.getParameter("saveOperation");
		log.info("INPUTS: ["+pluLong+", "+operation+"]");
		if(!serv.checkValidProduct(pluLong) || operation == null) {
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
			
		}else {
			InstantSaveBean saveBean = new InstantSaveBean();
			if(operation.equalsIgnoreCase("save_generate")) {
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				String reg_price = request.getParameter("reg_price");
				String price = request.getParameter("price");
				log.info("VALUES: ["+pluLong +", "+startDate+", "+endDate+", "+reg_price+", "+price+"]");
				
				if(!FrysHelper.checkDateValidity(startDate, endDate)) {
					log.info("FrysInstantSavingServlet.doGet():  Start date is after the end date");
					msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY <br>(START DATE CANNOT BE AFTER THE END DATE)</span>";
					problemFlag = true;
				}else {
					if(reg_price== null || price==null) {
						log.info("FrysInstantSavingServlet.doGet():  price/reg_price is null");
						msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>PRICE CANNOT BE NULL</span>";
						problemFlag = true;
					}else {
						double regular_price = Double.valueOf(reg_price);
						double price_shown = Double.valueOf(price);
						saveBean.setProdId(pluLong);
						saveBean.setStartDate(startDate);
						saveBean.setEndDate(endDate);
						saveBean.setRegPrice(regular_price);
						saveBean.setPrice(price_shown);
					}
				}
			}else if(operation.equalsIgnoreCase("save_delete")) {
				saveBean.setProdId(pluLong);
			}
			save_service.setSave_bean(saveBean);
			save_service.setService(save_service);
			process = InstantSavingFactory.getInstance(operation);
			log.info("PROCESS TO EXECUTE: "+process);
			if(!problemFlag && process.service(save_service)) {
				if(operation.equalsIgnoreCase("save_generate")) {
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"INSTANT SAVING UPDATION DONE SUCCESSFULLY FOR PRODUCT: "+pluLong +"</span>";
				} else if(operation.equalsIgnoreCase("save_delete")) {
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" +"INSTANT SAVING DATA DELETED SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				}
			}else {
				if(!problemFlag) {
					msg="<span style='color:#B22222; text-align:center; font-weight: bold;'>OOPS!! SOME ERROR HAS OCCURED</span>";
				}
				
			}
			log.info("<<<<<<<<<<<<<<<<<<<<<<<FRYS INSTANT SAVING PROCESS ENDS>>>>>>>>>>>>>>>>>>>>>>>>>");
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
