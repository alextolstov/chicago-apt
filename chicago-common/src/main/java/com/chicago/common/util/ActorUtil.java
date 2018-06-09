package com.chicago.common.util;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

/**
 * Simple utility class to encapsulate ActorSystem and ActorSelection.
 */
public class ActorUtil
{
    private static final ActorSystem _ACTOR_SYSTEM = ActorSystem.create("kafka-request-response");

    private ActorUtil()
    {
    }

    public static ActorSystem getActorSystem()
    {
        return _ACTOR_SYSTEM;
    }

    public static ActorSelection getRootActor(String rootActorName)
    {
        return _ACTOR_SYSTEM.actorSelection("/user/" + rootActorName);
    }
}
