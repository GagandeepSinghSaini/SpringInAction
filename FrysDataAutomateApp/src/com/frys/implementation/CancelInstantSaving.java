package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class CancelInstantSaving implements FrysProcess{

	private Logger log = Logger.getLogger(CancelInstantSaving.class);
	
	@Override
	public boolean service(DBHelper service) {
		log.info("DeleteInstantSaving.service(): STARTS CANCELLING THE INSTANT SAVING");
		if(service != null) {
			return service.cancelData();
		}
		log.info("DeleteInstantSaving.service(): SOME ERROR WHILE CANCELLING INSTANT SAVING");
		return false;
	}

	

}
