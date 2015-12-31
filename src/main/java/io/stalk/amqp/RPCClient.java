package io.stalk.amqp;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.util.UUID;

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

    public RPCSerializable execute(RPCSerializable message) throws Exception {

        RPCSerializable response;
        String corrId = UUID.randomUUID().toString();

        BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        System.out.println("REQUEST  : " + requestQueueName);
        System.out.println("RESPONSE : " + replyQueueName);
        System.out.println("CORR_ID  : " + corrId);

        channel.basicPublish("", requestQueueName, props, SerializationUtils.serialize(message));

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = SerializationUtils.deserialize(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        if (connection != null) connection.close();
    }

}
