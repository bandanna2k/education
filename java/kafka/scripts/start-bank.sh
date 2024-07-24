docker pull apache/kafka:3.7.1

docker stop my-kafka-bank

docker run -d --rm -p 9092:9092 --name my-kafka-bank apache/kafka:3.7.1

docker ps

# Create Topics
docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --topic bank-requests --bootstrap-server localhost:9092  --create
docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --topic bank-responses --bootstrap-server localhost:9092 --create \
    --config cleanup.policy=delete --config file.delete.delay.ms=5000 --config retention.check.interval.ms=5000
# We are not interested in responses after a certain time. Set a retention period
docker exec my-kafka-bank /opt/kafka/bin/kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name bank-responses --add-config retention.ms=5000

# What does these do?
docker exec my-kafka-bank /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --topic bank-requests  --reset-offsets --to-earliest --execute --all-groups
docker exec my-kafka-bank /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --topic bank-responses --reset-offsets --to-earliest --execute --all-groups

# View Topic status
docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic bank-requests  --describe
docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic bank-responses --describe

# View All Messages
# docker exec my-kafka-bank /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bank-requests --from-beginning
# docker exec my-kafka-bank /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bank-responses --from-beginning

# Clear All Messages
# docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic bank-requests  --delete
# docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic bank-responses --delete