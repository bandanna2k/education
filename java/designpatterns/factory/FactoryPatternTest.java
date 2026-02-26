package education.designpatterns.factory;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * How to get a instance of a class given a string.
 */
public class FactoryPatternTest
{
    @Test
    public void shouldGetClassFromString()
    {
        final VehicleFactoryFactory factory = new VehicleFactoryFactory();

        factory.register("car", new CarFactory());
        factory.register("bike", new BikeFactory());

        assertThat(factory.newInstance("car")).isInstanceOf(Car.class);
        assertThat(factory.newInstance("bike")).isInstanceOf(Bike.class);
    }
}
