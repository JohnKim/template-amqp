package io.stalk.amqp;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public abstract class RPCServer<IN extends RPCSerializable, OUT extends RPCSerializable> {

    public void execute(String queueName) {

        Connection connection = null;
        Channel channel;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);

            System.out.println(" [x] Awaiting RPC requests [" + queueName + "]");

            while (true) {

                OUT response = null;

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                BasicProperties props = delivery.getProperties();
                BasicProperties replyProps = new BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    IN data = SerializationUtils.deserialize(delivery.getBody());

                    response = message(data);
                } catch (Exception e) {
                    System.out.println(" [.] " + e.toString());

                    response = (OUT) new RPCSerializable(); // @TODO handling ShutdownSignalException (check and reconnect)
                    response.setStatus("ERR");
                    response.setError(e.getMessage());
                } finally {

                    channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    public abstract OUT message(IN data);

}