package com.instantsaving.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysInstantSaveProcess;
import com.instantsaving.service.InstantSavingService;

public class DeleteInstantSaving implements FrysInstantSaveProcess{

	private Logger log = Logger.getLogger(DeleteInstantSaving.class);
	@Override
	public boolean service(InstantSavingService service) {
		log.info("DeleteInstantSaving.service(): STARTS CANCELLING THE INSTANT SAVING");
		if(service != null) {
			return service.cancelInstantSaving();
		}
		log.info("DeleteInstantSaving.service(): SOME ERROR WHILE CANCELLING INSTANT SAVING");
		return false;
	}

	

}
