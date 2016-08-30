package com.frys.factory;

import com.frys.interfaces.FrysSPType;
import com.sp.implementation.ButtonSpecialType;
import com.sp.implementation.ClickDetailSpecialType;
import com.sp.implementation.FindMoreSpecialType;
import com.sp.implementation.WasSpecialType;

public class SPTypeFactory {

	private static FrysSPType specType;
	
	public static FrysSPType getSpecType (String type) {
		if(("button").equalsIgnoreCase(type)) {
			specType = new ButtonSpecialType();
		}else if(("Was").equalsIgnoreCase(type)) {
			specType = new WasSpecialType();
		}else if(("FindOutMore").equalsIgnoreCase(type)) {
			specType = new FindMoreSpecialType();
		}else if(("ClickForDetail").equalsIgnoreCase(type)) {
			specType = new ClickDetailSpecialType();
		}
		return specType;
	}
}
