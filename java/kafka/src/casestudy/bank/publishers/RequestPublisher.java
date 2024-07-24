package casestudy.bank.publishers;

import casestudy.bank.Topics;
import education.jackson.requests.Request;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class RequestPublisher
{
    private final Producer<String, Request> producer;

    public RequestPublisher(Producer<String, Request> producer)
    {
        this.producer = producer;
    }

    public void publishRequest(Request request)
    {
        ProducerRecord<String, Request> record = new ProducerRecord<>(Topics.RESPONSE_TOPIC, request);
        producer.send(record);
        producer.flush();
        System.out.println("Request sent: " + request);
    }
}
