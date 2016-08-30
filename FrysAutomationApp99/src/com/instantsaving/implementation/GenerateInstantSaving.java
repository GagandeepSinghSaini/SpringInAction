package com.instantsaving.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysInstantSaveProcess;
import com.instantsaving.service.InstantSavingService;
import com.rebate.implementation.GenerateRebate;

public class GenerateInstantSaving implements FrysInstantSaveProcess{

	private Logger log = Logger.getLogger(GenerateInstantSaving.class);
	@Override
	public boolean service(InstantSavingService service) {
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
			log.error("ERROR: "+e);
			e.printStackTrace();
		}
		return false;
	}

	
}
