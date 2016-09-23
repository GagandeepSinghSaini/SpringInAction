package com.frys.factory;

import com.frys.handler.CouponHandler;
import com.frys.handler.InstantSavingHandler;
import com.frys.handler.ProductTypeHandler;
import com.frys.handler.RebateHandler;
import com.frys.handler.SpecialPriceHandler;
import com.frys.interfaces.ProcessService;

public class HandlerMapping {

	private static ProcessService handler = null;
	public static ProcessService getHandler (String type) {
		if(type == null) {
			return handler;
		}else {
			if(type.contains("rebate")) {
				handler = new RebateHandler();
			}else if(type.contains("save")) {
				handler = new InstantSavingHandler();
			}else if(type.contains("product_type")) {
				handler = new ProductTypeHandler();
			}else if(type.contains("sprice")) {
				handler = new SpecialPriceHandler();
			}else if(type.contains("coupon")) {
				handler = new CouponHandler();
			}
		}
		return handler;
	}
}
