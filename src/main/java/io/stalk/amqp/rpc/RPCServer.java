package io.stalk.amqp.rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class RPCServer<IN extends RPCSerializable, OUT extends RPCSerializable> {

    public static Logger logger = LoggerFactory.getLogger(RPCServer.class);

    public static String LOG_FILE_NAME = "logFileName"; // the file name for logback MDC.

    public void execute(String queueName) {

        Connection connection = null;
        Channel channel;

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setAutomaticRecoveryEnabled(true);
            factory.setTopologyRecoveryEnabled(true);

            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            channel.basicQos(1);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(queueName, false, consumer);

            logger.info(" [x] Awaiting RPC requests [" + queueName + "]");

            while (true) {

                OUT response = (OUT) new RPCSerializable();

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

                    e.printStackTrace();
                    System.out.println(" [.] " + e.toString());

                    response.setStatus("ERR");
                    response.setError(e.getMessage());
                } finally {

                    channel.basicPublish("", props.getReplyTo(), replyProps, SerializationUtils.serialize(response));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }

        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        } catch (TimeoutException te) {
            logger.error(te.getMessage(), te);
        } catch (InterruptedException ie) {
            logger.error(ie.getMessage(), ie);
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