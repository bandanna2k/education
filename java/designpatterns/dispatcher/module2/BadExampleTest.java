package education.designpatterns.dispatcher.module2;

import education.designpatterns.dispatcher.module1.CancelOrderInstruction;
import education.designpatterns.dispatcher.module1.Instruction;
import education.designpatterns.dispatcher.module1.PlaceOrderInstruction;
import org.junit.Test;

public class BadExampleTest
{
    @Test
    public void shouldShowBadExample()
    {
        BadProcessor badProcessor = new BadProcessor(new PlaceOrderProcessor(), new CancelOrderProcessor());
        badProcessor.process(new PlaceOrderInstruction());
        badProcessor.process(new CancelOrderInstruction());
    }

    private static class BadProcessor
    {
        private final PlaceOrderProcessor placeOrderProcessor;
        private final CancelOrderProcessor cancelOrderProcessor;

        private BadProcessor(final PlaceOrderProcessor placeOrderProcessor, final CancelOrderProcessor cancelOrderProcessor)
        {
            this.placeOrderProcessor = placeOrderProcessor;
            this.cancelOrderProcessor = cancelOrderProcessor;
        }

        public void process(final Instruction instruction)
        {
            if (instruction instanceof PlaceOrderInstruction)
            {
                placeOrderProcessor.process((PlaceOrderInstruction)instruction);
                return;
            }
            if (instruction instanceof CancelOrderInstruction)
            {
                cancelOrderProcessor.process((CancelOrderInstruction)instruction);
                return;
            }
            throw new RuntimeException("Bad instruction.");
        }
    }
}
