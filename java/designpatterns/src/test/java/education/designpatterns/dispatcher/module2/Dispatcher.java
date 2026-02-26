package education.designpatterns.dispatcher.module2;

import education.designpatterns.dispatcher.module1.CancelOrderInstruction;
import education.designpatterns.dispatcher.module1.PlaceOrderInstruction;

class Dispatcher
{
    private final PlaceOrderProcessor placeOrderProcessor;
    private final CancelOrderProcessor cancelOrderProcessor;

    public Dispatcher(final PlaceOrderProcessor placeOrderProcessor,
                      final CancelOrderProcessor cancelOrderProcessor)
    {
        this.placeOrderProcessor = placeOrderProcessor;
        this.cancelOrderProcessor = cancelOrderProcessor;
    }

    public void process(PlaceOrderInstruction placeOrderInstruction)
    {
        placeOrderProcessor.process(placeOrderInstruction);
    }

    public void process(CancelOrderInstruction cancelOrderInstruction)
    {
        cancelOrderProcessor.process(cancelOrderInstruction);
    }
}
