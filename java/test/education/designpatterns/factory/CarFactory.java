package education.designpatterns.factory;

public class CarFactory implements VehicleFactory
{
    @Override
    public Vehicle newInstance() {
        return new Car();
    }
}
