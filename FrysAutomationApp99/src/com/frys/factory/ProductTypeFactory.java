package com.frys.factory;

import com.frys.interfaces.FrysProductTypeProcess;
import com.product.type.implementation.CancelProduct;
import com.product.type.implementation.GenerateProduct;

public class ProductTypeFactory {

	private static FrysProductTypeProcess process;
	public static FrysProductTypeProcess getInstance(String type) {
		if(("generate_product_type").equalsIgnoreCase(type)) {
			process = new GenerateProduct();
		}else if(("delete_product_type").equalsIgnoreCase(type)) {
			process = new CancelProduct();
		}
		return process;
	}
}
