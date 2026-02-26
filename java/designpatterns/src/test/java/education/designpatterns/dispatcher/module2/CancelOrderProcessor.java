package education.designpatterns.dispatcher.module2;

import education.designpatterns.dispatcher.module1.CancelOrderInstruction;

class CancelOrderProcessor
{
    void process(final CancelOrderInstruction cancelOrderInstruction)
    {
        System.out.println("CancelOrderInstruction:" + cancelOrderInstruction);
    }
}
