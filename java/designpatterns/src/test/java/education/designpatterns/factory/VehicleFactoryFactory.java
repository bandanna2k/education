package education.designpatterns.factory;

import java.util.HashMap;
import java.util.Map;

public class VehicleFactoryFactory
{
    private final Map<String, VehicleFactory> nameToFactory = new HashMap<>();

    public Vehicle newInstance(String type)
    {
        final VehicleFactory vehicleFactory = nameToFactory.get(type);
        return vehicleFactory == null ? new NoVehicle() : vehicleFactory.newInstance();
    }

    public void register(String name, VehicleFactory factory)
    {
        nameToFactory.put(name, factory);
    }
}
