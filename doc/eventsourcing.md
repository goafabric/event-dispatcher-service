# Intro

Small document to describe the pitfalls of data replication via a Message Broker (aka as Eventsourcing).
It describes the sender (producer) and receiver side (consumer) as well as the Message Broker (MB)
      
# Constraints

## Order of Event

For a replication of data we need to maintain the order of the CRUD Operations.
Because if e.g. the update or delete of an entity would be processed before the initial Create,
the data will be inconsistent

## Acknowledge

It is also required to manually set the acknowledge,
if the data received could have been processed correctly (e.g. database storage, rest calls).
All MB have solutions for this ... because with the standard "auto ack" enabled, a message could have been set as processed,
evn if there was an error

## Persistence

It is important for the MB to store the messages on disk, aka persistance.
Otherwise if the MB restarts all messages stored in memory will be lost.
Usally all MB can do this, NATS specifically has the so called Jetstream Mode that needs to be activated.

## Clustering / Replicasets

While everything can be easy in a non clustered environment with a single instance per MB, Consumer, Producer.
This can get much harder if we deploy multiple instances (aka Replicasets), as concurrency for sending and receiving messages
in parallel, comes into play.
The following sections will explain different scenarios

# Scenarios      

## Summary

- Order can be retainend by using the entity name for topic and the PK for the key
- For multiple producers consumers Kafka should automatically take care, NATS might need extra Code and Configuration
- One has to think twice for doing Rest Calls inside the Consumers as it imposes extra complexity for the ack.
- For further details see the following sections

## Single Producer Instance

For a single producer everything can be rather simple.
It just needs to send the message (fire and forget) with the defined rules above (topic = entity, key = PK)

## Single Instances for Producer and Consumer

In this Scenario maintaining the order of the Events is possible.

The Topics have to be designed with just the entity name (e.g. patient, practitioner).
While the CRUD operation type should be part of the EventMessage transported (operation = create, update, delete).
The MB will then usually process the messages in order, for this single topic.

According to ChatGpt, it seems to be also required for Kafka to put the primary key inside the "key" field, for the MB to handle the Msgs correct
                               
## Multiple Instances for Consumers

If we have multiple instances for the Consumers, which we usually have in Kubernetes, things get a little bit more tricky.
In this case we have to ensure that one specific consumer is only bound to on specific Instance.
Because if beeing bound to all instances ... messages could be processed in parallel, without any guarantee of the order.

The Kafka Spring Implementation seems to be automatically taking care of that by assigning specific partitions / groups.
For NATS that's also possible, but may have to be configured manually.

Verification of this Scenario is possible inside Kubernetes, but should also be possible inside Docker Compose by just spawning
two Instances of Consumer (in this case the Event Dispatcher Service)

## Multiple Instances for Producers

Now this is the part where things can get really tricky and the cookie might crumble
Usually not only the Consumer side is replicated, but also the Producer side.

ChatGPT claims that Kafka automatically takes care of that, with the Partitioning in the background,
as long as we keep the TOPIC = entity pattern and send over the entity id as key (all of this is unverified!)

As for NATS at least the NATS documentation clearly states that a Order of Events **cannot be guarenteed**:
https://docs.nats.io/reference/faq#does-nats-offer-any-guarantee-of-message-ordering
                                                                                          
So with NATS we would either have to set on of the instance into a "master" mode and only this one is allowed to produces msgs.

Or we need to implement distributed locking per entity ... to guarantee that one entity is only send by one producer.
This of course could hurt the performance.
                                                 
## Consumer Acknowledgement and Rest Calls

As described above, msgs should be acknowledged manually.
Both MB can do this.

However ... it could be a good practice that the consumer just stores the data inside the database asap.
Because extra processing like Rest Calls can impose extra challenges.

First and foremost with manually ack there is a latency time defined ... if the ack does not get processed in that timeframe
the message will be resent, which could create duplicates (mitigiation, use the entity id if already beeing processed).

But even with everything setup, rest calls could impose extra latency of up to 10-60 secs.
Or could even break entirely if the connected systems is down.

So a timeout an Circuit Breaker has to be defined that is in sync with the MB time frame.
And if should just fail fast .. meaning that the ack. will not be set and the MB will try to resend the message,
and the consumer will automatically retry.

While that sounds good at first sight ... that leads to another problem ... if the connected system is down for minutes to hours,
messages might pile up inside the Messagebroker and causing it to collapse.
For this case, in case of multiple failures, a dead letter queue needs to be defined.

Or .. just consider to don't do rest calls inside the consumer and just store the data.




