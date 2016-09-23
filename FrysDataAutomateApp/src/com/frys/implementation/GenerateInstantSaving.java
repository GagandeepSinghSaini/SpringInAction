package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class GenerateInstantSaving implements FrysProcess{

	private Logger log = Logger.getLogger(GenerateInstantSaving.class);
	@Override
	public boolean service(DBHelper service) {
		log.info("GenerateInstantSaving.service(): STARTS");
		try {
			if(service != null) {
				if(service.updateData()) {
					log.info("GenerateInstantSaving.service(): SUCCESSFULLY UPDATED DATA FOR INSTANT SAVING");
	    			return true;
				}else {
					log.info("GenerateInstantSaving.service(): SOME ERROR WHILE UPDATION");
				}
			}
		}catch(Exception e) {
			log.error("GenerateInstantSaving.service():: ERROR: "+e);
			e.printStackTrace();
		}
		return false;
	}
}
