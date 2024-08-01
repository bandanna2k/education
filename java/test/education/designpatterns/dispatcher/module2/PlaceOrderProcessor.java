package education.designpatterns.dispatcher.module2;

import education.designpatterns.dispatcher.module1.PlaceOrderInstruction;

class PlaceOrderProcessor
{
    void process(final PlaceOrderInstruction placeOrderInstruction)
    {
        System.out.println("PlaceOrderProcessor: " + placeOrderInstruction);
    }
}
