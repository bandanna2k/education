package education.jackson.versioning.casestudyByBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import education.jackson.versioning.casestudyByBuilder.pojos.AnotherRequest;
import education.jackson.versioning.casestudyByBuilder.converters.AnotherRequestBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.UpsertCustomerBuilder;
import education.jackson.versioning.casestudyByBuilder.converters.Request;
import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        private ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion1());

        @Test
        public void givenVersion1Protocol_RequestVersion1Works() throws JsonProcessingException
        {
            UpsertCustomer upsertCustomer = ((UpsertCustomerBuilder)mapper.readValue(JSON_WITH_NAME_V1, Request.class)).build();
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        @Test
        public void givenVersion1Protocol_RequestVersion2DoesNotWork() throws JsonProcessingException
        {
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class));
        }
        @Test
        public void givenVersion1Protocol_RequestVersion3DoesNotWork() throws JsonProcessingException
        {
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class));
        }
    }

    @Nested
    class TestJsonModuleVersion2
    {
        private ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion2());

        @Test
        public void givenVersion2Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
        {
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, Request.class));
        }
        @Test
        public void givenVersion2Protocol_RequestVersion2Works() throws JsonProcessingException
        {
            UpsertCustomer upsertCustomer = ((UpsertCustomerBuilder)mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class)).build();
            assertThat(upsertCustomer.firstName).isEqualTo("Tom");
        }
        @Test
        public void givenVersion2Protocol_RequestVersion3DoesNotWork() throws JsonProcessingException
        {
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> System.out.println(mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class)));
        }
    }

    @Nested
    class TestJsonModuleVersion3
    {
        private ObjectMapper mapper = newBaseMapper()
                .registerModule(new JsonModuleVersion3());

        @Test
        public void givenVersion3Protocol_RequestVersion1DoesNotWork() throws JsonProcessingException
        {
            assertThatExceptionOfType(JsonMappingException.class)
                    .isThrownBy(() -> mapper.readValue(JSON_WITH_NAME_V1, Request.class));
        }
        @Test
        public void givenVersion3Protocol_RequestVersion2Works() throws JsonProcessingException
        {
            UpsertCustomer upsertCustomer = ((UpsertCustomerBuilder)mapper.readValue(JSON_WITH_FIRST_AND_SECOND_NAME_V2, Request.class)).build();
            assertThat(upsertCustomer.address).isEqualTo(null);
        }
        @Test
        public void givenVersion3Protocol_RequestVersion3Works() throws JsonProcessingException
        {
            UpsertCustomer upsertCustomer = ((UpsertCustomerBuilder)mapper.readValue(JSON_WITH_ADDRESS_V3, Request.class)).build();
            assertThat(upsertCustomer.address).isEqualTo("999 Letsbe Avenue");
        }
    }

    @Nested
    class TestAnotherRequest
    {
        @Test
        public void canAlsoDeserialiseOtherRequests () throws JsonProcessingException
        {
            String exampleJson = """
                    {
                        "type": "AnotherRequest"
                    }
                    """;
            ObjectMapper mapper = newBaseMapper()
                    .registerModule(new JsonModuleVersion3());
            AnotherRequest anotherRequest = ((AnotherRequestBuilder) mapper.readValue(exampleJson, Request.class)).build();
            Assertions.assertThat(anotherRequest).isNotNull();
        }
    }
}
