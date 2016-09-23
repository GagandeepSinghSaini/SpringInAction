package com.frys.implementation;

import org.apache.log4j.Logger;

import com.frys.dao.services.RebateDBHelper;
import com.frys.interfaces.DBHelper;
import com.frys.interfaces.FrysProcess;

public class GenerateRebate implements FrysProcess{

	private Logger log = Logger.getLogger(GenerateRebate.class);

	@Override
	public boolean service(DBHelper service) {
		log.info("GenerateRebate.service()");
		try {
			if(service!=null){
				if(service.checkData()) {
					log.info("GenerateRebate.service(): DATA ALREADY EXISTS IN REBATE TABLES");
					if(service.updateData()) {
		    			log.info("GenerateRebate.service(): SUCCESSFULLY UPDATED DATA FOR REBATE");
		    			return true;
		    		 }
				}else {
	    			 log.info("GenerateRebate.service(): DATA DOES NOT EXIST IN REBATE TABLES, NEED TO ADD NEW ENTRY");
	    			 if(service.insertData()) {
	    				 return true;
	    			 }
	    			 log.info("GenerateRebate.service(): SORRY! SOME PROBLEM HAS OCCURED WHILE INSERTING DATA");
	    		 }	
			}
		}catch(Exception e) {
			log.error("GenerateRebate.service(): **** ERROR ******** : "+e);
			return false;
		}
		return false;
	}

}
