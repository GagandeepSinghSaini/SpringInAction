package com.frys.factory;

import com.coupon.implementation.coupon_type.CouponType36Or37;
import com.coupon.implementation.coupon_type.CouponType47;
import com.frys.interfaces.FrysCouponType;

public class CouponTypeFactory {

	private static FrysCouponType ctype = null;
	
	public static FrysCouponType getCouponType(String type) {
		if(("36").equals(type) || ("37").equals(type)) {
			ctype = new CouponType36Or37();
		}else if(("47").equals(type)) {
			ctype = new CouponType47();
		}
		return ctype;
	}
}
