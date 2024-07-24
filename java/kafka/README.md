
# Start

```
docker pull apache/kafka:3.7.1

docker stop my-kafka

docker run -d --rm -p 9092:9092 --name my-kafka apache/kafka:3.7.1

docker ps

docker exec my-kafka /opt/kafka/bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092

docker exec my-kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning

docker exec my-kafka /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --topic test-topic --reset-offsets --to-earliest --execute --all-groups

docker exec my-kafka /opt/kafka/bin/kafka-topics.sh --describe --topic test-topic --bootstrap-server localhost:9092
```

# My Story

What is Kafka?
- Getting it started
- Sending strings, receiving strings

How to convert strings to objects?
- JSON Objects and Polymorphism 

What is a projection?
- This answers, what is the CQRS all about? Where is the line that separates these 2 concepts?
- Projection can be in memory, can also be your database. E.g. if you are doing crud.
- Now we have a database, I can just create a REST service that makes read calls on that database.

You have this command, what if this fails?
- Implement a very LMAXy, async request/response pattern?





# Questions

- How do I use Kafka?

- Where exactly is the Command Query separation?

- How do I validate a command?

- How can I snapshot?

# Todo

- Projection
- Read / Write separation
- Snapshotting example