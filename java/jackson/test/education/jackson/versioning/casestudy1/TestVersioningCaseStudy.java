package education.jackson.versioning.casestudy1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.versioning.casestudy1.requests.Request;
import education.jackson.versioning.casestudy1.requests.UpsertCustomer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TestVersioningCaseStudy
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
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
    }

    @Nested
    class TestJsonModuleVersion1
    {
        @Test
        public void givenVersion1Protocol_RequestVersion1Works() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion1());
            UpsertCustomer upsertCustomer = (UpsertCustomer)mapper.readValue(JSON_WITH_NAME_V1, Request.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        @Test
        public void givenVersion1Protocol_RequestVersion2DoesNotWork() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion1());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class));
        }
        @Test
        public void givenVersion1Protocol_RequestVersion3DoesNotWork() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion1());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class));
        }
    }

    @Nested
    class TestJsonModuleVersion2
    {
        @Test
        public void givenVersion2Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion2());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, Request.class));
        }
        @Test
        public void givenVersion2Protocol_RequestVersion2Works() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion2());

            UpsertCustomer upsertCustomer = (UpsertCustomer)mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class);
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        @Test
        public void givenVersion2Protocol_RequestVersion3DoesNotWork() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion2());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> System.out.println(mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class)));
        }
    }

    @Nested
    class TestJsonModuleVersion3
    {
        @Test
        public void givenVersion3Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion3());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, Request.class));
        }
        @Test
        public void givenVersion3Protocol_RequestVersion2Works() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion3());
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class));
        }
        @Test
        public void givenVersion3Protocol_RequestVersion3Works() throws JsonProcessingException
        {
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion3());
            UpsertCustomer upsertCustomer = (UpsertCustomer)mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class);
            assertThat(upsertCustomer.address).isEqualTo("999 Letsbe Avenue");
        }
    }
}
