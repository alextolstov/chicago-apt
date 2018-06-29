package com.chicago.common.components.kafka;

import com.chicago.common.core.AbstractComponent;
import com.chicago.common.core.AbstractEventDispatcher;
import com.chicago.common.core.ComponentManager;
import com.chicago.common.core.ConfigAccessor;
import com.chicago.common.core.EventHandler;
import com.chicago.common.util.KafkaUtil;
import com.chicago.dto.Common;
import com.chicago.dto.Config;
import com.chicago.dto.Usermessages;
import com.google.protobuf.Message;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaMessageProducer extends AbstractComponent
{
    private static final Logger _LOG = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private AbstractEventDispatcher _ed;
    private String _producerTopic;
    private Producer<byte[], byte[]> _producer;

    public KafkaMessageProducer(ComponentManager cm) throws ClassNotFoundException
    {
        _ed = cm.getResource(AbstractEventDispatcher.class.getName());
        _ed.registerHandler(Usermessages.CreateUserResponse.class, new KafkaMessageProducer.MessageEventHandler());
        _ed.registerHandler(Usermessages.LoginUserResponse.class, new KafkaMessageProducer.MessageEventHandler());
    }

    public boolean init(ConfigAccessor ca)
    {
        Config.KafkaConfig kafkaConfig = ca.getConfig(KafkaUtil._KAFKA_CONFIG_NAME);
        Config.ZooKeeperConfig zooConfig = ca.getConfig(KafkaUtil._ZOO_CONFIG_NAME);
        _producerTopic = kafkaConfig.getProducerTopic();
        _producer = KafkaUtil.createProducer(zooConfig.getServers(), kafkaConfig.getServers(), _producerTopic);
        return true;
    }

    public static void registerComponentFactories()
    {
        ComponentManager.registerComponentFactory(new Exception().getStackTrace()[0].getClassName());
    }

    class MessageEventHandler implements EventHandler<Message>
    {
        @Override
        public void handleEvent(Message event, String transactionId)
        {
            _LOG.debug("Publish to kafka: {}", event.toString());
            Common.TransactionKey transactionKey = Common.TransactionKey.newBuilder()
                    .setTransactionId(transactionId)
                    .setDataType(event.getClass().getSimpleName())
                    .build();
            byte[] byteKey = transactionKey.toByteArray();
            byte[] byteValue = event.toByteArray();
            ProducerRecord<byte[], byte[]> data = new ProducerRecord<>(_producerTopic, byteKey, byteValue);
            _producer.send(data);
        }
    }
}
