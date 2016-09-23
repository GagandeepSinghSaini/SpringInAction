package com.frys.requesthandler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frys.factory.HandlerMapping;
import com.frys.interfaces.ProcessService;
import com.frys.util.ProductService;

/**
 * Servlet implementation class RequestDispatcherHandler
 */
@WebServlet("/RequestDispatcherHandler")
public class RequestDispatcherHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(RequestDispatcherHandler.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestDispatcherHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("____________________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<<<<<<< REQUEST PROCESSING: START >>>>>>>>>>>>>>>>>>>>>>>>>");
		ProductService serv = new ProductService();
		String prodId = request.getParameter("prodId");
		String action = request.getParameter("action");
		String redirectTo = request.getParameter("request_source")+".jsp";
		log.info("INPUTS: ["+prodId+", "+action+"]");
		if(!serv.checkValidProduct(prodId) || action == null) {
			log.info("FrysRebateServlet.doGet():  YOUR PROD_ID IS NOT CORRECT OR OPERATION IS NULL");
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
		}else {
			ProcessService process = HandlerMapping.getHandler(action);
			process.processRequest(request, response);
		}
		log.info("<<<<<<<<<<<<<<<<<<<<<<< REQUEST PROCESSING: ENDS >>>>>>>>>>>>>>>>>>>>>>>>>");
		log.info("____________________________________________________________________________");
		RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
