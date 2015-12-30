package com.gsshop.payment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public abstract class RPCConsumer {

	public RPCConsumer() {
	
	}
	
	
	public void execute(String queueName){

		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.queueDeclare(queueName, false, false, false, null);

			channel.basicQos(1);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, consumer);

			System.out.println(" [x] Awaiting RPC requests ["+queueName+"]");

			while (true) {
				Map<String, String> response = null;

				QueueingConsumer.Delivery delivery = consumer.nextDelivery();

				BasicProperties props = delivery.getProperties();
				BasicProperties replyProps = new BasicProperties
						.Builder()
				.correlationId(props.getCorrelationId())
				.build();

				try {

					HashMap<String, String> data = (HashMap<String, String>)convertObject(delivery.getBody());

					response = message(data);
				}
				catch (Exception e){
					System.out.println(" [.] " + e.toString());
					response = new HashMap<String, String>();
					response.put("error", e.getMessage());
				}
				finally {  

					channel.basicPublish( "", props.getReplyTo(), replyProps, convertByteArray(response));
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				}
			}
		}
		catch  (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			}
		}      		   		    
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

	public abstract Map<String, String> message(Map<String, String> data);

}