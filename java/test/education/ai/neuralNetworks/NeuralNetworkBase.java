package education.ai.neuralNetworks;

import com.lmax.simpledsl.DslParams;
import com.lmax.simpledsl.OptionalParam;
import com.lmax.simpledsl.RequiredParam;
import org.assertj.core.data.Offset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class NeuralNetworkBase
{
    private final Map<String, NeuralNetwork> nameToNeuralNetwork = new HashMap<>();

    public NeuralNetworkBase()
    {
    }

    protected void buildNeuralNetwork(final String... args)
    {
        DslParams params = new DslParams(args,
                new RequiredParam("name"),
                new RequiredParam("layout"),
                new OptionalParam("weights"),
                new OptionalParam("seed").setDefault("1")
        );

        final int[] layout = Arrays.stream(params.value("layout").split(", ")).mapToInt(Integer::parseInt).toArray();

        final double[] weights = params.hasValue("weights")
                ? Arrays.stream(params.value("weights").split(", ")).mapToDouble(Double::parseDouble).toArray()
                : null;

        final Random random = new Random(params.valueAsInt("seed"));
        final NeuralNetwork nn = new NeuralNetworkBuilder(random, layout).weights(weights).build();

        nameToNeuralNetwork.put(params.value("name"), nn);
    }

    protected void input(final String... args)
    {
        DslParams params = new DslParams(args,
                new RequiredParam("name"),
                new RequiredParam("input")
        );

        final double[] input = Arrays.stream(params.value("input").split(", ")).mapToDouble(Double::parseDouble).toArray();

        final NeuralNetwork nn = nameToNeuralNetwork.get(params.value("name"));

        nn.input(input);
    }

    protected void verifyOutput(final String... args)
    {
        DslParams params = new DslParams(args,
                new RequiredParam("name"),
                new OptionalParam("output"),
                new OptionalParam("tolerance")
        );

        final NeuralNetwork nn = nameToNeuralNetwork.get(params.value("name"));

        if(params.hasValue("output"))
        {
            assertThat(nn.getOutput().getAsDouble()).isCloseTo(
                    params.valueAsDouble("output"),
                    Offset.offset(params.valueAsDouble("tolerance")));
        }
    }
}
