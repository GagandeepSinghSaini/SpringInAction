package com.frys.interfaces;

import com.frys.bean.SpecPriceBean;

public interface FrysSPType {

	public  boolean generateSpService();
	public boolean expireSpService();
	public  SpecPriceBean getUserSpInfo();
	public  void setUserSpInfo(SpecPriceBean specInfo);
	public  void setSpServ(FrysSPType stype);
}
