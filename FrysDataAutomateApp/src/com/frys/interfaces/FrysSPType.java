package com.frys.interfaces;

import com.frys.bean.SpecialPriceBean;



public interface FrysSPType {

	public  boolean generateSpService();
	public boolean expireSpService();
	public  SpecialPriceBean getUserSpInfo();
	public  void setUserSpInfo(SpecialPriceBean specInfo);
	public  void setSpServ(FrysSPType stype);
}
