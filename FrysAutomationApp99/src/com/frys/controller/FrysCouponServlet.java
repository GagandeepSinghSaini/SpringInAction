package com.frys.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;








import org.apache.log4j.Logger;

import com.coupon.operation.DeleteCoupon;
import com.frys.service.ProductService;
import com.frys.util.FrysHelper;
import com.frys.bean.CouponBean;
import com.frys.factory.CouponFactory;
import com.frys.factory.CouponTypeFactory;
import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;

/**
 * Servlet implementation class FrysCouponServlet
 */
public class FrysCouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(FrysCouponServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrysCouponServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("____________________________________________________________________________");
		log.info("<<<<<<<<<<<<<<<<<<<<FRYS COUPON PROCESS STARTS >>>>>>>>>>>>>>>>>>>>");
		String redirectTo =  "coupon.jsp";
		//String tabOpen = "generate";
		boolean problemFlag = false;
		ProductService serv = new ProductService();
		String pluLong = request.getParameter("prodId");
		String operation = request.getParameter("couponOperation");
		log.info("INPUTS: ["+pluLong+", "+operation+"]");
		if(!serv.checkValidProduct(pluLong) || operation == null) {
			request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PRODUCT IS INVALID</span>");
			RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
			rd.forward(request, response);
			
		}else {
			CouponBean couponInfo = new CouponBean();
			String couponRequired = null;
			FrysCouponProcess cprocess = null;
			String promotionCode = null;
			String discType = null;
			String discount = null;
			String startDate = null;
			String endDate = null;
			String msg="";
			
			boolean processSuccess = false;
			if(("generate_coupon").equalsIgnoreCase(operation)) {
				promotionCode = request.getParameter("promotionCode");
				couponRequired = request.getParameter("requiredFlag");
				discType = request.getParameter("optradio");
				discount = request.getParameter("discValue");
				startDate = request.getParameter("startDate");
				endDate = request.getParameter("endDate");
				
				log.info("VALUES: "+pluLong+", "+promotionCode+", "+couponRequired+", "+discType+", "+discount+", "+
							startDate +", "+endDate+", "+operation);
				if(!FrysHelper.checkDateValidity(startDate, endDate)) {
					log.info("FrysRebateServlet.doGet():  Start date is after the end date");
					problemFlag = true;
				} else{
					couponInfo.setPluLong(pluLong);
					couponInfo.setPromotionCode(promotionCode);
					couponInfo.setCouponRequired(couponRequired);
					couponInfo.setTypeOfDis(discType);
					couponInfo.setDisValue(discount);
					couponInfo.setStartDate(startDate);
					couponInfo.setEndDate(endDate);
					msg = "<span style='color:#32CD32; text-align:center; font-weight: bold;'>" + "COUPON DATA MADE SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				}
				
			} else if(("delete_coupon").equalsIgnoreCase(operation)) {
				couponInfo.setPluLong(pluLong);
				promotionCode="37";
				msg = "<span style='color:#32CD32; text-align:center;font-weight: bold;'>"+"COUPON DATA EXPIRED SUCCESSFULLY FOR PRODUCT: "+pluLong+"</span>";
				/*redirectTo = "delete_coupon.jsp";*/
			}
			FrysCouponType couponType = CouponTypeFactory.getCouponType(promotionCode);
			if(!problemFlag) {
				couponType.setUserCouponInfo(couponInfo);
				couponType.setCouponServ(couponType);
				cprocess = CouponFactory.getOperationInstance(operation);
				processSuccess = cprocess.service(couponType);
			}
				if(cprocess!=null && processSuccess) {
					log.info("FrysCouponServlet.doGet(): "+msg);
					request.setAttribute("msg", msg);
					if(couponRequired != null && couponRequired.equalsIgnoreCase("Y")) {
						if(couponType.getCouponPromoBean() != null) {
							request.setAttribute("promoCode", couponType.getCouponPromoBean().getPromoCode());
						}
					}
				}else {
					log.info("FrysCouponServlet.doGet(): SOME ERROR HAS OCUURED While executing the service");
					if(problemFlag) {
						request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>PLEASE SELECT THE DATES PROPERLY <br>(START DATE CANNOT BE AFTER THE END DATE)</span>");
					}else {
						request.setAttribute("msg", "<span style='color:#B22222; text-align:center; font-weight: bold;'>Sorry! Coupon not applied successfully</span>");
					}
					
				}
				/*if(redirectTo == null) {
					redirectTo = "index.jsp";
				}*/
				//request.setAttribute("tabOpen", tabOpen);
				log.info("<<<<<<<<<<<<<<<<<<<<FRYS COUPON PROCESS ENDS >>>>>>>>>>>>>>>>>>>>");
				log.info("__________________________________________________________________");
				RequestDispatcher rd = request.getRequestDispatcher(redirectTo);
				rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("FrysCouponServlet.doPost()");
		doGet(request, response);
	}
}
