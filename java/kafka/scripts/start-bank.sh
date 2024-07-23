TOPIC=the-bank

docker pull apache/kafka:3.7.1

docker stop my-kafka-bank

docker run -d --rm -p 9092:9092 --name my-kafka-bank apache/kafka:3.7.1

docker ps

docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --topic $TOPIC --bootstrap-server localhost:9092 --create

docker exec my-kafka-bank /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --topic $TOPIC --reset-offsets --to-earliest --execute --all-groups

docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic $TOPIC --describe

# View All Messages
# docker exec my-kafka-bank /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic the-bank --from-beginning

# Clear All Messages
# docker exec my-kafka-bank /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic the-bank --delete