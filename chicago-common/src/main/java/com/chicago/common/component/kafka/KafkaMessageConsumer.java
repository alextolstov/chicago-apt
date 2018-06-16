package com.chicago.common.component.kafka;

import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventBase;
import com.chicago.common.util.KafkaUtil;
import com.chicago.dto.Common;
import com.chicago.dto.Config;
import com.chicago.dto.Usermessages;
import com.google.protobuf.Message;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class KafkaMessageConsumer extends AbstractComponent
{
    private static final Logger _LOG = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private AbstractEventDispatcher _ed;
    private List<String> _topicList;
    private KafkaConsumer<byte[], byte[]> _consumer;

    public KafkaMessageConsumer(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
    }

    public boolean init(ConfigAccessor ca)
    {
        Config.ZooKeeperConfig zooConfig = ca.getConfig(KafkaUtil._ZOO_CONFIG_NAME);
        _LOG.info("Connecting to zookeeper servers: {}", zooConfig.getServers());
        Config.KafkaConfig kafkaConfig = ca.getConfig(KafkaUtil._KAFKA_CONFIG_NAME);
        _LOG.info("Connecting to kafka servers: {}", kafkaConfig.getServers());
        _topicList = kafkaConfig.getConsumerTopicList();

        _consumer = KafkaUtil.createConsumer(zooConfig.getServers(), kafkaConfig.getServers(),
                _topicList, kafkaConfig.getConsumerGroup());

        Thread t = new Thread(() -> run());
        t.start();
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    private void run()
    {
        _LOG.info("Started to consume messages from topic {}", _topicList.get(0));
        while (true)
        {
            ConsumerRecords<byte[], byte[]> records = _consumer.poll(1000);
            _LOG.debug("Got {} messages", records.count());

            for (ConsumerRecord<byte[], byte[]> record : records)
            {
                try
                {
                    Common.TransactionKey transactionKey = Common.TransactionKey.parseFrom(record.key());
                    _LOG.info("Received record with transaction Id: {}", transactionKey.getTransactionId());
                    Message message = _ed.deserializeMessage(transactionKey.getDataType(), record.value());
                    if (message != null)
                    {
                        _ed.publishRealTimeEvent(new EventBase(LocalDateTime.now(), message, transactionKey.getTransactionId()));
                    }
                } catch (Exception e)
                {
                    _LOG.error("Stack trace: {}", e);
                }
            }
            try
            {
                if (!records.isEmpty())
                {
                    _consumer.commitSync();
                }
            } catch (CommitFailedException ex)
            {
                _LOG.warn("Commit failed. Ignore");
            }
        }
//        _LOG.info("Exit infinite loop");
    }
}
