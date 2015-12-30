package com.gsshop.payment.mobilians;

import java.util.HashMap;
import java.util.Map;

public class MobiliansRPCClient {

	public static void main(String[] argv) {

		RPCClient mobiliansRPC = null; 
		try {

			mobiliansRPC = new RPCClient("payment.mobilians");

			Map<String, String> requestMap = new HashMap<String, String>();

			requestMap.put("Mode", "MMM1");
			requestMap.put("Reordkey", "MMM2");
			requestMap.put("Mrchid", "MMM3");
			requestMap.put("Svcid", "MMM4");
			requestMap.put("No", "MMM5");
			requestMap.put("Socialno", "MMM6");

			Map<String, String> responseMap = mobiliansRPC.execute(requestMap);
			
			System.out.println(responseMap);
			
		}
		catch  (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (mobiliansRPC!= null) {
				try {
					mobiliansRPC.close();
				}
				catch (Exception ignore) {}
			}
		}
	}
}
