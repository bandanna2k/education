package casestudy.bank;

import education.jackson.response.Response;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Publisher
{
    private static final String RESPONSE_TOPIC = "bank-responses";

    private final Producer<String, Response> producer;

    public Publisher(Producer<String, Response> producer)
    {
        this.producer = producer;
    }

    public void publishResponse(Response response)
    {
        ProducerRecord<String, Response> record = new ProducerRecord<>(RESPONSE_TOPIC, response);
        producer.send(record);
        producer.flush();
        System.out.println("Response sent: " + response);
    }
}
