package com.coupon.implementation.coupon_type;

import org.apache.log4j.Logger;

import com.coupon.service.HelpingCouponService;
import com.frys.bean.CouponBean;
import com.frys.bean.FrysCoupBean;
import com.frys.interfaces.FrysCouponType;

public class CouponType36Or37 implements FrysCouponType{
	private CouponType36Or37  couponService;
	private CouponBean couponInfo;
	private FrysCoupBean coupBean;
	private Logger log = Logger.getLogger(CouponType36Or37.class);

	@Override
	public boolean generateCouponService() {
		log.info("CouponType36Or37.generateCouponService() : STARTS");
		try{
			if(couponService!=null) {
				HelpingCouponService couponserv = new HelpingCouponService(couponService);
				couponserv.populateUserCouponInfo();
				/*if(couponserv.insertCouponData()) {
	    			 System.out.println("MainCoupon.applyCoupon(): SUCCESSFULLY INSERTED DATA FOR COUPON");
	    			 return true;
	    		 } else {
	    			 System.out.println("MainCoupon.applyCoupon(): SORRY! SOME PROBLEM HAS OCCURED WHILE INSERTING DATA");
	    			 return false;
	    		 }*/
		    	 if(couponserv != null && couponserv.checkCouponData()) {
		    		 couponserv.deleteFrysCoIfExist();
		    		 if(couponserv.updateCouponData()) {
		    			log.info("CouponType36Or37.generateCouponService(): SUCCESSFULLY UPDATED DATA FOR COUPON");
		    			return true;
		    		 }else {
		    			 log.info("CouponType36Or37.generateCouponService(): SORRY! SOME PROBLEM HAS OCCURED WHILE UPDATING DATA");
		    			 return false;
		    		 }	 
		    	 }else {
		    		 if(couponserv.insertCouponData()) {
		    			log.info("CouponType36Or37.generateCouponService(): SUCCESSFULLY INSERTED DATA FOR COUPON");
		    			 return true;
		    		 } else {
		    			 log.info("CouponType36Or37.generateCouponService(): SORRY! SOME PROBLEM HAS OCCURED WHILE INSERTING DATA");
		    			 return false;
		    		 }
		    	 }
		     }else {
		    	 log.error("CouponType36Or37.generateCouponService(): couponService is Null");
		     }
		}catch(Exception exp) {
			log.error("CouponType36Or37.generateCouponService(): ERROR"+exp);
		}
	return false;

	}

	@Override
	public void setCouponServ(FrysCouponType couponService) {
		this.couponService = (CouponType36Or37) couponService;
		
	}

	@Override
	public void setUserCouponInfo(CouponBean couponInfo) {
		this.couponInfo = couponInfo;
		
	}

	
	@Override
	public void setCouponPromoBean(FrysCoupBean coupBean) {
		this.coupBean = coupBean;
	}

	@Override
	public CouponBean getUserCouponInfo() {
		return couponInfo;
	}

	@Override
	public FrysCoupBean getCouponPromoBean() {
		return coupBean;
	}

	@Override
	public boolean expireCouponService() {
		log.info("CouponType36Or37.expireCouponService(): STARTS");
		HelpingCouponService couponserv = new HelpingCouponService(couponService);
		couponserv.populateUserCouponInfo();
		if(couponserv != null && couponserv.checkCouponData()) {
			log.info("CouponType36Or37.expireCouponService(): Coupon Data exists for the given product");
			return couponserv.expireCoupon();
		}
		log.info("CouponType36Or37.expireCouponService(): Coupon Data does not exists for the given product");
		return false;
	}

}
