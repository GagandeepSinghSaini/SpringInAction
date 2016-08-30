package com.frys.factory;

import com.coupon.operation.DeleteCoupon;
import com.coupon.operation.GenerateCoupon;
import com.frys.interfaces.FrysCouponProcess;

public class CouponFactory {
	private static FrysCouponProcess cprocess = null;
	public static FrysCouponProcess getOperationInstance(String operation) {
		
		if(("generate_coupon").equalsIgnoreCase(operation)) {
			cprocess = new GenerateCoupon();
		}else if(("delete_coupon").equalsIgnoreCase(operation)) {
			cprocess = new DeleteCoupon();
		}
		return cprocess;
	}
	
}
