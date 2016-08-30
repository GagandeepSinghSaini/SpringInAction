package com.frys.factory;

import com.frys.interfaces.FrysSpProcess;
import com.sp.implementation.DeleteSPProduct;
import com.sp.implementation.GenerateSPProduct;

public class SPFactory {

	private static FrysSpProcess process;
	public static FrysSpProcess getInstance(String operation) {
		if(("sprice_generate").equalsIgnoreCase(operation)) {
			process = new GenerateSPProduct();
		}else if(("delete_sprice").equalsIgnoreCase(operation)) {
			process = new DeleteSPProduct();
		}
		return process;
	}
}
