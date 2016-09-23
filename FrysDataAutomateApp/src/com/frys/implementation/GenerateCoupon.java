package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysCouponProcess;
import com.frys.interfaces.FrysCouponType;
import com.frys.interfaces.FrysProcess;

public class GenerateCoupon implements FrysCouponProcess{

	private Logger log = Logger.getLogger(GenerateCoupon.class);

	@Override
	public boolean service(FrysCouponType couponType) {
		couponType.generateCouponService();
		return false;
	}
	
	
}
