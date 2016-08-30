package com.frys.factory;

import com.frys.interfaces.FrysInstantSaveProcess;
import com.instantsaving.implementation.DeleteInstantSaving;
import com.instantsaving.implementation.GenerateInstantSaving;

public class InstantSavingFactory {

	private static FrysInstantSaveProcess process;
	public static FrysInstantSaveProcess getInstance(String type) {
		if(("save_generate").equalsIgnoreCase(type)) {
			process = new GenerateInstantSaving();
		}else if(("save_delete").equalsIgnoreCase(type)) {
			process = new DeleteInstantSaving();
		}
		return process;
	}
}
