package com.frys.implementation.coupon;

import org.apache.log4j.Logger;

import com.frys.dao.services.CouponDBHelper;
import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysCouponType;

public class CouponType36Or37 implements FrysCouponType{

	private CouponDBHelper couponDBService;
	private Logger log = Logger.getLogger(CouponType36Or37.class);
	@Override
	public boolean generateCouponService() {
		log.info("CouponType36Or37.generateCouponService(): STARTS");
		log.info("CouponType36Or37.generateCouponService(): CouponDBService: "+couponDBService);
		if(couponDBService != null) {
			if(couponDBService.checkData()) {
				couponDBService.deleteFrysCoIfExist();
				couponDBService.updateData();
			}else {
				couponDBService.insertData();
			}
		}
		return false;
	}

	@Override
	public boolean expireCouponService() {
		log.info("CouponType36Or37.expireCouponService(): STARTS");
		log.info("CouponType36Or37.expireCouponService(): CouponDBService: "+couponDBService);
		if(couponDBService != null) {
			
		}
		return false;
	}

	@Override
	public void setCouponDBService(DBHelper couponDBService) {
		this.couponDBService = (CouponDBHelper)couponDBService;
		
	}

}
