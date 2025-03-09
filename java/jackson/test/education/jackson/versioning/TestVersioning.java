package education.jackson.versioning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class TestVersioning
{
    public static class ConverterVersion1 extends Module
    {
        @Override
        public String getModuleName() {
            return this.getClass().getSimpleName();
        }
        @Override
        public Version version()
        {
            return new Version(1, 0, 0, null, null, null);
        }
        @Override
        public void setupModule(SetupContext setupContext)
        {
            setupContext.setMixInAnnotations(UpsertCustomer.class, UpsertCustomerMixIn.class);
        }
    }

    public static class ConverterVersion2 extends Module
    {
        @Override
        public String getModuleName() {
            return this.getClass().getSimpleName();
        }
        @Override
        public Version version()
        {
            return new Version(2, 0, 0, null, null, null);
        }
        @Override
        public void setupModule(SetupContext setupContext)
        {
            setupContext.setMixInAnnotations(UpsertCustomer.class, UpsertCustomerMixInVersion2.class);
        }
    }

    @JsonIgnoreProperties({"type"})
    public interface Request
    {
        String getType();
    }

    public static class UpsertCustomer implements Request
    {
        public String customerId;
        public String source;
        public String address;

        @Override
        public String getType() {
            return this.getClass().getSimpleName();
        }

        @Override
        public String toString() {
            return "UpsertCustomer{" +
                    "customerId='" + customerId + '\'' +
                    ", source='" + source + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    public abstract static class UpsertCustomerMixIn
    {
        @JsonCreator
        UpsertCustomerMixIn(
                @JsonProperty("customerId") final String customerId,
                @JsonProperty("source") final String source)
        {
        }
    }

    public abstract static class UpsertCustomerMixInVersion2
    {
        @JsonCreator
        UpsertCustomerMixInVersion2(
                @JsonProperty("customerId") final String customerId,
                @JsonProperty("source") final String source,
                @JsonProperty("address") final String address) // New field for version 2
        {
        }
    }

    @Test
    public void shouldDeserialise() throws JsonProcessingException
    {
        ConverterVersion1 converterVersion1 = new ConverterVersion1();

        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(new ConverterVersion1());

        final String jsonVersion1 = """
                {
                    "type": "UpsertCustomer",
                    "customerId": "67",
                    "source": "FIAT"
                }
                """;
        final String jsonVersion2 = """
                {
                    "type": "UpsertCustomer",
                    "customerId": "67",
                    "source": "FIAT",
                    "address": "Accacia Avenue"
                }
                """;

        {
            UpsertCustomer upsertCustomer = mapper.readValue(jsonVersion1, UpsertCustomer.class);
            assertThat(upsertCustomer.address).isNull();
        }
        {
            UpsertCustomer upsertCustomer = mapper.readValue(jsonVersion2, UpsertCustomer.class);
            assertThat(upsertCustomer.address).isNull();
        }
    }
}
