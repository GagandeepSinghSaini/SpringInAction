package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.dao.services.RebateDBHelper;
import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;
import com.frys.util.DbConnection;

public class CancelRebate implements FrysProcess{

	private Logger log = Logger.getLogger(CancelRebate.class);

	@Override
	public boolean service(DBHelper service) {
		log.info("CancelRebate.service()");
		try {
			if(service!=null){
				if(service.checkData()) {
					return service.cancelData();
				} else {
					log.info("CancelRebate.service(): PRODUCT ALREADY DOES NOT HAVE REBATE");
				}
			}
		}catch(Exception e) {
			log.error("CancelRebate.service(): SOME ERROR IS THERE");
			e.printStackTrace();
		}
		return false;
	}

}
