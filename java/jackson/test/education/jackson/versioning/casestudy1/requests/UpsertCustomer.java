package education.jackson.versioning.casestudy1.requests;

/**
 * Replacing name with first name and last name
 */
public class UpsertCustomer implements Request
{
    public final String customerId;
    public final String source;
    //        public final String name; // removed in Version 1;
    public final String firstName;
    public final String secondName;
    public final String address; // added in Version 3

    public UpsertCustomer(String customerId, String source, String firstName, String secondName, String address)    // Input version 3, output version 2
    {
        this.customerId = customerId;
        this.source = source;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
    }

    public UpsertCustomer(String customerId, String source, String firstName, String secondName)    // Input version 2, output version 2
    {
        this(customerId, source, firstName, secondName, null);
    }

    public UpsertCustomer(String customerId, String source, String name)    // Input version 1, Output version 2
    {
        this(customerId, source, getFirstNameFromName(name), getSecondNameFromName(name));
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    private static String getFirstNameFromName(final String name)
    {
        return name.contains(" ") ? name.substring(0, name.indexOf(" ")) : name;
    }

    private static String getSecondNameFromName(final String name)
    {
        return name.contains(" ") ? name.substring(name.indexOf(" ") + 1) : null;
    }

    @Override
    public String toString()
    {
        return "UpsertCustomer{" +
                "customerId='" + customerId + '\'' +
                ", source='" + source + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
