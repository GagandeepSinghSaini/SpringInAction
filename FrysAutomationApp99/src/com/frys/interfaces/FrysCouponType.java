package com.frys.interfaces;

import com.frys.bean.CouponBean;
import com.frys.bean.FrysCoupBean;

public interface FrysCouponType {

	public  void setCouponServ(FrysCouponType couponService);
	public  boolean generateCouponService();
	public  void setUserCouponInfo(CouponBean couponInfo);
	public  void setCouponPromoBean(FrysCoupBean coupBean);
	public  CouponBean getUserCouponInfo();
	public  FrysCoupBean getCouponPromoBean();
	public boolean expireCouponService();
}
