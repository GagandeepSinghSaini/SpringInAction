package com.rebate.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysRebateProcess;
import com.rebate.service.RebateService;

public class GenerateRebate implements FrysRebateProcess{

	private Logger log = Logger.getLogger(GenerateRebate.class);
	
	@Override
	public boolean service(RebateService service) {
		log.info("GenerateRebate.service()");
		try {
			if(service!=null){
				if(service.checkData()) {
					log.info("GenerateRebate.service(): DATA ALREADY EXISTS IN REBATE TABLES");
					if(service.updateRebateData()) {
		    			log.info("GenerateRebate.service(): SUCCESSFULLY UPDATED DATA FOR REBATE");
		    			return true;
		    		 }
				}else {
	    			 log.info("GenerateRebate.service(): DATA DOES NOT EXIST IN REBATE TABLES, NEED TO ADD NEW ENTRY");
	    			 if(service.insertRebateData()) {
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
