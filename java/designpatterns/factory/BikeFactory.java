package education.designpatterns.factory;

public class BikeFactory implements VehicleFactory
{
    @Override
    public Vehicle newInstance() {
        return new Bike();
    }
}
