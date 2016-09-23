package com.frys.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ProcessService {

	public void processRequest(HttpServletRequest request, HttpServletResponse response);
}
