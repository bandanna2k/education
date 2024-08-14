package education;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class MyJsonObjectTest
{
    @Test
    public void testMyJsonObject() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        MyJsonObject myJsonObject = new MyJsonObject();
        System.out.println(mapper.writeValueAsString(myJsonObject));
    }
}
