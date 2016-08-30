package com.frys.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.ProductTypeBean;
import com.frys.factory.ProductTypeFactory;
import com.frys.interfaces.FrysProductTypeProcess;
import com.frys.service.ProductService;
import com.producttype.service.ProductTypeService;

/**
 * Servlet implementation class FrysProductTypeServlet
 */
@WebServlet("/FrysProductTypeServlet")
public class FrysProductTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrysProductTypeServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrysProductTypeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("____________________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<<<<FRYS PRODUCT TYPE PROCESS STARTS>>>>>>>>>>>>>>>>>>>>");
		String redirectTo = "product_type.jsp";
		ProductService serv = new ProductService();
		ProductTypeService prodTypeService = new ProductTypeService();
		String msg="";
		FrysProductTypeProcess ptprocess = null;
		String pluLong = request.getParameter("prodId");
		String operation = request.getParameter("productOperation");
		log.info("INPUTS: ["+pluLong+", "+operation+"]");
		if(!serv.checkValidProduct(pluLong) || operation == null) {
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
			
		}else {
			ProductTypeBean ptb = new ProductTypeBean();
			String prodId = null;
			String productType = null;
			String transportType = null;
			if(("generate_product_type").equalsIgnoreCase(operation)) {
				prodId = request.getParameter("prodId");
				productType = request.getParameter("typeFlag");
				if(("StoreSpecialOrder").equalsIgnoreCase(productType)) {
					ptb.setTransportType("store");
				}else {
					ptb.setTransportType("ship");
				}
				log.info("VALUES: ("+prodId+", "+productType+")");
				ptb.setProdId(prodId);
				ptb.setProdType(productType);
				msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" + productType.toUpperCase()+ " PRODUCT MADE SUCCESSFULLY WITH PRODUCT ID: "+pluLong+"</span>";
			}else if(("delete_product_type").equals(operation)) {
				prodId = request.getParameter("prodId");
				transportType = request.getParameter("gettype");
				log.info("VALUES: ("+prodId+", "+transportType+")");
				ptb.setProdId(pluLong);
				ptb.setTransportType(transportType);
				msg = "<span style='color:#32CD32; text-align:center;font-weight: bold;'>PRODUCT #" + pluLong +" EXPIRED SUCCESSFULLY</span>";
			}
			prodTypeService.setProductType(ptb);
			prodTypeService.setProductService(prodTypeService);
			ptprocess = ProductTypeFactory.getInstance(operation);
			log.info("FrysProductTypeServlet.doGet(): Process To be Executed: "+ptprocess);
			if(ptprocess!= null && ptprocess.service(prodTypeService)) {
				log.info("PRODUCT HAS BEEN MADE SUCCESSFULLY....");
			}else {
				log.error("OOPS!!, SOME ERROR HAS OCCURED......");
				msg = "<span style='color:#B22222; text-align:center;font-weight: bold;'>OOPS!!, SOME ERROR HAS OCCURED......</span>";
			}
			log.info("<<<<<<<<<<<<<<<<<<<<<<< FRYS PRODUCT TYPE PROCESS ENDS >>>>>>>>>>>>>>>>>>>>>>>>>");
			log.info("____________________________________________________________________________");
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
