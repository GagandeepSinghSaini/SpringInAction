package com.sp.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysSPType;
import com.frys.interfaces.FrysSpProcess;

public class DeleteSPProduct implements FrysSpProcess{

	Logger log = Logger.getLogger(DeleteSPProduct.class);
	
	@Override
	public boolean service(FrysSPType service) {
		log.info("DeleteSPProduct.service(): STARTS");
		if(service != null) {
			return service.expireSpService();
		}
		log.info("DeleteSPProduct.service(): FrysSPType Obect is null");
		return false;
	}

}
