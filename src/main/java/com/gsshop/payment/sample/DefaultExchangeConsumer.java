package com.gsshop.payment.sample;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class DefaultExchangeConsumer {

	private static final String QUEUE_NAME = "payment.sample.direct";

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();

		factory.setHost("127.0.0.1");
		factory.setUsername("guest");
		factory.setPassword("guest");
		
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME,  false, consumer);
		
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery(); // timeout !. 다음 메시지르 수진될 때 까지  BLOCK !
			
			String message = new String(delivery.getBody());
			
			System.out.println("MESSAGE : "+message);
			
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
		
	}
}
