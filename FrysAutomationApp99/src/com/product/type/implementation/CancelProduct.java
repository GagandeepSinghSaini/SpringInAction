package com.product.type.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysProductTypeProcess;
import com.producttype.service.ProductTypeService;

public class CancelProduct implements FrysProductTypeProcess{

	private Logger log = Logger.getLogger(CancelProduct.class);
	@Override
	public boolean service(ProductTypeService service) {
		log.info("CancelProduct.service():  STARTS");
		try {
			if(service != null) {
				if(("store").equalsIgnoreCase(service.getProductTypeBean().getTransportType())) {
					return service.cancelStoreProductType();
				}else {
					return service.cancelProductType();
				}
				
			}
		}catch(Exception e) {
			log.error("EXCEPTION HAS COME: "+e);
		}
		return false;
	}

}
