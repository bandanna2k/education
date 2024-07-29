
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

# Curl Commands

`
curl -X POST "http://localhost:8888/withdraw?accountId=1&amount=100" | jq
`

`curl -X POST "http://localhost:8888/withdraw?accountId=1&amount=100" | jq
`

`
curl -X GET "http://localhost:8888/balances" | jq
`

# My Story

What is Kafka?
- Getting it started
- Sending strings, receiving strings
- Because this is testing, Kafka is not persisting between restarts (come back to that)

How to convert strings to objects?
- JSON Objects and Polymorphism 

What is a projection?
- This answers, what is the CQRS all about? Where is the line that separates these 2 concepts?
- Projection can be in memory, can also be your database. E.g. if you are doing crud. 
  - Oooo, I'm using Kafka docker. I can spin up MySQL (need to see if I can run MySQL in non-persisting mode similar to my kafka)
- Now we have a database, I can just create a REST service that makes read calls on that database.

You have this command, what if this fails?
- Implement a very LMAXy, async request/response pattern?





# Questions

- How do I use Kafka?

- Where exactly is the Command Query separation?

- How do I validate a command?

- How can I snapshot?

# Todo

- Snapshotting example
  - Pause writing
  - MySql dump
  - Write offset
  - Unpause writing
  - Set new topic
- Fix vertx bug
- Removing message
  - Event store requirements. 
    - Delete up to a certain message

## Done

- 27/7/24   Separate code into handling and projection
- 27/7/24   DAO tests
- 27/7/24   Bug, why does 2 updates always give a result of 100?
- 27/7/24   Create REST GET calls
- 26/7/24   Use database for projection
- 25/7/24   Stream results on response topic
- 25/7/24   Connect request with response
- 25/7/24   Projection 
- 25/7/24   Read / Write separation 

## Not Done

- (Using testContainers) MySQL non-persisting https://medium.com/@pybrarian/mysql-databases-that-dont-retain-data-293bc2ed7f02
