package com.frys.factory;
import com.frys.implementation.CancelInstantSaving;
import com.frys.implementation.CancelProductType;
import com.frys.implementation.CancelRebate;
import com.frys.implementation.CancelSpecialPrice;
import com.frys.implementation.GenerateInstantSaving;
import com.frys.implementation.GenerateProductType;
import com.frys.implementation.GenerateRebate;
import com.frys.implementation.GenerateSpecialPrice;
import com.frys.interfaces.FrysProcess;

public class FrysActionFactory {

	private static FrysProcess process = null;
	public static FrysProcess getInstance(String op) {
		System.out.println("FrysProcess.getInstance()");
		if(op != null && ("rebate_generate").equalsIgnoreCase(op)) {
			process = new GenerateRebate();
		}else if(op != null && ("delete_rebate").equalsIgnoreCase(op)) {
			process = new CancelRebate();
		}else if(op != null && ("save_generate").equalsIgnoreCase(op)) {
			process = new GenerateInstantSaving();
		}else if(op != null && ("save_delete").equalsIgnoreCase(op)) {
			process = new CancelInstantSaving();
		}else if(op != null && ("generate_product_type").equalsIgnoreCase(op)) {
			process = new GenerateProductType();
		}else if(op != null && ("delete_product_type").equalsIgnoreCase(op)) {
			process = new CancelProductType();
		}else if(op != null && ("sprice_generate").equalsIgnoreCase(op)) {
			process = new GenerateSpecialPrice();
		}else if(op != null && ("delete_sprice").equalsIgnoreCase(op)) {
			process = new CancelSpecialPrice();
		}
		return process;
	}
	
}
