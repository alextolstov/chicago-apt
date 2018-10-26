package com.chicago.app.test;

import com.chicago.common.comm.KafkaAsyncCommunicator;
import com.chicago.dto.Common;
import com.chicago.dto.Config;
import com.chicago.dto.UserOuterClass;
import com.chicago.dto.Usermessages;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeoutException;

public class TestSendRequestResponse
{
    @Test
    public void SendCreateUserRequest()
    {
        Config.ZooKeeperConfig zk = Config.ZooKeeperConfig.newBuilder()
                .setServers("localhost:2181")
                .build();

        Config.KafkaConfig kf = Config.KafkaConfig.newBuilder()
                .setProducerTopic("user_requests")
                .setConsumerGroup("test-group")
                .setServers("localhost:9092")
                .addConsumerTopic("user_response")
                .build();

        KafkaAsyncCommunicator comm = new KafkaAsyncCommunicator(zk, kf);
        UserOuterClass.User usr = UserOuterClass.User.newBuilder()
                .setFirstName("Aleksey")
                .setLastName("Telyshev")
                .build();
        Usermessages.UserRequest cur = Usermessages.UserRequest.newBuilder()
                .setCrudOperation(Common.CrudOperation.CREATE)
                .setUser(usr)
                .build();
        try
        {
            for(int i = 0; i < 100000; i++)
            {
                byte[] data = comm.transaction(cur);
                Usermessages.UserResponse resp = Usermessages.UserResponse.parseFrom(data);
                System.out.println("Done: " + i);
            }
        } catch (TimeoutException e)
        {
            e.printStackTrace();
        } catch (InvalidProtocolBufferException e)
        {
            e.printStackTrace();
        }

    }
}
