package com.sp.implementation;

import org.apache.log4j.Logger;

import com.frys.bean.SpecPriceBean;
import com.frys.interfaces.FrysSPType;
import com.specialprice.service.SpService;

public class ClickDetailSpecialType implements FrysSPType{

	Logger log = Logger.getLogger(ClickDetailSpecialType.class);
	private FrysSPType spType;
	private SpecPriceBean userSpInfo;
	@Override
	public boolean generateSpService() {
		log.info("ClickDetailSpecialType.generateSpService():STARTS with service type object: "+spType);
		boolean generateFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			generateFlag = service.updateSpClickDetailData();
		}
		log.info("ClickDetailSpecialType.generateSpService(): generate flag - "+generateFlag);
		return generateFlag;
	}

	@Override
	public boolean expireSpService() {
		log.info("ClickDetailSpecialType.expireSpService(): STARTS with service type object: "+spType);
		boolean deletedFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			deletedFlag = service.expireSpData();
		}
		log.info("ClickDetailSpecialType.expireSpService(): DELETED FLAG: "+deletedFlag);
		return deletedFlag;
	}

	@Override
	public SpecPriceBean getUserSpInfo() {
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
