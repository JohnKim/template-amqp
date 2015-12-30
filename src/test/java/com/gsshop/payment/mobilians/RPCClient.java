package com.gsshop.payment.mobilians;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.UUID;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RPCClient {

	private Connection connection;
	private Channel channel;

	private String replyQueueName;
	private QueueingConsumer consumer;

	private String requestQueueName = null;

	public RPCClient(String requestQueueName) throws Exception {

		this.requestQueueName = requestQueueName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();

		replyQueueName = channel.queueDeclare().getQueue();
		consumer = new QueueingConsumer(channel);
		channel.basicConsume(replyQueueName, true, consumer);

	}

	public Map<String, String> execute(Map<String, String> message) throws Exception {

		Map<String, String> response = null;
		String corrId = UUID.randomUUID().toString();

		BasicProperties props = new BasicProperties
				.Builder()
		.correlationId(corrId)
		.replyTo(replyQueueName)
		.build();

		System.out.println("REQUEST  : "+requestQueueName);
		System.out.println("RESPONSE : "+replyQueueName);
		System.out.println("CORR_ID  : "+corrId);

		channel.basicPublish("", requestQueueName, props, convertByteArray(message));

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {				
				response = (Map<String, String>) convertObject(delivery.getBody());
				break;
			}
		}

		return response;
	}

	public void close() throws Exception {
		if(connection != null) connection.close();
	}


	private Object convertObject(byte[] ba) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(ba);
		ObjectInput in = new ObjectInputStream(bis);
		return in.readObject();
	}

	private byte[] convertByteArray(Object o) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);   
		out.writeObject(o);
		return bos.toByteArray(); 
	}	


}
