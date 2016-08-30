package com.coupon.operation;

import org.apache.log4j.Logger;

import com.coupon.implementation.coupon_type.CouponType36Or37;
import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;

public class DeleteCoupon implements FrysCouponProcess{
	private Logger log = Logger.getLogger(DeleteCoupon.class);
	@Override
	public boolean service(FrysCouponType couponType) {
		try {
			if(couponType!=null) {
				return couponType.expireCouponService();
			}
		}catch(Exception e) {
			log.error("DeleteCoupon.service(): EXCEPTION");
			e.printStackTrace();
		}
		return false;
	}

}
