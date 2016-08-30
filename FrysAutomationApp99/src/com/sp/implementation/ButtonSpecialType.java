package com.sp.implementation;

import org.apache.log4j.Logger;

import com.frys.bean.SpecPriceBean;
import com.frys.interfaces.FrysSPType;
import com.specialprice.service.SpService;

public class ButtonSpecialType implements FrysSPType{

	Logger log = Logger.getLogger(ButtonSpecialType.class);
	private FrysSPType spType;
	private SpecPriceBean userSpInfo;
	@Override
	public boolean generateSpService() {
		log.info("ButtonSpecialType.generateSpService():STARTS with service type object: "+spType);
		boolean generateFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			generateFlag = service.updateSpButtonData();
		}
		log.info("ButtonSpecialType.generateSpService(): generate flag - "+generateFlag);
		return generateFlag;
	}

	@Override
	public boolean expireSpService() {
		log.info("ButtonSpecialType.expireSpService(): STARTS with service type object: "+spType);
		boolean deletedFlag = false;
		if(spType != null) {
			SpService service = new SpService(spType);
			service.populateSpecialPriceInfo();
			deletedFlag = service.expireSpData();
		}
		log.info("ButtonSpecialType.expireSpService(): DELETED FLAG: "+deletedFlag);
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
