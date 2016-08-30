package com.coupon.operation;

import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;

public class GenerateCoupon implements FrysCouponProcess{

	@Override
	public boolean service(FrysCouponType couponType) {
		return couponType.generateCouponService();
		
	}

}
