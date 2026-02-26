package education.designpatterns.visitorPattern;

import org.junit.Test;

public class VisitorPatternTest
{
    @Test
    public void test()
    {
        IComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }

    interface IComputerPartVisitor
    {
        void visit(Computer c);
        void visit(KeyBoard kb);
        void visit(Mouse m);
    }
    class ComputerPartDisplayVisitor implements IComputerPartVisitor
    {
        @Override
        public void visit(Computer c)
        {
            System.out.println("Computer:" + c);
        }

        @Override
        public void visit(KeyBoard kb)
        {
            System.out.println("Keyboard:" + kb);
        }

        @Override
        public void visit(Mouse m)
        {
            System.out.println("Mouse:" + m);
        }
    }
    class Computer implements IComputerPart
    {
        IComputerPart[] parts = new IComputerPart[]
        {
            new KeyBoard()
        };

        @Override
        public void accept(IComputerPartVisitor cpv)
        {
            for(IComputerPart part : parts)
            {
                part.accept(cpv);
            }
            cpv.visit(this);
        }
    }

    interface IComputerPart
    {
        void accept(IComputerPartVisitor cpv);
    }
    class KeyBoard implements IComputerPart
    {
        @Override
        public void accept(IComputerPartVisitor cpv)
        {
            cpv.visit(this);
        }
    }
    class Mouse implements IComputerPart
    {
        @Override
        public void accept(IComputerPartVisitor cpv)
        {
            cpv.visit(this);
        }
    }
}
