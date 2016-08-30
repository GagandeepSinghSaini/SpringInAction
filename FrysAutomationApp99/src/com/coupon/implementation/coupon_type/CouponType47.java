package com.coupon.implementation.coupon_type;

import org.apache.log4j.Logger;

import com.coupon.service.HelpingCouponService;
import com.frys.bean.CouponBean;
import com.frys.bean.FrysCoupBean;
import com.frys.interfaces.FrysCouponType;

public class CouponType47 implements FrysCouponType{

	private Logger log = Logger.getLogger(CouponType47.class);
	private CouponBean couponInfo;
	private CouponType47 couponService;
	@Override
	public void setCouponServ(FrysCouponType couponService) {
		this.couponService = (CouponType47) couponService;
		
	}

	@Override
	public boolean generateCouponService() {
		log.info("CouponType47.generateCouponService() : STARTS");
		boolean updateFlag = false;
		try {
			if(couponService != null) {
				HelpingCouponService couponserv = new HelpingCouponService(couponService);
				couponserv.populateUserCouponInfo();
				if(couponserv != null && couponserv.checkCouponData()) {
					updateFlag = couponserv.updateFrPromDetail();
				}
			}else {
		    	 log.error("CouponType47.generateCouponService(): couponService is Null");
		     }
		}catch(Exception e) {
			log.error("CouponType47.generateCouponService(): ERROR"+e);
		}
		log.info("UPDATE DATA STATUS: "+updateFlag);
		return updateFlag;
	}

	@Override
	public void setUserCouponInfo(CouponBean couponInfo) {
		this.couponInfo = couponInfo;
		
	}

	@Override
	public void setCouponPromoBean(FrysCoupBean coupBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CouponBean getUserCouponInfo() {
		// TODO Auto-generated method stub
		return couponInfo;
	}

	@Override
	public FrysCoupBean getCouponPromoBean() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean expireCouponService() {
		log.info("CouponType47.expireCouponService(): STARTS");
		HelpingCouponService couponserv = new HelpingCouponService(couponService);
		couponserv.populateUserCouponInfo();
		if(couponserv != null && couponserv.checkCouponData()) {
			log.info("CouponType47.expireCouponService(): Coupon Data exists for the given product");
			return couponserv.expireCoupon();
		}
		log.info("CouponType47.expireCouponService(): Coupon Data does not exists for the given product");
		return false;
	}

}
