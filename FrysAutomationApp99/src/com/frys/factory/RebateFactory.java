package com.frys.factory;
import com.frys.interfaces.FrysRebateProcess;
import com.rebate.implementation.CancelRebate;
import com.rebate.implementation.GenerateRebate;


public class RebateFactory {

	private static FrysRebateProcess rbProcess = null;
	public static FrysRebateProcess getInstance(String op) {
		System.out.println("RebateFactory.getInstance()");
		if(op != null && ("rebate_generate").equalsIgnoreCase(op)) {
			rbProcess = new GenerateRebate();
		} if(op != null && ("delete_rebate").equalsIgnoreCase(op)) {
			rbProcess = new CancelRebate();
		}
		return rbProcess;
	}
	
}
