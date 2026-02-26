package education.designpatterns;

import org.junit.Test;

public class FascadePatternTest {
    public interface Shape {
        void draw();
    }

    public class Rectangle implements Shape {
        @Override
        public void draw() {
            System.out.println("Rectangle::draw()");
        }
    }

    public class Square implements Shape {
        @Override
        public void draw() {
            System.out.println("Square::draw()");
        }
    }

    public class Circle implements Shape {
        @Override
        public void draw() {
            System.out.println("Circle::draw()");
        }
    }

    public class ShapeMaker {
        private Shape circle;
        private Shape rectangle;
        private Shape square;

        public ShapeMaker() {
            circle = new Circle();
            rectangle = new Rectangle();
            square = new Square();
        }

        public void drawCircle() {
            circle.draw();
        }

        public void drawRectangle() {
            rectangle.draw();
        }

        public void drawSquare() {
            square.draw();
        }
    }

    @Test
    public void testFascadeMethod() {
        ShapeMaker maker = new ShapeMaker();
        maker.drawRectangle();
        maker.drawSquare();
        maker.drawCircle();
    }
}
