package com.rebate.implementation;

import org.apache.log4j.Logger;

import com.frys.interfaces.FrysRebateProcess;
import com.frys.util.DbConnection;
import com.rebate.service.RebateService;

public class CancelRebate implements FrysRebateProcess{

	private Logger log = Logger.getLogger(CancelRebate.class);
	
	@Override
	public boolean service(RebateService service) {
		log.info("CancelRebate.service()");
		try {
			if(service!=null){
				if(service.checkData()) {
					return service.cancelRebate();
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
