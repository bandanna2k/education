
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
