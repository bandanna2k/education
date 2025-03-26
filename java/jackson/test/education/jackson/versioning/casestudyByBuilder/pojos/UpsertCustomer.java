package education.jackson.versioning.casestudyByBuilder.pojos;

public class UpsertCustomer
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
