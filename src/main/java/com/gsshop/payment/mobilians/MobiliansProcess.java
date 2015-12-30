package com.gsshop.payment.mobilians;

import java.util.HashMap;
import java.util.Map;

import com.gsshop.payment.RPCConsumer;

public class MobiliansProcess extends RPCConsumer {

	@Override
	public Map<String, String> message(Map<String, String> data) {
		
		
		// ............
		
		
		System.out.println(data);
		
		Map<String, String> retValue = new HashMap<String, String>();
		retValue.put("value", "valueDatas");
		
		return retValue;
	}
	
	public static void main(String[] args) {
		MobiliansProcess process = new MobiliansProcess();
		process.execute("payment.mobilians");
	}

}
