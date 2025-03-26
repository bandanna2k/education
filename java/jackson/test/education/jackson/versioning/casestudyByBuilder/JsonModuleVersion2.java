package education.jackson.versioning.casestudyByBuilder;

import education.jackson.versioning.casestudyByBuilder.pojos.UpsertCustomerBuilder;

public class JsonModuleVersion2 extends JsonModuleVersion1
{
    @Override
    protected int getMajor()
    {
        return 2;
    }

    @Override
    protected void addMixInForUpsertCustomer(SetupContext setupContext)
    {
        setupContext.setMixInAnnotations(UpsertCustomerBuilder.class, UpsertCustomerMixIn_V2.class);
    }
}
