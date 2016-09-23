package com.frys.interfaces;


public interface FrysCouponType {

	public  void setCouponDBService(DBHelper couponDBService);
	public  boolean generateCouponService();
	public boolean expireCouponService();
}
