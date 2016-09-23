package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class GenerateSpecialPrice implements FrysProcess {

	private Logger log = Logger.getLogger(GenerateSpecialPrice.class);
	
	@Override
	public boolean service(DBHelper service) {
		log.info("GenerateSpecialPrice.service()");
		try {
			if(service != null) {
				return service.updateData();
		}else {
			log.info("CancelProduct.service():  SERVICE IS NULL");
		}
		}catch(Exception e) {
			log.error("GenerateSpecialPrice.service(): EXCEPTION HAS COME: "+e);
		}
		return false;
	}

}
