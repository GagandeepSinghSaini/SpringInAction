package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class CancelProductType implements FrysProcess{

	private Logger log = Logger.getLogger(CancelProductType.class);
	
	@Override
	public boolean service(DBHelper service) {
		log.info("CancelProduct.service():  STARTS");
		try {
			if(service != null) {
					return service.cancelData();
			}else {
				log.info("CancelProduct.service():  SERVICE IS NULL");
			}
		}catch(Exception e) {
			log.error("CancelProduct.service(): EXCEPTION HAS COME: "+e);
		}
		return false;
	}

}
