package com.product.type.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysProductTypeProcess;
import com.producttype.service.ProductTypeService;
import com.rebate.implementation.GenerateRebate;

public class GenerateProduct implements FrysProductTypeProcess{
	private Logger log = Logger.getLogger(GenerateProduct.class);
	
	@Override
	public boolean service(ProductTypeService service) {
		log.info("GenerateProduct.service():  STARTS");
		try {
			if(service != null) {
				if(service.getProductTypeBean().getTransportType().equals("store")) {
					return service.updateStoreProduct();
				}else {
					return service.updateShipProduct();
				}
				
			}
		}catch(Exception e) {
			log.error("EXCEPTION HAS COME: "+e);
		}
		return false;
	}

}
