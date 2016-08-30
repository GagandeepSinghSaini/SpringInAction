package com.sp.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysSPType;
import com.frys.interfaces.FrysSpProcess;
import com.specialprice.service.SpService;

public class GenerateSPProduct implements FrysSpProcess{

	Logger log = Logger.getLogger(GenerateSPProduct.class);
	
	@Override
	public boolean service(FrysSPType service) {
		log.info("generateSPProduct.service(): STARTS");
		if(service != null) {
			return service.generateSpService();
		}
		log.info("GenerateSPProduct.service(): FrysSPType Obect is null");
		return false;
	}

}
