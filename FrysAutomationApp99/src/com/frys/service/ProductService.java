package com.frys.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.frys.controller.FrysSpecPriceServlet;
import com.frys.util.DbConnection;

public class ProductService {
	private Logger log = Logger.getLogger(ProductService.class);
	
	final private String PRODUCT_QUERY = "SELECT bv.PROD_ID, bv.STATUS, bv.DELETED FROM BV_PRODUCT bv, FR_PRODUCT_FLAGS fr "
			+ "WHERE bv.PROD_ID = ? and bv.STATUS=1 and bv.DELETED=0 and bv.PROD_ID = fr.PROD_ID";
	
	public boolean checkValidProduct(String prodId) {
		log.info("ProductService.checkValidProduct(): START");
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DbConnection dbConnect = new DbConnection();
		con = dbConnect.getBv1Connection(MainConstants.ORAC_DRIVER, MainConstants.BV1_URL, MainConstants.BV1_USERNAME, MainConstants.BV1_PASSWORD);
		try {
			if(con != null && !con.isClosed()) {
				log.info("ProductService.checkValidProduct(): QUERY - "+PRODUCT_QUERY);
				stmt = con.prepareStatement(PRODUCT_QUERY);
				stmt.setString(1, prodId);
				rs = stmt.executeQuery();
				if(rs.next()) {
					log.info("ProductService.checkValidProduct(): PRODUCT IS VALID");
					return true;
				}
				log.info("ProductService.checkValidProduct(): PRODUCT IS NOT VALID");
			}
		} catch (SQLException e) {
			log.error("ProductService.checkValidProduct(): "+e);
			e.printStackTrace();
		}
		return false;
	}
}
