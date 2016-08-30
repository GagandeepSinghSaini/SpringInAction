package com.sp.implementation;

import org.apache.log4j.Logger;

import com.frys.bean.SpecPriceBean;
import com.frys.interfaces.FrysSPType;
import com.specialprice.service.SpService;

public class WasSpecialType implements FrysSPType{

	Logger log = Logger.getLogger(WasSpecialType.class);
	private FrysSPType spType;
	private SpecPriceBean userSpInfo;
	@Override
	public boolean generateSpService() {
		log.info("WasSpecialType.generateSpService(): STARTS with service type object: "+spType);
		boolean generateFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			generateFlag = service.updateSpWasData();
		}
		log.info("WasSpecialType.generateSpService(): DELETED FLAG: "+generateFlag);
		return generateFlag;
	}

	@Override
	public boolean expireSpService() {
		log.info("WasSpecialType.expireSpService(): STARTS with service type object: "+spType);
		boolean deletedFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			deletedFlag = service.expireSpData();
		}
		log.info("WasSpecialType.expireSpService(): DELETED FLAG: "+deletedFlag);
		return deletedFlag;
	}

	@Override
	public SpecPriceBean getUserSpInfo() {
		// TODO Auto-generated method stub
		return userSpInfo;
	}

	@Override
	public void setUserSpInfo(SpecPriceBean specInfo) {
		userSpInfo = specInfo;
		
	}

	@Override
	public void setSpServ(FrysSPType stype) {
		spType = stype;
		
	}

}
