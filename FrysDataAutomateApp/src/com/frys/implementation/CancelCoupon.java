package com.frys.implementation;

import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;

public class CancelCoupon implements FrysCouponProcess{

	@Override
	public boolean service(FrysCouponType couponType) {
		couponType.expireCouponService();
		return false;
	}

	
	

}
