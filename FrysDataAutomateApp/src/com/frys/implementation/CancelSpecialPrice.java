package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class CancelSpecialPrice implements FrysProcess {

	private Logger log = Logger.getLogger(CancelSpecialPrice.class);
	@Override
	public boolean service(DBHelper service) {
		log.info("CancelSpecialPrice.service()");
		try {
			if(service!=null){
					return service.cancelData();
			}
		}catch(Exception e) {
			log.error("CancelSpecialPrice.service(): SOME ERROR IS THERE");
			e.printStackTrace();
		}
		return false;
	}

}
