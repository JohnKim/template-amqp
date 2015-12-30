package com.gsshop.payment;

public class Payment {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		
		String className = System.getProperty("targetClass");
		String queueName = System.getProperty("queueName");
		
		System.out.println("Target Class Name : "+className);
		System.out.println("Queue Name : "+queueName);
		
		Class<RPCConsumer> claz = (Class<RPCConsumer>) Class.forName(className);
		RPCConsumer iClass = claz.newInstance();
		
		iClass.execute(queueName);
		
	}
}
