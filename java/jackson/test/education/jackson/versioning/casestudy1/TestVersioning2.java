package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.versioning.casestudy1.requests.UpsertCustomer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TestVersioning2
{
    private static final String JSON_WITH_NAME_V1 = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "name": "Tom SAWYER"
            }
            """;
    private static final String JSON_WITH_FIRST_AND_SECOND_NAME_V2 = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "firstName": "Tom",
                "secondName": "SAWYER"
            }
            """;
    private static final String JSON_WITH_ADDRESS_V3 = """
            {
                "type": "UpsertCustomer",
                "customerId": "67",
                "source": "FIAT",
                "firstName": "Tom",
                "secondName": "SAWYER",
                "address": "999 Letsbe Avenue"
            }
            """;

    private static ObjectMapper newBaseMapper()
    {
        return new ObjectMapper();
    }

    @Test
    public void givenVersion1ProtocolVersion1Works() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion1());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
    @Test
    public void givenVersion1ProtocolVersion2DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion1());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class));
    }
    @Test
    public void givenVersion1ProtocolVersion3DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion1());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_ADDRESS_V3, UpsertCustomer.class));
    }


    @Test
    public void givenVersion2ProtocolVersion1DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion2());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }
    @Test
    public void givenVersion2ProtocolVersion2Works() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion2());

        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class);
        assertThat(upsertCustomer.firstName).isEqualTo("Tom");
    }
    @Test
    public void givenVersion2ProtocolVersion3DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion2());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> System.out.println(mapper.readValue(JSON_WITH_ADDRESS_V3, UpsertCustomer.class)));
    }

    @Test
    public void givenVersion3Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion3());
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, UpsertCustomer.class));
    }
    @Test
    public void givenVersion3Protocol_RequestVersion2Works() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion3());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, UpsertCustomer.class);
        assertThat(upsertCustomer.address).isEqualTo(null);
    }
    @Test
    public void givenVersion3Protocol_RequestVersion3Works() throws JsonProcessingException
    {
        ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion3());
        UpsertCustomer upsertCustomer = mapper.readValue(JSON_WITH_ADDRESS_V3, UpsertCustomer.class);
        assertThat(upsertCustomer.address).isEqualTo("999 Letsbe Avenue");
    }
}
