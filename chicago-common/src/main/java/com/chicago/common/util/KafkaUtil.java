package com.chicago.common.util;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class KafkaUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(KafkaUtil.class);
    public static final String KAFKA_CONFIG_NAME = "com.chicago.dto.KafkaConfig";
    public static final String ZOO_CONFIG_NAME = "com.chicago.dto.ZooKeeperConfig";

    public static <T1, T2> KafkaConsumer<T1, T2> createConsumer(String zookeerServers,
                                                               String bootstrapServers,
                                                               List<String> topicList,
                                                               String group)
    {
        KafkaUtil.createTopic(zookeerServers, topicList.get(0));
        LOG.info("Connecting to kafka servers: {} to create consumer", bootstrapServers);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        KafkaConsumer<T1, T2> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topicList);
        return consumer;
    }

    public static <T1, T2> KafkaProducer<T1, T2> createProducer(String zookeerServers,
                                                               String bootstrapServers,
                                                               String topic)
    {
        KafkaUtil.createTopic(zookeerServers, topic);
        LOG.info("Connecting to kafka servers: {} to create producer", bootstrapServers);
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static void createTopic(String zookeeerServers, String topicName)
    {
        LOG.info("Connecting to zookeeperservers: {}", zookeeerServers);
        int sessionTimeOutInMs = 15 * 1000; // 15 secs
        int connectionTimeOutInMs = 10 * 1000; // 10 secs
        ZkClient zkClient = new ZkClient(zookeeerServers, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);
        ZkUtils zkUtils = ZkUtils.apply(zkClient, false);
        if (!AdminUtils.topicExists(zkUtils, topicName))
        {
            Properties brokerProps = new Properties();
            brokerProps.setProperty("zookeeper.connect", zookeeerServers);
            brokerProps.setProperty("broker.id", "0");

            AdminUtils.createTopic(zkUtils, topicName, 1, 1, new Properties(), RackAwareMode.Disabled$.MODULE$);
            LOG.info("Topic {} was created", topicName);
        } else
        {
            LOG.info("Topic {} already exists", topicName);
        }

        zkClient.close();
    }
}
