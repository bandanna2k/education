package education.localai;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LocalLLMTest extends LocalLLMBase {

    @Test
    void shouldGetHeightOfTheTennisNet() throws IOException {
        LocalLLM localLLM = new LocalLLM();
        localLLM.init(
                "/tmp/docs",
                "http://localhost:11434");

        localLLM.query("At the centre of the tennis net, what is the height in centi-metres?");
    }
}
