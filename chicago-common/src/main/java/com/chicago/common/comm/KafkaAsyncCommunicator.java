package com.chicago.common.comm;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import com.chicago.common.actors.KafkaResponseActor;
import com.chicago.common.core.AbstractComponent;
import com.chicago.common.util.ActorUtil;
import com.chicago.common.util.KafkaUtil;
import com.chicago.dto.Common;
import com.chicago.dto.Config;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaAsyncCommunicator implements AsyncCommunicator
{
    private static final Logger _LOG = LoggerFactory.getLogger(KafkaAsyncCommunicator.class);

    private KafkaProducer<byte[], byte[]> _kafkaProducer;
    private KafkaConsumer<byte[], byte[]> _kafkaConsumer;
    private Config.KafkaConfig _kafkaConfig;

    @Inject
    public KafkaAsyncCommunicator(Config.ZooKeeperConfig zooConfig, Config.KafkaConfig kafkaConfig)
    {
        _kafkaConfig = kafkaConfig;
        _kafkaConsumer = KafkaUtil.createConsumer(zooConfig.getServers(), kafkaConfig.getServers(),
                kafkaConfig.getConsumerTopicList(), kafkaConfig.getConsumerGroup());
        _kafkaProducer = KafkaUtil.createProducer(zooConfig.getServers(), kafkaConfig.getServers(),
                kafkaConfig.getProducerTopic());

        Thread t = new Thread(() -> run());
        t.start();
    }

    private void run()
    {
        while (true)
        {
            ConsumerRecords<byte[], byte[]> consumerRecords = _kafkaConsumer.poll(1000);
            for (ConsumerRecord<byte[], byte[]> record : consumerRecords.records(_kafkaConfig.getConsumerTopicList().get(0)))
            {
                String transactionId = null;
                try
                {
                    Common.TransactionKey transactionKey = Common.TransactionKey.parseFrom(record.key());
                    transactionId = transactionKey.getTransactionId();
                    _LOG.info("Received response with transaction Id: {}", transactionId);
                } catch (InvalidProtocolBufferException e)
                {
                    _LOG.error(e.getMessage());
                }
                // Get actor with same name as message key and send message value to it
                ActorUtil.getRootActor(transactionId).tell(
                        new KafkaResponseActor.KafkaResponse(record.value()),
                        ActorRef.noSender()
                );
            }
        }
    }

    @Override
    public byte[] transaction(Message value) throws TimeoutException
    {
        // Get Akka Actor System
        ActorSystem system = ActorUtil.getActorSystem();
        String actorName = UUID.randomUUID().toString();
        ActorRef kafkaReqResActorRef = system.actorOf(KafkaResponseActor.props(), actorName);
        Common.TransactionKey transactionKey = Common.TransactionKey.newBuilder()
                .setTransactionId(actorName)
                .setDataType(value.getClass().getCanonicalName())
                .build();
        // Create an inbox to communicate with Actor
        Inbox inbox = Inbox.create(system);
        inbox.send(kafkaReqResActorRef, new KafkaResponseActor.KafkaMessageRequest());
        // Send message to Kafka for processing
        byte[] byteKey = transactionKey.toByteArray();
        byte[] byteValue = value.toByteArray();
        _kafkaProducer.send(new ProducerRecord<>(_kafkaConfig.getProducerTopic(),
                byteKey, byteValue));
        _LOG.info("Sent message with transaction Id: {}", transactionKey.getTransactionId());

        return (byte[])inbox.receive(Duration.create(5000, TimeUnit.MILLISECONDS));
    }
}
