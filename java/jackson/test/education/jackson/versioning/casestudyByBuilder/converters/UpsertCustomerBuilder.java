package education.jackson.versioning.casestudyByBuilder.converters;

import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomer;

public class UpsertCustomerBuilder implements Builder<UpsertCustomer>, Request
{
    private final int version;

    private final String customerId;
    private final String source;
    private String name;
    private String firstName;
    private String secondName;
    private String address;

    public UpsertCustomerBuilder(String customerId, String source, String name)
    {
        this.version = 1;
        this.customerId = customerId;
        this.source = source;
        this.name = name;
    }

    public UpsertCustomerBuilder(String customerId, String source, String firstName, String secondName)
    {
        this.version = 2;
        this.customerId = customerId;
        this.source = source;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public UpsertCustomerBuilder(String customerId, String source, String firstName, String secondName, String address)
    {
        this.version = 3;
        this.customerId = customerId;
        this.source = source;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
    }

    @Override
    public UpsertCustomer build()
    {
        return switch (this.version)
        {
            case 1 -> new UpsertCustomer(customerId, source, getFirstNameFromName(name), getSecondNameFromName(name), null);
            case 2 -> new UpsertCustomer(customerId, source, firstName, secondName, null);
            case 3 -> new UpsertCustomer(customerId, source, firstName, secondName, address);
            default -> throw new IllegalArgumentException("Invalid version: " + this.version);
        };
    }

    private static String getFirstNameFromName(final String name)
    {
        return name.contains(" ") ? name.substring(0, name.indexOf(" ")) : name;
    }

    private static String getSecondNameFromName(final String name)
    {
        return name.contains(" ") ? name.substring(name.indexOf(" ") + 1) : null;
    }
}
