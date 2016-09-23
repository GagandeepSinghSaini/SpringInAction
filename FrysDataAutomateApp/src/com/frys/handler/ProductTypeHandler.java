package com.frys.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.bean.ProductTypeBean;
import com.frys.dao.services.ProductTypeDBHelper;
import com.frys.factory.FrysActionFactory;
import com.frys.interfaces.FrysProcess;
import com.frys.interfaces.ProcessService;

public class ProductTypeHandler implements ProcessService{
	private Logger log = Logger.getLogger(ProductTypeHandler.class);
	public ProductTypeHandler() {
		
	}
	@Override
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("ProductTypeHandler.processRequest(): STARTS PROCESSING THE REQUEST");
		String action = request.getParameter("action");
		String prodId = request.getParameter("prodId");
		log.info("InstantSavingHandler.processRequest():INPUT: ["+action+", "+prodId+"]");
		String msg="";
		FrysProcess process = null;
		String productType = null;
		//String transportType = null;
		ProductTypeDBHelper prodTypeService = new ProductTypeDBHelper();
		ProductTypeBean ptb = new ProductTypeBean();
		if(action.equalsIgnoreCase("generate_product_type")) {
			productType = request.getParameter("typeFlag");
			if(productType == null) {
				log.info("ProductTypeHandler.processRequest(): Product type is null");
				msg = "<span style='color:#B22222; text-align:center; font-weight: bold;'> Please select the product type</span>";
				request.setAttribute("msg", msg);
				return;
			}
			if(("StoreSpecialOrder").equalsIgnoreCase(productType)) {
				ptb.setTransportType("store");
			}else {
				ptb.setTransportType("ship");
			}
			log.info("VALUES: ("+prodId+", "+productType+")");
			ptb.setProdId(prodId);
			ptb.setProdType(productType);
			msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" + productType.toUpperCase()+ " PRODUCT MADE SUCCESSFULLY WITH PRODUCT ID: "+prodId+"</span>";
		}else if(action.equalsIgnoreCase("delete_product_type")) {
			productType = request.getParameter("gettype");
			log.info("VALUES: ("+prodId+", "+productType+")");
			if(productType == null) {
				log.info("ProductTypeHandler.processRequest(): Product type is null");
				msg = "<span style='color:#B22222; text-align:center; font-weight: bold;'> Please select the product type</span>";
				request.setAttribute("msg", msg);
				return;
			}
			ptb.setProdId(prodId);
			ptb.setTransportType(productType);
			msg = "<span style='color:#32CD32; text-align:center;font-weight: bold;'>PRODUCT #" + prodId +" EXPIRED SUCCESSFULLY</span>";
		}
		prodTypeService.setProductType(ptb);
		prodTypeService.setProductService(prodTypeService);
		process = FrysActionFactory.getInstance(action);
		log.info("ProductTypeHandler.processRequest(): Process To be Executed: "+process);
		if(process!= null && process !=null && process.service(prodTypeService)) {
			log.info("PRODUCT HAS BEEN MADE SUCCESSFULLY....");
		}else {
			log.error("OOPS!!, SOME ERROR HAS OCCURED......");
			msg = "<span style='color:#B22222; text-align:center;font-weight: bold;'>OOPS!!, SOME ERROR HAS OCCURED......</span>";
		}
		request.setAttribute("msg", msg);
	}

}
