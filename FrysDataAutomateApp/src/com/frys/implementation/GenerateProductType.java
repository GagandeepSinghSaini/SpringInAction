package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class GenerateProductType implements FrysProcess{
	private Logger log = Logger.getLogger(GenerateProductType.class);

	@Override
	public boolean service(DBHelper service) {
		log.info("GenerateProduct.service():  STARTS");
		try {
			if(service != null) {
				if(service.updateData()) {
					log.info("GenerateProduct.service(): SUCCESSFULLY UPDATED PRODUCT DATA");
	    			return true;
				}else {
					log.info("GenerateProduct.service(): OOPS!... UNSUCCESSFULLY UPDATE");
				}
				
			}
		}catch(Exception e) {
			log.error("GenerateProduct.service(): EXCEPTION HAS COME: "+e);
		}
		return false;
	}

}
